/*
 * Copyright (c) 2026 Auxio Project
 * DirectFS.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.musikr.fs.direct

import android.util.Log
import java.io.File as JavaFile
import java.security.MessageDigest
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.fs.saf.SAF
import org.oxycblt.musikr.util.startOwning

/** Bounded live DirectFS work counters used by the app's no-progress watchdog. */
data class DirectFsWorkProgress(
    val directoriesVisited: Int,
    val entriesInspected: Int,
    val filesEmitted: Int,
    val queuedDirectories: Int,
    val activeEnumerators: Int,
)

/**
 * Filesystem backend that reads configured folders through ordinary app-UID access.
 *
 * Traversal itself is delegated to [DirectFsTraversal], which owns a single explicit work queue per
 * source and therefore completes structurally. This class is responsible for the source-level
 * contract around it: canonical root identity, exact-duplicate collapse, fingerprints, and turning
 * per-source traversal outcomes into the shared failure protocol.
 */
class DirectFS
internal constructor(private val query: SAF.Query, private val options: DirectFsOptions) :
    SourceAwareFS {
    /**
     * Exact canonical duplicates are collapsed here as the last defensive boundary.
     *
     * Persistence and the source picker collapse them first, but a backend that fingerprints,
     * counts or traverses one physical folder twice is never correct, so the invariant is
     * re-established before any work is planned.
     */
    private val roots: List<Location.Opened> =
        CanonicalSourcePolicy.collapseDuplicates(
            query.source,
            SourceIdentity::canonicalKeyForLocation,
        )

    private val excludedCanonicalPaths: Set<String> =
        query.exclude.mapNotNullTo(linkedSetOf()) { location ->
            location.uri.path?.let(CanonicalSourcePolicy::normalizePath)
                ?: CanonicalSourcePolicy.externalStorageTreePath(location.uri)
        }

    private val sourceFailures = ConcurrentHashMap<String, String>()

    @Volatile private var lastMetrics: DirectFsTraversalMetrics? = null

    constructor(query: SAF.Query) : this(query, DirectFsOptions.DEFAULT)

    constructor(
        query: SAF.Query,
        onWorkProgress: (DirectFsWorkProgress) -> Unit,
    ) : this(query, DirectFsOptions.DEFAULT.copy(onWorkProgress = onWorkProgress))

    constructor(
        roots: List<Location.Opened>
    ) : this(
        SAF.Query(
            source = roots,
            exclude = emptyList(),
            withHidden = false,
            multithread = false,
            sourceOrigins =
                roots.associate {
                    SourceIdentity.canonicalKeyForLocation(it) to
                        CanonicalSourcePolicy.Origin.EXPLICIT
                },
        ),
        DirectFsOptions.DEFAULT,
    )

    override suspend fun sourceSnapshots(): List<SourceSnapshot> =
        withContext(Dispatchers.IO) {
            val preparedBySourceKey = prepareRoots().groupBy { it.sourceKey }
            roots.groupBy(SourceIdentity::forLocation).map { (sourceKey, configuredRoots) ->
                val preparedRoots = preparedBySourceKey[sourceKey].orEmpty()
                if (preparedRoots.isEmpty()) {
                    return@map unavailableSnapshot(sourceKey, configuredRoots.first())
                }
                val evaluated =
                    preparedRoots.map { root ->
                        val readable =
                            DirectFsRootPolicy.isAllowedRoot(root.directory) &&
                                listFilesSafe(root.directory) != null
                        RootSnapshot(root, readable)
                    }
                // One snapshot represents the complete volume-scoped source. A fingerprint of
                // only the surviving subset could incorrectly suppress a scan after another
                // configured root was rejected, so availability requires every configured root.
                val available =
                    preparedRoots.size == configuredRoots.size && evaluated.all { it.readable }
                val first = preparedRoots.first()
                SourceSnapshot(
                    sourceKey = sourceKey,
                    sourceType = SOURCE_TYPE,
                    // A source key may cover more than one configured folder. The first path is
                    // display metadata only; the combined fingerprint below covers every root.
                    rootUri = first.normalizedUri,
                    rootPath = first.displayPath,
                    available = available,
                    fingerprint =
                        if (available) {
                            combineRootFingerprints(evaluated.map { it.root })
                        } else {
                            null
                        },
                    fingerprintStrength =
                        if (available) SourceFingerprintStrength.ADVISORY
                        else SourceFingerprintStrength.NONE,
                    canonicalKey = first.canonicalKey,
                    sourceOrigin = first.origin,
                    traversalScope = first.scope,
                )
            }
        }

    override fun selectSources(sourceKeys: Set<String>): FS =
        DirectFS(
            query.copy(
                source = roots.filter { SourceIdentity.forLocation(it) in sourceKeys },
                sourceOrigins =
                    query.sourceOrigins.filterKeys { canonicalKey ->
                        roots.any {
                            SourceIdentity.forLocation(it) in sourceKeys &&
                                SourceIdentity.canonicalKeyForLocation(it) == canonicalKey
                        }
                    },
            ),
            options,
        )

    override fun drainSourceFailures(): Map<String, String> =
        sourceFailures.toMap().also { sourceFailures.clear() }

    /**
     * Hands the traversal back to the pipeline immediately.
     *
     * The returned task is the sole owner of [files]: it closes the channel on success, closes it
     * with the causal exception on failure and cancels it on cancellation. Returning before the
     * traversal finishes is what lets a bounded channel apply real back-pressure instead of
     * deadlocking against a consumer that has not been started yet.
     */
    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> =
        startOwning(files, Dispatchers.IO) { output ->
            val traversal = DirectFsTraversal(prepareRoots(), options)
            try {
                val metrics = traversal.explore(output)
                lastMetrics = metrics
                publish(metrics)
            } catch (e: Throwable) {
                val metrics = traversal.metricsSnapshot()
                lastMetrics = metrics
                publish(metrics)
                throw e
            }
        }

    override fun track(): Flow<FSUpdate> = emptyFlow()

    /** Deterministic measurements of the most recent traversal, for diagnostics and tests. */
    internal fun lastTraversalMetrics(): DirectFsTraversalMetrics? = lastMetrics

    /**
     * Resolves configured roots into canonical, ordered traversal roots.
     *
     * Ordering places narrow explicit folders before whole-volume roots so an explicit source
     * always keeps its own unfiltered policy. The traversal's shared visited set then suppresses
     * the overlapping part of the wider root instead of scanning it twice.
     */
    private fun prepareRoots(): List<PreparedRoot> {
        val prepared = mutableListOf<PreparedRoot>()
        for (location in roots) {
            val sourceKey = SourceIdentity.forLocation(location)
            if (location.uri.scheme != "file") {
                recordFailure(
                    sourceKey,
                    "TEMPORARILY_UNAVAILABLE|Unsupported DirectFS URI ${location.uri}",
                )
                continue
            }
            val root = appFacingRoot(location)
            val canonicalPath = root?.let(DirectFsTraversal::canonicalAppFacingPath)
            if (
                root == null ||
                    canonicalPath == null ||
                    !options.isAllowedCanonicalPath(canonicalPath)
            ) {
                recordFailure(
                    sourceKey,
                    "TEMPORARILY_UNAVAILABLE|Unsafe or missing DirectFS source ${location.uri}",
                )
                continue
            }
            prepared.add(
                PreparedRoot(
                    sourceKey = sourceKey,
                    directory = root,
                    canonicalPath = canonicalPath,
                    relativePath = location.path,
                    scope = CanonicalSourcePolicy.scopeOf(canonicalPath),
                    origin =
                        query.sourceOrigins[SourceIdentity.canonicalKeyForLocation(location)]
                            ?: CanonicalSourcePolicy.legacyOriginForPath(canonicalPath),
                    excludedCanonicalPaths =
                        excludedCanonicalPaths.filterTo(linkedSetOf()) { excluded ->
                            excluded == canonicalPath ||
                                CanonicalSourcePolicy.isAncestorOf(canonicalPath, excluded) ||
                                CanonicalSourcePolicy.isAncestorOf(excluded, canonicalPath)
                        },
                    withHidden = query.withHidden,
                    canonicalKey = SourceIdentity.canonicalKeyForLocation(location),
                    normalizedUri = location.uri.toString(),
                    displayPath = canonicalPath,
                )
            )
        }
        return applyOverlapPolicy(
            CanonicalSourcePolicy.traversalOrder(prepared) { it.canonicalPath }
        )
    }

    /** Turns the explicit outcome of every source into the shared failure protocol. */
    private fun publish(metrics: DirectFsTraversalMetrics) {
        for (result in metrics.results) {
            val detail = result.detail ?: result.canonicalPath
            when (result.completion) {
                SourceCompletion.COMPLETED,
                SourceCompletion.COMPLETED_EMPTY,
                SourceCompletion.CANCELLED -> Unit
                SourceCompletion.TEMPORARILY_UNAVAILABLE ->
                    recordFailure(result.sourceKey, "TEMPORARILY_UNAVAILABLE|$detail")
                SourceCompletion.PERMISSION_REQUIRED ->
                    recordFailure(result.sourceKey, "PERMISSION_REQUIRED|$detail")
                SourceCompletion.TRUNCATED -> recordFailure(result.sourceKey, "TRUNCATED|$detail")
                SourceCompletion.FAILED -> recordFailure(result.sourceKey, detail)
            }
        }
        Log.i(
            TAG,
            "DirectFS traversal finished in ${metrics.elapsedMs}ms " +
                "[directories=${metrics.directoriesVisited}, files=${metrics.filesEmitted}, " +
                "duplicatesSuppressed=${metrics.duplicateDirectoriesSuppressed}, " +
                "peakQueued=${metrics.peakQueuedDirectories}, " +
                "slowOperations=${metrics.slowOperations.size}, " +
                "outcomes=${metrics.results.map { "${it.canonicalPath}=${it.completion}" }}]",
        )
    }

    private fun recordFailure(sourceKey: String, detail: String) {
        if (sourceFailures.putIfAbsent(sourceKey, detail) == null) {
            Log.w(TAG, detail)
        }
    }

    /**
     * Retains configured identity when every root for a volume is rejected during preparation.
     *
     * Incremental planning distinguishes "configured but unavailable" from "no configured sources".
     * Returning this advisory row therefore prevents a transient canonical-path or access failure
     * from collapsing the configuration into an empty preflight result. Actual enumeration remains
     * authoritative and still requires an allowed app-facing root.
     */
    private fun unavailableSnapshot(sourceKey: String, location: Location.Opened): SourceSnapshot {
        val canonicalKey = SourceIdentity.canonicalKeyForLocation(location)
        val normalizedPath = location.uri.path?.let(CanonicalSourcePolicy::normalizePath)
        return SourceSnapshot(
            sourceKey = sourceKey,
            sourceType = SOURCE_TYPE,
            rootUri =
                CanonicalSourcePolicy.canonicalUriString(location.uri.toString())
                    ?: location.uri.toString(),
            rootPath = normalizedPath ?: location.uri.path,
            available = false,
            fingerprint = null,
            fingerprintStrength = SourceFingerprintStrength.NONE,
            canonicalKey = canonicalKey,
            sourceOrigin =
                query.sourceOrigins[canonicalKey]
                    ?: CanonicalSourcePolicy.legacyOriginForPath(normalizedPath),
            traversalScope = normalizedPath?.let(CanonicalSourcePolicy::scopeOf),
        )
    }

    /**
     * The app-facing file for [location].
     *
     * A configured root may still be persisted as a privileged backing path from an older build.
     * Scanning and playback both require the app-facing namespace, so the alias is collapsed before
     * the path is opened.
     */
    private fun appFacingRoot(location: Location.Opened): JavaFile? {
        val path = location.uri.path ?: return null
        return JavaFile(CanonicalSourcePolicy.normalizePath(path) ?: path)
    }

    internal fun combineRootFingerprints(roots: List<PreparedRoot>): String {
        val digest = MessageDigest.getInstance("SHA-256")
        roots
            .distinctBy { it.canonicalKey }
            .sortedBy { it.canonicalPath }
            .forEach { root ->
                digest.update(root.canonicalKey.toByteArray(Charsets.UTF_8))
                digest.update(0)
                digest.update(root.origin.name.toByteArray(Charsets.UTF_8))
                digest.update(0)
                digest.update(root.scope.name.toByteArray(Charsets.UTF_8))
                digest.update(0)
                digest.update(root.withHidden.toString().toByteArray(Charsets.UTF_8))
                digest.update(0)
                root.excludedCanonicalPaths
                    .filter { excluded ->
                        excluded == root.canonicalPath ||
                            CanonicalSourcePolicy.isAncestorOf(root.canonicalPath, excluded) ||
                            CanonicalSourcePolicy.isAncestorOf(excluded, root.canonicalPath)
                    }
                    .sorted()
                    .forEach {
                        digest.update(it.toByteArray(Charsets.UTF_8))
                        digest.update(0)
                    }
                digest.update(boundedFingerprint(root.directory).toByteArray(Charsets.UTF_8))
                digest.update(0)
            }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun boundedFingerprint(root: JavaFile): String {
        val digest = MessageDigest.getInstance("SHA-256")
        fun update(value: String) = digest.update(value.toByteArray(Charsets.UTF_8))
        update(root.absolutePath)
        update("|${root.lastModified()}|${root.length()}|")
        listFilesSafe(root)
            .orEmpty()
            .asSequence()
            .sortedBy { it.name.lowercase(Locale.ROOT) }
            .take(FINGERPRINT_ENTRY_LIMIT)
            .forEach {
                update(
                    "${it.name}\u0000${it.isDirectory}\u0000${it.lastModified()}\u0000${it.length()}\u0000"
                )
            }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun listFilesSafe(directory: JavaFile): List<JavaFile>? {
        val local =
            try {
                directory.listFiles()
            } catch (e: RuntimeException) {
                Log.d(TAG, "Direct listing unavailable for ${directory.path}", e)
                null
            }
        if (local != null) return local.toList()
        Log.w(TAG, "DirectFS source is unavailable or inaccessible: ${directory.path}")
        return null
    }

    private data class RootSnapshot(val root: PreparedRoot, val readable: Boolean)

    internal companion object {
        private const val TAG = "DirectFS"
        private const val SOURCE_TYPE = "DIRECT_FS"
        private const val FINGERPRINT_ENTRY_LIMIT = 128

        internal const val MAX_VISITED_DIRECTORIES = 100_000
        internal const val MAX_VISITED_FILES = 50_000

        fun isAllowedRoot(file: JavaFile): Boolean = DirectFsRootPolicy.isAllowedRoot(file)

        internal fun isExpectedRestrictedSharedStorageChild(
            directory: JavaFile,
            canonicalRoot: JavaFile,
        ): Boolean =
            DirectFsRootPolicy.isExpectedRestrictedSharedStorageChild(directory, canonicalRoot)

        /**
         * Whole-volume descent policy, kept as the default so existing callers and policy tests
         * keep describing the stricter of the two scopes.
         */
        internal fun shouldDescendIntoDirectory(
            name: String,
            scope: CanonicalSourcePolicy.Scope = CanonicalSourcePolicy.Scope.WHOLE_VOLUME,
            withHidden: Boolean = false,
            origin: CanonicalSourcePolicy.Origin =
                if (scope == CanonicalSourcePolicy.Scope.WHOLE_VOLUME) {
                    CanonicalSourcePolicy.Origin.WHOLE_VOLUME_FALLBACK
                } else {
                    CanonicalSourcePolicy.Origin.EXPLICIT
                },
        ): Boolean = DirectFsTraversal.shouldDescendIntoDirectory(name, scope, withHidden, origin)

        /**
         * Backend-enforced overlap policy. Picker filtering is advisory; a broad automatic fallback
         * must still be suppressed when a narrower explicit source is present.
         */
        internal fun applyOverlapPolicy(roots: List<PreparedRoot>): List<PreparedRoot> {
            val explicitPaths =
                roots
                    .filter { it.origin == CanonicalSourcePolicy.Origin.EXPLICIT }
                    .map { it.canonicalPath }
            return roots.filterNot { root ->
                root.origin == CanonicalSourcePolicy.Origin.WHOLE_VOLUME_FALLBACK &&
                    explicitPaths.any { explicit ->
                        CanonicalSourcePolicy.isAncestorOf(root.canonicalPath, explicit)
                    }
            }
        }
    }
}

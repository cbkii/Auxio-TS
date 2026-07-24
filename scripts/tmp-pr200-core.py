#!/usr/bin/env python3
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]


def write(path: str, content: str) -> None:
    target = ROOT / path
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(content.rstrip() + "\n", encoding="utf-8")


def replace_once(path: str, old: str, new: str) -> None:
    target = ROOT / path
    text = target.read_text(encoding="utf-8")
    if text.count(old) != 1:
        raise SystemExit(f"expected one match in {path}: {old[:80]!r}; found {text.count(old)}")
    target.write_text(text.replace(old, new), encoding="utf-8")


def regex_once(path: str, pattern: str, replacement: str, flags: int = re.S) -> None:
    target = ROOT / path
    text = target.read_text(encoding="utf-8")
    updated, count = re.subn(pattern, replacement, text, count=1, flags=flags)
    if count != 1:
        raise SystemExit(f"expected one regex match in {path}: {pattern[:100]!r}; found {count}")
    target.write_text(updated, encoding="utf-8")


write(
    "musikr/src/main/java/org/oxycblt/musikr/fs/RootGate.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * RootGate.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.musikr.fs

/** One bounded read-only root snapshot entry, relative to [RootTreeSnapshot.rootPath]. */
data class RootTreeEntry(
    val relativePath: String,
    val isDirectory: Boolean,
    val isSymlink: Boolean,
    val modifiedMs: Long,
    val size: Long,
)

/** A single-process, bounded snapshot of one configured storage root. */
data class RootTreeSnapshot(
    val rootPath: String,
    val entries: List<RootTreeEntry>,
)

/**
 * Narrow storage-only root authority exposed to Musikr.
 *
 * Implementations must construct the command internally. Callers cannot submit free-form shell
 * commands, package mutations or vendor-service operations through this boundary.
 */
interface RootGate {
    fun snapshotTreeSync(
        rootPath: String,
        maxDepth: Int = 32,
        timeoutMs: Long = 15_000L,
    ): RootTreeSnapshot?
}
''',
)

write(
    "musikr/src/main/java/org/oxycblt/musikr/fs/RootTreeSnapshotCodec.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * RootTreeSnapshotCodec.kt is part of Auxio.
 */
package org.oxycblt.musikr.fs

/** Strict parser for the fixed TSV emitted by the TS18 root storage snapshot command. */
object RootTreeSnapshotCodec {
    private const val DEFAULT_MAX_ENTRIES = 100_000

    fun parse(
        rootPath: String,
        text: String,
        maxEntries: Int = DEFAULT_MAX_ENTRIES,
    ): RootTreeSnapshot? {
        if (rootPath.isBlank() || maxEntries <= 0) return null
        val entries = ArrayList<RootTreeEntry>()
        for (line in text.lineSequence()) {
            if (line.isBlank()) continue
            if (entries.size >= maxEntries) return null
            val parts = line.split('\t', limit = 4)
            if (parts.size != 4) return null
            val type = parts[0]
            val relative = normaliseRelative(parts[3]) ?: return null
            val modifiedSeconds = parts[1].toLongOrNull() ?: return null
            val size = parts[2].toLongOrNull() ?: return null
            if (modifiedSeconds < 0L || size < 0L) return null
            entries +=
                RootTreeEntry(
                    relativePath = relative,
                    isDirectory = type == "d",
                    isSymlink = type == "l",
                    modifiedMs = modifiedSeconds * 1000L,
                    size = size,
                )
            if (type != "d" && type != "f" && type != "l") return null
        }
        return RootTreeSnapshot(rootPath = rootPath, entries = entries)
    }

    private fun normaliseRelative(value: String): String? {
        val clean = value.replace('\\', '/').trim('/')
        if (clean.isBlank() || clean.contains('\n') || clean.contains('\r') || clean.contains('\t')) {
            return null
        }
        val segments = clean.split('/')
        if (segments.any { it.isBlank() || it == "." || it == ".." }) return null
        return segments.joinToString("/")
    }
}
''',
)

write(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/RootStorageCommandPolicy.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * RootStorageCommandPolicy.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root

/** Builds the only recursive root command accepted by the storage root gate. */
object RootStorageCommandPolicy {
    private val usb = Regex("^/storage/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val rawUsb = Regex("^/mnt/media_rw/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val prepared =
        Regex("^/storage/auxio-root/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val uuid = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")

    fun isAllowedStorageRoot(value: String): Boolean {
        val path = value.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
        if (
            path.contains('\n') ||
                path.contains('\r') ||
                path.contains('\t') ||
                path.contains("/../") ||
                path.endsWith("/..") ||
                path.contains("/./") ||
                path.endsWith("/.")
        ) {
            return false
        }
        return path == "/sdcard" ||
            path.startsWith("/sdcard/") ||
            path == "/storage/emulated/0" ||
            path.startsWith("/storage/emulated/0/") ||
            usb.matches(path) ||
            rawUsb.matches(path) ||
            prepared.matches(path) ||
            uuid.matches(path)
    }

    fun buildSnapshotCommand(rootPath: String, maxDepth: Int): String {
        require(isAllowedStorageRoot(rootPath)) { "unsafe root storage path" }
        require(maxDepth in 1..32) { "invalid snapshot depth" }
        val quoted = shellQuote(rootPath.trimEnd('/'))
        return "root=$quoted; [ -d \"\$root\" ] || exit 4; " +
            "find \"\$root\" -xdev -mindepth 1 -maxdepth $maxDepth -print 2>/dev/null | " +
            "while IFS= read -r p; do " +
            "rel=\${p#\"\$root\"/}; [ -n \"\$rel\" ] || continue; " +
            "t=f; [ -d \"\$p\" ] && t=d; [ -L \"\$p\" ] && t=l; " +
            "m=\$(stat -c %Y \"\$p\" 2>/dev/null || echo 0); " +
            "s=\$(stat -c %s \"\$p\" 2>/dev/null || echo 0); " +
            "printf '%s\\t%s\\t%s\\t%s\\n' \"\$t\" \"\$m\" \"\$s\" \"\$rel\"; " +
            "done"
    }

    internal fun shellQuote(value: String): String = "'${value.replace("'", "'\"'\"'")}'"
}
''',
)

write(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * RootStateHolder.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.headunit.root

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.music.RootAccessPolicy
import org.oxycblt.musikr.fs.RootGate
import org.oxycblt.musikr.fs.RootTreeSnapshot
import org.oxycblt.musikr.fs.RootTreeSnapshotCodec

@Singleton
class RootStateHolder
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val processRunner: RootProcessRunner,
    private val journal: DiagnosticJournal,
) : RootGate {
    enum class State {
        Unknown,
        Available,
        Unavailable,
        Denied,
        TimedOut,
        UnsupportedForVariant,
        DisabledByUser,
    }

    private val stateLock = Any()
    private val probeLock = Any()
    private val storageOperationLock = Any()
    private var consentGeneration = 0L

    @Volatile var state: State = State.Unknown
        private set

    init {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) state = State.UnsupportedForVariant
    }

    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    private fun userEnabled(): Boolean =
        BuildConfig.TOPWAY_COMPAT_FLAVOR && prefs.getBoolean(KEY_USE_ROOT_FS, false)

    /** Snapshot persisted storage-root consent without invoking `su`. */
    fun isUserEnabled(): Boolean = userEnabled()

    /** Persist an explicit storage-only root decision and reset the bounded probe generation. */
    fun setUserEnabled(enabled: Boolean) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
            return
        }
        prefs.edit {
            putBoolean(KEY_USE_ROOT_FS, enabled)
            putString(
                context.getString(R.string.set_key_root_access_policy),
                if (enabled) RootAccessPolicy.ON_DEMAND.name else RootAccessPolicy.OFF.name,
            )
        }
        synchronized(stateLock) {
            consentGeneration += 1L
            state = if (enabled) State.Unknown else State.DisabledByUser
        }
    }

    fun stateSnapshot(): State {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return State.UnsupportedForVariant
        if (!userEnabled()) return State.DisabledByUser
        if (state == State.DisabledByUser) state = State.Unknown
        return state
    }

    /** Fixed read-only TS18 compatibility probes remain separate from storage root authority. */
    fun runTs18ProbeSync(probe: org.oxycblt.auxio.headunit.root.dofun.Ts18RootProbe): String? {
        if (stateSnapshot() == State.Unknown || stateSnapshot() == State.TimedOut) probeSync()
        if (stateSnapshot() != State.Available) return null
        return successfulStdout(
            processRunner.runRootCommand(
                probe.command,
                timeoutMs = TS18_OPERATION_TIMEOUT_MS,
                maxOutputBytes = TS18_OPERATION_OUTPUT_BYTES,
            )
        )
    }

    fun probeSync(): State =
        synchronized(probeLock) {
            val generation =
                synchronized(stateLock) {
                    if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                        state = State.UnsupportedForVariant
                        return state
                    }
                    if (!userEnabled()) {
                        state = State.DisabledByUser
                        return state
                    }
                    if (state == State.DisabledByUser) state = State.Unknown
                    if (state != State.Unknown && state != State.TimedOut) return state
                    consentGeneration
                }
            val probed =
                when (
                    val result =
                        processRunner.runRootCommand(
                            "id",
                            timeoutMs = ROOT_PROBE_TIMEOUT_MS,
                            maxOutputBytes = ROOT_PROBE_OUTPUT_BYTES,
                        )
                ) {
                    is RootProcessResult.Success ->
                        if (result.stdout.contains("uid=0")) State.Available else State.Denied
                    is RootProcessResult.NonZeroExit -> State.Denied
                    RootProcessResult.TimedOut -> State.TimedOut
                    RootProcessResult.OutputLimitExceeded -> State.Denied
                    is RootProcessResult.ExecutionFailure -> State.Unavailable
                }
            synchronized(stateLock) {
                if (generation == consentGeneration) {
                    state = if (userEnabled()) probed else State.DisabledByUser
                }
                state
            }
        }

    /**
     * Take one recursive, bounded, read-only snapshot for a configured volume.
     *
     * The command is constructed internally from an allow-listed path. No free-form shell command
     * crosses the Musikr root boundary and no package mutation is available here.
     */
    override fun snapshotTreeSync(
        rootPath: String,
        maxDepth: Int,
        timeoutMs: Long,
    ): RootTreeSnapshot? =
        synchronized(storageOperationLock) {
            if (!RootStorageCommandPolicy.isAllowedStorageRoot(rootPath)) return null
            if (maxDepth !in 1..32 || timeoutMs !in 1L..MAX_STORAGE_TIMEOUT_MS) return null
            if (stateSnapshot() == State.Unknown || stateSnapshot() == State.TimedOut) probeSync()
            if (stateSnapshot() != State.Available) return null
            val command = RootStorageCommandPolicy.buildSnapshotCommand(rootPath, maxDepth)
            when (
                val result =
                    processRunner.runRootCommand(
                        command,
                        timeoutMs = timeoutMs,
                        maxOutputBytes = ROOT_SNAPSHOT_OUTPUT_BYTES,
                    )
            ) {
                is RootProcessResult.Success ->
                    RootTreeSnapshotCodec.parse(
                        rootPath = rootPath.trimEnd('/'),
                        text = result.stdout,
                        maxEntries = MAX_ROOT_SNAPSHOT_ENTRIES,
                    )
                RootProcessResult.TimedOut -> {
                    state = State.TimedOut
                    journal.log(
                        DiagnosticJournal.CAT_STORAGE,
                        "Root volume snapshot timed out",
                        "root=$rootPath timeoutMs=$timeoutMs",
                    )
                    null
                }
                RootProcessResult.OutputLimitExceeded -> {
                    journal.log(
                        DiagnosticJournal.CAT_STORAGE,
                        "Root volume snapshot output limit exceeded",
                        "root=$rootPath maxBytes=$ROOT_SNAPSHOT_OUTPUT_BYTES",
                    )
                    null
                }
                is RootProcessResult.NonZeroExit,
                is RootProcessResult.ExecutionFailure -> null
            }
        }

    /** Read only the fixed Magisk-prepared volume manifest. */
    fun readPreparedVolumeManifestSync(): String? =
        synchronized(storageOperationLock) {
            if (stateSnapshot() == State.Unknown || stateSnapshot() == State.TimedOut) probeSync()
            if (stateSnapshot() != State.Available) return null
            successfulStdout(
                processRunner.runRootCommand(
                    "cat '$PREPARED_VOLUME_MANIFEST'",
                    timeoutMs = MANIFEST_READ_TIMEOUT_MS,
                    maxOutputBytes = MANIFEST_OUTPUT_BYTES,
                )
            )
        }

    private fun successfulStdout(result: RootProcessResult): String? =
        when (result) {
            is RootProcessResult.Success -> result.stdout
            RootProcessResult.TimedOut -> {
                state = State.TimedOut
                null
            }
            is RootProcessResult.NonZeroExit,
            RootProcessResult.OutputLimitExceeded,
            is RootProcessResult.ExecutionFailure -> null
        }

    private companion object {
        const val KEY_USE_ROOT_FS = "auxio_use_root_fs"
        const val ROOT_PROBE_TIMEOUT_MS = 2_000L
        const val ROOT_PROBE_OUTPUT_BYTES = 4 * 1024
        const val TS18_OPERATION_TIMEOUT_MS = 5_000L
        const val TS18_OPERATION_OUTPUT_BYTES = 64 * 1024
        const val ROOT_SNAPSHOT_OUTPUT_BYTES = 16 * 1024 * 1024
        const val MAX_ROOT_SNAPSHOT_ENTRIES = 100_000
        const val MAX_STORAGE_TIMEOUT_MS = 20_000L
        const val MANIFEST_READ_TIMEOUT_MS = 3_000L
        const val MANIFEST_OUTPUT_BYTES = 256 * 1024
        const val PREPARED_VOLUME_MANIFEST = "/data/adb/auxio-ts-root/volumes.tsv"
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface RootEntryPoint {
    fun rootGate(): RootStateHolder
}
''',
)

# DirectFS: keep normal app-UID traversal as the actual scan path. A single root snapshot is used only
# to classify an inaccessible configured root; it is never converted into playable app-UID Files.
direct_path = "musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt"
replace_once(
    direct_path,
    "    private val sourceFailures = ConcurrentHashMap<String, String>()\n",
    "    private val sourceFailures = ConcurrentHashMap<String, String>()\n"
    "    private val rootSnapshotChecked = ConcurrentHashMap.newKeySet<String>()\n"
    "    private val rootSnapshotOnly = ConcurrentHashMap.newKeySet<String>()\n",
)
regex_once(
    direct_path,
    r"    private fun listFilesSafe\(directory: JavaFile\): List<DirectEntry>\? \{.*?\n    \}\n\n    private data class RootSnapshot",
    r'''    private fun listFilesSafe(directory: JavaFile): List<DirectEntry>? {
        val local =
            try {
                directory.listFiles()
            } catch (e: RuntimeException) {
                Log.d(TAG, "Direct listing unavailable for ${directory.path}", e)
                null
            }
        if (local != null) {
            return local.map {
                DirectEntry(
                    javaFile = it,
                    name = it.name,
                    isDirectory = it.isDirectory,
                    isSymlink = isSymbolicLinkCompat(it),
                    modifiedMs = it.lastModified(),
                    size = it.length(),
                )
            }
        }

        val configuredRoot = configuredRootFor(directory)
        if (configuredRoot != null && rootGate != null) {
            val key = configuredRoot.absolutePath
            if (rootSnapshotChecked.add(key)) {
                if (rootGate.snapshotTreeSync(key, MAX_DEPTH, ROOT_SNAPSHOT_TIMEOUT_MS) != null) {
                    rootSnapshotOnly.add(key)
                }
            }
            if (key in rootSnapshotOnly) {
                Log.w(
                    TAG,
                    "Root can snapshot $key but Auxio cannot open it as the app UID; " +
                        "use a validated /storage or prepared alias",
                )
            }
        }
        Log.w(TAG, "DirectFS source is unavailable or inaccessible: ${directory.path}")
        return null
    }

    private fun configuredRootFor(directory: JavaFile): JavaFile? =
        roots
            .asSequence()
            .mapNotNull { it.uri.path?.let(::JavaFile) }
            .mapNotNull(::canonicalFileOrNull)
            .filter { root -> isWithinCanonicalRoot(directory, root) }
            .maxByOrNull { it.absolutePath.length }

    private data class RootSnapshot''',
)
regex_once(
    direct_path,
    r"\n    private fun parseRootEntry\(parent: JavaFile, line: String\): DirectEntry\? \{.*?\n    \}\n\n    private fun getMimeType",
    "\n    private fun getMimeType",
)
regex_once(
    direct_path,
    r"\n        fun shellQuote\(value: String\): String = .*?\n\n        fun buildRootListCommand\(directory: String\): String \{.*?\n        \}\n",
    "\n",
)
replace_once(
    direct_path,
    "        private const val QUEUE_POLL_INTERVAL_MS = 100L\n",
    "        private const val QUEUE_POLL_INTERVAL_MS = 100L\n"
    "        private const val ROOT_SNAPSHOT_TIMEOUT_MS = 15_000L\n",
)

# Topway source discovery: include prepared aliases and use one root snapshot per inaccessible root.
topway_path = "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicy.kt"
replace_once(
    topway_path,
    "    private val MEDIA_RW_USB_SOURCE_REGEX =\n        Regex(\"^/mnt/media_rw/usbdisk\\\\d+(/.*)?$\", RegexOption.IGNORE_CASE)\n",
    "    private val MEDIA_RW_USB_SOURCE_REGEX =\n"
    "        Regex(\"^/mnt/media_rw/usbdisk\\\\d+(/.*)?$\", RegexOption.IGNORE_CASE)\n"
    "    private val PREPARED_USB_SOURCE_REGEX =\n"
    "        Regex(\"^/storage/auxio-root/usbdisk\\\\d+(/.*)?$\", RegexOption.IGNORE_CASE)\n",
)
replace_once(
    topway_path,
    "        discoverChildren(mediaRwRoot, removableOnly = true).filterTo(out) {\n            isAccessibleCandidate(it)\n        }\n",
    "        discoverChildren(mediaRwRoot, removableOnly = true).filterTo(out) {\n"
    "            isAccessibleCandidate(it)\n"
    "        }\n"
    "        discoverChildren(File(storageRoot, \"auxio-root\"), removableOnly = true).filterTo(out) {\n"
    "            isAccessibleCandidate(it)\n"
    "        }\n",
)
replace_once(
    topway_path,
    "        if (enforceSafeRoot && !isAllowedSourceCandidate(root.absolutePath)) return\n        val canonicalCache = mutableMapOf<String, File?>()\n",
    "        if (enforceSafeRoot && !isAllowedSourceCandidate(root.absolutePath)) return\n"
    "        val directRootReadable = runCatching { root.listFiles() }.getOrNull() != null\n"
    "        if (!directRootReadable && rootGate != null) {\n"
    "            val snapshot =\n"
    "                rootGate.snapshotTreeSync(root.absolutePath, MAX_SCAN_DEPTH, MAX_SCAN_ELAPSED_MS)\n"
    "                    ?: return\n"
    "            snapshot.entries\n"
    "                .asSequence()\n"
    "                .filter { !it.isDirectory && !it.isSymlink }\n"
    "                .filter { entry ->\n"
    "                    entry.relativePath.substringAfterLast('.', \"\").lowercase() in AUDIO_EXTENSIONS\n"
    "                }\n"
    "                .map { entry ->\n"
    "                    entry.relativePath.substringBeforeLast('/', \"\")\n"
    "                }\n"
    "                .distinct()\n"
    "                .take(MAX_CANDIDATES - out.size)\n"
    "                .mapTo(out) { relative ->\n"
    "                    if (relative.isBlank()) root.absolutePath else File(root, relative).absolutePath\n"
    "                }\n"
    "            return\n"
    "        }\n"
    "        val canonicalCache = mutableMapOf<String, File?>()\n",
)
replace_once(
    topway_path,
    "            val children = listFilesSafe(dir, rootGate) ?: continue\n",
    "            val children = listFilesSafe(dir) ?: continue\n",
)
regex_once(
    topway_path,
    r"    private fun listFilesSafe\(dir: File, rootGate: RootGate\?\): List<FileEntry>\? \{.*?\n    \}\n\n    fun canListRootBackedDirectory\(path: String, rootGate: RootGate\): Boolean =\n        isAllowedSourceCandidate\(path\) && listFilesSafe\(File\(path\), rootGate\) != null\n\n    private fun buildRootListCommand\(directory: String\): String \{.*?\n    \}\n\n    internal fun parseRootEntry\(parent: File, line: String\): FileEntry\? \{.*?\n    \}\n",
    r'''    private fun listFilesSafe(dir: File): List<FileEntry>? {
        val direct =
            try {
                dir.listFiles()
            } catch (e: Exception) {
                L.w(e, "Cannot list music candidate directory ${dir.absolutePath}")
                null
            }
        return direct?.map { FileEntry(it, isDirectory = it.isDirectory, isFile = it.isFile) }
    }

    fun canListRootBackedDirectory(path: String, rootGate: RootGate): Boolean =
        isAllowedSourceCandidate(path) &&
            rootGate.snapshotTreeSync(path, MAX_SCAN_DEPTH, MAX_SCAN_ELAPSED_MS) != null
''',
)
replace_once(
    topway_path,
    "                MEDIA_RW_USB_SOURCE_REGEX.matches(clean) ||\n                STORAGE_UUID_SOURCE_REGEX.matches(clean)\n",
    "                MEDIA_RW_USB_SOURCE_REGEX.matches(clean) ||\n"
    "                PREPARED_USB_SOURCE_REGEX.matches(clean) ||\n"
    "                STORAGE_UUID_SOURCE_REGEX.matches(clean)\n",
)
replace_once(
    topway_path,
    "                MEDIA_RW_USB_SOURCE_REGEX.matches(canonical) ||\n                STORAGE_UUID_SOURCE_REGEX.matches(canonical)\n",
    "                MEDIA_RW_USB_SOURCE_REGEX.matches(canonical) ||\n"
    "                PREPARED_USB_SOURCE_REGEX.matches(canonical) ||\n"
    "                STORAGE_UUID_SOURCE_REGEX.matches(canonical)\n",
)
replace_once(
    topway_path,
    "        USB_DISK_SOURCE_REGEX.matches(path) ||\n            MEDIA_RW_USB_SOURCE_REGEX.matches(path) ||\n            STORAGE_UUID_SOURCE_REGEX.matches(path)\n",
    "        USB_DISK_SOURCE_REGEX.matches(path) ||\n"
    "            MEDIA_RW_USB_SOURCE_REGEX.matches(path) ||\n"
    "            PREPARED_USB_SOURCE_REGEX.matches(path) ||\n"
    "            STORAGE_UUID_SOURCE_REGEX.matches(path)\n",
)

write(
    "musikr/src/test/java/org/oxycblt/musikr/fs/RootTreeSnapshotCodecTest.kt",
    r'''package org.oxycblt.musikr.fs

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RootTreeSnapshotCodecTest {
    @Test
    fun parsesBoundedTypedEntries() {
        val snapshot =
            RootTreeSnapshotCodec.parse(
                "/mnt/media_rw/usbdisk0",
                "d\t10\t0\tMusic\nf\t11\t4\tMusic/track.flac\nl\t12\t1\tMusic/link\n",
            )
        requireNotNull(snapshot)
        assertEquals(3, snapshot.entries.size)
        assertTrue(snapshot.entries[0].isDirectory)
        assertEquals(11_000L, snapshot.entries[1].modifiedMs)
        assertTrue(snapshot.entries[2].isSymlink)
    }

    @Test
    fun rejectsTraversalAndEntryOverflow() {
        assertNull(RootTreeSnapshotCodec.parse("/storage/usbdisk0", "f\t1\t1\t../escape.mp3\n"))
        assertNull(
            RootTreeSnapshotCodec.parse(
                "/storage/usbdisk0",
                "f\t1\t1\ta.mp3\nf\t1\t1\tb.mp3\n",
                maxEntries = 1,
            )
        )
    }
}
''',
)

write(
    "app/src/test/java/org/oxycblt/auxio/headunit/root/RootStorageCommandPolicyTest.kt",
    r'''package org.oxycblt.auxio.headunit.root

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RootStorageCommandPolicyTest {
    @Test
    fun acceptsOnlyStorageBackingsAndPreparedAliases() {
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/usbdisk0/Music"))
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/mnt/media_rw/usbdisk1"))
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/auxio-root/usbdisk1"))
        assertFalse(RootStorageCommandPolicy.isAllowedStorageRoot("/data/local/tmp"))
        assertFalse(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/usbdisk0/../data"))
    }

    @Test
    fun commandIsOneBoundedRecursiveSnapshot() {
        val command =
            RootStorageCommandPolicy.buildSnapshotCommand(
                "/mnt/media_rw/usbdisk0/Music's",
                maxDepth = 8,
            )
        assertTrue(command.contains("find \"\$root\" -xdev"))
        assertTrue(command.contains("-maxdepth 8"))
        assertTrue(command.contains("Music'\"'\"'s"))
        assertFalse(command.contains("pm disable-user"))
    }
}
''',
)

write(
    "musikr/src/test/java/org/oxycblt/musikr/fs/direct/DirectFsRootPolicyTest.kt",
    r'''/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.musikr.fs.direct

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.isAllowedRoot

class DirectFsRootPolicyTest {
    @Test
    fun rejectsProtectedRoots() {
        assertFalse(isAllowedRoot(File("/")))
        assertFalse(isAllowedRoot(File("/system")))
        assertFalse(isAllowedRoot(File("/vendor")))
        assertFalse(isAllowedRoot(File("/data")))
    }

    @Test
    fun allowsAppFacingPreparedAndRawBackingRoots() {
        assertTrue(isAllowedRoot(File("/storage/usbdisk0")))
        assertTrue(isAllowedRoot(File("/storage/auxio-root/usbdisk0")))
        assertTrue(isAllowedRoot(File("/mnt/media_rw/usbdisk0")))
    }
}
''',
)

# Update the existing Topway root snapshot test and remove parser-specific assertions.
test_path = "app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicyDiscoveryTest.kt"
regex_once(
    test_path,
    r"    @Test\n    fun rootBackedEntriesPreserveTypesForAudioParentDiscovery\(\) \{.*?\n    \}\n\n    @Test\n    fun rootEntryParserPreservesDirectoryAndFileTypes\(\) \{.*?\n    \}\n",
    r'''    @Test
    fun oneRootSnapshotDiscoversAudioParents() {
        val root = File("/storage/usbdisk9/Music")
        val out = linkedSetOf<String>()
        var snapshots = 0
        val gate =
            object : org.oxycblt.musikr.fs.RootGate {
                override fun snapshotTreeSync(
                    rootPath: String,
                    maxDepth: Int,
                    timeoutMs: Long,
                ): org.oxycblt.musikr.fs.RootTreeSnapshot {
                    snapshots++
                    return org.oxycblt.musikr.fs.RootTreeSnapshot(
                        rootPath,
                        listOf(
                            org.oxycblt.musikr.fs.RootTreeEntry(
                                relativePath = "Album/track.flac",
                                isDirectory = false,
                                isSymlink = false,
                                modifiedMs = 0,
                                size = 4,
                            )
                        ),
                    )
                }
            }

        TopwaySourcePolicy.discoverAudioParents(root, out, rootGate = gate)

        assertEquals(1, snapshots)
        assertTrue(out.contains(File(root, "Album").absolutePath))
    }
''',
)

print("PR200 core patches applied")

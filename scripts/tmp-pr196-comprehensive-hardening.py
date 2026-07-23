#!/usr/bin/env python3
"""Apply the final scoped PR #196 hardening findings exactly once."""

from pathlib import Path


def read(path: str) -> str:
    return Path(path).read_text(encoding="utf-8")


def write(path: str, text: str) -> None:
    Path(path).write_text(text, encoding="utf-8", newline="\n")


def replace_once(path: str, old: str, new: str) -> None:
    text = read(path)
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{path}: expected exactly one match, found {count}: {old[:160]!r}")
    write(path, text.replace(old, new, 1))


write(
    "app/src/main/java/org/oxycblt/auxio/music/service/StartupScanAuthorityPolicy.kt",
    '''/*
 * Copyright (c) 2026 Auxio Project
 * StartupScanAuthorityPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music.service

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.os.SystemClock
import androidx.core.content.ContextCompat
import java.io.File
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.StartupLibraryPolicy
import org.oxycblt.auxio.music.locations.LocationMode

/** Origin of a service startup request. */
enum class StartupScanOrigin {
    USER_VISIBLE,
    BACKGROUND,
    EARLY_PRESTART;
}

/**
 * Compatibility-boundary policy that maps trusted lifecycle origin and current source authority to
 * automatic scan authority.
 *
 * The shared music startup core receives only the resulting boolean. Topway boot/ACC restrictions,
 * Android permissions and path readability stay outside generic source/cache policy.
 */
object StartupScanAuthorityPolicy {
    private var trustedUserVisibleUntilElapsedMs = 0L

    /** Issue a short-lived, process-local token immediately before MainActivity starts the service. */
    @Synchronized
    fun issueTrustedUserVisibleStart() {
        trustedUserVisibleUntilElapsedMs =
            SystemClock.elapsedRealtime() + TRUSTED_USER_VISIBLE_WINDOW_MS
    }

    /** Consume the one-shot token; exported service callers cannot mint it through Intent extras. */
    @Synchronized
    fun consumeTrustedUserVisibleStart(): Boolean {
        val trusted =
            trustedUserVisibleUntilElapsedMs > 0L &&
                SystemClock.elapsedRealtime() <= trustedUserVisibleUntilElapsedMs
        trustedUserVisibleUntilElapsedMs = 0L
        return trusted
    }

    fun allowAutomaticScan(
        topwayCompatFlavor: Boolean,
        origin: StartupScanOrigin,
        sourceAuthority: Boolean,
    ): Boolean = sourceAuthority && (!topwayCompatFlavor || origin == StartupScanOrigin.USER_VISIBLE)

    /** Lightweight current authority check; never invokes root and never enumerates a source tree. */
    fun hasCurrentSourceAuthority(context: Context, settings: MusicSettings): Boolean {
        if (
            !StartupLibraryPolicy.isMusicSourceConfigured(
                settings.locationMode,
                settings.configuredSourceCount,
            )
        ) {
            return false
        }
        return when (settings.locationMode) {
            LocationMode.MEDIA_STORE -> hasStoragePermission(context)
            LocationMode.SAF -> settings.safQuery.source.all { hasUriReadAuthority(context, it.uri) }
            LocationMode.DIRECT_FS ->
                settings.safQuery.source.all { location ->
                    val path = location.uri.path ?: return@all false
                    val file = File(path)
                    file.exists() && file.isDirectory && file.canRead()
                }
        }
    }

    private fun hasStoragePermission(context: Context): Boolean {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        return ContextCompat.checkSelfPermission(context, permission) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun hasUriReadAuthority(context: Context, uri: android.net.Uri): Boolean {
        if (uri.scheme == "file") {
            val file = uri.path?.let(::File) ?: return false
            return file.exists() && file.canRead()
        }
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (
            context.checkUriPermission(uri, Process.myPid(), Process.myUid(), flags) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return context.contentResolver.persistedUriPermissions.any { permission ->
            permission.isReadPermission && uri.toString().startsWith(permission.uri.toString())
        }
    }

    private const val TRUSTED_USER_VISIBLE_WINDOW_MS = 5_000L
}
''',
)

replace_once(
    "app/src/main/java/org/oxycblt/auxio/MainActivity.kt",
    "import org.oxycblt.auxio.music.MusicSettings\n",
    "import org.oxycblt.auxio.music.MusicSettings\n"
    "import org.oxycblt.auxio.music.service.StartupScanAuthorityPolicy\n",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/MainActivity.kt",
    """            startService(
                Intent(this, serviceClass)
""",
    """            StartupScanAuthorityPolicy.issueTrustedUserVisibleStart()
            startService(
                Intent(this, serviceClass)
""",
)

replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    """    private var activeStartupOrigin: StartupScanOrigin? = null
    private var pendingStartupOrigin: StartupScanOrigin? = null
""",
    """    private var activeStartupOrigin: StartupScanOrigin? = null
    private var activeStartupAutomaticScanAllowed = false
    private var pendingStartupOrigin: StartupScanOrigin? = null
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    """                    activeStartupOrigin = null
                    pendingStartupOrigin = null
""",
    """                    activeStartupOrigin = null
                    activeStartupAutomaticScanAllowed = false
                    pendingStartupOrigin = null
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    """            if (startupJob?.isActive == true) {
                val activePriority = activeStartupOrigin?.priority ?: 0
                if (origin.priority > activePriority) {
                    pendingStartupOrigin = StartupScanOrigin.merge(pendingStartupOrigin, origin)
                    L.d(
                        "Queued higher-priority startup origin while load is active " +
                            "[active=$activeStartupOrigin pending=$pendingStartupOrigin]"
                    )
                } else {
                    L.d(
                        "Startup library load already running; ignoring duplicate " +
                            "[active=$activeStartupOrigin requested=$origin]"
                    )
                }
                return
            }
            val automaticScanAllowed =
                StartupScanAuthorityPolicy.allowAutomaticScan(
                    topwayCompatFlavor = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    origin = origin,
                )
            activeStartupOrigin = origin
""",
    """            val sourceAuthority =
                StartupScanAuthorityPolicy.hasCurrentSourceAuthority(workerContext, musicSettings)
            val automaticScanAllowed =
                StartupScanAuthorityPolicy.allowAutomaticScan(
                    topwayCompatFlavor = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    origin = origin,
                    sourceAuthority = sourceAuthority,
                )
            if (startupJob?.isActive == true) {
                if (automaticScanAllowed && !activeStartupAutomaticScanAllowed) {
                    pendingStartupOrigin = origin
                    L.d(
                        "Queued startup because effective scan authority increased " +
                            "[active=$activeStartupOrigin pending=$pendingStartupOrigin]"
                    )
                } else {
                    L.d(
                        "Startup library load already running; ignoring duplicate " +
                            "[active=$activeStartupOrigin requested=$origin " +
                            "activeAuthority=$activeStartupAutomaticScanAllowed " +
                            "requestedAuthority=$automaticScanAllowed]"
                    )
                }
                return
            }
            activeStartupOrigin = origin
            activeStartupAutomaticScanAllowed = automaticScanAllowed
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    """                                    activeStartupOrigin = null
                                    pendingStartupOrigin = null
""",
    """                                    activeStartupOrigin = null
                                    activeStartupAutomaticScanAllowed = false
                                    pendingStartupOrigin = null
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    """                                    activeStartupOrigin = null
                                    pendingStartupOrigin.also { pendingStartupOrigin = null }
""",
    """                                    activeStartupOrigin = null
                                    activeStartupAutomaticScanAllowed = false
                                    pendingStartupOrigin.also { pendingStartupOrigin = null }
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    """    fun createNotification(post: (ForegroundServiceNotification?) -> Unit) {
""",
    """    /** Side-effect-free snapshot for prestart foreground-owner restoration. */
    fun hasForegroundWork(): Boolean =
        musicRepository.indexingState is IndexingState.Indexing || musicSettings.shouldBeObserving

    fun createNotification(post: (ForegroundServiceNotification?) -> Unit) {
""",
)

replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/service/MusicServiceFragment.kt",
    """    /** Snapshot whether indexing/observation currently owns a foreground notification. */
    fun hasForegroundWork(): Boolean {
        var active = false
        createNotification { active = it != null }
        return active
    }
""",
    """    /** Snapshot without mutating notification throttling or progress state. */
    fun hasForegroundWork(): Boolean = indexer.hasForegroundWork()
""",
)

replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    "suspend fun startup(worker: IndexingWorker, automaticScanAllowed: Boolean = true)",
    "suspend fun startup(worker: IndexingWorker, automaticScanAllowed: Boolean)",
)

# Root-only listings cannot provide app-readable file descriptors for extraction/playback.
replace_once(
    "musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt",
    '''        val rootList =
            try {
                rootGate
                    ?.runRootCommandSync(buildRootListCommand(directory.absolutePath))
                    ?.mapNotNull { parseRootEntry(directory, it) }
            } catch (e: RuntimeException) {
                Log.w(TAG, "Root-assisted DirectFS listing failed for ${directory.path}", e)
                null
            }
        if (rootList != null) return rootList
        Log.w(TAG, "DirectFS root is unavailable or inaccessible: ${directory.path}")
        return null
''',
    '''        if (rootGate != null) {
            Log.w(
                TAG,
                "Rejecting root-only DirectFS source without app-readable descriptors: ${directory.path}",
            )
        } else {
            Log.w(TAG, "DirectFS source is unavailable or inaccessible: ${directory.path}")
        }
        return null
''',
)

# Manual raw mount paths may be inspected with root, but remain unsaveable until end-to-end reads
# are implemented. Ordinary /storage aliases remain the supported runtime source.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    "import android.content.ActivityNotFoundException\n",
    "import android.content.ActivityNotFoundException\nimport android.content.Intent\n",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    "import android.provider.DocumentsContract\n",
    "import android.provider.DocumentsContract\nimport android.provider.Settings as AndroidSettings\n",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    "import androidx.lifecycle.lifecycleScope\n",
    "import androidx.lifecycle.lifecycleScope\nimport androidx.preference.PreferenceManager\n",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """        if (mode != LocationMode.SAF && !hasStoragePermission) {
            showStoragePermissionExplanation()
        }
""",
    """        // MediaStore cannot proceed without framework permission. DirectFS defers the
        // decision until a concrete path is validated, because raw and ordinary paths have
        // different authority requirements.
        if (mode == LocationMode.MEDIA_STORE && !hasStoragePermission) {
            showStoragePermissionExplanation()
        }
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """                ManualPathValidation.OK,
                ManualPathValidation.ROOT_BACKED -> Unit
""",
    """                ManualPathValidation.OK -> Unit
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """        ROOT_UNAVAILABLE(R.string.set_path_root_unavailable),
        ROOT_BACKED(R.string.lbl_ok),
        OPEN_FAILED(R.string.set_path_open_failed),
""",
    """        ROOT_UNAVAILABLE(R.string.set_path_root_unavailable),
        OPEN_FAILED(R.string.set_path_open_failed),
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """            when {
                isRootBackedRawDirectPath(path, file, directTs18Path) ->
                    ManualPathValidation.ROOT_BACKED
                rawRootCandidate && (!file.exists() || !file.isDirectory || !file.canRead()) ->
                    ManualPathValidation.ROOT_UNAVAILABLE
""",
    """            when {
                rawRootCandidate &&
                    rootGate.stateSnapshot() != RootStateHolder.State.Available &&
                    (!file.exists() || !file.isDirectory || !file.canRead()) ->
                    ManualPathValidation.ROOT_UNAVAILABLE
                rawRootCandidate && (!file.exists() || !file.isDirectory || !file.canRead()) ->
                    // Root can inspect this directory, but current scanning/playback still opens
                    // app-UID file:// URIs. Keep the raw mount unavailable and direct users to its
                    // /storage/usbdiskN alias rather than saving a source that cannot play.
                    ManualPathValidation.UNREADABLE
""",
)
old_root_helper = '''
    private fun isRootBackedRawDirectPath(
        path: String,
        file: File,
        directTs18Path: Boolean,
    ): Boolean {
        if (!directTs18Path || !path.startsWith("/mnt/media_rw/usbdisk")) return false
        if (file.exists() && file.isDirectory && file.canRead()) return false
        if (rootGate.stateSnapshot() != RootStateHolder.State.Available) return false
        return TopwaySourcePolicy.canListRootBackedDirectory(path, rootGate)
    }
'''
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    old_root_helper,
    "",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """    private fun requestStoragePermission() {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

        val launcher =
""",
    """    private fun requestStoragePermission() {
        val permission = requiredStoragePermission()
        if (
            storagePermissionRequestedBefore() &&
                !shouldShowRequestPermissionRationale(permission)
        ) {
            showOpenAppSettingsDialog()
            return
        }

        val launcher =
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """            L.d("Requesting storage permission: $permission")
            launcher.launch(permission)
""",
    """            L.d("Requesting storage permission: $permission")
            markStoragePermissionRequested()
            launcher.launch(permission)
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """    private fun updateSaveButtonState() {
""",
    """    private fun requiredStoragePermission(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    private fun storagePermissionRequestedBefore(): Boolean =
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getBoolean(KEY_STORAGE_PERMISSION_REQUESTED, false)

    private fun markStoragePermissionRequested() {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .edit()
            .putBoolean(KEY_STORAGE_PERMISSION_REQUESTED, true)
            .apply()
    }

    private fun showOpenAppSettingsDialog() {
        val ctx = context ?: return
        AlertDialog.Builder(ctx)
            .setTitle(R.string.recovery_permission_title)
            .setMessage(R.string.recovery_permission_denied)
            .setPositiveButton(R.string.recovery_action_open_settings) { _, _ ->
                startActivity(
                    Intent(
                        AndroidSettings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", ctx.packageName, null),
                    )
                )
            }
            .setNegativeButton(R.string.lbl_cancel, null)
            .show()
    }

    private fun updateSaveButtonState() {
""",
)
# Add a shared key in companion if one exists, otherwise inject before class close.
text = read("app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt")
if "KEY_STORAGE_PERMISSION_REQUESTED" not in text:
    raise SystemExit("LocationsDialog key insertion precondition failed")
# The key is referenced but not declared yet.
text = text.replace(
    "\n    private fun updateSaveButtonState() {",
    "\n    private fun updateSaveButtonState() {",
    1,
)
text = text.replace(
    "\n}\n",
    "\n\n    private companion object {\n        const val KEY_STORAGE_PERMISSION_REQUESTED = \"auxio_storage_permission_requested\"\n    }\n}\n",
    1,
)
write("app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt", text)

write(
    "app/src/main/java/org/oxycblt/auxio/home/list/LibraryRecoveryPolicy.kt",
    '''/*
 * Copyright (c) 2026 Auxio Project
 * LibraryRecoveryPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.home.list

import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.StartupLibraryStatus
import org.oxycblt.auxio.music.StartupReadinessState

/** Pure first-launch and missing-library recovery policy. */
object LibraryRecoveryPolicy {
    data class Input(
        val empty: Boolean,
        val indexingState: IndexingState?,
        val startupState: StartupReadinessState,
        val libraryStatus: StartupLibraryStatus,
        val sourceConfigured: Boolean,
        val storagePermissionRequired: Boolean,
        val storagePermissionGranted: Boolean,
        val rootSupported: Boolean,
        val rootRequired: Boolean,
        val rootState: RootStateHolder.State,
        val lastScanFailed: Boolean,
    )

    enum class Kind {
        HIDDEN,
        WAITING,
        PERMISSION_REQUIRED,
        SOURCE_REQUIRED,
        SOURCE_UNAVAILABLE,
        CACHE_UNAVAILABLE,
        INDEXING,
        EMPTY,
        FAILED,
    }

    enum class Action {
        GRANT_PERMISSION,
        CHOOSE_SOURCE,
        REFRESH,
        RESCAN,
        ENABLE_ROOT,
    }

    data class ActionItem(val action: Action, val enabled: Boolean = true)

    data class State(
        val kind: Kind,
        val showProgress: Boolean,
        val primary: ActionItem? = null,
        val secondary: ActionItem? = null,
        val tertiary: ActionItem? = null,
    ) {
        val visible: Boolean
            get() = kind != Kind.HIDDEN
    }

    fun resolve(input: Input): State {
        if (!input.empty) return State(Kind.HIDDEN, showProgress = false)

        if (input.storagePermissionRequired && !input.storagePermissionGranted) {
            return State(
                kind = Kind.PERMISSION_REQUIRED,
                showProgress = false,
                primary = ActionItem(Action.GRANT_PERMISSION),
                secondary = ActionItem(Action.CHOOSE_SOURCE),
                tertiary = rootAction(input),
            )
        }

        if (input.indexingState is IndexingState.Indexing) {
            return State(
                kind = Kind.INDEXING,
                showProgress = true,
                primary = ActionItem(Action.CHOOSE_SOURCE),
            )
        }

        if (
            input.libraryStatus == StartupLibraryStatus.NeedsMusicSource || !input.sourceConfigured
        ) {
            return State(
                kind = Kind.SOURCE_REQUIRED,
                showProgress = false,
                primary = ActionItem(Action.CHOOSE_SOURCE),
                secondary = rootAction(input),
            )
        }

        if (input.libraryStatus == StartupLibraryStatus.SourceUnavailable) {
            return State(
                kind = Kind.SOURCE_UNAVAILABLE,
                showProgress = false,
                primary = ActionItem(Action.REFRESH),
                secondary = ActionItem(Action.CHOOSE_SOURCE),
                tertiary = rootAction(input),
            )
        }

        if (
            input.lastScanFailed ||
                (input.indexingState is IndexingState.Completed &&
                    input.indexingState.error != null)
        ) {
            return State(
                kind = Kind.FAILED,
                showProgress = false,
                primary = ActionItem(Action.REFRESH),
                secondary = ActionItem(Action.RESCAN),
                tertiary = ActionItem(Action.CHOOSE_SOURCE),
            )
        }

        return when (input.libraryStatus) {
            StartupLibraryStatus.CacheUnavailable ->
                State(
                    kind = Kind.CACHE_UNAVAILABLE,
                    showProgress = false,
                    primary = ActionItem(Action.REFRESH),
                    secondary = ActionItem(Action.RESCAN),
                    tertiary = ActionItem(Action.CHOOSE_SOURCE),
                )
            StartupLibraryStatus.Empty ->
                State(
                    kind = Kind.EMPTY,
                    showProgress = false,
                    primary = ActionItem(Action.REFRESH),
                    secondary = ActionItem(Action.RESCAN),
                    tertiary = ActionItem(Action.CHOOSE_SOURCE),
                )
            StartupLibraryStatus.Unknown -> {
                val waiting = input.startupState.rank < StartupReadinessState.FastBrowseReady.rank
                State(
                    kind = if (waiting) Kind.WAITING else Kind.CACHE_UNAVAILABLE,
                    showProgress = waiting,
                    primary = ActionItem(Action.REFRESH),
                    secondary = ActionItem(Action.CHOOSE_SOURCE),
                    tertiary = rootAction(input),
                )
            }
            StartupLibraryStatus.Usable ->
                State(
                    kind = Kind.CACHE_UNAVAILABLE,
                    showProgress = false,
                    primary = ActionItem(Action.REFRESH),
                    secondary = ActionItem(Action.RESCAN),
                    tertiary = ActionItem(Action.CHOOSE_SOURCE),
                )
            StartupLibraryStatus.NeedsMusicSource,
            StartupLibraryStatus.SourceUnavailable -> error("Handled above")
        }
    }

    private fun rootAction(input: Input): ActionItem? =
        if (
            input.rootSupported &&
                input.rootRequired &&
                input.rootState != RootStateHolder.State.Available &&
                input.rootState != RootStateHolder.State.UnsupportedForVariant
        ) {
            ActionItem(Action.ENABLE_ROOT)
        } else {
            null
        }
}
''',
)

# Home recovery becomes path-aware, resumes after settings, and completes root-source selection.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/home/list/SongListFragment.kt",
    """    private var currentRecoveryState =
        LibraryRecoveryPolicy.State(LibraryRecoveryPolicy.Kind.HIDDEN, showProgress = false)
""",
    """    private var currentRecoveryState =
        LibraryRecoveryPolicy.State(LibraryRecoveryPolicy.Kind.HIDDEN, showProgress = false)
    private var awaitingPermissionSettings = false
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/home/list/SongListFragment.kt",
    """    override fun onResume() {
        super.onResume()
        if (view != null) refreshRecoveryState()
    }
""",
    """    override fun onResume() {
        super.onResume()
        if (awaitingPermissionSettings) {
            awaitingPermissionSettings = false
            if (hasStoragePermission()) continueAfterStoragePermission()
        }
        if (view != null) refreshRecoveryState()
    }
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/home/list/SongListFragment.kt",
    """                    locationMode = musicSettings.locationMode,
                    sourceConfigured =
                        StartupLibraryPolicy.isMusicSourceConfigured(
                            musicSettings.locationMode,
                            musicSettings.configuredSourceCount,
                        ),
                    storagePermissionGranted = hasStoragePermission(),
                    rootSupported = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    rootEnabled = rootStateHolder.isUserEnabled(),
                    lastScanFailed = musicSettings.lastScanFailed,
""",
    """                    sourceConfigured =
                        StartupLibraryPolicy.isMusicSourceConfigured(
                            musicSettings.locationMode,
                            musicSettings.configuredSourceCount,
                        ),
                    storagePermissionRequired = storagePermissionRequiredForCurrentSource(),
                    storagePermissionGranted = hasStoragePermission(),
                    rootSupported = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    rootRequired = rootRequiredForCurrentSource(),
                    rootState = rootStateHolder.stateSnapshot(),
                    lastScanFailed = musicSettings.lastScanFailed,
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/home/list/SongListFragment.kt",
    """                startActivity(intent)
""",
    """                awaitingPermissionSettings = true
                startActivity(intent)
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/home/list/SongListFragment.kt",
    """                    if (
                        state == RootStateHolder.State.Available &&
                            StartupLibraryPolicy.isMusicSourceConfigured(
                                musicSettings.locationMode,
                                musicSettings.configuredSourceCount,
                            )
                    ) {
                        musicModel.refresh()
                    }
""",
    """                    if (state == RootStateHolder.State.Available) {
                        if (
                            StartupLibraryPolicy.isMusicSourceConfigured(
                                musicSettings.locationMode,
                                musicSettings.configuredSourceCount,
                            )
                        ) {
                            musicModel.refresh()
                        } else {
                            homeModel.startChooseMusicLocations()
                        }
                    }
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/home/list/SongListFragment.kt",
    """    private fun requiredStoragePermission(): String =
""",
    """    private fun storagePermissionRequiredForCurrentSource(): Boolean =
        when (musicSettings.locationMode) {
            org.oxycblt.auxio.music.locations.LocationMode.MEDIA_STORE -> true
            org.oxycblt.auxio.music.locations.LocationMode.SAF -> false
            org.oxycblt.auxio.music.locations.LocationMode.DIRECT_FS ->
                musicSettings.safQuery.source.any { location ->
                    location.uri.path?.startsWith("/storage/") == true
                }
        }

    private fun rootRequiredForCurrentSource(): Boolean =
        musicSettings.locationMode ==
            org.oxycblt.auxio.music.locations.LocationMode.DIRECT_FS &&
            musicSettings.safQuery.source.any { location ->
                location.uri.path?.startsWith("/mnt/media_rw/usbdisk") == true
            }

    private fun requiredStoragePermission(): String =
""",
)

# Root state uses a consent generation so an old probe cannot overwrite a newer user decision.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt",
    """    @Volatile
    var state: State = State.Unknown
        private set
""",
    """    private val stateLock = Any()
    private var consentGeneration = 0L

    @Volatile
    var state: State = State.Unknown
        private set
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt",
    """        state = if (enabled) State.Unknown else State.DisabledByUser
""",
    """        synchronized(stateLock) {
            consentGeneration += 1L
            state = if (enabled) State.Unknown else State.DisabledByUser
        }
""",
)
old_probe = '''    fun probeSync(): State {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
            return state
        }

        if (!userEnabled()) {
            state = State.DisabledByUser
            return state
        }
        if (state == State.DisabledByUser) {
            state = State.Unknown
        }

        // A timeout is retryable. Other resolved states are retained until process restart or the
        // user disables/re-enables root-assisted storage.
        if (state != State.Unknown && state != State.TimedOut) return state
        state =
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
        return state
    }
'''
new_probe = '''    fun probeSync(): State {
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
                // A timeout is retryable. Other resolved states remain cached for this consent
                // generation.
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

        return synchronized(stateLock) {
            if (generation != consentGeneration || !userEnabled()) {
                state = if (userEnabled()) State.Unknown else State.DisabledByUser
            } else {
                state = probed
            }
            state
        }
    }
'''
replace_once("app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt", old_probe, new_probe)

# Early-prestart settings cannot report stale REQUESTED forever or NEVER with an old timestamp.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/prestart/EarlyPrestartSettings.kt",
    """    fun summary(): String {
        val state =
            when (outcome) {
""",
    """    fun summary(): String {
        val persistedOutcome = outcome
        val effectiveOutcome =
            if (
                persistedOutcome == Outcome.REQUESTED &&
                    lastRunEpochMs > 0L &&
                    System.currentTimeMillis() - lastRunEpochMs > REQUEST_STALE_AFTER_MS
            ) {
                Outcome.TIMED_OUT
            } else {
                persistedOutcome
            }
        val state =
            when (effectiveOutcome) {
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/prestart/EarlyPrestartSettings.kt",
    """        return if (lastRunEpochMs > 0L) {
""",
    """        return if (effectiveOutcome != Outcome.NEVER && lastRunEpochMs > 0L) {
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/prestart/EarlyPrestartSettings.kt",
    """        private const val KEY_EARLY_PRESTART_LAST_RUN = "auxio_early_prestart_last_run"
""",
    """        private const val KEY_EARLY_PRESTART_LAST_RUN = "auxio_early_prestart_last_run"
        private const val REQUEST_STALE_AFTER_MS = 20_000L
""",
)

replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/prestart/EarlyPrestartNotification.kt",
    "import org.oxycblt.auxio.R\n",
    "import org.oxycblt.auxio.R\nimport org.oxycblt.auxio.util.newMainPendingIntent\n",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/prestart/EarlyPrestartNotification.kt",
    """        setContentText(context.getString(R.string.notification_early_prestart_text))
""",
    """        setContentText(context.getString(R.string.notification_early_prestart_text))
        setContentIntent(context.newMainPendingIntent())
""",
)

# Settings dependent-switch state is rechecked after probes; status summaries update immediately.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt",
    "    private var applyingEarlyPrestartState = false\n",
    "",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt",
    """                        applyingEarlyPrestartState = true
                        findPreference<SwitchPreferenceCompat>(
                                getString(R.string.set_key_early_prestart)
                            )
                            ?.isChecked = false
                        applyingEarlyPrestartState = false
""",
    """                        findPreference<SwitchPreferenceCompat>(
                                getString(R.string.set_key_early_prestart)
                            )
                            ?.apply {
                                isChecked = false
                                isEnabled = false
                            }
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt",
    """        applyingEarlyPrestartState = true
        preference.isChecked = earlyPrestartSettings.enabled
        applyingEarlyPrestartState = false
""",
    """        preference.isChecked = earlyPrestartSettings.enabled
        preference.isEnabled = rootStateHolder.isUserEnabled()
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt",
    """                if (applyingEarlyPrestartState) return@OnPreferenceChangeListener true
                val enable = newValue as? Boolean == true
""",
    """                val enable = newValue as? Boolean == true
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt",
    """                    val available = state == RootStateHolder.State.Available
                    earlyPrestartSettings.enabled = available
                    applyingEarlyPrestartState = true
                    preference.isChecked = available
                    applyingEarlyPrestartState = false
                    preference.isEnabled = true
                    requireContext().showToast(rootStateMessage(state))
                    refreshEarlyPrestartStatus()
""",
    """                    val rootStillEnabled = rootStateHolder.isUserEnabled()
                    val available = rootStillEnabled && state == RootStateHolder.State.Available
                    earlyPrestartSettings.enabled = available
                    preference.isChecked = available
                    preference.isEnabled = rootStillEnabled
                    requireContext().showToast(rootStateMessage(state))
                    refreshRootStatus()
                    refreshEarlyPrestartStatus()
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt",
    """            requireContext().showToast(rootStateMessage(state))
            refreshEarlyPrestartStatus()
""",
    """            requireContext().showToast(rootStateMessage(state))
            refreshRootStatus()
            findPreference<SwitchPreferenceCompat>(getString(R.string.set_key_early_prestart))
                ?.isEnabled = rootStateHolder.isUserEnabled()
            refreshEarlyPrestartStatus()
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt",
    """    private fun refreshEarlyPrestartStatus() {
""",
    """    private fun refreshRootStatus() {
        findPreference<Preference>(getString(R.string.set_key_root_fs_status))?.let { preference ->
            RootDiagnosticsHelper.setupRootFsStatus(
                requireContext(),
                preference,
                rootStateHolder,
                viewLifecycleOwner.lifecycleScope,
            )
        }
    }

    private fun refreshEarlyPrestartStatus() {
""",
)

# Responsive recovery panel and shared touch-target dimension.
write(
    "app/src/main/res/layout/fragment_home_list.xml",
    '''<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:animateLayoutChanges="true"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <org.oxycblt.auxio.list.recycler.FastScrollRecyclerView
        android:id="@+id/home_recycler"
        style="@style/Widget.Auxio.RecyclerView.Grid.WithAdaptiveFab"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:listitem="@layout/item_song" />

    <org.oxycblt.auxio.ui.EdgeFrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.core.widget.NestedScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:clipToPadding="false"
            android:fillViewport="true"
            android:overScrollMode="ifContentScrolls">

            <LinearLayout
                android:id="@+id/home_no_music"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:gravity="center"
                android:orientation="vertical"
                android:padding="@dimen/spacing_medium"
                android:visibility="invisible">

                <FrameLayout
                    android:layout_width="@dimen/size_fast_scroll_popup"
                    android:layout_height="@dimen/size_fast_scroll_popup"
                    android:layout_marginBottom="@dimen/spacing_small">

                    <org.oxycblt.auxio.home.list.CookieImageView
                        android:id="@+id/home_no_music_placeholder"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:backgroundTint="?attr/colorSurfaceVariant"
                        android:src="@drawable/ic_song_48"
                        app:tint="?attr/colorOnSurfaceVariant"
                        tools:ignore="ContentDescription" />

                    <ProgressBar
                        android:id="@+id/home_no_music_progress"
                        style="?android:attr/progressBarStyleLarge"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center"
                        android:indeterminate="true"
                        android:visibility="gone" />
                </FrameLayout>

                <TextView
                    android:id="@+id/home_no_music_msg"
                    android:layout_width="256dp"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="@dimen/spacing_small"
                    android:textAlignment="center"
                    android:textAppearance="?attr/textAppearanceBodyLarge"
                    tools:text="@string/lng_empty_songs" />

                <org.oxycblt.auxio.ui.RippleFixMaterialButton
                    android:id="@+id/home_no_music_action"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:minHeight="@dimen/size_touchable_small"
                    android:text="@string/set_locations" />

                <org.oxycblt.auxio.ui.RippleFixMaterialButton
                    android:id="@+id/home_no_music_secondary_action"
                    style="?attr/materialButtonOutlinedStyle"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="@dimen/spacing_tiny"
                    android:minHeight="@dimen/size_touchable_small"
                    android:visibility="gone"
                    tools:text="@string/recovery_action_rescan" />

                <org.oxycblt.auxio.ui.RippleFixMaterialButton
                    android:id="@+id/home_no_music_tertiary_action"
                    style="?attr/materialButtonOutlinedStyle"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="@dimen/spacing_tiny"
                    android:minHeight="@dimen/size_touchable_small"
                    android:visibility="gone"
                    tools:text="@string/recovery_action_choose_source" />

            </LinearLayout>
        </androidx.core.widget.NestedScrollView>

    </org.oxycblt.auxio.ui.EdgeFrameLayout>
</FrameLayout>
''',
)

# Update policy tests for path-aware inputs and source-state precedence.
path = "app/src/test/java/org/oxycblt/auxio/home/list/LibraryRecoveryPolicyTest.kt"
text = read(path)
text = text.replace("import org.oxycblt.auxio.music.IndexingState\n", "import org.oxycblt.auxio.headunit.root.RootStateHolder\nimport org.oxycblt.auxio.music.IndexingState\n")
text = text.replace("        locationMode: LocationMode = LocationMode.SAF,\n", "        locationMode: LocationMode = LocationMode.SAF,\n")
text = text.replace("        rootEnabled: Boolean = false,\n", "        rootEnabled: Boolean = false,\n")
text = text.replace(
    """                locationMode = locationMode,
                sourceConfigured = sourceConfigured,
                storagePermissionGranted = storagePermissionGranted,
                rootSupported = rootSupported,
                rootEnabled = rootEnabled,
                lastScanFailed = lastScanFailed,
""",
    """                sourceConfigured = sourceConfigured,
                storagePermissionRequired = locationMode == LocationMode.MEDIA_STORE,
                storagePermissionGranted = storagePermissionGranted,
                rootSupported = rootSupported,
                rootRequired = locationMode == LocationMode.DIRECT_FS && !rootEnabled,
                rootState =
                    if (rootEnabled) RootStateHolder.State.Available
                    else RootStateHolder.State.DisabledByUser,
                lastScanFailed = lastScanFailed,
""",
)
write(path, text)

# Authority tests now state source authority explicitly.
path = "app/src/test/java/org/oxycblt/auxio/music/StartupScanOriginTest.kt"
text = read(path)
text = text.replace(
    "origin = StartupScanOrigin.USER_VISIBLE,\n                    )",
    "origin = StartupScanOrigin.USER_VISIBLE,\n                        sourceAuthority = true,\n                    )",
)
text = text.replace(
    "origin = origin,\n                        )",
    "origin = origin,\n                            sourceAuthority = true,\n                        )",
)
text = text.replace(
    "origin = StartupScanOrigin.BACKGROUND,\n            )",
    "origin = StartupScanOrigin.BACKGROUND,\n                sourceAuthority = true,\n            )",
)
insert = '''

    @Test
    fun missingSourceAuthorityAlwaysSuppressesAutomaticScan() {
        assertFalse(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayCompatFlavor = false,
                origin = StartupScanOrigin.USER_VISIBLE,
                sourceAuthority = false,
            )
        )
    }
'''
text = text.replace("\n    @Test\n    fun noConfiguredSourceNeverRequestsScan()", insert + "\n    @Test\n    fun noConfiguredSourceNeverRequestsScan()")
write(path, text)

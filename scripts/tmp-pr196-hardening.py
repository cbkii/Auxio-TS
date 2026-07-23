#!/usr/bin/env python3
"""Apply the final PR #196 hardening fixes before stacking onto PR #195."""

from pathlib import Path


def replace_once(path_value: str, old: str, new: str) -> None:
    path = Path(path_value)
    text = path.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{path}: expected exactly one match, found {count}: {old[:120]!r}")
    path.write_text(text.replace(old, new, 1), encoding="utf-8", newline="\n")


# Keep PR #195's notification-publication accounting while integrating PR #196's bounded prestart.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    "import org.oxycblt.auxio.headunit.prestart.EarlyPrestartSettings\n",
    "import org.oxycblt.auxio.headunit.prestart.EarlyPrestartSettings\n"
    "import org.oxycblt.auxio.headunit.root.RootStateHolder\n",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    "import org.oxycblt.auxio.playback.service.PlaybackServiceFragment\n",
    "import org.oxycblt.auxio.playback.service.PlaybackNotificationChannel\n"
    "import org.oxycblt.auxio.playback.service.PlaybackServiceFragment\n",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    """    @Inject lateinit var startupReadinessController: StartupReadinessController
    @Inject lateinit var earlyPrestartSettings: EarlyPrestartSettings
""",
    """    @Inject lateinit var startupReadinessController: StartupReadinessController
    @Inject lateinit var earlyPrestartSettings: EarlyPrestartSettings
    @Inject lateinit var rootStateHolder: RootStateHolder
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    """            val earlyPrestart = intent?.action == ACTION_EARLY_PRESTART
            if (earlyPrestart && !beginEarlyPrestart()) {
                stopSelfResult(startId)
                return@trace START_NOT_STICKY
            }
            onHandleForeground(intent)
            if (earlyPrestart) scheduleEarlyPrestartCompletion(startId)
""",
    """            val earlyPrestart = intent?.action == ACTION_EARLY_PRESTART
            if (earlyPrestart && !isEarlyPrestartAllowed()) {
                stopSelfResult(startId)
                return@trace START_NOT_STICKY
            }
            if (earlyPrestart && !beginEarlyPrestart()) {
                stopSelfResult(startId)
                return@trace START_NOT_STICKY
            }
            onHandleForeground(intent, earlyPrestart)
            if (earlyPrestart) scheduleEarlyPrestartCompletion(startId)
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    """    override fun onBind(intent: Intent): IBinder? {
        val binder = super.onBind(intent)
        onHandleForeground(intent)
        return binder
    }

    private fun onHandleForeground(intent: Intent?) {
        // TS18 fast-resume priority: handle playback/launcher commands before any heavy
        // music indexing path. This keeps raw snapshot restore independent from library readiness.
        val startId = intent?.getIntExtra(INTENT_KEY_START_ID, -1)
        val origin =
            when {
                intent?.action == ACTION_EARLY_PRESTART -> StartupScanOrigin.EARLY_PRESTART
                startId == IntegerTable.START_ID_ACTIVITY -> StartupScanOrigin.USER_VISIBLE
                else -> StartupScanOrigin.BACKGROUND
            }
        playbackFragment.start(intent)
        musicFragment.start(origin)
    }

    private fun beginEarlyPrestart(): Boolean {
""",
    """    override fun onBind(intent: Intent): IBinder? {
        val binder = super.onBind(intent)
        // Binding is a normal MediaBrowser operation. A foreign client cannot opt into the
        // privileged early-prestart path merely by supplying its action string.
        onHandleForeground(intent, earlyPrestart = false)
        return binder
    }

    private fun onHandleForeground(intent: Intent?, earlyPrestart: Boolean) {
        // TS18 fast-resume priority: handle playback/launcher commands before any heavy
        // music indexing path. This keeps raw snapshot restore independent from library readiness.
        val startId = intent?.getIntExtra(INTENT_KEY_START_ID, -1)
        val origin =
            when {
                earlyPrestart -> StartupScanOrigin.EARLY_PRESTART
                startId == IntegerTable.START_ID_ACTIVITY -> StartupScanOrigin.USER_VISIBLE
                else -> StartupScanOrigin.BACKGROUND
            }
        // Early prestart is preparation-only. Never route its boot start id into playback restore
        // or autoplay policy; the canonical playback fragment remains attached but unstimulated.
        if (!earlyPrestart) playbackFragment.start(intent)
        musicFragment.start(origin)
    }

    private fun isEarlyPrestartAllowed(): Boolean {
        val enabled = earlyPrestartSettings.enabled
        val rootEnabled = rootStateHolder.isUserEnabled()
        if (enabled && rootEnabled) return true

        if (enabled && !rootEnabled) {
            earlyPrestartSettings.mark(EarlyPrestartSettings.Outcome.SKIPPED_ROOT_DISABLED)
        }
        journal.log(
            DiagnosticJournal.CAT_BOOT,
            "Early prestart rejected",
            "enabled=$enabled root_user_enabled=$rootEnabled",
        )
        return false
    }

    private fun beginEarlyPrestart(): Boolean {
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    """            if (change == ForegroundListener.Change.MEDIA_SESSION) {
                startForeground(mediaNotification.code, mediaNotification.build())
            }
""",
    """            if (change == ForegroundListener.Change.MEDIA_SESSION) {
                PlaybackNotificationChannel.markPublicationRequested()
                startForeground(mediaNotification.code, mediaNotification.build())
            }
""",
)

# The prestart action must enter the dataSync-declared canonical service, not the Topway
# mediaPlayback wrapper used for ordinary compatibility starts.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/BootReceiver.kt",
    """        try {
            val serviceClass =
                TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java)
            val serviceIntent =
                Intent(context, serviceClass)
                    .setAction(AuxioService.ACTION_EARLY_PRESTART)
""",
    """        try {
            val serviceIntent =
                Intent(context, AuxioService::class.java)
                    .setAction(AuxioService.ACTION_EARLY_PRESTART)
""",
)

# Raw TS18 DirectFS paths use the explicit root path before any broad storage-permission flow.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """        if (directTs18Path && !TopwaySourcePolicy.isAllowedSourceCandidate(path)) {
            return ManualPathValidation.UNSAFE
        }
        if (!hasStoragePermission && locationMode != LocationMode.SAF) {
            return ManualPathValidation.PERMISSION_MISSING
        }
        return try {
            val file = File(path)
            val rawRootCandidate = directTs18Path && path.startsWith("/mnt/media_rw/usbdisk")
""",
    """        if (directTs18Path && !TopwaySourcePolicy.isAllowedSourceCandidate(path)) {
            return ManualPathValidation.UNSAFE
        }
        val rawRootCandidate = directTs18Path && path.startsWith("/mnt/media_rw/usbdisk")
        if (!hasStoragePermission && locationMode != LocationMode.SAF && !rawRootCandidate) {
            return ManualPathValidation.PERMISSION_MISSING
        }
        return try {
            val file = File(path)
""",
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    """        } catch (e: SecurityException) {
            L.w(e, "Security exception while validating manual path $path")
            ManualPathValidation.PERMISSION_MISSING
""",
    """        } catch (e: SecurityException) {
            L.w(e, "Security exception while validating manual path $path")
            if (rawRootCandidate) {
                ManualPathValidation.ROOT_UNAVAILABLE
            } else {
                ManualPathValidation.PERMISSION_MISSING
            }
""",
)

# Root consent changes access authority but must not scan the old source before a pending source is
# accepted and saved. Source persistence or an explicit user refresh remains the scan trigger.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt",
    """            getString(R.string.set_key_scan_priority),
            getString(R.string.set_key_root_access_policy) -> {
                L.d("Dispatching indexing setting change for $key")
                listener.onIndexingSettingChanged()
            }
""",
    """            getString(R.string.set_key_scan_priority) -> {
                L.d("Dispatching indexing setting change for $key")
                listener.onIndexingSettingChanged()
            }
            getString(R.string.set_key_root_access_policy) -> {
                L.d("Applying root access policy change without reindex")
            }
""",
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/music/MusicSettingsIndexingTriggerTest.kt",
    """    fun `scan-affecting settings still request a reindex`() {
        dispatch(context.getString(R.string.set_key_scan_priority))
        dispatch(context.getString(R.string.set_key_root_access_policy))
        dispatch(context.getString(R.string.set_key_separators))
        assertEquals(3, listener.indexingSettingChanges)
    }

    @Test
    fun `observation settings dispatch observing changes only`() {
""",
    """    fun `scan-affecting settings still request a reindex`() {
        dispatch(context.getString(R.string.set_key_scan_priority))
        dispatch(context.getString(R.string.set_key_separators))
        assertEquals(2, listener.indexingSettingChanges)
    }

    @Test
    fun `root consent change defers scanning until source save or explicit refresh`() {
        dispatch(context.getString(R.string.set_key_root_access_policy))
        assertEquals(0, listener.indexingSettingChanges)
        assertEquals(0, listener.locationChanges)
        assertEquals(0, listener.observingChanges)
    }

    @Test
    fun `observation settings dispatch observing changes only`() {
""",
)

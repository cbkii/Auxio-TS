#!/usr/bin/env python3
"""Apply final early-prestart and foreground-service hardening for PR #196."""

from pathlib import Path


def patch(path_value: str, replacements: list[tuple[str, str]]) -> None:
    path = Path(path_value)
    text = path.read_text(encoding="utf-8")
    for old, new in replacements:
        count = text.count(old)
        if count != 1:
            raise SystemExit(f"{path}: expected exactly one match, found {count}: {old[:180]!r}")
        text = text.replace(old, new, 1)
    path.write_text(text, encoding="utf-8", newline="\n")


patch(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    [
        (
            "import android.content.Intent\n",
            "import android.content.Intent\nimport android.content.pm.ServiceInfo\n",
        ),
        (
            "import android.os.BadParcelableException\n",
            "import android.os.BadParcelableException\nimport android.os.Build\n",
        ),
        (
            "import kotlinx.coroutines.launch\n",
            "import kotlinx.coroutines.launch\nimport kotlinx.coroutines.withContext\n",
        ),
        (
            "import org.oxycblt.auxio.music.service.MusicServiceFragment\n",
            "import org.oxycblt.auxio.music.service.MusicServiceFragment\n"
            "import org.oxycblt.auxio.music.service.StartupScanAuthorityPolicy\n",
        ),
        (
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
            journal.log(
                DiagnosticJournal.CAT_LIFECYCLE,
                "AuxioService onStartCommand",
                "Action: ${intent?.action}, StartId: $startId",
            )
            // Playback services are expected to survive process churn when possible so that
            // MediaSession/controller interactions continue to route to the same service endpoint.
            // Keep ordinary starts sticky. The bounded early-prestart path stops itself after the
            // saved startup projections become available or the timeout expires.
            if (earlyPrestart) START_NOT_STICKY else START_STICKY
""",
            """            val earlyPrestart = intent?.action == ACTION_EARLY_PRESTART
            if (earlyPrestart) {
                if (!isEarlyPrestartAllowed() || !beginEarlyPrestart()) {
                    stopSelfResult(startId)
                    return@trace START_NOT_STICKY
                }
                startEarlyPrestart(startId)
                journal.log(
                    DiagnosticJournal.CAT_LIFECYCLE,
                    "AuxioService onStartCommand",
                    "Action: ${intent?.action}, StartId: $startId",
                )
                return@trace START_NOT_STICKY
            }

            onHandleForeground(intent)
            journal.log(
                DiagnosticJournal.CAT_LIFECYCLE,
                "AuxioService onStartCommand",
                "Action: ${intent?.action}, StartId: $startId",
            )
            // Playback services remain sticky so MediaSession/controller interactions continue to
            // route to the same canonical service endpoint after process churn.
            START_STICKY
""",
        ),
        (
            """        // Binding is a normal MediaBrowser operation. A foreign client cannot opt into the
        // privileged early-prestart path merely by supplying its action string.
        onHandleForeground(intent, earlyPrestart = false)
""",
            """        // Binding is a normal MediaBrowser operation. A foreign client cannot opt into the
        // privileged early-prestart path merely by supplying its action string.
        onHandleForeground(intent)
""",
        ),
        (
            """    private fun onHandleForeground(intent: Intent?, earlyPrestart: Boolean) {
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
""",
            """    private fun onHandleForeground(intent: Intent?) {
        // A public start-id extra is not authority. Only MainActivity can issue the process-local
        // one-shot token immediately before starting the service.
        val startId = intent?.getIntExtra(INTENT_KEY_START_ID, -1)
        val trustedUserVisible =
            startId == IntegerTable.START_ID_ACTIVITY &&
                StartupScanAuthorityPolicy.consumeTrustedUserVisibleStart()
        if (startId == IntegerTable.START_ID_ACTIVITY && !trustedUserVisible) {
            journal.log(
                DiagnosticJournal.CAT_LIFECYCLE,
                "Untrusted user-visible start ignored",
                "caller supplied activity start id without process token",
            )
        }
        val origin =
            if (trustedUserVisible) StartupScanOrigin.USER_VISIBLE else StartupScanOrigin.BACKGROUND
        playbackFragment.start(intent)
        musicFragment.start(origin)
    }
""",
        ),
        (
            """    private fun isEarlyPrestartAllowed(): Boolean {
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
""",
            """    private fun isEarlyPrestartAllowed(): Boolean {
        val supportedApi = Build.VERSION.SDK_INT < 35
        val enabled = earlyPrestartSettings.enabled
        val rootEnabled = rootStateHolder.isUserEnabled()
        if (BuildConfig.TOPWAY_COMPAT_FLAVOR && supportedApi && enabled && rootEnabled) return true

        if (enabled && !rootEnabled) {
            earlyPrestartSettings.mark(EarlyPrestartSettings.Outcome.SKIPPED_ROOT_DISABLED)
        } else if (enabled && !supportedApi) {
            earlyPrestartSettings.mark(EarlyPrestartSettings.Outcome.START_FAILED)
        }
        journal.log(
            DiagnosticJournal.CAT_BOOT,
            "Early prestart rejected",
            "topway=${BuildConfig.TOPWAY_COMPAT_FLAVOR} api=${Build.VERSION.SDK_INT} " +
                "enabled=$enabled root_user_enabled=$rootEnabled",
        )
        return false
    }
""",
        ),
        (
            """            val notification = EarlyPrestartNotification(this)
            startForeground(notification.code, notification.build())
""",
            """            val notification = EarlyPrestartNotification(this)
            ServiceCompat.startForeground(
                this,
                notification.code,
                notification.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
            )
""",
        ),
        (
            """    private fun scheduleEarlyPrestartCompletion(startId: Int) {
        earlyPrestartJob?.cancel()
        earlyPrestartJob =
            serviceScope.launch {
                val deadline = android.os.SystemClock.elapsedRealtime() + EARLY_PRESTART_TIMEOUT_MS
                while (
                    isActive &&
                        startupReadinessController.capability.rank <
                            StartupReadinessState.SearchReady.rank &&
                        android.os.SystemClock.elapsedRealtime() < deadline
                ) {
                    delay(EARLY_PRESTART_POLL_MS)
                }
                val ready =
                    startupReadinessController.capability.rank >=
                        StartupReadinessState.SearchReady.rank
                earlyPrestartSettings.mark(
                    if (ready) {
                        EarlyPrestartSettings.Outcome.READY
                    } else {
                        EarlyPrestartSettings.Outcome.TIMED_OUT
                    }
                )
                journal.log(
                    DiagnosticJournal.CAT_BOOT,
                    "Early prestart completed",
                    "ready=$ready capability=${startupReadinessController.capability}",
                )
                delay(EARLY_PRESTART_SETTLE_MS)

                // Restore any real playback/indexing foreground owner that became active while
                // prestart was running. Otherwise remove only the temporary prestart notification.
                val playbackForeground = playbackFragment.notification != null
                val musicForeground = musicFragment.hasForegroundWork()
                if (playbackForeground || musicForeground) {
                    updateForeground(
                        if (playbackForeground) {
                            ForegroundListener.Change.MEDIA_SESSION
                        } else {
                            ForegroundListener.Change.INDEXER
                        }
                    )
                } else {
                    ServiceCompat.stopForeground(
                        this@AuxioService,
                        ServiceCompat.STOP_FOREGROUND_REMOVE,
                    )
                    isForeground = false
                    stopSelfResult(startId)
                }
            }
    }
""",
            """    private fun startEarlyPrestart(startId: Int) {
        earlyPrestartJob?.cancel()
        earlyPrestartJob =
            serviceScope.launch {
                val rootState = withContext(Dispatchers.IO) { rootStateHolder.probeSync() }
                if (
                    !rootStateHolder.isUserEnabled() ||
                        rootState != RootStateHolder.State.Available
                ) {
                    earlyPrestartSettings.mark(
                        if (!rootStateHolder.isUserEnabled()) {
                            EarlyPrestartSettings.Outcome.SKIPPED_ROOT_DISABLED
                        } else {
                            EarlyPrestartSettings.Outcome.START_FAILED
                        }
                    )
                    journal.log(
                        DiagnosticJournal.CAT_BOOT,
                        "Early prestart root verification failed",
                        "state=$rootState userEnabled=${rootStateHolder.isUserEnabled()}",
                    )
                    finishEarlyPrestart(startId)
                    return@launch
                }

                // Preparation-only: attach the existing music authority after root verification.
                // Playback remains untouched, so boot cannot restore or autoplay a queue here.
                musicFragment.start(StartupScanOrigin.EARLY_PRESTART)
                val deadline = android.os.SystemClock.elapsedRealtime() + EARLY_PRESTART_TIMEOUT_MS
                while (
                    isActive &&
                        startupReadinessController.capability.rank <
                            StartupReadinessState.SearchReady.rank &&
                        android.os.SystemClock.elapsedRealtime() < deadline
                ) {
                    delay(EARLY_PRESTART_POLL_MS)
                }
                val ready =
                    startupReadinessController.capability.rank >=
                        StartupReadinessState.SearchReady.rank
                earlyPrestartSettings.mark(
                    if (ready) EarlyPrestartSettings.Outcome.READY
                    else EarlyPrestartSettings.Outcome.TIMED_OUT
                )
                journal.log(
                    DiagnosticJournal.CAT_BOOT,
                    "Early prestart completed",
                    "ready=$ready capability=${startupReadinessController.capability}",
                )
                finishEarlyPrestart(startId)
            }
    }

    private suspend fun finishEarlyPrestart(startId: Int) {
        delay(EARLY_PRESTART_SETTLE_MS)
        val playbackForeground = playbackFragment.notification != null
        val musicForeground = musicFragment.hasForegroundWork()
        if (playbackForeground || musicForeground) {
            updateForeground(
                if (playbackForeground) {
                    ForegroundListener.Change.MEDIA_SESSION
                } else {
                    ForegroundListener.Change.INDEXER
                }
            )
        } else {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            isForeground = false
            stopSelfResult(startId)
        }
    }
""",
        ),
        (
            """        earlyPrestartJob?.cancel()
        earlyPrestartJob = null
""",
            """        earlyPrestartJob?.cancel()
        earlyPrestartJob = null
        if (earlyPrestartSettings.outcome == EarlyPrestartSettings.Outcome.REQUESTED) {
            earlyPrestartSettings.mark(EarlyPrestartSettings.Outcome.TIMED_OUT)
        }
""",
        ),
        (
            """                PlaybackNotificationChannel.markPublicationRequested()
                startForeground(mediaNotification.code, mediaNotification.build())
""",
            """                PlaybackNotificationChannel.markPublicationRequested()
                ServiceCompat.startForeground(
                    this,
                    mediaNotification.code,
                    mediaNotification.build(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK,
                )
""",
        ),
        (
            """                if (it != null) {
                    startForeground(it.code, it.build())
                    isForeground = true
""",
            """                if (it != null) {
                    ServiceCompat.startForeground(
                        this,
                        it.code,
                        it.build(),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
                    )
                    isForeground = true
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/BootReceiver.kt",
    [
        (
            "import android.content.Intent\n",
            "import android.content.Intent\nimport android.os.Build\n",
        ),
        (
            """    private fun startEarlyPrestartIfEnabled(context: Context) {
        if (!earlyPrestartSettings.enabled) return
""",
            """    private fun startEarlyPrestartIfEnabled(context: Context) {
        if (!earlyPrestartSettings.enabled) return
        // Android 15+ forbids dataSync foreground-service starts from BOOT_COMPLETED. Keep the
        // exact Android 10 TS18 path supported and fall back to normal user-visible startup later.
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR || Build.VERSION.SDK_INT >= 35) {
            earlyPrestartSettings.mark(EarlyPrestartSettings.Outcome.START_FAILED)
            journal.log(
                DiagnosticJournal.CAT_BOOT,
                "Early prestart skipped",
                "unsupported_variant_or_api api=${Build.VERSION.SDK_INT}",
            )
            return
        }
""",
        ),
    ],
)

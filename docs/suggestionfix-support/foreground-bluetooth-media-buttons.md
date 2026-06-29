# Foreground services, Bluetooth, media buttons, and overlays

## Foreground service launch and service type rules

**Observed in external docs:** Apps start a foreground service by calling `startForegroundService()` or `startService()`, then the service must call `ServiceCompat.startForeground()` to promote itself with a notification.

**Observed in external docs:** Android 12+ restricts starting foreground services from the background. Android 14+ checks foreground service type permissions and can throw `SecurityException` when prerequisites are missing.

**Observed in external docs:** The `mediaPlayback` foreground service type requires `FOREGROUND_SERVICE_MEDIA_PLAYBACK` in the manifest and is intended for media playback. Android 15+ target apps cannot launch a media playback foreground service from `BOOT_COMPLETED`.

**Inferred for Auxio-TS:** Do not blindly force foreground on every `PlaybackServiceFragment.start(intent)` call. Only promote when there is a valid foreground-service reason and notification/session path. Preserve PR #120's `alwaysPlayImmediately` and `autostartFloatingOnly` without creating duplicate notifications or services.

**Requires repo validation:** Check `AuxioService`, service lifecycle, notification holder, `PlaybackServiceFragment`, `BootReceiver`, `MediaButtonReceiver`, Tasker starts, and Topway start-intent handling together.

## Bluetooth connect receiver

**Observed in external docs:** Android 12+ uses runtime Bluetooth permissions including `BLUETOOTH_CONNECT`. Legacy `BLUETOOTH` and `BLUETOOTH_ADMIN` are generally capped at API 30 when targeting newer systems.

**Inferred for Auxio-TS:** A Bluetooth connect broadcast should not unexpectedly start playback. A safe implementation would only warm/restore service state if current permissions and background-start rules allow it and if existing user settings imply that behaviour.

**Requires TS18 validation:** TS18 Topway Bluetooth/radio/phone audio routing can be vendor-specific. Android Bluetooth connection, media button routing, focus ownership, and Topway Bluetooth app behaviour must remain separate findings.

## Media buttons

**Observed in external docs:** Media3's `MediaSession.Callback.onMediaButtonEvent` exists, but Media3 handles media button events internally by default; apps usually do not need to override it. Returning true means the app handled the event and propagation stops.

**Inferred for Auxio-TS:** If the repo has a custom `MediaButtonReceiver`, treat it as a compatibility bridge. Avoid duplicate handling with Media3's session path. Prefer tests around intent filtering, key event extraction, focus/current-song gates, and service start ID.

## Floating controls overlay

**Observed in external docs:** Apps that draw over other apps need `SYSTEM_ALERT_WINDOW` and user-granted overlay access; the overlay settings activity can be unavailable, so callers must guard the intent.

**Inferred for Auxio-TS:** `Launch Floating Controls only` should check permission and fail visibly/reversibly if missing. Do not silently skip all startup behaviour unless current code/user setting intentionally requires that.

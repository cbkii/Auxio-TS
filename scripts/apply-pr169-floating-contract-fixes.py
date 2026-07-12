from __future__ import annotations

import os
import tempfile
from pathlib import Path

ROOT = Path.cwd()


class PatchError(RuntimeError):
    pass


def read(path: str) -> str:
    return (ROOT / path).read_text(encoding="utf-8")


def write(path: str, content: str) -> None:
    target = ROOT / path
    target.parent.mkdir(parents=True, exist_ok=True)
    fd, tmp_name = tempfile.mkstemp(prefix=f".{target.name}.", suffix=".tmp", dir=target.parent)
    tmp = Path(tmp_name)
    try:
        with os.fdopen(fd, "w", encoding="utf-8", newline="\n") as fh:
            fh.write(content)
            fh.flush()
            os.fsync(fh.fileno())
        os.replace(tmp, target)
    except BaseException:
        try:
            tmp.unlink(missing_ok=True)
        except OSError:
            pass
        raise


def replace_once(path: str, old: str, new: str) -> None:
    content = read(path)
    count = content.count(old)
    if count != 1:
        raise PatchError(f"{path}: expected one match, found {count}: {old[:160]!r}")
    write(path, content.replace(old, new, 1))


def require_contains(path: str, needle: str) -> None:
    if needle not in read(path):
        raise PatchError(f"{path}: required postcondition missing: {needle!r}")


# Return typed restore results and make foreground-service scheduling observable.
service = "app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt"
replace_once(
    service,
    '''        fun restoreIfEnabled(context: Context, reason: String) {
            val prefs =
                try {
                    CarOverlayPrefs.from(context)
                } catch (e: RuntimeException) {
                    L.w(e, "Cannot restore overlay: preferences unavailable")
                    return
                }
            if (!prefs.enabled) {
                L.d("Skipping overlay restore; disabled [$reason]")
                return
            }
            if (!Settings.canDrawOverlays(context)) {
                L.w("Skipping overlay restore; permission missing [$reason]")
                return
            }
            if (clearsForegroundSuppression(reason)) {
                CarOverlayVisibilityHooks.isSuppressedByAuxioForeground = false
            }
            // Always start the foreground service when the feature is enabled. Optional
            // hide-over-Auxio affects only the overlay window; it must not prevent the persistent
            // service from existing and later reattaching the controls.
            start(context, reason)
        }
''',
    '''        fun restoreIfEnabled(
            context: Context,
            reason: String,
        ): CarOverlayContract.OverlayRestoreResult {
            val prefs =
                try {
                    CarOverlayPrefs.from(context)
                } catch (e: RuntimeException) {
                    L.w(e, "Cannot restore overlay: preferences unavailable")
                    return CarOverlayContract.OverlayRestoreResult.StartRejected(
                        "PreferencesUnavailable"
                    )
                }
            if (!prefs.enabled) {
                L.d("Skipping overlay restore; disabled [$reason]")
                return CarOverlayContract.OverlayRestoreResult.Disabled
            }
            if (!Settings.canDrawOverlays(context)) {
                L.w("Skipping overlay restore; permission missing [$reason]")
                return CarOverlayContract.OverlayRestoreResult.PermissionMissing
            }
            if (isOverlayRuntimeAttached) {
                return CarOverlayContract.OverlayRestoreResult.AlreadyVisible
            }
            if (clearsForegroundSuppression(reason)) {
                CarOverlayVisibilityHooks.isSuppressedByAuxioForeground = false
            }
            return if (start(context, reason)) {
                CarOverlayContract.OverlayRestoreResult.StartRequested
            } else {
                CarOverlayContract.OverlayRestoreResult.StartRejected("ServiceStartRejected")
            }
        }
''',
)
replace_once(
    service,
    '''        fun start(context: Context) {
            start(context, "explicit")
        }

        fun start(context: Context, reason: String) {
            if (!Settings.canDrawOverlays(context)) return
            val intent = Intent(context, CarFloatingControlsService::class.java)
            intent.action = ACTION_START
            intent.putExtra(EXTRA_START_REASON, reason)
            try {
                ContextCompat.startForegroundService(context, intent)
            } catch (e: IllegalStateException) {
                L.w(e, "Cannot start overlay service: IllegalStateException")
            } catch (e: SecurityException) {
                L.w(e, "Cannot start overlay service: SecurityException")
            }
        }
''',
    '''        fun start(context: Context): Boolean = start(context, "explicit")

        fun start(context: Context, reason: String): Boolean {
            if (!Settings.canDrawOverlays(context)) return false
            val intent = Intent(context, CarFloatingControlsService::class.java)
            intent.action = ACTION_START
            intent.putExtra(EXTRA_START_REASON, reason)
            return try {
                ContextCompat.startForegroundService(context, intent)
                true
            } catch (e: IllegalStateException) {
                L.w(e, "Cannot start overlay service: IllegalStateException")
                false
            } catch (e: SecurityException) {
                L.w(e, "Cannot start overlay service: SecurityException")
                false
            } catch (e: RuntimeException) {
                L.w(e, "Cannot start overlay service: RuntimeException")
                false
            }
        }
''',
)

settings = "app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarOverlaySettings.kt"
replace_once(
    settings,
    '''        if (result.startService) {
            CarFloatingControlsService.start(context)
        }
        if (result.stopService) {
            CarFloatingControlsService.stop(context)
        }
        return result.completedImmediately
''',
    '''        var completedImmediately = result.completedImmediately
        if (result.startService && !CarFloatingControlsService.start(context)) {
            prefs.enabled = false
            prefs.pendingEnable = false
            completedImmediately = false
        }
        if (result.stopService) {
            CarFloatingControlsService.stop(context)
        }
        return completedImmediately
''',
)

# Replace reflection with mutually exclusive flavour implementations of the same bridge API.
main_bridge = "app/src/main/java/org/oxycblt/auxio/headunit/overlay/TopwayOverlayRestoreBridge.kt"
(ROOT / main_bridge).unlink()
write(
    "app/src/standard/java/org/oxycblt/auxio/headunit/overlay/TopwayOverlayRestoreBridge.kt",
    '''/*
 * Copyright (c) 2026 Auxio Project
 * TopwayOverlayRestoreBridge.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.headunit.overlay

import android.content.Context

/** Standard-flavour no-op implementation of the Topway overlay restore bridge. */
object TopwayOverlayRestoreBridge {
    fun requestOverlayRestore(context: Context): CarOverlayContract.OverlayRestoreResult {
        context.applicationContext
        return CarOverlayContract.OverlayRestoreResult.UnsupportedBuild
    }
}
''',
)
write(
    "app/src/topwayCompat/java/org/oxycblt/auxio/headunit/overlay/TopwayOverlayRestoreBridge.kt",
    '''/*
 * Copyright (c) 2026 Auxio Project
 * TopwayOverlayRestoreBridge.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.headunit.overlay

import android.content.Context
import org.oxycblt.auxio.car.overlay.CarFloatingControlsService

/** Compile-safe Topway implementation that directly delegates to the flavour overlay service. */
object TopwayOverlayRestoreBridge {
    fun requestOverlayRestore(context: Context): CarOverlayContract.OverlayRestoreResult =
        CarFloatingControlsService.restoreIfEnabled(context, "boot_receiver:floating_only")
}
''',
)

# Only suppress the full boot UI after a typed successful overlay outcome.
boot = "app/src/main/java/org/oxycblt/auxio/BootReceiver.kt"
replace_once(
    boot,
    '''        // Floating-only is an explicit request not to launch the full UI. Return after every
        // typed restore outcome, including disabled/permission/rejected results.
        if (playbackSettings.autostartFloatingOnly) {
''',
    '''        // Floating-only suppresses the full UI only after a typed overlay start/visible
        // result. Disabled, permission-missing and rejected states degrade to the normal UI so a
        // stale or impossible preference combination cannot leave a blank boot experience.
        if (playbackSettings.autostartFloatingOnly) {
''',
)
replace_once(
    boot,
    '''            } else {
                L.w("Launch Floating Controls only could not start the overlay: $result")
                journal.log(DiagnosticJournal.CAT_BOOT, "Floating-only skipped", result.toString())
            }
            return
        }

        // Attempt to show the activity UI for head-unit use. Background activity starts may be
''',
    '''                return
            }
            L.w("Launch Floating Controls only could not start the overlay: $result")
            journal.log(
                DiagnosticJournal.CAT_BOOT,
                "Floating-only fallback",
                result.toString(),
            )
        }

        // Attempt to show the activity UI for head-unit use. Background activity starts may be
''',
)

# Apply the same typed fallback rule to the fixed DoFun alias.
entry = "app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/TopwayMusicEntryActivity.kt"
replace_once(
    entry,
    '''                if (
                    CarOverlaySettings.isEnabled(this) &&
                        CarOverlaySettings.hasOverlayPermission(this)
                ) {
                    CarFloatingControlsService.restoreIfEnabled(this, "topway_music_entry")
                } else {
                    CarOverlaySettings.setEnabled(this, true)
                }
''',
    '''                val hadPermission = CarOverlaySettings.hasOverlayPermission(this)
                val result =
                    if (CarOverlaySettings.isEnabled(this) && hadPermission) {
                        CarFloatingControlsService.restoreIfEnabled(this, "topway_music_entry")
                    } else {
                        val completed = CarOverlaySettings.setEnabled(this, true)
                        when {
                            !hadPermission ->
                                org.oxycblt.auxio.headunit.overlay.CarOverlayContract
                                    .OverlayRestoreResult.PermissionMissing
                            completed ->
                                org.oxycblt.auxio.headunit.overlay.CarOverlayContract
                                    .OverlayRestoreResult.StartRequested
                            else ->
                                org.oxycblt.auxio.headunit.overlay.CarOverlayContract
                                    .OverlayRestoreResult.StartRejected("SettingsEnableRejected")
                        }
                    }
                when (result) {
                    org.oxycblt.auxio.headunit.overlay.CarOverlayContract
                        .OverlayRestoreResult.StartRequested,
                    org.oxycblt.auxio.headunit.overlay.CarOverlayContract
                        .OverlayRestoreResult.AlreadyVisible,
                    org.oxycblt.auxio.headunit.overlay.CarOverlayContract
                        .OverlayRestoreResult.PermissionMissing -> Unit
                    else -> {
                        L.w("Floating-only Topway entry fell back to full player: $result")
                        startActivity(
                            Intent(this, MainActivity::class.java)
                                .setAction(
                                    org.oxycblt.auxio.headunit.HeadUnitEntryPoints
                                        .ACTION_OPEN_NOW_PLAYING
                                )
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        )
                    }
                }
''',
)

# Dedicated launcher: avoid duplicate starts and open the full player if scheduling is rejected.
car_entry = "app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarOverlayActivity.kt"
replace_once(car_entry, "import android.app.Activity\n", "import android.app.Activity\nimport android.content.Intent\n")
replace_once(
    car_entry,
    "import android.os.Bundle\n",
    "import android.os.Bundle\nimport org.oxycblt.auxio.MainActivity\nimport org.oxycblt.auxio.headunit.HeadUnitEntryPoints\n",
)
replace_once(
    car_entry,
    '''        } else {
            CarOverlaySettings.setEnabled(this, true)
            CarOverlayVisibilityHooks.isSuppressedByAuxioForeground = false
            CarFloatingControlsService.start(this, "explicit")
        }
''',
    '''        } else {
            CarOverlayVisibilityHooks.isSuppressedByAuxioForeground = false
            if (!CarOverlaySettings.setEnabled(this, true)) {
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .setAction(HeadUnitEntryPoints.ACTION_OPEN_NOW_PLAYING)
                )
            }
        }
''',
)

# Complete pending permission enablement through the same settings/start policy.
permission_activity = (
    "app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/"
    "CarOverlayPermissionActivity.kt"
)
replace_once(
    permission_activity,
    "import org.oxycblt.auxio.R\n",
    "import org.oxycblt.auxio.MainActivity\nimport org.oxycblt.auxio.R\nimport org.oxycblt.auxio.headunit.HeadUnitEntryPoints\n",
)
replace_once(
    permission_activity,
    '''            if (prefs.pendingEnable) {
                prefs.pendingEnable = false
                prefs.enabled = true
                CarFloatingControlsService.start(this)
            }
''',
    '''            if (prefs.pendingEnable && !CarOverlaySettings.setEnabled(this, true)) {
                L.w("Overlay permission was granted but the service start was rejected")
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .setAction(HeadUnitEntryPoints.ACTION_OPEN_NOW_PLAYING)
                )
                finish()
            }
''',
)

# A floating-only preference is actionable only when overlay enablement and permission are ready.
car_preferences = (
    "app/src/topwayCompat/java/org/oxycblt/auxio/settings/categories/"
    "CarPreferenceFragment.kt"
)
replace_once(
    car_preferences,
    '''        val overlayEnabled = CarOverlaySettings.isEnabled(requireContext())
        preference.isEnabled = overlayEnabled
        preference.summary =
            if (overlayEnabled) {
''',
    '''        val overlayReady =
            CarOverlaySettings.isEnabled(requireContext()) &&
                CarOverlaySettings.hasOverlayPermission(requireContext())
        preference.isEnabled = overlayReady
        if (!overlayReady) {
            (preference as? androidx.preference.TwoStatePreference)?.isChecked = false
        }
        preference.summary =
            if (overlayReady) {
''',
)

# Extend the audit document written by the primary patch.
doc = "docs/TS18_V605_RUNTIME_REGRESSION_FIXES.md"
replace_once(
    doc,
    '''- Floating controls default to persistent visibility; sticky restart promotes before suppression;
  application startup establishes the service; window attach has two bounded retries; the fixed
  Topway entry requests permission when needed and starts the overlay immediately when permitted.
''',
    '''- Floating controls default to persistent visibility; sticky restart promotes before suppression;
  application startup establishes the service; window attach has two bounded retries; fixed entry
  points use typed start results, a compile-safe flavour delegate and full-UI fallback on rejection.
''',
)

require_contains(service, "ServiceStartRejected")
require_contains(boot, "Floating-only fallback")
require_contains(car_preferences, "overlayReady")
require_contains(
    "app/src/topwayCompat/java/org/oxycblt/auxio/headunit/overlay/TopwayOverlayRestoreBridge.kt",
    'restoreIfEnabled(context, "boot_receiver:floating_only")',
)
if (ROOT / main_bridge).exists():
    raise PatchError(f"{main_bridge}: reflective main-source bridge was not removed")

print("PR #169 floating-only contract fixes applied successfully")

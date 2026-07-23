#!/usr/bin/env python3
"""Apply the exact PR #195 review fixes once."""

from pathlib import Path


def patch(path_value: str, replacements: list[tuple[str, str]]) -> None:
    path = Path(path_value)
    text = path.read_text(encoding="utf-8")
    for old, new in replacements:
        count = text.count(old)
        if count != 1:
            raise SystemExit(f"{path}: expected one match, found {count}: {old[:100]!r}")
        text = text.replace(old, new, 1)
    path.write_text(text, encoding="utf-8", newline="\n")


patch(
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt",
    [
        (
            "import android.support.v4.media.session.PlaybackStateCompat\n",
            "import android.support.v4.media.session.PlaybackStateCompat\nimport android.view.KeyEvent\n",
        ),
        (
            "import androidx.media.session.MediaButtonReceiver as AndroidXMediaButtonReceiver\n",
            "",
        ),
        (
            """        val stopIntent =
            AndroidXMediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                android.view.KeyEvent.KEYCODE_MEDIA_STOP,
            )
""",
            """        val stopIntent = buildMediaButtonPendingIntent(KeyEvent.KEYCODE_MEDIA_STOP)
""",
        ),
        (
            """                AndroidXMediaButtonReceiver.buildMediaButtonPendingIntent(context, keyCode),
""",
            """                buildMediaButtonPendingIntent(keyCode),
""",
        ),
        (
            """            .build()

    private fun buildPlayPauseAction(
""",
            """            .build()

    private fun buildMediaButtonPendingIntent(keyCode: Int): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            keyCode,
            Intent(Intent.ACTION_MEDIA_BUTTON)
                .setComponent(mediaButtonReceiver)
                .putExtra(
                    Intent.EXTRA_KEY_EVENT,
                    KeyEvent(KeyEvent.ACTION_DOWN, keyCode),
                ),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

    private fun buildPlayPauseAction(
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/playback/service/PlaybackNotificationChannel.kt",
    [
        (
            "import android.os.Build\n",
            "import android.net.Uri\nimport android.os.Build\n",
        ),
        (
            """        } else {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                } else {
                    putExtra("app_package", context.packageName)
                    putExtra("app_uid", context.applicationInfo.uid)
                }
            }
        }
""",
            """        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", context.packageName, null))
        }
""",
        ),
    ],
)

patch(
    "app/src/main/res/values/strings.xml",
    [
        (
            """    <string name="set_playback_notification_access_desc">Review the exact playback channel used by DoFun and other media controllers</string>
""",
            """    <string name="set_playback_notification_access_desc">Review Auxio's playback notification channel. Device-specific controller behaviour, including TS18/DoFun compatibility, requires runtime validation.</string>
""",
        )
    ],
)

patch(
    "docs/ts18/launcher-integration/DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md",
    [
        (
            """Implementation specification for the draft PR that supersedes the current TS18 launcher strategy with a standards-first, evidence-driven generic media compatibility path.

Repository evidence and automated tests can validate code structure and Android contracts. Exact TS18/DoFun acceptance remains **Requires device validation**.
""",
            """Implementation specification for the draft PR that supersedes the current TS18 launcher strategy with a standards-first, evidence-driven generic media compatibility path.

- **Evidence status:** Observed for repository structure, Android media contracts and the captured blocked Auxio playback channel; Inferred for DoFun's use of the generic notification/MediaSession lane.
- **Confidence:** Medium for Android standards compatibility; low-to-medium for exact fixed-widget recognition until physical validation.
- **Porting decision:** Implement and default to the standards-first generic profile on Topway variants, retain explicit legacy adapters as bounded fallbacks, and do not claim exact DoFun acceptance from CI.
- **Device validation:** Exact TS18/DoFun acceptance remains **Requires device validation**.
""",
        )
    ],
)

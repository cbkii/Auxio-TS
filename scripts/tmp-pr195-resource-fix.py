#!/usr/bin/env python3
"""Apply the remaining PR #195 Android resource and media-button compile fixes."""

from pathlib import Path


def patch(path_value: str, replacements: list[tuple[str, str]], *, allow_already: bool = False) -> None:
    path = Path(path_value)
    text = path.read_text(encoding="utf-8")
    changed = False
    for old, new in replacements:
        count = text.count(old)
        if count == 1:
            text = text.replace(old, new, 1)
            changed = True
        elif allow_already and new in text:
            continue
        else:
            raise SystemExit(f"{path}: expected one match, found {count}: {old[:120]!r}")
    if changed:
        path.write_text(text, encoding="utf-8", newline="\n")


patch(
    "app/src/main/res/values/strings.xml",
    [
        (
            "Review Auxio's playback notification channel. Device-specific controller behaviour, including TS18/DoFun compatibility, requires runtime validation.",
            "Review Auxio’s playback notification channel. Device-specific controller behaviour, including TS18/DoFun compatibility, requires runtime validation.",
        )
    ],
    allow_already=True,
)

patch(
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt",
    [
        (
            "import androidx.media.app.NotificationCompat.MediaStyle\n",
            "import androidx.media.app.NotificationCompat.MediaStyle\n"
            "import androidx.media.session.MediaButtonReceiver as AndroidXMediaButtonReceiver\n",
        ),
        (
            """    private val _notification =
        PlaybackNotification(context, mediaSession.sessionToken) {
""",
            """    private val _notification =
        PlaybackNotification(context, mediaSession.sessionToken, mediaButtonReceiver) {
""",
        ),
        (
            """private class PlaybackNotification(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    private val profileProvider: () -> PlaybackNotificationProfile,
""",
            """private class PlaybackNotification(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    private val mediaButtonReceiver: ComponentName,
    private val profileProvider: () -> PlaybackNotificationProfile,
""",
        ),
    ],
    allow_already=True,
)

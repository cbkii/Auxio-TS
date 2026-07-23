#!/usr/bin/env python3
"""Apply the final scoped PR #195 media-intent wiring.

This branch-local helper is temporary and is removed after canonical verification.
"""

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def replace_once(path: str, old: str, new: str) -> None:
    target = ROOT / path
    content = target.read_text(encoding="utf-8")
    count = content.count(old)
    if count != 1:
        raise RuntimeError(f"{path}: expected one match, found {count}: {old[:100]!r}")
    target.write_text(content.replace(old, new, 1), encoding="utf-8")


holder = "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt"
replace_once(holder, "import android.content.ComponentName\n", "")
replace_once(
    holder,
    """    private val mediaButtonReceiver =
        ComponentName(context, org.oxycblt.auxio.playback.service.MediaButtonReceiver::class.java)
""",
    """    private val mediaButtonReceiver = MediaButtonIntentFactory.receiverComponent(context)
""",
)
replace_once(
    holder,
    """    private fun rebuildGenericActions() {
        val keys = DofunMediaCompatPolicy.genericActionKeyCodes(isPlaying)
""",
    """    private fun rebuildGenericActions() {
        val state = DofunMediaCompatPolicy.genericNotificationState(isPlaying)
        val keys = state.actionKeyCodes
""",
)
replace_once(
    holder,
    """        val stopIntent = buildMediaButtonPendingIntent(KeyEvent.KEYCODE_MEDIA_STOP)
        setDeleteIntent(stopIntent)
        setOngoing(isPlaying)
""",
    """        val stopIntent = buildMediaButtonPendingIntent(state.deleteKeyCode)
        setDeleteIntent(stopIntent)
        setOngoing(state.ongoing)
""",
)
replace_once(
    holder,
    """            Intent(Intent.ACTION_MEDIA_BUTTON)
                .setComponent(mediaButtonReceiver)
                .putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, keyCode)),
""",
    """            MediaButtonIntentFactory.receiverIntent(context, keyCode),
""",
)

widget = "app/src/topwayCompat/java/com/tw/music/view/MusicWidgetProvider.kt"
replace_once(
    widget,
    """import org.oxycblt.auxio.playback.service.DofunMediaCompatPolicy
import org.oxycblt.auxio.playback.service.PendingIntentRequestCodePolicy
""",
    """import org.oxycblt.auxio.playback.service.DofunMediaCompatPolicy
import org.oxycblt.auxio.playback.service.MediaButtonIntentFactory
import org.oxycblt.auxio.playback.service.PendingIntentRequestCodePolicy
""",
)
replace_once(
    widget,
    """        val intent =
            Intent(context, MusicService::class.java)
                .setAction(Intent.ACTION_MEDIA_BUTTON)
                .putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
                .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_MEDIA_BUTTON)

        return PendingIntent.getService(
""",
    """        val intent = MediaButtonIntentFactory.serviceIntent(context, MusicService::class.java, keyCode)

        return PendingIntent.getService(
""",
)

for path, markers in {
    holder: [
        "DofunMediaCompatPolicy.genericNotificationState",
        "MediaButtonIntentFactory.receiverIntent(context, keyCode)",
    ],
    widget: [
        "DofunMediaCompatPolicy.usesCanonicalWidgetControls",
        "MediaButtonIntentFactory.serviceIntent(context, MusicService::class.java, keyCode)",
    ],
}.items():
    content = (ROOT / path).read_text(encoding="utf-8")
    for marker in markers:
        if marker not in content:
            raise RuntimeError(f"{path}: missing generated marker {marker!r}")

print("PR #195 final generic media wiring applied")

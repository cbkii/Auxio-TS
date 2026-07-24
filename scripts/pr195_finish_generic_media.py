#!/usr/bin/env python3
"""Apply the final scoped PR #195 session-initialization wiring.

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
replace_once(
    holder,
    """            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or
                    MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
            )
            setCallback(mediaSessionInterface)
            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setActions(MediaSessionInterface.ACTIONS)
                    .setState(PlaybackStateCompat.STATE_NONE, 0L, 0f)
                    .build()
            )
""",
    """            setFlags(MediaSessionInitializationPolicy.FLAGS)
            setCallback(mediaSessionInterface)
            setPlaybackState(MediaSessionInitializationPolicy.initialPlaybackState())
""",
)

content = (ROOT / holder).read_text(encoding="utf-8")
for marker in (
    "setFlags(MediaSessionInitializationPolicy.FLAGS)",
    "setPlaybackState(MediaSessionInitializationPolicy.initialPlaybackState())",
    "isActive = true",
):
    if marker not in content:
        raise RuntimeError(f"{holder}: missing generated marker {marker!r}")

print("PR #195 deterministic session initialization policy wired successfully")

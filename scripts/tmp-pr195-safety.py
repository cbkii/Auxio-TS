#!/usr/bin/env python3
"""Update the DoFun safety assertion to the reviewed explicit receiver contract."""

from pathlib import Path

path = Path("scripts/check-dofun-topway-compat.sh")
text = path.read_text(encoding="utf-8")
old = 'require_file_contains "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt" "AndroidXMediaButtonReceiver.buildMediaButtonPendingIntent" "generic DoFun AndroidX media-button pending intents"'
new = 'require_file_contains "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt" "buildMediaButtonPendingIntent" "generic DoFun explicit media-button pending intents"'
if text.count(old) != 1:
    raise SystemExit("Expected exactly one stale AndroidX media-button assertion")
path.write_text(text.replace(old, new, 1), encoding="utf-8", newline="\n")

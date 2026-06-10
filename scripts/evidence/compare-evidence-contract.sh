#!/usr/bin/env sh
# Read-only static check: report whether key Topway/TW Music compatibility strings exist in the current repo.
# This is not a proof of correctness; it is a quick guardrail for agents.
set -eu
ROOT=${1:-.}
REQUIRED='com.tw.music.MusicActivity
com.tw.music.MusicService
com.tw.music.view.MusicWidgetProvider
com.tw.music.action.prev
com.tw.music.action.next
com.tw.music.action.pp
com.tw.music.action.cmd
cmd
update
com.tw.music.info
musicTitle
musicaArtist
musicAlbum
musicPath
com.tw.launcher.music_progress_duration
msg_music_progress
msg_music_duration
com.android.launcher.widget_music_progress
music_progress'
missing=0
printf '%s
' "$REQUIRED" | while IFS= read -r term; do
  [ -n "$term" ] || continue
  if grep -RIl --exclude-dir=.git --exclude='*.png' --exclude='*.webp' --exclude='*.jpg' --exclude='*.apk' -- "$term" "$ROOT" >/dev/null 2>&1; then
    printf 'OK      %s\n' "$term"
  else
    printf 'MISSING %s\n' "$term"
    missing=1
  fi
done
exit 0

#!/usr/bin/env sh
# Read-only helper: grep the repo/evidence tree for Topway/DoFun/TW Music contract strings.
# Run from the Auxio-TS repo root.
set -eu
ROOT=${1:-.}
TERMS='com.tw.music
com.tw.media
com.dofun.variety
MusicActivity
MusicService
MusicWidgetProvider
AppWidgetProvider
RemoteViews
MediaSession
MediaBrowserService
MediaSessionService
androidx.media3
widget_music_progress
music_progress
music_duration
msg_music_progress
msg_music_duration
com.tw.launcher.music_progress_duration
com.tw.music.info
musicTitle
musicaArtist
musicAlbum
musicPath
com.tw.music.action.prev
com.tw.music.action.next
com.tw.music.action.pp
com.tw.music.action.cmd
cmd
prev
next
pp
update
SYSTEM_ALERT_WINDOW
ACTION_OPEN_DOCUMENT
GET_CONTENT'
printf '%s
' "$TERMS" | while IFS= read -r term; do
  [ -n "$term" ] || continue
  printf '\n### %s\n' "$term"
  grep -RIn --exclude-dir=.git --exclude='*.png' --exclude='*.webp' --exclude='*.jpg' --exclude='*.apk' -- "$term" "$ROOT" 2>/dev/null | head -80 || true
done

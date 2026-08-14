#!/usr/bin/env bash

# Structural regression gates for low-risk TS18 runtime hardening.

fail() {
  printf 'FAILED: %s\n' "$1" >&2
  exit 1
}

require_file() {
  [ -f "$1" ] || fail "required file is missing: $1"
}

require_contains() {
  local file=$1
  local text=$2
  grep -Fq -- "$text" "$file" || fail "$file is missing required contract: $text"
}

require_absent() {
  local file=$1
  local text=$2
  if grep -Fq -- "$text" "$file"; then
    fail "$file contains obsolete/forbidden contract: $text"
  fi
}

service='app/src/main/java/org/oxycblt/auxio/AuxioService.kt'
startup_notification='app/src/main/java/org/oxycblt/auxio/playback/service/PlaybackStartupNotification.kt'
start_contract='app/src/main/java/org/oxycblt/auxio/playback/service/ForegroundServiceStartContract.kt'
playback_fragment='app/src/main/java/org/oxycblt/auxio/playback/service/PlaybackServiceFragment.kt'
doc='docs/architecture/STARTUP_PROFILES_BENCHMARKS.md'

for file in "$service" "$startup_notification" "$start_contract" "$playback_fragment" "$doc"; do
  require_file "$file"
done

require_contains "$start_contract" 'ContextCompat.startForegroundService(context, markRequired(intent))'
require_contains "$service" 'ensureImmediateForegroundIfRequired(intent)'
require_contains "$service" 'startupForegroundActive'
require_contains "$service" 'PlaybackNotificationChannel.markPublicationRequested()'
require_contains "$startup_notification" 'PlaybackNotificationChannel.id'
require_contains "$startup_notification" 'IntegerTable.PLAYBACK_NOTIFICATION_CODE'

for file in \
  app/src/main/java/org/oxycblt/auxio/BootReceiver.kt \
  app/src/main/java/org/oxycblt/auxio/playback/service/BluetoothHeadsetReceiver.kt \
  app/src/main/java/org/oxycblt/auxio/playback/service/MediaButtonReceiver.kt \
  app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicBridgeReceiver.kt \
  app/src/main/java/org/oxycblt/auxio/tasker/Start.kt; do
  require_contains "$file" 'ForegroundServiceStartContract.start(context, serviceIntent)'
done
require_contains app/src/topwayCompat/java/com/tw/music/view/MusicWidgetProvider.kt \
  'ForegroundServiceStartContract.markRequired(serviceIntent)'

require_contains "$playback_fragment" 'TopwayProgressTickerPolicy.directive(topwayCoordinator.mode, running)'
require_contains "$playback_fragment" 'mode.sendsTopwayBroadcasts && !running'
require_contains "$playback_fragment" '!mode.sendsTopwayBroadcasts && running'
require_absent "$playback_fragment" 'startTopwayProgressTicker()'

require_contains "$doc" 'schema version: `3`'
require_contains "$doc" '`app/src/main/generated/baselineProfiles/startup-prof.txt`'
require_contains "$doc" 'The former `standard` flavour is retired'
require_contains "$doc" 'published Auxio-TS release APK: `arm64-v8a` only'

bash ./scripts/check-native-abi-contracts.sh || fail 'native ABI contract gate failed'

printf 'SUCCESS: TS18 runtime hardening contracts passed\n' >&2

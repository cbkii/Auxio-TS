# Evidence gaps this pack is designed to close

## DoFun generic media integration

Known: VLC works in the DoFun music window/widget. This strongly suggests a generic Android media path, but the exact mechanism on this TS18 build is not yet proven.

Needed evidence:

- active `dumpsys media_session` state for VLC and Auxio while each is playing;
- relative session priority/order;
- playback state/action bitmasks;
- metadata keys and whether title/artist/duration/art URI are present;
- notification category/style/session-token evidence;
- DoFun package and service state while switching apps;
- whether DoFun sends standard media-button/session commands, Topway broadcasts, or both.

## Auxio-TS post-v5.3 runtime success/failure

The collector maps runtime evidence for:

- package/variant identity and Topway-compatible aliases;
- MediaBrowserService / MediaSessionCompat / MediaStyle notification;
- notification artwork stability and RemoteViews crash prevention;
- DoFun widget/zero-ID fallback and Topway bridge;
- DirectFS/manual path/USB roots;
- RootGate and bounded root command behaviour;
- DocumentsUI/SAF picker fallback;
- overlay/floating controls;
- boot/autostart;
- playback queue/shuffle/seek/restore;
- external collector report and app logcat markers;
- external modules/apps in the TS18 project scope.

## Device/system gaps

Still requiring capture on the exact TS18 unit:

- whether DoFun ranks sessions by notification state, active playback state, or package allowlist;
- whether `com.tw.media` vs `org.oxycblt.auxio` package identity changes DoFun behaviour;
- whether stock `com.tw.music` private broadcasts are required only for fixed launcher slots;
- whether root-gated DirectFS is necessary for all USB volumes or only raw `/mnt/media_rw` paths;
- whether DocumentsUI module changes SAF source behaviour;
- whether vendor audio focus/routing differs for Auxio versus VLC;
- whether overlay and notification bitmap changes survive ACC sleep/wake and reboot.


## Added from PR/release audit request

The pack now explicitly captures lower-confidence or unverified areas from the Auxio-TS PR/release stream:

- metadata-before-artwork timing for DoFun-visible media sessions;
- `com.tw.media` versus standard package behaviour;
- queue/shuffle/autoplay/current-track retention;
- listener-concurrency and queue bounds failures surfacing as crashes;
- root gate denial/timeout loops;
- DirectFS inaccessible-vs-empty source handling;
- SAF/DocumentsUI resolver availability;
- app crash reports and external collector output;
- BTAndroidTS, ts18-intent-bridge, NavRadio+ and other active project modules/apps;
- vendor service interactions affecting audio focus/routing.

## v3 added evidence gaps

The v3 pack specifically tries to close these remaining lower-confidence gaps:

1. **Exact source path/mode used by Auxio** — previous captures could show storage roots, but not always the chosen source mode, persisted SAF grants, stale manual paths, or whether a path was inaccessible versus genuinely empty.
2. **DocumentsUI/SAF viability** — after installing a DocumentsUI module, the picker may resolve but expose only limited directories; v3 captures resolver and grant state so this can be separated from Auxio source logic.
3. **Earliest boot/autostart readiness** — service.d cannot observe true init timing, but it can capture the first late_start state and the main delayed state to show whether storage, DoFun, package manager, media services and Auxio boot receivers are ready.
4. **Overlay obstruction by system/gesture layers** — normal app overlays are not guaranteed above SystemUI/status/nav/gesture layers. v3 captures window/layer/touch/input/gesture context to decide whether the fix belongs in Auxio layout/flags/positioning or requires privileged/system integration.
5. **Unexpected pause/resume source** — v3 repeatedly captures audio focus, media-session state, notification/player state, telecom/Bluetooth/radio/NavRadio/context and log markers to distinguish user commands from focus loss, noisy intents, app takeover, process death or vendor routing.

# TS18 Launcher Media Integration Validation Matrix

> **Current authority:** validate the public/direct Track-A path first. A healthy Android
> `MediaSession`, `MediaBrowserService` or notification is a prerequisite, not proof that DoFun's
> fixed homepage media surface selected Auxio.

The product is the one `com.tw.media` Auxio-TS app. Exact-package coverage uses contract fixtures
and tests; do not restore a distributable flavour.

## Current capability model

| Mode | Generic 3-action MediaStyle | `com.android.music.*` legacy broadcasts | Topway metadata/progress TX | Topway command RX | CommandService query/callback |
|---|---:|---:|---:|---:|---:|
| `GenericDofunMedia` | yes | yes | no | no | no |
| `AutoAllSafePaths` | yes | yes | yes | yes | yes |
| `AndroidMediaSessionOnly` | no (normal rich Auxio notification) | no | no | no | no |
| `TopwayBroadcastOnly` | no | no | yes | no | no |
| `TopwayCommandOnly` | no | no | no | yes | yes |
| `TopwayBroadcastAndCommand` | no | no | yes | yes | yes |
| `DiagnosticsOnly` | no | no | no | no | query/diagnostic bind only |
| `Disabled` | no | no | no | no | no |

`com.android.music.metachanged` and `com.android.music.playstatechanged` are public/VLC-compatible
legacy Android signals and are deliberately independent from the observed Topway `com.tw.*`
broadcasts. `AutoAllSafePaths` is additive: its canonical notification remains the conventional
previous / play-pause / next DoFun profile while the separately gated Topway paths are also enabled.

Fresh Topway-compatible installs now default to `AutoAllSafePaths` so one Auxio playback authority is
visible through every currently observed safe generic and Topway-compatible surface. Existing
persisted valid choices are not rewritten because older releases did not record enough preference
provenance to distinguish an old default from an explicit user selection. `GenericDofunMedia`
remains the standards-only physical comparator.

## Read-only baseline collector

Use the current collector before interpreting homepage failure:

```sh
sh scripts/evidence/collect-ts18-dofun-homepage-media-v1.sh
```

It exports to:

```text
/storage/emulated/0/Download/AuxioTS/dofun-homepage-media-<timestamp>/
```

The default collector is read-only. It records identity, package/component state, full bounded
`dumpsys notification --noredact`, relevant playback-channel context, notification-listener/DoFun
listener state, full bounded `dumpsys media_session`, audio focus, relevant services/processes,
DoFun's exported `hotseat_app_music` provider result when readable, and focused logcat. When root is
already available it performs only a narrow read of Auxio's launcher-mode preference; failure is
reported rather than treated as absence.

Do not put stock-package mutation in this collector.

## Required physical sequence

### A. Notification prerequisite

With the exact `com.tw.media` release playing:

1. Confirm `com.tw.media.channel.PLAYBACK` exists and is not importance `0`.
2. Confirm a live transport `NotificationRecord` for `com.tw.media` exists.
3. Confirm DoFun's notification listener / `NotifyService` is enabled and connected.
4. Confirm the Auxio `MediaSession` is visible and active with current metadata/actions.
5. Save the baseline before changing launcher mode or stock-package state.

If the channel is blocked, use Auxio's exact playback-channel settings row to re-enable it, restart
playback and capture a new baseline. Do not interpret generic-path failure while this prerequisite is
false. Auxio must not silently raise importance or rotate the channel ID to bypass user/system state.

### B. VLC positive control

Record independently while VLC is the only intended active comparator:

- title and artist;
- artwork;
- progress/play state;
- one press of previous;
- one play/pause press;
- one press of next;
- source/icon tap and opened activity.

Do not reduce this to the historical phrase "VLC partially works".

### C. `GenericDofunMedia`

With stock unchanged and Auxio's channel healthy:

1. select `GenericDofunMedia`;
2. start Auxio playback;
3. re-enter/restart DoFun only as needed for the observation;
4. record displayed metadata/progress;
5. press previous once, play/pause once, next once;
6. tap the source;
7. export the Auxio diagnostics report/journal and external collector evidence.

Expected public Auxio surfaces are the canonical MediaSession/MediaBrowser path, three-action
MediaStyle notification and VLC-compatible `com.android.music.*` broadcasts. Topway-private TX/RX
and CommandService binding remain off.

### D. `AutoAllSafePaths`

Repeat the same one-button-at-a-time sequence.

This is the key hybrid discriminator and the fresh-install default. It must retain the same
three-action generic notification and legacy Android broadcasts while additionally enabling the
observed Topway TX/RX and read/query CommandService callback lane.

Use the ingress telemetry to classify each physical button. Do not add a speculative global debounce:
first determine whether one press actually reaches more than one external ingress path.

### E. Long-lived recovery

Proxy-style reliability must survive service-order and process-order races, not only a clean launch.
Test the recovery lanes separately:

1. start Auxio while the Topway command endpoint is unavailable or restarting, without changing the
   selected integration mode;
2. allow the initial fast retry burst to expire;
3. wait through the 30-second quiet cooldown and make the endpoint available;
4. confirm callback registration recovers without restarting Auxio or toggling the mode;
5. for Track C, start genuine stock `com.tw.music` with Auxio unavailable, allow its initial
   MediaBrowser/command retries to expire, then make Auxio available and confirm the mirror reconnects;
6. repeat with Auxio process death/recreation while the stock process remains alive;
7. confirm a Track-C command Binder timeout still opens the circuit and leaves stock behaviour in
   control rather than retrying a potentially wedged command path.

The retry implementation owns one delayed runnable per endpoint. It must not produce a hot loop,
unbounded queue, duplicate browser/controller or second playback authority.

### F. Identity comparator

Only if C/D still do not explain the result, use a maintained debug/application-ID-suffixed build as
a controlled **non-fixed identity comparator**. It is not a release product and is not proof of
fixed `com.tw.media` behaviour.

If the otherwise equivalent generic path works under the non-fixed identity but not under
`com.tw.media`, classify the package/source classification hypothesis as strongly supported and keep
investigating the fixed DoFun source-selection boundary.

### G. Reversible stock-selection comparator

This touches the protected stock package and is **not** part of the normal collector. Run only after
a saved baseline and explicit approval, using the existing guarded tool:

```sh
sh tools/ts18-root-storage-fastpath/tier3/stock-music-selection-test.sh --disable-after-baseline
```

After the bounded observation, restore immediately:

```sh
sh tools/ts18-root-storage-fastpath/tier3/stock-music-selection-test.sh --restore
```

The tool uses `pm disable-user --user 0`; do not delete/uninstall the APK. STOP if the baseline,
rollback marker, user identity or recovery path is uncertain.

Interpretation:

- Auxio works only with stock disabled → fixed-slot/source-election conflict supported;
- Auxio still fails but the non-fixed comparator works → identity/package classification supported;
- neither Auxio identity works while VLC does → hidden generic source-selection difference remains;
- CommandService callbacks work but homepage metadata remains stock → command and display/source
  selection are separate paths.

## Per-source behavioural matrix

| Scenario | Auxio-side expectation | DoFun observation | Evidence |
|---|---|---|---|
| Idle | no stale Auxio metadata | no false Auxio attribution | diagnostics + media_session |
| Start playback | session active; notification requested; metadata published | title/artist/art/progress may adopt Auxio | notification + media_session + screenshot |
| Pause | one MediaSession/Topway ingress according to selected path | icon/progress follows once | ingress journal + logcat |
| Resume | one corresponding action | state follows once | ingress journal + logcat |
| Next | queue advances exactly once | title/progress update once | before/after queue + journal |
| Previous | queue changes exactly once | title/progress update once | before/after queue + journal |
| Source tap | session activity resolves to Auxio's fixed component | Auxio opens, not stock | window/activity state |
| Process death + PLAY | bounded saved-state restoration may start playback | command is not lost/duplicated | service/session timing |
| Topway service late/restart | bounded burst + cooldown watcher rebinds | controls recover without Auxio restart | callback journal + service state |
| Track-C Auxio late/restart | MediaBrowser/command watcher reconnects; state snapshot republishes | stock-selected panel follows Auxio again | stock bridge log + media_session |
| Launcher restart | public state remains/reappears | source selection is re-evaluated correctly | before/after collector |
| ACC sleep/wake | no duplicate service/session/queue owner | no stale or duplicate homepage action | exact-device ACC evidence |

## Failure classification

Use these classifications only from successful inspection in the correct user/process context:

- **Notification prerequisite failure** — package/channel blocked or no live transport notification.
- **Source-selection failure** — DoFun does not select Auxio despite a healthy public media surface.
- **Identity trap** — equivalent public surface works under a non-fixed identity but not `com.tw.media`.
- **Stock source conflict** — fixed panel adopts Auxio only while genuine stock `com.tw.music` is
  reversibly disabled for user 0.
- **Display mapping failure** — DoFun selects/receives Auxio but maps metadata/art/progress incorrectly.
- **Control routing failure** — homepage controls do not reach any supported Auxio ingress.
- **Duplicate ingress** — one physical press reaches two distinct external Auxio ingress paths and
  causes duplicate semantic action.
- **Permission/listener failure** — DoFun notification listener or normal-app vendor callback access
  is unavailable.
- **Recovery exhaustion failure** — a transient endpoint outage does not recover after the bounded
  burst/cooldown re-arm while the owning process is still alive.
- **Physical evidence incomplete** — required probe failed, was truncated, used the wrong identity,
  or the relevant physical observation was not made.

Do not claim absence from a failed/permission-denied/truncated probe; say "not found in the inspected
scope" and identify the better probe.

## Exactly-once rule

The direct implementation intentionally does **not** add a broad cross-path debounce before physical
evidence demonstrates duplicate delivery. Current telemetry distinguishes MediaSession callback,
media-button receiver, Topway broadcast/action and Topway CommandService callback origins. If one
physical press is later proven to produce equivalent semantic commands through two distinct external
origins, add a short monotonic correlation policy narrowly at that external boundary. Ordinary
in-app UI commands must remain outside such a policy.

## Inactive-session timing

Do not keep the MediaSession permanently active as a speculative workaround. Current policy makes it
inactive only when there is no current song, raw restored metadata, hydrated queue or primitive queue
window. The branch logs activation transitions. Change this policy only if exact-device timing proves
that DoFun scans during a recoverable inactive restoration window and never re-attaches.

## Topway source lane

`TopwayCommandServiceClient` remains read/query/callback-only. It may bind to the verified descriptor,
register the observed callback subset and request the current source state. It must not set source
`3`, invent Binder transactions or assume root/platform authority.

A transient bind/registration failure uses the bounded fast retry burst and then a 30-second quiet
re-arm while the Auxio service and selected mode still own the adapter. A descriptor mismatch remains
an explicit STOP for that adapter rather than a retry condition.

Classify runtime results separately:

- no bind/registration after re-arm windows → access/authority blocker;
- callback registered but reported source is not local music → source-selection blocker;
- local-music source but no music callbacks → routing/ownership blocker;
- callback arrives while DoFun UI remains stock → command and display/source adapters are separate.

## Track-B gate

A separate `com.dofun.variety` LSPosed research adapter becomes eligible only after the healthy
notification baseline, corrected hybrid mode, identity comparator and reversible stock-selection
experiment still leave the fixed-panel selection path unexplained. The existing `com.tw.music`
Track-C module must not be repurposed.

First Track-B build, if justified, is logging-only, exact-version scoped, bounded, kill-switchable and
fail-open. No return-value/source mutation is permitted until the exact private seam is recovered and
validated.

## Acceptance boundary

Static/JVM/API-29/CI success proves only software contracts. Full acceptance requires the exact TS18
to show:

- current-track identification on the DoFun homepage;
- previous / play-pause / next each causing exactly one Auxio action;
- source tap opening Auxio;
- no stock Music, Radio, Bluetooth, notification, audio-focus or duplicate-owner regression;
- process restart and launcher restart safety;
- recovery after late/restarted Topway/Auxio endpoints;
- ACC sleep/wake when practical.

Until those observations are supplied, classify the result as
`TRACK_A_SOFTWARE_READY_NEEDS_DEVICE_TEST` or `PHYSICAL_EVIDENCE_INCOMPLETE`, not `TRACK_A_CONFIRMED`.

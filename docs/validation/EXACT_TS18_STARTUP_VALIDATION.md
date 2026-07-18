# Exact TS18 startup-performance validation

Status: **Requires device validation**. Repository, JVM and managed-emulator results do not prove the timings or runtime behaviour below.

## Exact target identity

- Product/build identity: `s9863a1h10_Natv`
- SoC/platform: `uis8581a2h10` / `sp9863a`
- Android: Android 10 / API 29
- System build: `TS18.2.2_20241210.165912_WINDOW-THEME1`
- Launcher/vendor layer: DoFun/TWTHEME
- Display: physical 1280×720; stable app content approximately 1225×665
- App-facing removable storage: `/storage/usbdisk0` and `/storage/usbdisk1`

STOP if the build, board, panel, launcher package, test APK/commit, playback authority, storage identities or rollback path cannot be confirmed.

## Common prerequisites

1. Preserve the installed known-good APK and the test APK for every maintained flavour being exercised.
2. Record the exact commit, application ID, version code/name, DoFun version, boot ID and wall-clock start time.
3. Confirm which app owns playback and that only one Auxio player, media session, playback service and notification exist.
4. Confirm the dedicated music sources and map their current mount paths and stable source identities.
5. Enable Auxio's bounded performance capture explicitly. Clear the prior event ring before each scenario.
6. Keep log collection bounded. Use normal application diagnostics first; open a short `ylog`/`yloglite` window only for a defined vendor-routing question, then export and stop it.
7. Ensure sufficient free storage before recording reports. Do not write to `/`, `/vendor` or raw `/mnt/media_rw/...` paths.

## Evidence package for every scenario

Capture:

- exported Auxio startup report;
- boot ID and process ID;
- APK hash, application ID, commit and variant;
- source/mount state before and after;
- playback queue/current URI before and after;
- relevant `logcat` excerpt with monotonic timestamps;
- screen recording or timestamped photographs where UI state matters;
- bounded CPU, memory, I/O and thermal snapshots for long-running scenarios;
- observed warnings, cancellation or recovery state.

Do not merge samples across different boots, processes, APKs or source layouts.

## Scenario matrix

### 1. Cold boot to first useful interaction

**Prerequisite:** known-good queue persisted; at least one source mounted; ACC/power state stable.

**Action:** power off fully, cold boot, let DoFun launch normally, open Auxio through the intended launcher slot, then immediately test Play/Pause and Next.

**Pass:** stable shell appears; queue commands work before `FULL_LIBRARY_READY`; playback is not restarted when rich metadata appears; no duplicate notification/session/service appears.

**Fail:** spinner or blocked Home prevents control, queue waits for full hydration, audio restarts, wrong player owns media keys, or vendor services crash.

**Rollback:** stop Auxio, restore known-good APK, reboot only after preserving logs.

### 2. Warm launch and launcher restart

**Prerequisite:** Auxio previously used and then backgrounded; source state unchanged.

**Action:** relaunch from DoFun, then separately restart only the launcher and open Auxio again.

**Pass:** Fast Start and primitive queue remain available; no unnecessary complete scan begins solely due to launcher restart.

**Fail:** repeated full reconstruction, duplicate service/session, stale queue, or DoFun slot no longer resolves.

### 3. Application process death

**Prerequisite:** active or paused saved queue and committed source generations.

**Action:** terminate only Auxio's process using authorised diagnostics, then launch it again without clearing data.

**Pass:** prior committed rows and primitive queue restore; pending generation never replaces committed data; playback controls become available independently of rich hydration.

**Fail:** empty library despite committed data, queue replacement, stuck pending state, crash loop or source rescan destroys last-known-good data.

### 4. Bluetooth and hardware/media-key start

**Prerequisite:** verified Android Bluetooth and Topway Bluetooth routing states recorded separately.

**Action:** with Auxio UI closed, send Play/Pause, Next and Previous through the actual steering-wheel/hardware route and paired Bluetooth controller.

**Pass:** the existing single service/session handles commands after queue readiness; no Home hydration dependency; DoFun metadata may enrich later without audio restart.

**Fail:** command lost, wrong application launches, duplicated audio, stock player takes authority, or key routing changes after enrichment.

### 5. Quick Find before rich hydration

**Prerequisite:** large committed library and cold process.

**Action:** open Quick Find as soon as Fast Start is visible, enter a title/artist/path query rapidly, replace it with another query, then play a result.

**Pass:** stale query is cancelled, result cap is respected, first result arrives before full-library readiness, selected URI plays directly.

**Fail:** full in-memory filter blocks the UI, stale results overwrite the latest query, unbounded list appears, or direct playback waits for graph hydration.

### 6. Direct USB folder playback after cache loss

**Prerequisite:** export/backup app settings and cache; one test volume mounted at `/storage/usbdisk0` or `/storage/usbdisk1`.

**Action:** create the documented cache-loss state without deleting user media, launch Auxio, browse one directory level at a time and play a file.

**Pass:** app-facing path is used, each page is bounded, playback starts while indexing continues, removal errors remain recoverable.

**Fail:** raw `/mnt/media_rw/...` path is required, DocumentsUI is assumed, entire tree is materialised, or app crashes on missing source.

### 7. Dual-USB mount-order change

**Prerequisite:** two uniquely identifiable FAT volumes with recorded content and stable source mapping.

**Action:** boot with both volumes, record mapping, power down safely, swap physical ports/order, reboot and compare source identities and committed rows.

**Pass:** identity follows the source rather than the incidental `usbdiskN` order; one source never invalidates the other.

**Fail:** libraries are exchanged, duplicated, deleted or attributed to the wrong volume.

### 8. Removal and reinsertion while browsing

**Prerequisite:** browse a bounded folder page from a removable source.

**Action:** remove that source, navigate/refresh, then reinsert unchanged content and repeat with one changed file.

**Pass:** temporary absence is distinct from deletion; UI fails safely; unchanged reinsertion reuses metadata; changed file alone is re-extracted.

**Fail:** committed rows are deleted on absence, stale files remain playable as available, full source re-extracts unnecessarily, or another source is affected.

### 9. Removal while indexing

**Prerequisite:** explicit scan active for one source; second source remains mounted.

**Action:** remove only the source currently scanning.

**Pass:** scan cancels/aborts safely, prior generation stays readable, pending rows remain invisible, second source remains intact.

**Fail:** pending generation commits partially, prior data disappears, cancellation is swallowed, or second volume is invalidated.

### 10. Removal while playing

**Prerequisite:** current item originates from test USB source.

**Action:** remove the source during playback.

**Pass:** playback failure is contained and reflected accurately; queue/session remain valid; reinsertion recovery does not create a second player or restart unrelated audio.

**Fail:** crash loop, stuck foreground service, wrong availability state, or broad library reset.

### 11. Enrichment while audio is active

**Prerequisite:** Full enrichment pending and playback running.

**Action:** observe one full enrichment window while repeatedly using Next/Previous and Quick Find.

**Pass:** playback-first policy lowers scan/enrichment pressure, interactions remain responsive, queue is never replaced, only affected rows update.

**Fail:** heavy parallel extraction causes audible interruption, thermal runaway, ANR, current-item restart or global artwork/cache clearing.

### 12. Real ACC sleep/wake

**Prerequisite:** stable ACC wiring/power behaviour and recovery APK available.

**Action:** play or pause a track, enter real ACC sleep for representative short and long intervals, wake and interact immediately.

**Pass:** one service/session remains, queue/current item and source status recover coherently, no implicit destructive rescan occurs, Fast Start works before enrichment.

**Fail:** boot loop, duplicate service, dead controls, stale mount identity, lost queue or high background work immediately after wake.

## Timing interpretation

Record the monotonic events emitted by the app. Treat first frame, queue ready, Fast Start first rows, first search result, first audio, full library ready and enrichment complete as separate milestones. Do not claim a wall-clock threshold until repeated exact-device evidence exists for the same APK, source fixture and power state.

## Issue-ready failure summary

For any failure, record:

- scenario and exact step;
- expected versus observed behaviour;
- APK hash/commit/variant;
- TS18 build and DoFun version;
- boot ID/process ID;
- source identities and mount paths;
- current queue/item URI;
- timing report and bounded logs;
- reproduction count;
- rollback performed;
- whether playback, source or vendor authority became ambiguous.

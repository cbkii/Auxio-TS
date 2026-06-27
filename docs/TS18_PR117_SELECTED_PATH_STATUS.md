# TS18 PR#117 selected-path status

## Scope completed in this pass

**Observed / exact PR#117 code path:**

- Keeps PR#117 as the working branch and does not mark it ready or merge it.
- Repairs the Musikr file-classification unit-test failure by moving most extension/MIME matching into a pure JVM helper.
- Preserves broader TS18 USB efficiency by rejecting known non-audio files early and accepting `application/octet-stream` only when the file extension is a recognised audio extension.
- Adds focused static checks for the public DoFun/Topway compatibility surface:
  - cold widget controls;
  - stock-style `cmd=update` plus `appWidgetIds`;
  - `com.tw.music.MusicActivity` launcher component;
  - RemoteViews artwork cap;
  - legacy public Android music broadcasts;
  - dynamic `SCREEN_ON` overlay handling;
  - non-exported boot receiver;
  - no manifest `SCREEN_ON`;
  - deferred Magisk/privileged lane.
- Adds a minimal `FastResumeSnapshot` persistence foundation containing URI/path/title/artist/album/duration/position/playing/timestamp.

## Deliberately not completed in this PR

**Requires TS18 device validation before promotion:**

- Direct pre-library ExoPlayer playback from `FastResumeSnapshot`.
- Reconciliation between a raw fast-resumed item and the full Auxio library/queue after indexing catches up.
- Complete source repair-state model and UI.
- Any Magisk priv-app/helper implementation.
- Any private Topway/Cardoor/native integration beyond public compatibility surfaces.

## Recommended next branch

Create a new branch from PR#117 after CI and review threads are green. Use that branch for the higher-risk runtime work:

1. direct raw URI/path fast playback before library availability;
2. source repair-state model/UI;
3. TS18 first-audio latency instrumentation;
4. real-device cold boot / ACC sleep-wake / DoFun widget validation.

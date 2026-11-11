# ts-theme integration audit and merge contract

Reviewed 19 October 2026 against Auxio-TS PRs #273/#275/#276/#277, ts-theme main
`2cd04022231064deec627bd1593fc4cfde5d94ce` and its current #26 candidate
`136c62ead089c3a0158161e0956b05913b82d582`. Recheck live heads before future edits.

## Owning contract, in priority order

| Boundary | Auxio-TS producer | ts-theme consumer and acceptance |
| --- | --- | --- |
| Cold preparation | Exported `com.tw.media/com.tw.music.MusicService`, public `android.media.browse.MediaBrowserService`; one token and advertised PREPARE | Normal app-UID MediaBrowser connection, source-owned PREPARE; notification access remains required by launcher policy. No root Auxio startservice or private Binder workaround. |
| Readiness | Canonical persisted primitive queue/raw fast resume, paused preparation, bounded restore arbitration, later library hydration | Inspect the configured source's controller/content. Token, process or advertised actions alone do not prove USB accessibility or audible output. |
| Play/Pause/Next/Previous/Seek | Existing MediaSession callbacks and single playback manager; cold restore coalesces intent | Resolve user intent once, dispatch once and observe directional acknowledgement. Preserve the previous playing source until replacement playback is observed. |
| Metadata | Standard TITLE/ARTIST/ALBUM_ARTIST/DISPLAY_TITLE/DISPLAY_SUBTITLE/MEDIA_ID for raw and hydrated media, before artwork completes | Source-specific session callbacks and effective ticker snapshot. Do not select an unrelated active player as configured Music. |
| Lifecycle | Background Ready and browser reconnect reuse prepared media; explicit exit remains authoritative | Reconcile process/session loss through the existing coordinator; distinguish screen-on from actual vehicle ACC. |
| Private bridge | Track-C Binder accepts only validated UID 1000 commands; unauthenticated binds do not start private library work | Ordinary ts-theme is not UID 1000. Its public browser/session route is deliberately independent and remains supported. |

The maintained package is `com.tw.media`, not genuine protected stock `com.tw.music`.
ts-theme main's unset Music preference can still resolve its historical stock fallback;
#26 prefers installed Auxio and preserves stored choices. For main, explicitly assign
Music to Auxio. Do not mutate the launcher repository or overwrite user preferences
as an Auxio-side workaround. Keep its notification-listener access enabled.

## Confirmed additional corrections

1. A public browser bind or Background Ready start used to enter a new paused saved-state
   restore even with live raw/hydrated media. Such a request could reload an older queue
   and alter live playback. Passive admission now returns without another restore when
   current media/queue authority exists. Truly cold preparation remains paused with no
   arbitrary ShuffleAll fallback; overlapping cold commands retain the existing arbiter.
   Library startup still runs through AuxioService after playback admission.
2. An image-settings callback used to clear MediaSession metadata during raw fast resume,
   because the hydrated currentSong was still null. It now republishes the owning raw item.
   Explicit session clearing still publishes empty values. No second metadata authority.

Focused policy tests cover passive warm/cold admission. The API29 Robolectric contract
harness invokes production MediaSession callbacks and the actual MediaSessionHolder,
checking cold PREPARE/one Play, prepared transport dispatch, raw metadata retention and
explicit clearing. These checks do not claim a physical device was exercised.

## Open PR dispositions

- #276 retains the source-authority, disabled-overlay consent, private Binder admission,
  queue persistence, paused ticker and NDK bootstrap work. Its approved immutable uploader
  SHA policy is a prerequisite for #275; it does not accept floating action tags.
- #275 is the independently pinned artifact action patch, not an app playback dependency.
- #273 updates validation-only macrobenchmark RC to stable; it does not change the app's
  runtime dependency graph. Android Build now compiles the maintained benchmark
  instrumentation when that lane is selected, closing the prior compile-coverage gap.
  Benchmark timing/performance execution remains NOT_RUN.
- #277 adapts only playlist database work to IO. Repository notifications and UI completion
  stay on Main. Upstream search dispatch is already adapted; the paused-aware playback UI
  ticker is intentionally retained. Issue #274 is a monitoring report, not current product
  policy: its obsolete multi-flavour instructions do not override AGENTS.md.

The merge preserves all original heads/ancestry. Canonical final-head checks, rather than
old successful checks or failed pre-prerequisite jobs, own build qualification.

## Physical acceptance: NOT_RUN

Use the existing TS18_RUNTIME_VALIDATION checklist and the matching launcher P/F/N kit.
Run the short same-session startup gate before an extended campaign:

1. Genuine boot, open neither app manually: browser/PREPARE restores paused media while
   another source is preserved; one HOME Music Play becomes audible.
2. Reconnect the launcher/browser and repeat Background Ready while Music is playing and
   paused. The current item, queue, position and playing/paused state must be preserved.
3. Cold Play/Pause/Next/Previous/Seek during delayed USB/library hydration: one intent,
   no duplicate queue/task, no unwanted autoplay or focus steal. Missing source is BLOCKED.
4. Verify track/artist on HOME before artwork/library completion, after image preference
   changes while paused, on next/previous, source handover and explicit session clear.
5. Process loss, late USB, reboot and real ACC sleep/wake; phone/reverse interruptions,
   source routing, source grants and genuine stock/DoFun rollback remain distinct gates.

No firmware, stock package, SELinux, protected partition, signer, LSPosed scope or vehicle
state changes are authorised by these app-side fixes. Signed release/install identity,
exact TS18 audibility and performance remain unqualified until independently observed.

## 20 October 2026: paired automatic HOME preparation

This child starts from dev `21cd6f3fd84f3d163345fedfa1b5214fe68854d4`, not the unknown-source installed 26.6.8 APK. The matching launcher selects immediate root Activity preparation while public browser/session discovery continues in parallel. It does not wait nine seconds for browser failure.

Manifest capability `org.oxycblt.auxio.PREPARE_HOME_PROTOCOL=1` identifies the non-exported `org.oxycblt.auxio.headunit.LauncherPreparationActivity`. Only the app or an actually privileged caller can enter it. It issues a random one-use, process-local ten-second nonce into the same MainActivity, then finishes its excluded, no-history wrapper task. An invented extra on exported MusicActivity is insufficient. Expired or process-lost handoffs cannot become automatic playback.

Prepare-only MainActivity starts the existing playback owner and requests paused Background Ready restoration with no fallback, no trusted user-visible scan authority and no generic Topway route request. Passive preparation does not overwrite pending commands, an active restore or current playback. Existing explicit playback remains authoritative. No second service, player, queue, MediaSession or audio-focus owner is introduced.

The public session custom action `org.oxycblt.auxio.action.HOME_PLAY_V1` accepts protocol 1 and a live elapsed-realtime deadline within five seconds. Launcher supplies the remainder of its existing 4.5-second command budget; this is an expiry bound, not a hard three-second pass threshold. Restore arbitration preserves the deadline through raw/library fallback, drops expired Play, and allows a newer explicit user command its own authority. Ordinary Play remains supported.

Observed host policy tests cover paused preparation, active-owner protection and expired/deferred Play. Full app/JVM/lint gates and physical silent startup, radio preservation and audible first Play remain pending. Around three seconds is a soft target; the mandatory goal is the fastest reliable measured behaviour.

## PR finalisation review — 20 October

A newer explicit Pause during a bounded HOME restore must also govern the no-saved-session fallback. RestoreIntentArbiter now projects its current play state into ShuffleAll/RestoreState fallbacks and suppresses an Open fallback while paused. Expired HOME commands still have no fallback; a still newer explicit Play may resume the same restore without inheriting the old HOME deadline. The new regression exercises HOME Play → Pause → slow/no-saved restore → explicit Play and verifies fallback intent as well as the primary play flag. Twelve focused restore/deadline/passive-policy tests pass locally with production policy sources and framework clock/URI boundary doubles. Physical audibility and same-signer PR testing remain separate gates.

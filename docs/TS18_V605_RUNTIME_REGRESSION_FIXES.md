# TS18 v6.0.5 runtime regression fixes

## Exact-device observations

**Observed** on `s9863a1h10_Natv`, Android 10/API 29, firmware
`TS18.2.2_20241210.165912_WINDOW-THEME1`, using the `com.tw.media` release:

- all Now Playing visualizer modes produced no visible visualizer;
- floating controls required an off/on settings toggle and did not remain persistent;
- floating-only startup still exposed the full player;
- the Large touch controls row had no useful effect;
- the Now Playing EQ action did not open stock EQ.

The in-app DoFun check also showed both `com.tw.media` and stock `com.tw.music` installed, an active
`com.tw.media` MediaSession, and package-level MAIN/LAUNCHER resolution returning ResolverActivity.
The latter was caused by two launcher entries in the Topway APK, not proof that stock music was
preferred.

## Evidence and porting decision

- **Observed — Confidence: High; Porting decision: Accepted.** The exact-device failures above were
  reproduced on the named TS18 build and `com.tw.media` v6.0.5 variant.
- **Implemented — Confidence: High from source and automated checks; Porting decision: Accepted
  pending runtime confirmation.** The bounded fixes below preserve Android 10/API 29 and variant
  isolation.
- **Runtime outcomes — Confidence: Requires device validation; Porting decision: Conditional.**
  Visualizer output, WindowManager persistence, DoFun routing, stock EQ launch and ACC sleep/wake
  must be revalidated on the exact unit.

## Implemented

- Android Visualizer now captures FFT and waveform, uses bounded retry with an alternate scaling
  mode, rejects stale frames, reacts immediately to preference/session changes, and dispatches only
  to the active cover page.
- Floating controls default to persistent visibility, sticky restart promotes before suppression,
  app startup always establishes the foreground service, window attach has two bounded retries, and
  the full-player action uses an explicit component.
- The fixed `com.tw.media/com.tw.music.MusicActivity` alias routes MAIN/MUSIC_PLAYER according to
  the floating-only preference, while ACTION_VIEW still opens the full player. The separate
  user-facing Floating Controls launcher is preserved.
- The library playback banner uses 76dp/85dp buttons, about 73% of the full-panel values, and
  41dp/48dp icons, about 85%; full Now Playing dimensions remain unchanged. The ineffective Large
  touch controls row is removed from the Topway UI.
- Stock EQ resolution now tries the enabled stock router `com.tw.eq/.EQChoiceActivity` first, then
  the exact-device proven fallback `com.tw.eq/.DSPActivity`, uses the correct DEFAULT category for
  `.EQActivity`, checks runtime component-enabled state via `getComponentEnabledSetting()`, and
  proceeds through safe package and Android AudioEffect fallbacks.
- DoFun diagnostics no longer recommend disabling stock solely because both package identities are
  installed and now collect targeted overlay, EQ, alias and visualizer evidence.
- `Hide while Auxio is foreground` remains available as an opt-in setting (default `false`).

## PR #169 versus PR #170 adoption matrix

| Feature area | PR #170 difference | Disposition |
|---|---|---|
| VisualizerState typed FailureReason enum | More states (Disabled, WaitingForPermission, WaitingForSession, Starting, Retrying); typed `FailureReason` enum | **Reject**: many states unused/unproduced; no `FrameSource` for waveform fallback; PR #169's 4-state model with `FrameSource` is cleaner and fully consumed |
| BlobVisualizer waveform dispatch | No `FrameSource`, FFT-only update path | **Already better in PR #169**: waveform fallback is critical for TS18 zero-FFT devices |
| EQ Candidate model | Typed `Candidate(intent, label, kind)` | **Adopted into PR #169**: improves logging and typed launch-time error handling |
| EQ component-enabled checking | `getActivityInfo` + `isEffectivelyEnabled()` (manifest `enabled` only) | **Adapted**: uses `getComponentEnabledSetting()` for runtime overrides instead of manifest-only check |
| EQ ordering | DSPActivity first | **Reject**: requirement specifies EQChoiceActivity (stock router) first; DSPActivity is the proven fallback when router is disabled |
| EQ tests | `TopwayEqualizerLauncherTest` with resolver ordering, disabled-component, package fallback, AudioEffect fallback tests | **Adapted into PR #169**: merged into expanded `TopwayEqualizerExactDeviceTest` |
| Floating Controls attach retries | No retries (stops on first failure) | **Already better in PR #169**: bounded retries with delayed retry and cancellation |
| Floating Controls routing | `getLaunchIntentForPackage()` (ambiguous) | **Already better in PR #169**: explicit `MainActivity` component with `HeadUnitEntryPoints.ACTION_OPEN_NOW_PLAYING` |
| Floating Controls sticky restart | Skips overlay restore when Auxio foreground suppression active | **Reject**: requirement says foreground service must always promote on sticky restart |
| CarOverlayPrefs persistence migration | No migration | **Already better in PR #169**: migrates hide-over-Auxio default to `false` for existing users |
| UISettings `largeHeadUnitControls` flavor guard | Guards with `!BuildConfig.TOPWAY_COMPAT_FLAVOR` | **Reject as unnecessary**: Topway flavor already forces `true` in PlaybackPanelFragment; the pref is not read on Topway path |
| Hide-while-Auxio-foreground preference in XML | Present | **Adopted into PR #169**: re-added as opt-in setting with `dependency` on `car_overlay_enabled` |
| Dimens banner sizing | Overwrites compact dims directly | **Already better in PR #169**: separate banner dims preserve full Now Playing sizing |
| `onVisualizerModeChanged` implementation | Resets retry, notifies cover state | **Already present in PR #169**: resets retry count, refreshes adapter, force-restarts |
| Runtime validation doc | `TS18_RUNTIME_VALIDATION.md` | **Already present**: `docs/TS18_RUNTIME_VALIDATION.md` exists from prior work and covers the same device-level checklist |
| `onRoundModeChanged` interface default | Removed default `{}` body from interface | **Reject**: breaking change requires all implementors to add empty override |

## Banner touch-target and icon expectations

| Dimension | Banner (library bar) | Full Now Playing |
|---|---|---|
| Button standard | 76dp | 104dp |
| Button primary (play/pause) | 85dp | 116dp |
| Icon standard | 41dp | 48dp |
| Icon primary | 48dp | 56dp |
| Tolerance | ±2dp | unchanged |

## Requires TS18 validation

Both Android Build and Android Quality must pass on the exact release-candidate commit before its
artifact is selected for device validation.

Automated checks cannot prove OEM audio-effect, WindowManager, DoFun or ACC behaviour. Validate the
`topwayTwMediaRelease` artifact on the exact unit:

1. Grant RECORD_AUDIO and overlay permission.
2. Test visualizer Off/Fallback/Always with artwork and without artwork; play, pause, skip, leave and
   return to Now Playing, then ACC sleep/wake.
3. Enable floating controls once. Move between Auxio, DoFun and at least two other apps; restart the
   launcher, kill Auxio's process, screen off/on, reboot and ACC sleep/wake. Confirm one foreground
   service, one notification and one overlay window.
4. Select floating-only startup and confirm MAIN/DoFun music entry attaches the overlay without
   showing MainActivity. Use the overlay's app button to open the full player explicitly.
5. Confirm the library banner buttons are approximately 27% smaller and its icons approximately
   15% smaller, while Now Playing controls are unchanged. Verify banner buttons are 76dp/85dp and
   icons are 41dp/48dp (±2dp tolerance).
6. Tap EQ and confirm `com.tw.eq/.EQChoiceActivity` launches if enabled, or `com.tw.eq/.DSPActivity`
   if the router is disabled, while playback remains healthy.
7. Confirm text, progress, artwork and controls remain unclipped within the TS18 1280×720 content area.

Do not claim physical success until this matrix is completed on the exact device.

# TS18 v6.0.5 runtime regression fixes

## Exact-device observations

**Observed** on `s9863a1h10_Natv`, Android 10/API 29, firmware
`TS18.2.2_20241210.165912_WINDOW-THEME1`, using the `com.tw.media` release:

- all Now Playing visualizer modes produced no visible visualizer;
- floating controls required a settings off/on toggle and did not remain persistent;
- floating-only startup still exposed the full player;
- the Large touch controls row had no useful effect;
- the Now Playing EQ action did not open stock EQ.

The in-app DoFun check also showed both `com.tw.media` and stock `com.tw.music` installed, an active
`com.tw.media` MediaSession, and package-level MAIN/LAUNCHER resolution returning ResolverActivity.
The latter was caused by two launcher entries in the Topway APK, not proof that stock music was
preferred.

## Implemented

- Android Visualizer now captures FFT and waveform, uses bounded retry with an alternate scaling
  mode, rejects stale frames, reacts immediately to preference/session changes, and dispatches only
  to the active cover page.
- Floating controls default to persistent visibility, sticky restart promotes before suppression,
  app startup always establishes the foreground service, window attach has two bounded retries, and
  the full-player action uses an explicit component.
- The duplicate floating-controls launcher entry is removed. The fixed
  `com.tw.media/com.tw.music.MusicActivity` alias now routes MAIN/MUSIC_PLAYER according to the
  floating-only preference while ACTION_VIEW still opens the full player.
- The library playback banner has dedicated ~85% dimensions; full Now Playing dimensions remain
  unchanged. The ineffective Large touch controls row is removed from the Topway UI.
- Stock EQ resolution now tries enabled `com.tw.eq/.DSPActivity` first and uses the correct DEFAULT
  category for `.EQActivity`, then proceeds through safe fallbacks.
- DoFun diagnostics no longer recommend disabling stock solely because both package identities are
  installed and now collect targeted overlay, EQ, alias and visualizer evidence.

## Requires TS18 validation

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
5. Confirm the library banner is approximately 15% smaller while Now Playing controls are unchanged.
6. Tap EQ and confirm `com.tw.eq/.DSPActivity` becomes resumed while playback remains healthy.

Do not claim physical success until this matrix is completed on the exact device.

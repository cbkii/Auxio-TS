# TS18 runtime excerpt: NavRadio/Radio widget evidence remains ambiguous

Source: `manual/manual_observations.md`, `runtime/phases/after_manual_navradio_widget_controls/window_focus.txt`, and `media_session.txt`.

Type: Observation plus caution.

Why it matters: The manual observation says radio controls worked, but the window dump after the NavRadio comparator phase shows stock `com.tw.radio/.RadioActivity`, and `dumpsys media_session` still only shows the Auxio `com.tw.media` session. This weakens any claim that TS18 DoFun radio-widget compatibility is primarily explained by NavRadio's Android Media3 session.

```text
--- manual observation ---
Observation: yes,yes

## Manual test 4: NavRadio comparator

Prompt: Open NavRadio+, start radio playback if possible, go back to DoFun, then use the Radio widget and/or Music widget if relevant. Return here afterwards.
Completed at: Wed Jun 10 13:01:41 AEST 2026

## Observation 4: NavRadio comparator

Prompt: Which DoFun widget controls NavRadio+? Radio widget only, Music widget, both, or unclear? Note button/metadata/progress behaviour.
Observation: yes, both stock radio and navradio+, buttons all work

## Broadcast probe setup

--- window evidence ---
dumpsys window windows 2>&1 | grep -Ei 'mCurrentFocus|mFocusedApp|mInputMethodTarget|Window #[0-9]|package=|ActivityRecord|com\.dofun|com\.tw\.music|com\.tw\.media|com\.navimods\.radio|auxio|MusicActivity|RadioActivity' | head -n 240
  Window #10 Window{3034c0 u0 com.tw.radio/com.tw.radio.RadioActivity}:
    mOwnerUid=10078 mShowToOwnerOnly=true package=com.tw.radio appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{8a7a8eb token=Token{a80043a ActivityRecord{ec48f65 u0 com.tw.radio/.RadioActivity t6774}}}
    mAppToken=AppWindowToken{8a7a8eb token=Token{a80043a ActivityRecord{ec48f65 u0 com.tw.radio/.RadioActivity t6774}}}
    WindowStateAnimator{50b8c83 com.tw.radio/com.tw.radio.RadioActivity}:
      topApp=AppWindowToken{8a7a8eb token=Token{a80043a ActivityRecord{ec48f65 u0 com.tw.radio/.RadioActivity t6774}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.radio/.RadioActivity mSnapshot=android.graphics.GraphicBuffer@b0cee00 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{697ba5a token=Token{56eca05 ActivityRecord{83cf17c u0 com.tw.music/.MusicActivity t6773}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.music/.MusicActivity mSnapshot=android.graphics.GraphicBuffer@71f39c8 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{a20b18e token=Token{1f90d89 ActivityRecord{927c090 u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6772}}}
      topApp=AppWindowToken{f47cb28 token=Token{b30854b ActivityRecord{e2b161a u0 com.dofun.carsetting/.ui.MainActivity t6769}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.dofun.carsetting/.ui.MainActivity mSnapshot=android.graphics.GraphicBuffer@75ff95e (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{50d4afc token=Token{6a78fef ActivityRecord{36ad4ce u0 com.termoneplus/.TermActivity t6768}}}
      topApp=AppWindowToken{3ecc419 token=Token{4f4f760 ActivityRecord{4925f63 u0 com.android.settings/.Settings$PhysicalKeyboardActivity t6765}}}
      topApp=AppWindowToken{39346d3 token=Token{1148c2 ActivityRecord{145710d u0 com.tw.media/com.tw.music.MusicActivity t6764}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@aeae96f (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{8913fd token=Token{fbac854 ActivityRecord{3c1cba7 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6763}}}
--- media-session relevant lines ---
  Media key listener package: 
  Callback: null
  Last MediaButtonReceiver: PendingIntent{6a2d937: PendingIntentRecord{90f6ea4 com.tw.media broadcastIntent}}
  Restored MediaButtonReceiver: null
  Restored MediaButtonReceiverComponentType: 0
  Media button session is com.tw.media/com.tw.media (userId=0)
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3166, ownerUid=10196, userId=0
      package=com.tw.media
      launchIntent=null
      mediaButtonReceiver=PendingIntent{6a2d937: PendingIntentRecord{90f6ea4 com.tw.media broadcastIntent}}
      active=true
      flags=3
--
Audio playback (lastly played comes first)
  uid=1000 packages=com.tw.dvd com.android.keychain com.android.dynsystem com.tw.coreservice com.tw.bt com.sprd.engineermode com.android.inputdevices com.ms.ms2160 com.tw.tv com.tw.service.xt com.tw.devicefan com.unisoc.storageclearmanager com.sprd.validationtools com.tw.reverse com.tw.auxin com.tw.dvr com.android.wallpaperbackup com.tw.core com.sprd.systemupdate com.android.location.fused com.spreadtrum.vce com.android.server.telecom com.tw.rightview com.android.localtransport com.abupdate.fota_demo_iot com.tw.music com.tw.eq com.tw.carinfoservice com.tw.service com.tw.bug.report android com.android.settings com.sprd.autoslt com.android.providers.settings com.sprd.vsimservice com.android.cellbroadcastreceiver com.sprd.cameracalibration com.android.packageinstaller com.tw.net com.tw.carchoose com.tw.bootanimation com.dofun.carsetting com.tw.video com.spreadtrum.sgps 
  uid=10196 packages=com.tw.media 

Exit code: 0
```

# TS18 runtime excerpt: DoFun Music widget opens stock TW Music

Source: `ts18_dofun_runtime_validation_20260610_125608/runtime/phases/after_manual_music_widget_tap/window_focus.txt`

Type: Observation.

Why it matters: After manually tapping DoFun's fixed Music widget, the window/task dump corroborates the manual observation that stock `com.tw.music/.MusicActivity` was launched even though Auxio-TS `com.tw.media/com.tw.music.MusicActivity` was also installed and present in recents/windows.

```text
dumpsys window windows 2>&1 | grep -Ei 'mCurrentFocus|mFocusedApp|mInputMethodTarget|Window #[0-9]|package=|ActivityRecord|com\.dofun|com\.tw\.music|com\.tw\.media|com\.navimods\.radio|auxio|MusicActivity|RadioActivity' | head -n 240
  Window #0 Window{79bc053 u0 NavigationBar0}:
  Window #1 Window{d494f94 u0 StatusBar}:
  Window #2 Window{4a55a7b u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{84bac86 u0 com.zjinnova.zlink}:
  Window #4 Window{a863bd9 u0 AssistPreviewPanel}:
  Window #5 Window{7397941 u0 DockedStackDivider}:
  Window #6 Window{6c3d06a u0 InputMethod}:
  Window #7 Window{7627878 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{50d4afc token=Token{6a78fef ActivityRecord{36ad4ce u0 com.termoneplus/.TermActivity t6768}}}
    mAppToken=AppWindowToken{50d4afc token=Token{6a78fef ActivityRecord{36ad4ce u0 com.termoneplus/.TermActivity t6768}}}
  Window #8 Window{bc33736 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{783f5d5 token=Token{31f2a8c ActivityRecord{afdf3bf u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6761}}}
    mAppToken=AppWindowToken{783f5d5 token=Token{31f2a8c ActivityRecord{afdf3bf u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6761}}}
    WindowStateAnimator{87105e com.dofun.variety/com.dofun.overseasvariety.Launcher}:
  Window #9 Window{94c7abe u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{edfa0d9 token=Token{e2a4f20 ActivityRecord{7eb3e23 u0 com.android.systemui/.recents.RecentsActivity t6770}}}
    mAppToken=AppWindowToken{edfa0d9 token=Token{e2a4f20 ActivityRecord{7eb3e23 u0 com.android.systemui/.recents.RecentsActivity t6770}}}
  Window #10 Window{4da4158 u0 com.tw.music/com.tw.music.MusicActivity}:
    mOwnerUid=1000 mShowToOwnerOnly=true package=com.tw.music appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{697ba5a token=Token{56eca05 ActivityRecord{83cf17c u0 com.tw.music/.MusicActivity t6773}}}
    mAppToken=AppWindowToken{697ba5a token=Token{56eca05 ActivityRecord{83cf17c u0 com.tw.music/.MusicActivity t6773}}}
    WindowStateAnimator{732cc3f com.tw.music/com.tw.music.MusicActivity}:
  Window #11 Window{351eaa2 u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a20b18e token=Token{1f90d89 ActivityRecord{927c090 u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6772}}}
    mAppToken=AppWindowToken{a20b18e token=Token{1f90d89 ActivityRecord{927c090 u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6772}}}
  Window #12 Window{ce42773 u0 com.android.settings/com.android.settings.Settings$PhysicalKeyboardActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{3ecc419 token=Token{4f4f760 ActivityRecord{4925f63 u0 com.android.settings/.Settings$PhysicalKeyboardActivity t6765}}}
    mAppToken=AppWindowToken{3ecc419 token=Token{4f4f760 ActivityRecord{4925f63 u0 com.android.settings/.Settings$PhysicalKeyboardActivity t6765}}}
  Window #13 Window{e6b009f u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{940c3b3 token=Token{d052d22 ActivityRecord{982aaed u0 com.android.settings/.Settings$WifiSettingsActivity t6765}}}
    mAppToken=AppWindowToken{940c3b3 token=Token{d052d22 ActivityRecord{982aaed u0 com.android.settings/.Settings$WifiSettingsActivity t6765}}}
  Window #14 Window{3d9d8b8 u0 com.dofun.carsetting/com.dofun.carsetting.ui.MainActivity}:
    mOwnerUid=1000 mShowToOwnerOnly=true package=com.dofun.carsetting appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f47cb28 token=Token{b30854b ActivityRecord{e2b161a u0 com.dofun.carsetting/.ui.MainActivity t6769}}}
    mAppToken=AppWindowToken{f47cb28 token=Token{b30854b ActivityRecord{e2b161a u0 com.dofun.carsetting/.ui.MainActivity t6769}}}
    WindowStateAnimator{4f10074 com.dofun.carsetting/com.dofun.carsetting.ui.MainActivity}:
  Window #15 Window{4916762 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{39346d3 token=Token{1148c2 ActivityRecord{145710d u0 com.tw.media/com.tw.music.MusicActivity t6764}}}
    mAppToken=AppWindowToken{39346d3 token=Token{1148c2 ActivityRecord{145710d u0 com.tw.media/com.tw.music.MusicActivity t6764}}}
    WindowStateAnimator{678b99d com.tw.media/com.tw.music.MusicActivity}:
  Window #16 Window{288b51c u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{8913fd token=Token{fbac854 ActivityRecord{3c1cba7 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6763}}}
    mAppToken=AppWindowToken{8913fd token=Token{fbac854 ActivityRecord{3c1cba7 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6763}}}
  Window #17 Window{18dca6c u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{697ba5a token=Token{56eca05 ActivityRecord{83cf17c u0 com.tw.music/.MusicActivity t6773}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.music/.MusicActivity mSnapshot=android.graphics.GraphicBuffer@46dc255 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{a20b18e token=Token{1f90d89 ActivityRecord{927c090 u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6772}}}
      topApp=AppWindowToken{f47cb28 token=Token{b30854b ActivityRecord{e2b161a u0 com.dofun.carsetting/.ui.MainActivity t6769}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.dofun.carsetting/.ui.MainActivity mSnapshot=android.graphics.GraphicBuffer@75ff95e (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{50d4afc token=Token{6a78fef ActivityRecord{36ad4ce u0 com.termoneplus/.TermActivity t6768}}}
      topApp=AppWindowToken{3ecc419 token=Token{4f4f760 ActivityRecord{4925f63 u0 com.android.settings/.Settings$PhysicalKeyboardActivity t6765}}}
      topApp=AppWindowToken{39346d3 token=Token{1148c2 ActivityRecord{145710d u0 com.tw.media/com.tw.music.MusicActivity t6764}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@be67f55 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{8913fd token=Token{fbac854 ActivityRecord{3c1cba7 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6763}}}
```

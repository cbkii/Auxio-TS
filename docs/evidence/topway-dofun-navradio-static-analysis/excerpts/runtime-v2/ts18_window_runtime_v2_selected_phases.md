# TS18 v2 selected window-state excerpts

**Source:** `ts18_dofun_runtime_validation_v2_20260610_162755/runtime/phases/*/window_focus.txt`

**Why this matters:** Confirms all relevant package windows/tasks exist on user `u0`, including DoFun, stock TW Music, Auxio-TS, stock radio, and Auxio overlay. The manual Music-widget tap observation remains the strongest evidence for routing; window dumps corroborate that stock `com.tw.music/.MusicActivity` was active in the relevant run.

## Phase: initial

```text
  Window #0 Window{7f85782 u0 NavigationBar0}:
  Window #1 Window{cc4ec00 u0 StatusBar}:
  Window #2 Window{78eb3fe u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{bd7954f u0 com.zjinnova.zlink}:
    mOwnerUid=10153 mShowToOwnerOnly=true package=com.zjinnova.zlink appop=SYSTEM_ALERT_WINDOW
  Window #4 Window{d05ff7b u0 AssistPreviewPanel}:
  Window #5 Window{34a6f2d u0 DockedStackDivider}:
  Window #6 Window{feb52f0 u0 InputMethod}:
  Window #7 Window{30c0359 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
    mAppToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
  Window #8 Window{f743fbd u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
    mAppToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
  Window #9 Window{ce30a7 u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
    mAppToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
  Window #10 Window{d374de u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
    mAppToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
  Window #11 Window{3e4c0a3 u0 com.tw.devicefan/com.tw.devicefan.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
    mAppToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
  Window #12 Window{b13c61e u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
    mAppToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
  Window #13 Window{f1e8cd3 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
    mAppToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
  Window #14 Window{65fd7c4 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
    mAppToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
  Window #15 Window{7e290d2 u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
      topApp=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
      topApp=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
      topApp=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
      topApp=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@f2ada3b (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
```
## Phase: after_music_widget_tap

```text
  Window #0 Window{7f85782 u0 NavigationBar0}:
  Window #1 Window{cc4ec00 u0 StatusBar}:
  Window #2 Window{78eb3fe u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{bd7954f u0 com.zjinnova.zlink}:
    mOwnerUid=10153 mShowToOwnerOnly=true package=com.zjinnova.zlink appop=SYSTEM_ALERT_WINDOW
  Window #4 Window{d05ff7b u0 AssistPreviewPanel}:
  Window #5 Window{34a6f2d u0 DockedStackDivider}:
  Window #6 Window{feb52f0 u0 InputMethod}:
  Window #7 Window{30c0359 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
    mAppToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
  Window #8 Window{f743fbd u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
    mAppToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
  Window #9 Window{ce30a7 u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
    mAppToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
  Window #10 Window{cb2e4b2 u0 com.tw.music/com.tw.music.MusicActivity}:
    mOwnerUid=1000 mShowToOwnerOnly=true package=com.tw.music appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
    mAppToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
  Window #11 Window{d374de u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
    mAppToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
  Window #12 Window{3e4c0a3 u0 com.tw.devicefan/com.tw.devicefan.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
    mAppToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
  Window #13 Window{b13c61e u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
    mAppToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
  Window #14 Window{f1e8cd3 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
    mAppToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
  Window #15 Window{65fd7c4 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
    mAppToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
  Window #16 Window{7e290d2 u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.music/.MusicActivity mSnapshot=android.graphics.GraphicBuffer@ff0a02c (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
      topApp=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
      topApp=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
      topApp=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
      topApp=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@f2ada3b (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
```
## Phase: after_auxio_active_music_widget_controls

```text
  Window #0 Window{7f85782 u0 NavigationBar0}:
  Window #1 Window{cc4ec00 u0 StatusBar}:
  Window #2 Window{78eb3fe u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{bd7954f u0 com.zjinnova.zlink}:
    mOwnerUid=10153 mShowToOwnerOnly=true package=com.zjinnova.zlink appop=SYSTEM_ALERT_WINDOW
  Window #4 Window{d05ff7b u0 AssistPreviewPanel}:
  Window #5 Window{34a6f2d u0 DockedStackDivider}:
  Window #6 Window{feb52f0 u0 InputMethod}:
  Window #7 Window{30c0359 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
    mAppToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
  Window #8 Window{f743fbd u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
    mAppToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
  Window #9 Window{ce30a7 u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
    mAppToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
  Window #10 Window{f1e8cd3 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
    mAppToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
  Window #11 Window{cb2e4b2 u0 com.tw.music/com.tw.music.MusicActivity}:
    mOwnerUid=1000 mShowToOwnerOnly=true package=com.tw.music appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
    mAppToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
  Window #12 Window{d374de u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
    mAppToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
  Window #13 Window{3e4c0a3 u0 com.tw.devicefan/com.tw.devicefan.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
    mAppToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
  Window #14 Window{b13c61e u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
    mAppToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
  Window #15 Window{65fd7c4 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
    mAppToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
  Window #16 Window{7e290d2 u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.music/.MusicActivity mSnapshot=android.graphics.GraphicBuffer@ff0a02c (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
      topApp=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
      topApp=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
      topApp=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
      topApp=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@60825e6 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
```
## Phase: after_stock_active_music_widget_controls

```text
  Window #0 Window{7f85782 u0 NavigationBar0}:
  Window #1 Window{cc4ec00 u0 StatusBar}:
  Window #2 Window{78eb3fe u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{bd7954f u0 com.zjinnova.zlink}:
    mOwnerUid=10153 mShowToOwnerOnly=true package=com.zjinnova.zlink appop=SYSTEM_ALERT_WINDOW
  Window #4 Window{d05ff7b u0 AssistPreviewPanel}:
  Window #5 Window{34a6f2d u0 DockedStackDivider}:
  Window #6 Window{feb52f0 u0 InputMethod}:
  Window #7 Window{30c0359 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
    mAppToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
  Window #8 Window{f743fbd u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
    mAppToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
  Window #9 Window{ce30a7 u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
    mAppToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
  Window #10 Window{cb2e4b2 u0 com.tw.music/com.tw.music.MusicActivity}:
    mOwnerUid=1000 mShowToOwnerOnly=true package=com.tw.music appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
    mAppToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
  Window #11 Window{f1e8cd3 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
    mAppToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
  Window #12 Window{d374de u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
    mAppToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
  Window #13 Window{3e4c0a3 u0 com.tw.devicefan/com.tw.devicefan.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
    mAppToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
  Window #14 Window{b13c61e u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
    mAppToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
  Window #15 Window{65fd7c4 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
    mAppToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
  Window #16 Window{7e290d2 u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.music/.MusicActivity mSnapshot=android.graphics.GraphicBuffer@707c93c (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
      topApp=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
      topApp=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
      topApp=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
      topApp=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@60825e6 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
```
## Phase: after_navradio_widget_controls

```text
  Window #0 Window{7f85782 u0 NavigationBar0}:
  Window #1 Window{cc4ec00 u0 StatusBar}:
  Window #2 Window{78eb3fe u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{bd7954f u0 com.zjinnova.zlink}:
    mOwnerUid=10153 mShowToOwnerOnly=true package=com.zjinnova.zlink appop=SYSTEM_ALERT_WINDOW
  Window #4 Window{d05ff7b u0 AssistPreviewPanel}:
  Window #5 Window{34a6f2d u0 DockedStackDivider}:
  Window #6 Window{feb52f0 u0 InputMethod}:
  Window #7 Window{30c0359 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
    mAppToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
  Window #8 Window{f743fbd u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
    mAppToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
  Window #9 Window{ce30a7 u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
    mAppToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
  Window #10 Window{70e5d3a u0 com.tw.radio/com.tw.radio.RadioActivity}:
    mOwnerUid=10078 mShowToOwnerOnly=true package=com.tw.radio appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
    mAppToken=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
  Window #11 Window{cb2e4b2 u0 com.tw.music/com.tw.music.MusicActivity}:
    mOwnerUid=1000 mShowToOwnerOnly=true package=com.tw.music appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
    mAppToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
  Window #12 Window{f1e8cd3 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
    mAppToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
  Window #13 Window{d374de u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
    mAppToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
  Window #14 Window{3e4c0a3 u0 com.tw.devicefan/com.tw.devicefan.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
    mAppToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
  Window #15 Window{b13c61e u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
    mAppToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
  Window #16 Window{65fd7c4 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
    mAppToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
  Window #17 Window{7e290d2 u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.radio/.RadioActivity mSnapshot=android.graphics.GraphicBuffer@b47f0f (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.music/.MusicActivity mSnapshot=android.graphics.GraphicBuffer@707c93c (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
      topApp=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
      topApp=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
      topApp=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
      topApp=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@60825e6 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
```
## Phase: after_auxio_launcher_seek

```text
  Window #0 Window{7f85782 u0 NavigationBar0}:
  Window #1 Window{cc4ec00 u0 StatusBar}:
  Window #2 Window{78eb3fe u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{bd7954f u0 com.zjinnova.zlink}:
    mOwnerUid=10153 mShowToOwnerOnly=true package=com.zjinnova.zlink appop=SYSTEM_ALERT_WINDOW
  Window #4 Window{d05ff7b u0 AssistPreviewPanel}:
  Window #5 Window{34a6f2d u0 DockedStackDivider}:
  Window #6 Window{feb52f0 u0 InputMethod}:
  Window #7 Window{30c0359 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
    mAppToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
  Window #8 Window{f743fbd u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
    mAppToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
  Window #9 Window{ce30a7 u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
    mAppToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
  Window #10 Window{70e5d3a u0 com.tw.radio/com.tw.radio.RadioActivity}:
    mOwnerUid=10078 mShowToOwnerOnly=true package=com.tw.radio appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
    mAppToken=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
  Window #11 Window{cb2e4b2 u0 com.tw.music/com.tw.music.MusicActivity}:
    mOwnerUid=1000 mShowToOwnerOnly=true package=com.tw.music appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
    mAppToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
  Window #12 Window{f1e8cd3 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
    mAppToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
  Window #13 Window{d374de u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
    mAppToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
  Window #14 Window{3e4c0a3 u0 com.tw.devicefan/com.tw.devicefan.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
    mAppToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
  Window #15 Window{b13c61e u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
    mAppToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
  Window #16 Window{65fd7c4 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
    mAppToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
  Window #17 Window{7e290d2 u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.radio/.RadioActivity mSnapshot=android.graphics.GraphicBuffer@b47f0f (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.music/.MusicActivity mSnapshot=android.graphics.GraphicBuffer@707c93c (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
      topApp=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
      topApp=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
      topApp=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
      topApp=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@60825e6 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
```
## Phase: after_stock_disable_attempts

```text
  Window #0 Window{7f85782 u0 NavigationBar0}:
  Window #1 Window{cc4ec00 u0 StatusBar}:
  Window #2 Window{78eb3fe u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{bd7954f u0 com.zjinnova.zlink}:
    mOwnerUid=10153 mShowToOwnerOnly=true package=com.zjinnova.zlink appop=SYSTEM_ALERT_WINDOW
  Window #4 Window{d05ff7b u0 AssistPreviewPanel}:
  Window #5 Window{34a6f2d u0 DockedStackDivider}:
  Window #6 Window{feb52f0 u0 InputMethod}:
  Window #7 Window{30c0359 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
    mAppToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
  Window #8 Window{f743fbd u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
    mAppToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
  Window #9 Window{ce30a7 u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
    mAppToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
  Window #10 Window{70e5d3a u0 com.tw.radio/com.tw.radio.RadioActivity}:
    mOwnerUid=10078 mShowToOwnerOnly=true package=com.tw.radio appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
    mAppToken=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
  Window #11 Window{cb2e4b2 u0 com.tw.music/com.tw.music.MusicActivity}:
    mOwnerUid=1000 mShowToOwnerOnly=true package=com.tw.music appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
    mAppToken=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
  Window #12 Window{f1e8cd3 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
    mAppToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
  Window #13 Window{d374de u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
    mAppToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
  Window #14 Window{3e4c0a3 u0 com.tw.devicefan/com.tw.devicefan.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
    mAppToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
  Window #15 Window{b13c61e u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
    mAppToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
  Window #16 Window{65fd7c4 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
    mAppToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
  Window #17 Window{7e290d2 u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.radio/.RadioActivity mSnapshot=android.graphics.GraphicBuffer@b47f0f (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{7a29d69 token=Token{50023f0 ActivityRecord{ef32433 u0 com.tw.music/.MusicActivity t6799}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.music/.MusicActivity mSnapshot=android.graphics.GraphicBuffer@707c93c (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
      topApp=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
      topApp=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
      topApp=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
      topApp=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@60825e6 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
```
## Phase: final

```text
  Window #0 Window{7f85782 u0 NavigationBar0}:
  Window #1 Window{cc4ec00 u0 StatusBar}:
  Window #2 Window{78eb3fe u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #3 Window{bd7954f u0 com.zjinnova.zlink}:
    mOwnerUid=10153 mShowToOwnerOnly=true package=com.zjinnova.zlink appop=SYSTEM_ALERT_WINDOW
  Window #4 Window{d05ff7b u0 AssistPreviewPanel}:
  Window #5 Window{34a6f2d u0 DockedStackDivider}:
  Window #6 Window{feb52f0 u0 InputMethod}:
  Window #7 Window{30c0359 u0 com.termoneplus/com.termoneplus.TermActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
    mAppToken=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
  Window #8 Window{f743fbd u0 com.dofun.variety/com.dofun.overseasvariety.Launcher}:
    mOwnerUid=10093 mShowToOwnerOnly=true package=com.dofun.variety appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
    mAppToken=AppWindowToken{f8d6849 token=Token{8bdce50 ActivityRecord{38d7c13 u0 com.dofun.variety/com.dofun.overseasvariety.Launcher t6789}}}
  Window #9 Window{ce30a7 u0 com.android.systemui/com.android.systemui.recents.RecentsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
    mAppToken=AppWindowToken{c0ac5d5 token=Token{d3f3a8c ActivityRecord{85443bf u0 com.android.systemui/.recents.RecentsActivity t6798}}}
  Window #10 Window{70e5d3a u0 com.tw.radio/com.tw.radio.RadioActivity}:
    mOwnerUid=10078 mShowToOwnerOnly=true package=com.tw.radio appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
    mAppToken=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
  Window #11 Window{f1e8cd3 u0 com.tw.media/com.tw.music.MusicActivity}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=NONE
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
    mAppToken=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
  Window #12 Window{d374de u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
    mAppToken=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
  Window #13 Window{3e4c0a3 u0 com.tw.devicefan/com.tw.devicefan.MainActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
    mAppToken=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
  Window #14 Window{b13c61e u0 com.android.settings/com.android.settings.Settings$WifiSettingsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
    mAppToken=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
  Window #15 Window{65fd7c4 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity}:
    mBaseLayer=21000 mSubLayer=0    mToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
    mAppToken=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
  Window #16 Window{7e290d2 u0 com.android.systemui.ImageWallpaper}:
      topApp=AppWindowToken{3ba0af7 token=Token{b6db6f6 ActivityRecord{17aa791 u0 com.tw.radio/.RadioActivity t6800}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.radio/.RadioActivity mSnapshot=android.graphics.GraphicBuffer@b47f0f (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1280 mIsTranslucent=true
      topApp=AppWindowToken{ec23d36 token=Token{d505ed1 ActivityRecord{73fe9f8 u0 com.termoneplus/.TermActivity t6797}}}
      topApp=AppWindowToken{7ad1a2a token=Token{f218715 ActivityRecord{fdf38cc u0 com.cxinventor.file.explorer/com.alphainventor.filemanager.activity.MainActivity t6796}}}
      topApp=AppWindowToken{f773b09 token=Token{1343410 ActivityRecord{16ae0d3 u0 com.tw.devicefan/.MainActivity t6795}}}
      topApp=AppWindowToken{fde26b token=Token{8cd33ba ActivityRecord{152cce5 u0 com.android.settings/.Settings$WifiSettingsActivity t6793}}}
      topApp=AppWindowToken{ce99fe8 token=Token{915b50b ActivityRecord{e2c2cda u0 com.tw.media/com.tw.music.MusicActivity t6792}}}
      snapshot=TaskSnapshot{ mTopActivityComponent=com.tw.media/org.oxycblt.auxio.MainActivity mSnapshot=android.graphics.GraphicBuffer@60825e6 (1280x720) mColorSpace=sRGB IEC61966-2.1 (id=0, model=RGB) mOrientation=2 mContentInsets=[0,55][55,0] mReducedResolution=false mScale=1.0 mIsRealSnapshot=true mWindowingMode=1 mSystemUiVisibility=1792 mIsTranslucent=false
      topApp=AppWindowToken{a3a852f token=Token{ec7570e ActivityRecord{b24d909 u0 com.google.android.apps.maps/com.google.android.maps.MapsActivity t6791}}}
```


# TS18 v2 selected media-session excerpts

**Source:** `ts18_dofun_runtime_validation_v2_20260610_162755/runtime/phases/*/media_session.txt`

**Why this matters:** Auxio-TS `com.tw.media` is visible to Android's media-session stack on the TS18. This strongly argues that the fixed DoFun Music widget issue is not generic MediaSession visibility alone.

## Phase: initial

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=838, ownerUid=1000, userId=0
    package=com.android.server.telecom
    mediaButtonReceiver=null
    active=false
    state=null
    metadata: null
  Last MediaButtonReceiver: null
  Restored MediaButtonReceiver: ComponentInfo{com.tw.media/org.oxycblt.auxio.playback.service.MediaButtonReceiver}
  Restored MediaButtonReceiverComponentType: 1
  Media button session is null
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3030, ownerUid=10196, userId=0
      package=com.tw.media
      mediaButtonReceiver=PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
      active=true
      state=PlaybackState {state=2, position=4878, buffered position=0, speed=0.0, updated=85415, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=2, error=null}
      metadata: size=26, description=You Can't Always Get What You Want, The Rolling Stones, Liked_Songs
```
## Phase: after_music_widget_tap

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=838, ownerUid=1000, userId=0
    package=com.android.server.telecom
    mediaButtonReceiver=null
    active=false
    state=null
    metadata: null
  Last MediaButtonReceiver: null
  Restored MediaButtonReceiver: ComponentInfo{com.tw.media/org.oxycblt.auxio.playback.service.MediaButtonReceiver}
  Restored MediaButtonReceiverComponentType: 1
  Media button session is null
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3030, ownerUid=10196, userId=0
      package=com.tw.media
      mediaButtonReceiver=PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
      active=true
      state=PlaybackState {state=2, position=4878, buffered position=0, speed=0.0, updated=85415, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=2, error=null}
      metadata: size=26, description=You Can't Always Get What You Want, The Rolling Stones, Liked_Songs
  uid=1000 packages=com.tw.auxin com.sprd.validationtools com.tw.service com.android.inputdevices com.sprd.vsimservice com.sprd.cameracalibration com.tw.tv com.tw.dvd com.spreadtrum.sgps com.android.dynsystem com.android.localtransport com.sprd.systemupdate com.android.cellbroadcastreceiver com.tw.music com.tw.bootanimation com.tw.dvr com.tw.bt com.android.wallpaperbackup com.tw.reverse com.android.settings com.abupdate.fota_demo_iot com.tw.core com.spreadtrum.vce com.tw.carinfoservice com.tw.rightview com.tw.net com.ms.ms2160 com.dofun.carsetting android com.sprd.autoslt com.android.packageinstaller com.tw.video com.tw.service.xt com.sprd.engineermode com.android.location.fused com.tw.bug.report com.tw.eq com.tw.devicefan com.android.providers.settings com.unisoc.storageclearmanager com.tw.coreservice com.tw.carchoose com.android.keychain com.android.server.telecom 
```
## Phase: after_auxio_active_music_widget_controls

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=838, ownerUid=1000, userId=0
    package=com.android.server.telecom
    mediaButtonReceiver=null
    active=false
    state=null
    metadata: null
  Last MediaButtonReceiver: PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
  Restored MediaButtonReceiver: null
  Restored MediaButtonReceiverComponentType: 0
  Media button session is com.tw.media/com.tw.media (userId=0)
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3030, ownerUid=10196, userId=0
      package=com.tw.media
      mediaButtonReceiver=PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
      active=true
      state=PlaybackState {state=2, position=19049, buffered position=0, speed=0.0, updated=1408941, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=4, error=null}
      metadata: size=26, description=Lady Lady, Olivia Dean • Various Artists, Liked_Songs
  uid=1000 packages=com.tw.auxin com.sprd.validationtools com.tw.service com.android.inputdevices com.sprd.vsimservice com.sprd.cameracalibration com.tw.tv com.tw.dvd com.spreadtrum.sgps com.android.dynsystem com.android.localtransport com.sprd.systemupdate com.android.cellbroadcastreceiver com.tw.music com.tw.bootanimation com.tw.dvr com.tw.bt com.android.wallpaperbackup com.tw.reverse com.android.settings com.abupdate.fota_demo_iot com.tw.core com.spreadtrum.vce com.tw.carinfoservice com.tw.rightview com.tw.net com.ms.ms2160 com.dofun.carsetting android com.sprd.autoslt com.android.packageinstaller com.tw.video com.tw.service.xt com.sprd.engineermode com.android.location.fused com.tw.bug.report com.tw.eq com.tw.devicefan com.android.providers.settings com.unisoc.storageclearmanager com.tw.coreservice com.tw.carchoose com.android.keychain com.android.server.telecom 
  uid=10196 packages=com.tw.media 
```
## Phase: after_stock_active_music_widget_controls

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=838, ownerUid=1000, userId=0
    package=com.android.server.telecom
    mediaButtonReceiver=null
    active=false
    state=null
    metadata: null
  Last MediaButtonReceiver: PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
  Restored MediaButtonReceiver: null
  Restored MediaButtonReceiverComponentType: 0
  Media button session is com.tw.media/com.tw.media (userId=0)
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3030, ownerUid=10196, userId=0
      package=com.tw.media
      mediaButtonReceiver=PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
      active=true
      state=PlaybackState {state=2, position=19049, buffered position=0, speed=0.0, updated=1408941, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=4, error=null}
      metadata: size=26, description=Lady Lady, Olivia Dean • Various Artists, Liked_Songs
  uid=1000 packages=com.tw.auxin com.sprd.validationtools com.tw.service com.android.inputdevices com.sprd.vsimservice com.sprd.cameracalibration com.tw.tv com.tw.dvd com.spreadtrum.sgps com.android.dynsystem com.android.localtransport com.sprd.systemupdate com.android.cellbroadcastreceiver com.tw.music com.tw.bootanimation com.tw.dvr com.tw.bt com.android.wallpaperbackup com.tw.reverse com.android.settings com.abupdate.fota_demo_iot com.tw.core com.spreadtrum.vce com.tw.carinfoservice com.tw.rightview com.tw.net com.ms.ms2160 com.dofun.carsetting android com.sprd.autoslt com.android.packageinstaller com.tw.video com.tw.service.xt com.sprd.engineermode com.android.location.fused com.tw.bug.report com.tw.eq com.tw.devicefan com.android.providers.settings com.unisoc.storageclearmanager com.tw.coreservice com.tw.carchoose com.android.keychain com.android.server.telecom 
  uid=10196 packages=com.tw.media 
```
## Phase: after_navradio_widget_controls

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=838, ownerUid=1000, userId=0
    package=com.android.server.telecom
    mediaButtonReceiver=null
    active=false
    state=null
    metadata: null
  Last MediaButtonReceiver: PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
  Restored MediaButtonReceiver: null
  Restored MediaButtonReceiverComponentType: 0
  Media button session is com.tw.media/com.tw.media (userId=0)
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3030, ownerUid=10196, userId=0
      package=com.tw.media
      mediaButtonReceiver=PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
      active=true
      state=PlaybackState {state=2, position=19049, buffered position=0, speed=0.0, updated=1408941, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=4, error=null}
      metadata: size=26, description=Lady Lady, Olivia Dean • Various Artists, Liked_Songs
  uid=1000 packages=com.tw.auxin com.sprd.validationtools com.tw.service com.android.inputdevices com.sprd.vsimservice com.sprd.cameracalibration com.tw.tv com.tw.dvd com.spreadtrum.sgps com.android.dynsystem com.android.localtransport com.sprd.systemupdate com.android.cellbroadcastreceiver com.tw.music com.tw.bootanimation com.tw.dvr com.tw.bt com.android.wallpaperbackup com.tw.reverse com.android.settings com.abupdate.fota_demo_iot com.tw.core com.spreadtrum.vce com.tw.carinfoservice com.tw.rightview com.tw.net com.ms.ms2160 com.dofun.carsetting android com.sprd.autoslt com.android.packageinstaller com.tw.video com.tw.service.xt com.sprd.engineermode com.android.location.fused com.tw.bug.report com.tw.eq com.tw.devicefan com.android.providers.settings com.unisoc.storageclearmanager com.tw.coreservice com.tw.carchoose com.android.keychain com.android.server.telecom 
  uid=10196 packages=com.tw.media 
```
## Phase: after_auxio_launcher_seek

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=838, ownerUid=1000, userId=0
    package=com.android.server.telecom
    mediaButtonReceiver=null
    active=false
    state=null
    metadata: null
  Last MediaButtonReceiver: PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
  Restored MediaButtonReceiver: null
  Restored MediaButtonReceiverComponentType: 0
  Media button session is com.tw.media/com.tw.media (userId=0)
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3030, ownerUid=10196, userId=0
      package=com.tw.media
      mediaButtonReceiver=PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
      active=true
      state=PlaybackState {state=3, position=0, buffered position=0, speed=1.0, updated=1719582, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=5, error=null}
      metadata: size=26, description=Crying Shame, Jack Johnson, In Between Dreams
  uid=10196 packages=com.tw.media 
```
## Phase: after_stock_disable_attempts

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=838, ownerUid=1000, userId=0
    package=com.android.server.telecom
    mediaButtonReceiver=null
    active=false
    state=null
    metadata: null
  Last MediaButtonReceiver: PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
  Restored MediaButtonReceiver: null
  Restored MediaButtonReceiverComponentType: 0
  Media button session is com.tw.media/com.tw.media (userId=0)
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3030, ownerUid=10196, userId=0
      package=com.tw.media
      mediaButtonReceiver=PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
      active=true
      state=PlaybackState {state=3, position=0, buffered position=0, speed=1.0, updated=1719582, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=5, error=null}
      metadata: size=26, description=Crying Shame, Jack Johnson, In Between Dreams
  uid=10196 packages=com.tw.media 
```
## Phase: final

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=838, ownerUid=1000, userId=0
    package=com.android.server.telecom
    mediaButtonReceiver=null
    active=false
    state=null
    metadata: null
  Last MediaButtonReceiver: PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
  Restored MediaButtonReceiver: null
  Restored MediaButtonReceiverComponentType: 0
  Media button session is com.tw.media/com.tw.media (userId=0)
  Sessions Stack - have 1 sessions:
    com.tw.media com.tw.media/com.tw.media (userId=0)
      ownerPid=3030, ownerUid=10196, userId=0
      package=com.tw.media
      mediaButtonReceiver=PendingIntent{16c195e: PendingIntentRecord{775a13f com.tw.media broadcastIntent}}
      active=true
      state=PlaybackState {state=2, position=53554, buffered position=0, speed=0.0, updated=1773252, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=5, error=null}
      metadata: size=26, description=Crying Shame, Jack Johnson, In Between Dreams
  uid=1000 packages=com.tw.auxin com.sprd.validationtools com.tw.service com.android.inputdevices com.sprd.vsimservice com.sprd.cameracalibration com.tw.tv com.tw.dvd com.spreadtrum.sgps com.android.dynsystem com.android.localtransport com.sprd.systemupdate com.android.cellbroadcastreceiver com.tw.music com.tw.bootanimation com.tw.dvr com.tw.bt com.android.wallpaperbackup com.tw.reverse com.android.settings com.abupdate.fota_demo_iot com.tw.core com.spreadtrum.vce com.tw.carinfoservice com.tw.rightview com.tw.net com.ms.ms2160 com.dofun.carsetting android com.sprd.autoslt com.android.packageinstaller com.tw.video com.tw.service.xt com.sprd.engineermode com.android.location.fused com.tw.bug.report com.tw.eq com.tw.devicefan com.android.providers.settings com.unisoc.storageclearmanager com.tw.coreservice com.tw.carchoose com.android.keychain com.android.server.telecom 
  uid=10196 packages=com.tw.media 
```


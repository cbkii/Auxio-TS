# TS18 runtime excerpt: Auxio-TS media session is visible to Android

Source: `ts18_dofun_runtime_validation_20260610_125608/runtime/phases/after_manual_auxio_widget_controls/media_session.txt`

Type: Observation with inference.

Why it matters: Auxio-TS is visible as an active Android media session and selected media-button session on the TS18, but the manual observation says DoFun Music widget controls still operated stock music rather than Auxio. This narrows the remaining gap to DoFun/TW Music-specific component/broadcast/widget handling rather than generic Android media-session visibility alone.

```text
Global priority session is com.android.server.telecom/HeadsetMediaButton (userId=0)
  HeadsetMediaButton com.android.server.telecom/HeadsetMediaButton (userId=0)
    ownerPid=859, ownerUid=1000, userId=0
    package=com.android.server.telecom
    launchIntent=null
    mediaButtonReceiver=null
    active=false
    flags=65537
    rating type=0
--
    audioAttrs=AudioAttributes: usage=USAGE_VOICE_COMMUNICATION content=CONTENT_TYPE_SPEECH flags=0x800 tags= bundle=null
    volumeType=1, controlType=2, max=0, current=0
    metadata: null
    queueTitle=null, size=0
User Records:
Record for full_user=0
--
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
      rating type=0
      controllers: 2
      state=PlaybackState {state=2, position=14006, buffered position=0, speed=0.0, updated=1457126, actions=2367295, custom actions=[Action:mName='Change repeat mode, mIcon=2131230971, mExtras=null, Action:mName='Turn shuffle on or off, mIcon=2131230981, mExtras=null], active item id=2, error=null}
      audioAttrs=AudioAttributes: usage=USAGE_MEDIA content=CONTENT_TYPE_UNKNOWN flags=0x800 tags= bundle=null
      volumeType=1, controlType=2, max=0, current=0
      metadata: size=26, description=You Can't Always Get What You Want, The Rolling Stones, Liked_Songs
      queueTitle=Queue, size=394

  Session2Tokens:
Audio playback (lastly played comes first)
  uid=10196 packages=com.tw.media 

Exit code: 0
```

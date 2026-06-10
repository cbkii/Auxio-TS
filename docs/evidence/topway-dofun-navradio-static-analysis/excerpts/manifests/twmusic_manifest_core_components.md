# Evidence excerpt: twmusic_manifest_core_components.md

 Source APK/variant: `com.tw.music_TW_THEME.20240715`
 Source path: `com.tw.music_TW_THEME.20240715/apktool/AndroidManifest.xml`
 Source lines: `1-45`
 Status: observation from static decompile/extract.
 Why it matters: Stock Topway TW Music package identity, sharedUserId/system-app context, activity/service/widget provider component names, and appwidget provider metadata.

 ```xml
     1: <?xml version="1.0" encoding="utf-8" standalone="no"?><manifest xmlns:android="http://schemas.android.com/apk/res/android" android:compileSdkVersion="29" android:compileSdkVersionCodename="10" android:sharedUserId="android.uid.system" package="com.tw.music" platformBuildVersionCode="29" platformBuildVersionName="10">
 2:     <uses-permission android:name="android.permission.BROADCAST_STICKY"/>
 3:     <uses-permission android:name="android.permission.RECORD_AUDIO"/>
 4:     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 5:     <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
 6:     <uses-permission android:name="android.permission.SYSTEM_OVERLAY_WINDOW"/>
 7:     <uses-permission android:name="android.permission.BLUETOOTH"/>
 8:     <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
 9:     <application android:allowBackup="true" android:appComponentFactory="android.support.v4.app.CoreComponentFactory" android:extractNativeLibs="false" android:icon="@drawable/ic_launcher" android:label="@string/app_name" android:name="com.tw.music.MusicApplication" android:supportsRtl="false" android:theme="@style/AppTheme" android:usesNonSdkApi="true">
10:         <activity android:launchMode="singleTask" android:name="com.tw.music.MusicActivity">
11:             <intent-filter>
12:                 <action android:name="android.intent.action.MAIN"/>
13:                 <category android:name="android.intent.category.LAUNCHER"/>
14:             </intent-filter>
15:         </activity>
16:         <activity android:excludeFromRecents="true" android:exported="true" android:name="com.tw.music.AudioPreview" android:taskAffinity="" android:theme="@android:style/Theme.Dialog">
17:             <intent-filter>
18:                 <action android:name="android.intent.action.VIEW"/>
19:                 <category android:name="android.intent.category.DEFAULT"/>
20:                 <data android:scheme="file"/>
21:                 <data android:mimeType="audio/*"/>
22:                 <data android:mimeType="application/ogg"/>
23:                 <data android:mimeType="application/x-ogg"/>
24:                 <data android:mimeType="application/itunes"/>
25:             </intent-filter>
26:             <intent-filter>
27:                 <action android:name="android.intent.action.VIEW"/>
28:                 <category android:name="android.intent.category.DEFAULT"/>
29:                 <category android:name="android.intent.category.BROWSABLE"/>
30:                 <data android:scheme="http"/>
31:                 <data android:mimeType="audio/*"/>
32:                 <data android:mimeType="application/ogg"/>
33:                 <data android:mimeType="application/x-ogg"/>
34:                 <data android:mimeType="application/itunes"/>
35:             </intent-filter>
36:             <intent-filter android:priority="-1">
37:                 <action android:name="android.intent.action.VIEW"/>
38:                 <category android:name="android.intent.category.DEFAULT"/>
39:                 <category android:name="android.intent.category.BROWSABLE"/>
40:                 <data android:scheme="content"/>
41:                 <data android:mimeType="audio/*"/>
42:                 <data android:mimeType="application/ogg"/>
43:                 <data android:mimeType="application/x-ogg"/>
44:                 <data android:mimeType="application/itunes"/>
45:             </intent-filter>
 ```

# Evidence excerpt: dofun_manifest_launcher_permissions_excerpt.md

 Source APK/variant: `com.dofun.variety_V9.7.2.367.260312_ac_anti`
 Source path: `com.dofun.variety_V9.7.2.367.260312_ac_anti/apktool/AndroidManifest.xml`
 Source lines: `1-70`
 Status: observation from static decompile/extract.
 Why it matters: DoFun launcher identity, permissions and protected/stub context. The manifest itself does not expose the music-widget protocol; the asset configs are stronger for launch target resolution.

 ```xml
     1: <?xml version="1.0" encoding="utf-8" standalone="no"?><manifest xmlns:android="http://schemas.android.com/apk/res/android" android:compileSdkVersion="34" android:compileSdkVersionCodename="14" package="com.dofun.variety" platformBuildVersionCode="34" platformBuildVersionName="14">
 2:     <supports-screens android:anyDensity="true" android:largeScreens="true" android:normalScreens="true" android:resizeable="true" android:smallScreens="true"/>
 3:     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 4:     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 5:     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 6:     <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 7:     <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
 8:     <uses-permission android:name="android.permission.INTERNET"/>
 9:     <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
10:     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
11:     <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"/>
12:     <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
13:     <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION"/>
14:     <permission android:name="cn.cardoor.variety.permission.ENTRANCE_ICON_FILTER" android:protectionLevel="normal"/>
15:     <uses-permission android:name="android.permission.BROADCAST_STICKY"/>
16:     <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
17:     <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
18:     <uses-permission android:name="android.permission.READ_LOGS"/>
19:     <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
20:     <uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>
21:     <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"/>
22:     <uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS"/>
23:     <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
24:     <uses-permission android:name="android.permission.RECORD_AUDIO"/>
25:     <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
26:     <permission android:name="com.dofun.variety.permission.THEME_LINKAGE_SERVICE"/>
27:     <permission android:name="com.dofun.variety.permission.provider.exported.WRITE_KV_CONFIG" android:protectionLevel="normal"/>
28:     <uses-permission android:name="android.permission.BLUETOOTH"/>
29:     <queries>
30:         <intent>
31:             <action android:name="cn.cardoor.intent.action.VEHICLE3D_BRIDGE_SERVER"/>
32:         </intent>
33:         <intent>
34:             <action android:name="cn.cardoor.intent.action.VEHICLE3D_BRIDGE_APP_LAUNCHED"/>
35:         </intent>
36:         <package android:name="cn.cardoor.d3dview"/>
37:         <intent>
38:             <action android:name="cn.cardoor.intent.action.VEHICLE3D_ATTITUDE_SERVER"/>
39:         </intent>
40:         <intent>
41:             <action android:name="cn.cardoor.libs.media.RemoteMediaService"/>
42:         </intent>
43:         <package android:name="com.spotify.music"/>
44:         <package android:name="com.spotify.music.debug"/>
45:         <package android:name="com.spotify.music.canary"/>
46:         <package android:name="com.spotify.music.partners"/>
47:     </queries>
48:     <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
49:     <uses-permission android:name="android.permission.MEDIA_CONTENT_CONTROL"/>
50:     <uses-permission android:name="com.tencent.wecarflow.PLAY_CONTROL"/>
51:     <uses-permission android:name="com.android.providers.media.MediaProvider"/>
52:     <uses-permission android:name="android.permission.CAMERA"/>
53:     <permission android:name="com.dofun.variety.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION" android:protectionLevel="signature"/>
54:     <uses-permission android:name="com.dofun.variety.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION"/>
55:     <application android:allowBackup="false" android:configChanges="orientation|screenLayout|screenSize|smallestScreenSize" android:hardwareAccelerated="true" android:icon="@mipmap/mipmap0013" android:label="@string/str03bc" android:largeHeap="true" android:name="com.stub.StubApp" android:requestLegacyExternalStorage="true" android:resizeableActivity="false" android:roundIcon="@mipmap/mipmap0018" android:supportsRtl="true">
56:         <meta-data android:name="PACKAGE_RELEASE_TIME" android:value="20260312-11:47:52"/>
57:         <meta-data android:name="VERSION_DESC" android:value=""/>
58:         <receiver android:exported="true" android:name="com.dofun.variety.VarietyReceiver">
59:             <intent-filter>
60:                 <action android:name="android.intent.action.BOOT_COMPLETED"/>
61:                 <category android:name="android.intent.category.HOME"/>
62:                 <category android:name="android.intent.category.LAUNCHER"/>
63:                 <category android:name="android.intent.category.DEFAULT"/>
64:             </intent-filter>
65:             <intent-filter>
66:                 <action android:name="android.intent.action.LOCALE_CHANGED"/>
67:                 <category android:name="android.intent.category.DEFAULT"/>
68:             </intent-filter>
69:         </receiver>
70:         <service android:enabled="true" android:name="com.baidu.location.f"/>
 ```

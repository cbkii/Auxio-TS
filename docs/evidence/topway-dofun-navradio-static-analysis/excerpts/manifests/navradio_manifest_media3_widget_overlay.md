# Evidence excerpt: navradio_manifest_media3_widget_overlay.md

 Source APK/variant: `NavRadio+_4.00_apks`
 Source path: `NavRadio+_4.00_apks/apktool/AndroidManifest.xml`
 Source lines: `1-70`
 Status: observation from static decompile/extract.
 Why it matters: NavRadio+ manifest component surface: MediaSessionService, floating widget service, exported widget providers, overlay permission.

 ```xml
     1: <?xml version="1.0" encoding="utf-8" standalone="no"?><manifest xmlns:android="http://schemas.android.com/apk/res/android" android:compileSdkVersion="36" android:compileSdkVersionCodename="16" package="com.navimods.radio" platformBuildVersionCode="36" platformBuildVersionName="16">
 2:     <uses-permission android:name="com.android.vending.CHECK_LICENSE"/>
 3:     <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
 4:     <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
 5:     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 6:     <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
 7:     <uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK"/>
 8:     <uses-permission android:maxSdkVersion="32" android:name="android.permission.READ_EXTERNAL_STORAGE"/>
 9:     <uses-permission android:maxSdkVersion="32" android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
10:     <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
11:     <uses-permission android:name="android.permission.INTERNET"/>
12:     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
13:     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
14:     <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
15:     <uses-permission android:name="android.permission.ACTION_MANAGE_OVERLAY_PERMISSION"/>
16:     <queries>
17:         <package android:name="com.google.android.gms"/>
18:         <package android:name="com.android.vending"/>
19:         <intent>
20:             <action android:name="com.android.vending.billing.InAppBillingService.BIND"/>
21:         </intent>
22:         <intent>
23:             <action android:name="com.google.android.apps.play.billingtestcompanion.BillingOverrideService.BIND"/>
24:         </intent>
25:     </queries>
26:     <uses-permission android:name="com.android.vending.BILLING"/>
27:     <uses-permission android:name="android.permission.WAKE_LOCK"/>
28:     <uses-permission android:name="com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE"/>
29:     <uses-permission android:name="android.permission.ACCESS_ADSERVICES_ATTRIBUTION"/>
30:     <uses-permission android:name="android.permission.ACCESS_ADSERVICES_AD_ID"/>
31:     <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>
32:     <permission android:name="com.navimods.radio.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION" android:protectionLevel="signature"/>
33:     <uses-permission android:name="com.navimods.radio.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION"/>
34:     <application android:allowBackup="true" android:appComponentFactory="androidx.core.app.CoreComponentFactory" android:enableOnBackInvokedCallback="true" android:extractNativeLibs="false" android:fullBackupContent="@xml/backup_descriptor" android:hardwareAccelerated="true" android:hasFragileUserData="true" android:icon="@mipmap/ic_launcher" android:label="@string/app_name" android:name="com.navimods.radio.RadioApplication" android:requestLegacyExternalStorage="true" android:roundIcon="@mipmap/ic_launcher_round" android:supportsRtl="true" android:theme="@style/AppTheme">
35:         <meta-data android:name="ui_id" android:value="1"/>
36:         <activity android:configChanges="density|locale|orientation|screenLayout|screenSize|smallestScreenSize" android:exported="true" android:launchMode="singleTask" android:name="com.navimods.radio.RadioActivity" android:resizeableActivity="true" android:theme="@style/Theme.App.Starting">
37:             <intent-filter>
38:                 <action android:name="android.intent.action.MAIN"/>
39:                 <category android:name="android.intent.category.LAUNCHER"/>
40:                 <category android:name="android.intent.category.DEFAULT"/>
41:             </intent-filter>
42:         </activity>
43:         <activity android:name="com.navimods.radio.ui.intro.DefaultIntro" android:theme="@style/AppIntroTheme"/>
44:         <activity android:enabled="false" android:exported="true" android:icon="@mipmap/ic_file_json" android:label="NR_Service" android:name="com.navimods.radio.ServiceActivity" android:theme="@android:style/Theme.NoDisplay">
45:             <intent-filter>
46:                 <action android:name="android.intent.action.MAIN"/>
47:                 <category android:name="android.intent.category.LAUNCHER"/>
48:             </intent-filter>
49:         </activity>
50:         <activity android:configChanges="density|locale|orientation|screenLayout|screenSize|smallestScreenSize" android:label="@string/title_activity_settings" android:launchMode="singleTask" android:name="com.navimods.radio.SettingsActivity" android:resizeableActivity="true" android:theme="@style/AppTheme"/>
51:         <activity android:configChanges="density|locale|orientation|screenLayout|screenSize|smallestScreenSize" android:exported="false" android:label="@string/redeem_title" android:name="com.navimods.radio.ui.RedeemCodeActivity" android:parentActivityName="com.navimods.radio.SettingsActivity" android:resizeableActivity="true" android:theme="@style/AppTheme"/>
52:         <service android:enabled="true" android:exported="true" android:foregroundServiceType="mediaPlayback" android:name="com.navimods.radio.RadioService" android:persistent="true">
53:             <intent-filter>
54:                 <action android:name="androidx.media3.session.MediaSessionService"/>
55:             </intent-filter>
56:         </service>
57:         <service android:enabled="true" android:exported="true" android:name="com.navimods.radio.widgets.FloatWidgetService"/>
58:         <receiver android:enabled="true" android:exported="true" android:name="com.navimods.radio.util.AlarmBroadcastManager"/>
59:         <receiver android:directBootAware="true" android:enabled="true" android:exported="true" android:name="com.navimods.radio.MyBootReceiver" android:permission="android.permission.RECEIVE_BOOT_COMPLETED">
60:             <intent-filter>
61:                 <action android:name="android.intent.action.BOOT_COMPLETED"/>
62:                 <action android:name="android.intent.action.LOCKED_BOOT_COMPLETED"/>
63:                 <action android:name="android.intent.action.QUICKBOOT_POWERON"/>
64:                 <action android:name="com.htc.intent.action.QUICKBOOT_POWERON"/>
65:                 <category android:name="android.intent.category.DEFAULT"/>
66:             </intent-filter>
67:         </receiver>
68:         <receiver android:exported="true" android:name="com.navimods.radio.widgets.RadioWidgetExtended" android:permission="android.permission.BIND_REMOTEVIEWS">
69:             <intent-filter>
70:                 <action android:name="android.appwidget.action.APPWIDGET_UPDATE"/>
 ```

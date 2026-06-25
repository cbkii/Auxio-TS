For **non-Magisk Auxio-TS on TS18**, I’d add only a small number of normal/runtime permissions. The current manifest already has most essentials: audio/media read, foreground service, wake lock, notifications, and boot completed; it also opts into legacy external storage.  The Topway-compatible flavour already declares overlay permission and a TS18 overlay boot receiver.

The main missing one I would add is:

```xml
<uses-permission
    android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="29" />
```

That is specifically useful for **Android 10 / API 29 TS18** when the app uses `requestLegacyExternalStorage="true"` and needs reliable file-path/media-library behaviour beyond simple MediaStore reads. Android’s own docs still show `WRITE_EXTERNAL_STORAGE` with `maxSdkVersion="29"` when an app needs to modify shared media on legacy/scoped-storage-opt-out devices. ([Android Developers][1])

I would also consider these, but only if the related feature is actually enabled:

```xml
<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />

<uses-permission
    android:name="android.permission.BLUETOOTH"
    android:maxSdkVersion="30" />
<uses-permission
    android:name="android.permission.BLUETOOTH_ADMIN"
    android:maxSdkVersion="30" />
<uses-permission
    android:name="android.permission.BLUETOOTH_CONNECT"
    android:minSdkVersion="31" />
```

`REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` only lets the app open/request the system battery-optimisation exemption screen; it does not grant the exemption by itself. Android defines it as the permission required for `ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`. ([Android Developers][2])

The Bluetooth permissions are only worth adding if Auxio-TS is going to re-enable Bluetooth headset/device connection handling. The current main manifest has the Bluetooth permission block commented out, so adding them without using Bluetooth APIs won’t improve playback by itself. Android defines legacy `BLUETOOTH` / `BLUETOOTH_ADMIN` as normal permissions for paired-device/discovery APIs, while `BLUETOOTH_CONNECT` is the Android 12+ dangerous permission for connecting to paired devices. ([Android Developers][2])

I would **not** add privileged/system permissions such as `BLUETOOTH_PRIVILEGED`, `WRITE_SECURE_SETTINGS`, `READ_LOGS`, `MANAGE_EXTERNAL_STORAGE`, or `QUERY_ALL_PACKAGES`. They are either not available to normal APKs, too broad, or not justified for Auxio-TS.

Suggested manifest additions:

```xml
<!-- Android 10 / TS18 legacy storage compatibility for file-path based media handling. -->
<uses-permission
    android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="29" />

<!-- Optional: allow the app to request battery optimisation exemption for autostart/overlay persistence. -->
<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />

<!-- Optional only if Bluetooth connection-state handling is enabled in code. -->
<uses-permission
    android:name="android.permission.BLUETOOTH"
    android:maxSdkVersion="30" />
<uses-permission
    android:name="android.permission.BLUETOOTH_ADMIN"
    android:maxSdkVersion="30" />
<uses-permission
    android:name="android.permission.BLUETOOTH_CONNECT"
    android:minSdkVersion="31" />
```

Also remember: for the floating controls, `SYSTEM_ALERT_WINDOW` is already declared in `topwayCompat`, but normal APK installs still require the user to grant “Display over other apps”. Auxio-TS already has a permission activity that opens `ACTION_MANAGE_OVERLAY_PERMISSION` and checks `Settings.canDrawOverlays()`.  Android explicitly requires user approval for this permission on API 23+. ([Android Developers][2])

[1]: https://developer.android.com/training/data-storage/shared/media "Access media files from shared storage  |  App data and files  |  Android Developers"
[2]: https://developer.android.com/reference/android/Manifest.permission "Manifest.permission  |  API reference  |  Android Developers"

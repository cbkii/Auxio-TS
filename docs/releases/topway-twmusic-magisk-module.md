# Topway `twmusic` Magisk release asset

Auxio-TS publishes the exact-package Topway/DoFun `topwayTwMusic` replacement as a Magisk module ZIP instead of a public raw `com.tw.music` APK asset.

The release asset is named:

```text
Auxio-TS-${RELEASE_TAG}-topway-twmusic-magisk.zip
```

It replaces the former raw APK asset name, which must not be uploaded:

```text
Auxio-TS-${RELEASE_TAG}-topway-twmusic-release.apk
```

## Module behavior

The module uses the official Magisk module layout with `module.prop`, `customize.sh`, and a `system/` payload tree. Its payload is the signed Auxio-TS `topwayTwMusic` APK staged only at:

```text
$MODPATH/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

That maps systemlessly to the observed TS18 stock path:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

The packaging script does not add `install.sh`, `post-fs-data.sh`, or `service.sh`; the module is a static systemless overlay. It does not write directly to `/system`, uninstall packages, disable packages, clear app data, delete stock apps, disable Topway services, or claim platform signing / `android.uid.system` / UID1000 privileges.

## Intended usage and targets

When validating DoFun launcher/head unit integrations, you must choose the correct identity:

- Use **`topwayTwMusicRelease` (Magisk module)** as the primary target for devices attempting to restore the stock `com.tw.music` experience. Stock DoFun firmware mechanically expects the `com.tw.music/com.tw.music.MusicActivity` package identity because the integration is heavily hard-coded in launcher configs.
- Use **`topwayTwMediaRelease` (raw APK)** only as an alternate/diagnostic DoFun entry path. Do not treat `com.tw.media` as the primary fix for devices attempting to replace `com.tw.music`. `com.tw.media` avoids package conflict with the existing `com.tw.music` priv-app but is not universally matched by all stock launchers.

The exact package identity (`com.tw.music`) is required because DoFun launcher widget/hotseat integration relies on exact component string matching, and the stock firmware uses a rigid system priv-app restore mechanic if it detects missing core apps. The Magisk module bypasses the system restorer by overlaying the application directly over the system path systemlessly.

## Expected installation flow and rollback

To test `topwayTwMusicRelease`:
1. Install `Auxio-TS-${RELEASE_TAG}-topway-twmusic-magisk.zip` via Magisk Manager.
2. Reboot the head unit or perform an ACC (ignition) cycle to apply the systemless overlay.
3. Verify that DoFun recognizes the Auxio app in the widget and hotseat. See `docs/TS18_RUNTIME_VALIDATION.md` for physical device checklist commands.

**Rollback steps:**
To revert to the stock music app, open Magisk Manager, disable or remove the "Auxio-TS Topway Music Replacement" module, and reboot the device. The systemless overlay will detach, and the original `com.tw.music` priv-app will be restored.

## Scope and validation

This asset is TS18-specific and intended for controlled Topway/DoFun validation. Magisk keeps the overlay systemless and reversible by disabling or removing the module, but it does not make Auxio-TS platform-signed and does not grant signature permissions. Real TS18 device validation is still required before compatibility claims are made.

Evidence confidence: Inferred from the observed TS18 stock path and Magisk's documented module overlay model. Porting decision: Directly reusable release-packaging requirement, requires TS18 runtime validation for device behavior.

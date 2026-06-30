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

The module uses the official Magisk module layout with `module.prop`, `customize.sh`, optional lifecycle shell scripts, and a `system/` payload tree. Its payload is the signed Auxio-TS `topwayTwMusic` APK staged only at:

```text
$MODPATH/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

That maps systemlessly to the observed TS18 stock path:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

The packaging script does not write directly to `/system`, uninstall packages, disable packages, clear app data, delete stock apps, disable Topway services, or claim platform signing / `android.uid.system` / UID1000 privileges.

## Scope and validation

This asset is TS18-specific and intended for controlled Topway/DoFun validation. Magisk keeps the overlay systemless and reversible by disabling or removing the module, but it does not make Auxio-TS platform-signed and does not grant signature permissions. Real TS18 device validation is still required before compatibility claims are made.

Evidence confidence: Inferred from the observed TS18 stock path and Magisk's documented module overlay model. Porting decision: Directly reusable release-packaging requirement, requires TS18 runtime validation for device behavior.

---
applyTo: "**"
---

# Review and compatibility rules

These rules apply to all code review, pull request review, and pre-commit validation in this repository.

## Compatibility review checklist

Before approving or finalising any change, verify:

### Package and component identity
- [ ] Package name is still exactly `com.tw.music`
- [ ] No component name in `AndroidManifest.xml` has been renamed without explicit justification
- [ ] No intent action string (`com.tw.music.action.*`) has been changed or removed
- [ ] No `sharedUserId` change or permission addition that alters the effective UID

### Vendor boundary integrity
- [ ] No AIDL interface token strings (`com.tw.service.xt.aidl.*`) have been modified
- [ ] No system property key (`persist.tw.ijk*` or other `persist.tw.*` / `persist.media.*`) references have been changed
- [ ] No EQ launch `ComponentName` has been altered
- [ ] No `com.tw.radio.*` intent surface has been removed or broken
- [ ] Widget provider resource references are intact (layout IDs, drawable names, string keys)
- [ ] `@style/AppTheme` and any `tw_`-prefixed style/colour/dimen have not been deleted or renamed
- [ ] TWTHEME theme-dispatch code (if present) has not been removed or disabled

### Media session / playback regression
- [ ] `MediaSession` or `MediaSessionCompat` token is still created and released correctly
- [ ] `PlaybackState` is still published on play/pause/stop transitions
- [ ] `MediaMetadata` (title, artist, album, artwork) is still published when track changes
- [ ] Foreground service is still started on playback begin and stopped on completion/error (required on Android 13)
- [ ] All existing `com.tw.music.action.*` broadcast actions are still handled
- [ ] Media button handler still dispatches prev/next/pp correctly (these drive steering-wheel controls on TS18)
- [ ] TLink / CarPlay / Android Auto coexistence is not broken — `MediaSession` state must remain correctly published when TLink suspends/resumes the app

### Build validation
- [ ] `scripts/02_build_unsigned.sh` exits with code 0
- [ ] APK is produced at `dist/com.tw.music-unsigned.apk`
- [ ] No new `aapt2` errors or warnings about missing resources

### De-obfuscation / rename safety
- [ ] Any renamed class/method/field has a corresponding entry in `mappings/manual-enigma/`
- [ ] Rename is based on understood smali behaviour, not JADX auto-alias alone
- [ ] No bulk rename of unrelated symbols in a single commit
- [ ] Change does not introduce phone-only UI assumptions unsuitable for a 1280×720 landscape TS18 head unit
- [ ] No TWTHEME / vendor theme dispatch code has been removed or disabled

## Regression risk classification

Use this classification when reviewing changes:

| Risk level | Examples |
|---|---|
| **Critical** | Manifest change, AIDL interface change, system property key change, service/receiver removal |
| **High** | Playback engine replacement, media session architecture change, foreground service lifecycle change |
| **Medium** | Layout restructure, new UI state, media notification change, new broadcast action |
| **Low** | String update, icon refresh, comment addition, de-obfuscation rename with mapping entry |

Critical and High risk changes require explicit justification in the commit message or PR description, including:
- What behaviour is changing and why
- What vendor or system integration was verified to remain intact
- How the change was validated (rebuild, on-device if available, log inspection)

## Forbidden review patterns

Reject or request changes for any PR that:

- Removes or renames a resource referenced by `MusicWidgetProvider` without proof the widget still functions
- Migrates the entire playback stack to Media3 / ExoPlayer without proof of vendor-property compatibility on TS18 Android 13
- Adds phone-first UI patterns (bottom nav, portrait-only layout qualifiers, gesture nav handlers) that would look wrong on a 1280×720 landscape head unit
- Introduces hardcoded secrets, signing credentials, or API keys
- Edits anything under `reference/` as a code change
- Performs speculative cleanup of obfuscated names without a mapping entry
- Changes `android.uid.system` or `sharedUserId` in the manifest
- Removes or breaks `com.tw.radio.*`, `com.tw.eq.*`, or `com.tw.service.xt.*` interaction points without explicit scope justification
- Removes or renames TWTHEME / `@style/AppTheme` resources that the TW theme switcher depends on
- Assumes phone lock-screen media session or AOSP stock notification behaviour applies unchanged on TS18 vendor firmware

## Release and update caution

- This app is installed as a system package under `android.uid.system`. Signature mismatch on update causes installation failure, not just a warning.
- Any change to the manifest that could affect the package's system UID assignment must be reviewed with the signing workflow in mind (see `docs/manual-steps/02-release-signing.md`).
- Version code increments must be intentional — do not bump `versionCode` speculatively.
- Test update-in-place behaviour (same package, new version code, same signature) before declaring a release candidate.

## Vendor boundary audit

When reviewing any change touching the following, require explicit vendor boundary audit notes:

- `MusicService` — primary playback + IPC surface
- `MusicWidgetProvider` — widget update logic
- Any class in `smali*/` that implements or stubs `com.tw.service.xt.aidl.*`
- Any class that invokes `SystemProperties.getInt("persist.tw.*")`
- Any class that fires an `Intent` to `com.tw.eq`

Reference `docs/reports/vendor-hooks.txt` for the full enumeration of vendor-touching code.

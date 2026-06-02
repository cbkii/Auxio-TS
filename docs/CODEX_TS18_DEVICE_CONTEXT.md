# Codex context — exact TS18 diagnostics source for follow-up implementation

Read this before implementing TS18/Topway/DoFun compatibility changes after PR #53.

This file exists because Codex does not have access to the original uploaded `TS18_diagnostics.zip`. It summarises the exact target-device facts needed for code, docs, validation, and release-workflow decisions.

Canonical details are in:

- [`docs/evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md`](evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md)
- [`docs/TS18_INSTALLATION_CONSTRAINTS.md`](TS18_INSTALLATION_CONSTRAINTS.md)

## Non-negotiable device facts

- The target device is a TS18/Topway Android head unit: `s9863a1h10_Natv` / `s9863a1h10`.
- Runtime Android is Android 10 / SDK 29, not modern Android 14+.
- The display is 1280x720 landscape.
- There is a top status bar around 55px and a right navigation bar around 55px.
- Stock `com.tw.music` is present as a system priv-app at `/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk`.
- DoFun Variety (`com.dofun.variety`) is installed as a user/data app.
- The diagnostics were captured from TermOne Plus as a normal app UID (`u0_a177`) under `u:r:untrusted_app`, not ADB shell/root.
- `adbd` is running for USB gadget state (`mtp,adb`), but no TCP/local ADB port was observed.
- No `127.0.0.1:5555` / TCP ADB listener was observed.
- USB/OTG ADB is physically unavailable for the user’s current setup.
- `/storage/usbdisk0` exists and must be included in music-library validation.

## Implementation consequences

1. `topwayTwMusicRelease` is still correct as the exact `com.tw.music` DoFun target, but it is not safely assumed installable on this stock device without privileged package management or matching signing.
2. `topwayTwMediaRelease` should be added as DoFun’s alternate fixed-entry candidate (`com.tw.media/com.tw.music.MusicActivity`), but document it as stock-conflict-aware and mainly for root/Shizuku/ADB/system-managed installs.
3. Do not describe `com.tw.media` as a universal no-root workaround.
4. Runtime validation docs must separate app-only TermOne diagnostics, ADB shell, Shizuku, and root/system lanes.
5. Overlay and foreground-service code must be Android 10 safe.
6. Overlay positioning must account for 1280x720, top status bar, and right nav bar.
7. Do not require platform signing, system UID, copied smali, TWUtil reflection, or fake Cardoor/vendor services in production code.
8. Thin stock-name wrapper classes are allowed only inside approved Topway/DoFun source sets and must delegate into Auxio-owned bridge/service code.

## Remaining task split

The diagnostic/source seeding is done by this branch. The next Codex implementation pass should complete the remaining work:

- add `topwayTwMedia` variant;
- update manifests/source-set sharing;
- update CI/static contract checks;
- update release workflow/assets;
- fix Topway widget-provider update mismatch;
- resolve or validate duplicate service entrypoints;
- update AGENTS.md with the wrapper exception and device-profile references;
- extend validation docs/tests around Android 10, package conflicts, and storage/media scan paths.

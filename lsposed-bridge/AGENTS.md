# LSPosed bridge authority

These instructions apply to `lsposed-bridge/`.

- This is an optional, separately installed LSPosed module. It is not an Auxio app flavour and must not change the maintained `topwayTwMedia` / `topwayTwMusic` package contracts.
- Target modern libxposed **API 100** exactly. Compile against the pinned official stubs in `:libxposed-api100-stubs`; never package those API classes in the APK.
- Keep static scope exactly `com.tw.music`. Do not scope or hook `system_server`, DoFun, Auxio, Package Manager, `com.tw.service*`, MCU/CAN or unrelated apps.
- Preserve the genuine stock package, platform signer and UID 1000. Never implement signature spoofing, shared-UID mutation, package database edits or a stock APK replacement.
- Hook only observed exact-device activity/control surfaces. Use normal explicit Android IPC to `com.tw.media` and Android MediaBrowser/MediaSession for state mirroring.
- Every callback must fail open to stock behaviour when identity, target readiness, method match or forwarding cannot be proven. Catch host-process failures and keep logs rate-limited.
- Preserve the shared-storage kill switch and documented rollback. No bridge change is runtime-proven until physically validated on the exact TS18 build across launch, controls, metadata, progress, restart, cold boot and ACC sleep/wake.
- Dedicated bridge CI remains PR-only and title-gated to Xposed/LSPosed, hook, bridge or platform-signing work. Do not broaden its triggers silently.

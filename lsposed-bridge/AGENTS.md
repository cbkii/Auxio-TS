# LSPosed bridge authority

These instructions apply to `lsposed-bridge/`.

- This is an optional, separately installed LSPosed module. It is not an Auxio app flavour and must not change the maintained `topwayTwMedia` / `topwayTwMusic` package contracts.
- Target modern libxposed **API 100** exactly. Compile against the pinned official stubs in `:libxposed-api100-stubs`; never package those API classes in the APK.
- Keep static scope exactly `com.tw.music`. Do not scope or hook `system_server`, DoFun, Auxio, Package Manager, `com.tw.service*`, MCU/CAN or unrelated apps.
- Preserve the genuine stock package, platform signer and UID 1000. Never implement signature spoofing, shared-UID mutation, package database edits or a stock APK replacement.
- Hook only observed exact-device activity/control surfaces. Version code is diagnostic, not identity authority: retain the exact signer and UID gates, capability-probe public class/method surfaces, and fail open for every unmatched path. Private obfuscated presenter suppression additionally requires the captured stock APK fingerprint so changed method semantics are never guessed.
- Use the connected Auxio MediaBrowser/MediaSession as the acknowledged control path. Suppress a stock callback only when the target session exists, advertises the required transport action and accepts the transport call without throwing.
- Every callback must fail open to stock behaviour when identity, target readiness, method match or forwarding cannot be proven. Catch host-process failures and keep logs rate-limited.
- Preserve the shared-storage kill switch and documented rollback. No bridge change is runtime-proven until physically validated on the exact TS18 build across launch, controls, metadata, progress, restart, cold boot and ACC sleep/wake.
- The bridge APK is the supported replacement for the retired Auxio exact-package Magisk overlay.
  Release builds must be signed, versioned with the paired Auxio release, packaged as a separate
  APK asset, and pass the release-variant bridge contract check.
- Dedicated bridge CI remains PR-only. It may run from a matching bridge/Xposed/platform-signing title, an exact bridge CI label, or an explicitly named LSPosed bridge branch; unrelated PRs must not start the bridge build job.

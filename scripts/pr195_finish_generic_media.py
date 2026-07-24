#!/usr/bin/env python3
"""Apply the final scoped PR #195 launcher-media hardening and documentation edits."""

from pathlib import Path
import runpy

ROOT = Path(__file__).resolve().parents[1]


def replace_once(path: str, old: str, new: str) -> None:
    target = ROOT / path
    content = target.read_text(encoding="utf-8")
    count = content.count(old)
    if count != 1:
        raise RuntimeError(f"{path}: expected one match, found {count}: {old[:120]!r}")
    target.write_text(content.replace(old, new, 1), encoding="utf-8")


runpy.run_path(
    str(Path(__file__).with_name("pr195_harden_launcher_media.py")),
    run_name="__main__",
)

replace_once(
    "scripts/check-dofun-topway-compat.sh",
    'require_file_contains "$mode_file" "usesGenericMediaNotification" "launcher integration generic notification gate"\n',
    'require_file_contains "$mode_file" "usesGenericDofunProfile" "launcher integration standards-first DoFun profile gate"\n'
    'require_file_contains "$mode_file" "if (BuildConfig.TOPWAY_COMPAT_FLAVOR) GenericDofunMedia else AndroidMediaSessionOnly" "launcher integration flavour-specific default"\n'
    'mode_test_file="app/src/test/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationModeTest.kt"\n'
    'require_file_contains "$mode_test_file" "default follows build flavor" "launcher integration default coverage"\n'
    'require_file_contains "$mode_test_file" "unset topway preference adopts generic media once" "launcher integration migration coverage"\n'
    'require_file_contains "$mode_test_file" "persisted all safe paths survives migration" "launcher integration explicit fallback preservation coverage"\n'
    'require_file_contains "app/src/topwayCompat/res/xml/preferences_car.xml" \'app:defaultValue="GenericDofunMedia"\' "Topway settings generic profile default"\n'
    'require_file_contains "app/src/main/java/org/oxycblt/auxio/playback/service/DofunMediaCompatPolicy.kt" "mode == Ts18LauncherIntegrationMode.AndroidMediaSessionOnly" "Android-only wrapper controls remain canonical"\n'
    'require_file_contains "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt" "prefs.registerOnSharedPreferenceChangeListener(modePreferenceListener)" "live launcher-mode notification refresh listener"\n'
    'require_file_contains "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt" "MediaButtonIntentFactory.serviceIntent(context, canonicalServiceClass, keyCode)" "generic notification routes to canonical service"\n',
)

replace_once(
    "app/src/main/res/values/strings.xml",
    '<string name="set_playback_notification_access">Playback notification access</string>',
    '<string name="set_playback_notification_access">Playback notification channel</string>',
)

replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    "value.isNotBlank() && value.length <= MAX_MEDIA_ID_LENGTH && value.none(Char::isISOControl)",
    "value.length <= MAX_MEDIA_ID_LENGTH && value.isNotBlank() && value.none(Char::isISOControl)",
)

replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/topway/ExportedCommandRateLimiter.kt",
    """        val now = elapsedRealtime()
        val window = windows.getOrPut(key) { Window(now, 0) }
        synchronized(window) {
            if (now < window.startedAtMs || now - window.startedAtMs >= windowMs) {
""",
    """        val window = windows.getOrPut(key) { Window(elapsedRealtime(), 0) }
        synchronized(window) {
            val now = elapsedRealtime()
            if (now < window.startedAtMs || now - window.startedAtMs >= windowMs) {
""",
)

replace_once(
    "docs/ts18/launcher-integration/TS18_LAUNCHER_COMPREHENSIVE_IN_APP_PLAN.md",
    "# TS18 Launcher Comprehensive In-App Integration Plan\n\n",
    "# TS18 Launcher Comprehensive In-App Integration Plan\n\n"
    "> **Superseded implementation guidance.** The active default and porting decision are defined in "
    "[`DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md`](DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md). "
    "The multi-bridge `AutoAllSafePaths` design below is retained only as historical rationale and an explicit diagnostic fallback; it is not the Topway-flavour default.\n\n",
)
replace_once(
    "docs/ts18/launcher-integration/TS18_LAUNCHER_COMPREHENSIVE_IN_APP_PLAN.md",
    "- `AutoAllSafePaths`: enable standards path + broadcasts + command bridge + diagnostics. This is the preferred TS18 default.",
    "- `AutoAllSafePaths`: enable standards path + broadcasts + command bridge + diagnostics as an explicit legacy fallback; it is not the current default.",
)

replace_once(
    "docs/ts18/launcher-integration/VALIDATION_MATRIX.md",
    "# TS18 Launcher Media Integration Validation Matrix\n\n",
    "# TS18 Launcher Media Integration Validation Matrix\n\n"
    "> **Current profile:** validate the standards-first `GenericDofunMedia` path described in "
    "[`DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md`](DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md) first. "
    "Run legacy Topway broadcast/command rows only when that fallback mode is explicitly selected. "
    "Passing Android `MediaSession` and notification checks does not by itself prove acceptance by the fixed DoFun player panel.\n\n",
)
replace_once(
    "docs/ts18/launcher-integration/VALIDATION_MATRIX.md",
    "- `auxio_ts18_launcher_integration_mode` stores `Ts18LauncherIntegrationMode` by enum name. Topway-compatible builds default to `AutoAllSafePaths`; generic builds default to `AndroidMediaSessionOnly`.",
    "- `auxio_ts18_launcher_integration_mode` stores `Ts18LauncherIntegrationMode` by enum name. Fresh Topway-compatible installs default to `GenericDofunMedia`; standard builds default to `AndroidMediaSessionOnly`. Persisted valid selections, including legacy fallback modes, are preserved because older releases did not record preference provenance.",
)
replace_once(
    "docs/ts18/launcher-integration/VALIDATION_MATRIX.md",
    "- `AutoAllSafePaths` (default for Topway-compatible builds): Android media session path plus Topway metadata/progress broadcasts, incoming Topway commands, incoming widget seek, diagnostics, and existing `topwayCompat` identity wrappers where that build variant provides them.",
    "- `GenericDofunMedia` (fresh-install default for Topway-compatible builds): conventional Android MediaSession/MediaBrowser/MediaStyle controls with legacy Topway TX/RX disabled. Exact fixed-panel recognition still requires TS18 validation.\n- `AutoAllSafePaths` (explicit legacy fallback): Android media session path plus Topway metadata/progress broadcasts, incoming Topway commands, incoming widget seek, diagnostics, and existing `topwayCompat` identity wrappers where that build variant provides them.",
)

replace_once(
    "docs/ts18/launcher-integration/DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md",
    "Add a standards-first profile that becomes the default launcher strategy for Topway variants. It must:",
    "Add a standards-first profile that becomes the fresh-install launcher default for Topway variants. Existing persisted valid selections remain explicit user choices and are not overwritten. It must:",
)
replace_once(
    "docs/ts18/launcher-integration/DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md",
    "- no claim of physical DoFun acceptance from CI.",
    "- no claim of physical DoFun acceptance from CI.\n\n## Migration and authority note\n\nThe one-time migration writes `GenericDofunMedia` only when no launcher-mode preference exists. It preserves every persisted valid mode, including `AutoAllSafePaths`, because older versions did not record whether that value was a default or an explicit choice. Generic callback registration and a healthy Android media session are compatibility prerequisites, not proof that DoFun selected Auxio as the active fixed-panel source.",
)

for path, markers in {
    "scripts/check-dofun-topway-compat.sh": [
        "usesGenericDofunProfile",
        "Topway settings generic profile default",
        "live launcher-mode notification refresh listener",
        "generic notification routes to canonical service",
    ],
    "app/src/main/res/values/strings.xml": ["Playback notification channel"],
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt": [
        "value.length <= MAX_MEDIA_ID_LENGTH && value.isNotBlank()"
    ],
    "app/src/main/java/org/oxycblt/auxio/headunit/topway/ExportedCommandRateLimiter.kt": [
        "synchronized(window) {\n            val now = elapsedRealtime()"
    ],
    "docs/ts18/launcher-integration/TS18_LAUNCHER_COMPREHENSIVE_IN_APP_PLAN.md": [
        "Superseded implementation guidance",
        "explicit legacy fallback; it is not the current default",
    ],
    "docs/ts18/launcher-integration/VALIDATION_MATRIX.md": [
        "Passing Android `MediaSession` and notification checks does not by itself prove acceptance",
        "Fresh Topway-compatible installs default to `GenericDofunMedia`",
        "`GenericDofunMedia` (fresh-install default",
    ],
    "docs/ts18/launcher-integration/DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md": [
        "fresh-install launcher default",
        "Migration and authority note",
    ],
}.items():
    content = (ROOT / path).read_text(encoding="utf-8")
    for marker in markers:
        if marker not in content:
            raise RuntimeError(f"{path}: missing final marker {marker!r}")

print("PR #195 final scoped hardening inputs applied")

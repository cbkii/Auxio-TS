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
    'require_file_contains "$mode_test_file" "persisted all safe paths survives migration" "launcher integration explicit fallback preservation coverage"\n',
)

replace_once(
    "app/src/main/res/values/strings.xml",
    '<string name="set_playback_notification_access">Playback notification access</string>',
    '<string name="set_playback_notification_access">Playback notification channel</string>',
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
    "docs/ts18/launcher-integration/VALIDATION_MATRIX.md",
    "# TS18 Launcher Media Integration Validation Matrix\n\n",
    "# TS18 Launcher Media Integration Validation Matrix\n\n"
    "> **Current profile:** validate the standards-first `GenericDofunMedia` path described in "
    "[`DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md`](DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md) first. "
    "Run legacy Topway broadcast/command rows only when that fallback mode is explicitly selected. "
    "Passing Android `MediaSession` and notification checks does not by itself prove acceptance by the fixed DoFun player panel.\n\n",
)

for path, markers in {
    "scripts/check-dofun-topway-compat.sh": [
        "usesGenericDofunProfile",
        "default follows build flavor",
        "unset topway preference adopts generic media once",
        "persisted all safe paths survives migration",
    ],
    "app/src/main/res/values/strings.xml": ["Playback notification channel"],
    "docs/ts18/launcher-integration/TS18_LAUNCHER_COMPREHENSIVE_IN_APP_PLAN.md": [
        "Superseded implementation guidance",
    ],
    "docs/ts18/launcher-integration/VALIDATION_MATRIX.md": [
        "Passing Android `MediaSession` and notification checks does not by itself prove acceptance",
    ],
}.items():
    content = (ROOT / path).read_text(encoding="utf-8")
    for marker in markers:
        if marker not in content:
            raise RuntimeError(f"{path}: missing final marker {marker!r}")

print("PR #195 final scoped hardening inputs applied")

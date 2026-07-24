#!/usr/bin/env python3
"""Remove stale root/early-prestart expectations from PR #196 unit tests."""

from pathlib import Path


def read(path: str | Path) -> str:
    return Path(path).read_text(encoding="utf-8")


def write(path: str | Path, content: str) -> None:
    Path(path).write_text(content, encoding="utf-8", newline="\n")


def fix_library_recovery_test() -> None:
    path = Path("app/src/test/java/org/oxycblt/auxio/home/list/LibraryRecoveryPolicyTest.kt")
    content = read(path)
    content = content.replace(
        "    fun sourceUnavailableShowsRetrySourceAndOptionalRootActions()",
        "    fun sourceUnavailableShowsRetryAndSourceActions()",
    )
    content = content.replace(
        "                locationMode = LocationMode.DIRECT_FS,\n"
        "                rootSupported = true,\n"
        "                rootEnabled = false,\n",
        "                locationMode = LocationMode.DIRECT_FS,\n",
    )
    content = content.replace(
        "                storagePermissionRequired = locationMode == LocationMode.MEDIA_STORE,",
        "                storagePermissionRequired = storagePermissionRequired,",
    )
    root_input = '''                rootSupported = rootSupported,
                rootRequired = locationMode == LocationMode.DIRECT_FS && !rootEnabled,
                rootState =
                    if (rootEnabled) RootStateHolder.State.Available
                    else RootStateHolder.State.DisabledByUser,
'''
    content = content.replace(root_input, "")
    for forbidden in ("rootSupported", "rootEnabled", "RootStateHolder"):
        assert forbidden not in content, forbidden
    write(path, content)


def fix_startup_origin_test() -> None:
    path = Path("app/src/test/java/org/oxycblt/auxio/music/StartupScanOriginTest.kt")
    content = read(path)
    test_start = content.index("    @Test\n    fun userVisibleOriginOverridesBackgroundOrigin()")
    next_test = content.index("    @Test\n    fun topwayUserVisibleFirstStartMayRequestOneScan()", test_start)
    content = content[:test_start] + content[next_test:]
    assert "StartupScanOrigin.merge" not in content
    assert "EARLY_PRESTART" not in content
    write(path, content)


def main() -> int:
    fix_library_recovery_test()
    fix_startup_origin_test()
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

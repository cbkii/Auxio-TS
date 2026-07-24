#!/usr/bin/env python3
"""Finish the scoped PR #196 cleanup after the legacy finalizer has run."""

from pathlib import Path


def read(path: str | Path) -> str:
    return Path(path).read_text(encoding="utf-8")


def write(path: str | Path, content: str) -> None:
    Path(path).write_text(content, encoding="utf-8", newline="\n")


def fix_auxio_service() -> None:
    path = Path("app/src/main/java/org/oxycblt/auxio/AuxioService.kt")
    content = read(path).replace(
        "                } else if (earlyPrestartJob?.isActive != true) {",
        "                } else {",
    )
    write(path, content)


def fix_home_empty_state() -> None:
    path = Path("app/src/main/java/org/oxycblt/auxio/home/list/HomeListEmptyState.kt")
    content = read(path).replace(
        "            LibraryRecoveryPolicy.Action.ENABLE_ROOT -> "
        "R.string.recovery_action_enable_root\n",
        "",
    )
    write(path, content)


def fix_startup_origin() -> None:
    policy_path = Path(
        "app/src/main/java/org/oxycblt/auxio/music/service/StartupScanAuthorityPolicy.kt"
    )
    policy = read(policy_path).replace(
        "enum class StartupScanOrigin {\n"
        "    USER_VISIBLE,\n"
        "    BACKGROUND,\n"
        "    EARLY_PRESTART;\n"
        "}",
        "enum class StartupScanOrigin {\n"
        "    USER_VISIBLE,\n"
        "    BACKGROUND;\n"
        "}",
    )
    write(policy_path, policy)

    test_path = Path("app/src/test/java/org/oxycblt/auxio/music/StartupScanOriginTest.kt")
    test = read(test_path)
    test = test.replace("StartupScanOrigin.EARLY_PRESTART", "StartupScanOrigin.BACKGROUND")
    test = test.replace(
        "fun topwayBackgroundAndEarlyPrestartDoNotRequestScans()",
        "fun topwayBackgroundDoesNotRequestScans()",
    )
    test = test.replace(
        "listOf(StartupScanOrigin.BACKGROUND, StartupScanOrigin.BACKGROUND)",
        "listOf(StartupScanOrigin.BACKGROUND)",
    )
    write(test_path, test)


def fix_locations_dialog() -> None:
    path = Path("app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt")
    content = read(path)

    if "import android.os.Build\n" not in content:
        content = content.replace("import android.net.Uri\n", "import android.net.Uri\nimport android.os.Build\n", 1)
    if "import androidx.core.content.ContextCompat\n" not in content:
        content = content.replace(
            "import androidx.appcompat.app.AlertDialog\n",
            "import androidx.appcompat.app.AlertDialog\nimport androidx.core.content.ContextCompat\n",
            1,
        )

    launcher_start = content.index("        appSettingsLauncher =")
    launcher_end = content.index("\n\n        binding.locationsIncludeRecycler.apply", launcher_start)
    launcher_block = '''        appSettingsLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val currentContext = context ?: return@registerForActivityResult
                hasStoragePermission = StoragePermissionPolicy.isGranted(currentContext)
                val retry = pendingPermissionRetry
                pendingPermissionRetry = null
                if (view != null) {
                    updateModeUI(requireBinding())
                    updateSaveButtonState()
                }
                if (hasStoragePermission) retry?.invoke()
            }

        storagePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                StoragePermissionPolicy.markRequested(requireContext())
                L.d("Storage permission granted: $isGranted")
                hasStoragePermission = isGranted
                if (isGranted && !permissionGrantedInSession) {
                    permissionGrantedInSession = true
                }
                val retry = pendingPermissionRetry
                pendingPermissionRetry = null
                updateModeUI(binding)
                updateSaveButtonState()
                if (isGranted) {
                    retry?.invoke()
                } else {
                    pendingLocationCallback = null
                }
            }'''
    content = content[:launcher_start] + launcher_block + content[launcher_end:]

    content = content.replace(
        "        hasStoragePermission = checkStoragePermission()",
        "        hasStoragePermission = StoragePermissionPolicy.isGranted(requireContext())",
    )
    content = content.replace(
        "        } catch (e: SecurityException) {\n"
        "            L.w(e, \"Security exception while validating manual path $path\")\n"
        "            if (rawRootCandidate) {\n"
        "                ManualPathValidation.ROOT_UNAVAILABLE\n"
        "            } else {\n"
        "                ManualPathValidation.PERMISSION_MISSING\n"
        "            }",
        "        } catch (e: SecurityException) {\n"
        "            L.w(e, \"Security exception while validating manual path $path\")\n"
        "            ManualPathValidation.PERMISSION_MISSING",
    )

    check_start = content.index("    private fun checkStoragePermission(): Boolean {")
    check_end = content.index("\n\n    private fun showStoragePermissionExplanation()", check_start)
    content = content[:check_start] + content[check_end + 2 :]

    root_start = content.index("    private fun showRootConsentAndRetry(")
    root_end = content.index("\n\n    private fun requestStoragePermission()", root_start)
    content = content[:root_start] + content[root_end + 2 :]

    content = content.replace(
        "        val permission = requiredStoragePermission()",
        "        val permission = StoragePermissionPolicy.requiredPermission()",
    )
    content = content.replace(
        "            storagePermissionRequestedBefore() &&",
        "            StoragePermissionPolicy.wasRequested(requireContext()) &&",
    )
    content = content.replace(
        "            markStoragePermissionRequested()",
        "            StoragePermissionPolicy.markRequested(requireContext())",
    )

    helper_start = content.index("    private fun requiredStoragePermission(): String =")
    helper_end = content.index("\n\n    private fun showOpenAppSettingsDialog()", helper_start)
    content = content[:helper_start] + content[helper_end + 2 :]

    old_settings = '''                startActivity(
                    Intent(
                        AndroidSettings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", ctx.packageName, null),
                    )
                )'''
    new_settings = '''                requireNotNull(appSettingsLauncher) {
                    "App settings launcher was not available"
                }.launch(
                    Intent(
                        AndroidSettings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", ctx.packageName, null),
                    )
                )'''
    content = content.replace(old_settings, new_settings, 1)
    write(path, content)


def assert_contracts() -> None:
    service = read("app/src/main/java/org/oxycblt/auxio/AuxioService.kt")
    assert "ACTION_EARLY_PRESTART" not in service
    assert "earlyPrestartJob" not in service

    authority = read(
        "app/src/main/java/org/oxycblt/auxio/music/service/StartupScanAuthorityPolicy.kt"
    )
    assert "EARLY_PRESTART" not in authority

    home_empty = read("app/src/main/java/org/oxycblt/auxio/home/list/HomeListEmptyState.kt")
    assert "ENABLE_ROOT" not in home_empty

    dialog = read("app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt")
    for forbidden in (
        "ROOT_UNAVAILABLE",
        "showRootConsentAndRetry",
        "bindingOrNull",
        "PreferenceManager",
        "KEY_STORAGE_PERMISSION_REQUESTED",
    ):
        assert forbidden not in dialog, forbidden
    for required in (
        "StoragePermissionPolicy.markRequested",
        "StoragePermissionPolicy.requiredPermission",
        "StoragePermissionPolicy.wasRequested",
        "StoragePermissionPolicy.isGranted",
        "if (rawRootCandidate) return ManualPathValidation.UNREADABLE",
        "appSettingsLauncher",
    ):
        assert required in dialog, required


def main() -> int:
    fix_auxio_service()
    fix_home_empty_state()
    fix_startup_origin()
    fix_locations_dialog()
    assert_contracts()
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

#!/usr/bin/env python3
"""Apply and validate the final scoped PR #196 hardening before self-cleanup."""

from __future__ import annotations

from pathlib import Path
import subprocess
import sys


def read(path: str | Path) -> str:
    return Path(path).read_text(encoding="utf-8")


def write(path: str | Path, content: str) -> None:
    Path(path).write_text(content, encoding="utf-8", newline="\n")


def extract_finalizer() -> Path:
    source = read(".github/workflows/tmp-pr196-apply-hardening.yml")
    marker = "          python3 <<'PY'\n"
    start = source.index(marker) + len(marker)
    end = source.index("\n          PY\n", start)
    lines = source[start:end].splitlines()
    code = "\n".join(line[10:] if line.startswith("          ") else line for line in lines) + "\n"

    original = (
        "def replace_once(text: str, old: str, new: str, label: str) -> str:\n"
        "    count = text.count(old)\n"
        "    if count != 1:\n"
        "        raise SystemExit(f'{label}: expected exactly one match, found {count}')\n"
        "    return text.replace(old, new, 1)\n"
    )
    replacement = (
        "def replace_once(text: str, old: str, new: str, label: str) -> str:\n"
        "    count = text.count(old)\n"
        "    if count == 1:\n"
        "        return text.replace(old, new, 1)\n"
        "    if count > 1:\n"
        "        raise SystemExit(f'{label}: expected exactly one match, found {count}')\n"
        "\n"
        "    old_lines = old.splitlines()\n"
        "    text_lines = text.splitlines(keepends=True)\n"
        "    old_norm = [line.strip() for line in old_lines]\n"
        "    span = len(old_norm)\n"
        "    matches = []\n"
        "    for offset in range(0, len(text_lines) - span + 1):\n"
        "        candidate = [line.rstrip('\\\\r\\\\n').strip() for line in text_lines[offset : offset + span]]\n"
        "        if candidate == old_norm:\n"
        "            matches.append(offset)\n"
        "    if len(matches) != 1:\n"
        "        raise SystemExit(\n"
        "            f'{label}: exact match count 0; whitespace-normalized match count {len(matches)}'\n"
        "        )\n"
        "    offset = matches[0]\n"
        "    end = offset + span\n"
        "    replacement_text = new\n"
        "    if text_lines[end - 1].endswith('\\\\n') and not replacement_text.endswith('\\\\n'):\n"
        "        replacement_text += '\\\\n'\n"
        "    return ''.join(text_lines[:offset]) + replacement_text + ''.join(text_lines[end:])\n"
    )
    if original not in code:
        raise SystemExit("Could not install whitespace-tolerant finalizer replacement")
    code = code.replace(original, replacement, 1)
    code = code.replace(
        "base = Path('/tmp/AuxioService.base.kt')\n"
        "# The shell writes the base version below before this script is invoked.\n",
        "",
        1,
    )
    output = Path("/tmp/pr196-finalize.py")
    write(output, code)
    return output


def deduplicate_imports() -> None:
    for path in Path("app/src").rglob("*.kt"):
        lines = read(path).splitlines(keepends=True)
        seen: set[str] = set()
        output: list[str] = []
        for line in lines:
            if line.startswith("import "):
                if line in seen:
                    continue
                seen.add(line)
            output.append(line)
        write(path, "".join(output))


def fix_recovery_container() -> None:
    path = Path("app/src/main/res/layout/fragment_home_list.xml")
    content = read(path)
    outer = (
        "        <androidx.core.widget.NestedScrollView\n"
        "            android:layout_width=\"match_parent\"\n"
        "            android:layout_height=\"match_parent\"\n"
        "            android:clipToPadding=\"false\"\n"
        "            android:fillViewport=\"true\"\n"
        "            android:overScrollMode=\"ifContentScrolls\">"
    )
    outer_with_id = (
        "        <androidx.core.widget.NestedScrollView\n"
        "            android:id=\"@+id/home_no_music\"\n"
        "            android:layout_width=\"match_parent\"\n"
        "            android:layout_height=\"match_parent\"\n"
        "            android:clipToPadding=\"false\"\n"
        "            android:fillViewport=\"true\"\n"
        "            android:overScrollMode=\"ifContentScrolls\"\n"
        "            android:visibility=\"invisible\">"
    )
    if outer in content:
        content = content.replace(outer, outer_with_id, 1)
    child = (
        "            <LinearLayout\n"
        "                android:id=\"@+id/home_no_music\"\n"
        "                android:layout_width=\"match_parent\"\n"
        "                android:layout_height=\"wrap_content\"\n"
        "                android:gravity=\"center\"\n"
        "                android:orientation=\"vertical\"\n"
        "                android:padding=\"@dimen/spacing_medium\"\n"
        "                android:visibility=\"invisible\">"
    )
    child_fixed = (
        "            <LinearLayout\n"
        "                android:layout_width=\"match_parent\"\n"
        "                android:layout_height=\"wrap_content\"\n"
        "                android:gravity=\"center\"\n"
        "                android:orientation=\"vertical\"\n"
        "                android:padding=\"@dimen/spacing_medium\">"
    )
    content = content.replace(child, child_fixed, 1)
    write(path, content)


def fix_dialog_callback() -> None:
    path = Path("app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt")
    content = read(path)
    content = content.replace(
        "                bindingOrNull?.let {\n"
        "                    updateModeUI(it)\n"
        "                    updateSaveButtonState()\n"
        "                }",
        "                if (view != null) {\n"
        "                    updateModeUI(requireBinding())\n"
        "                    updateSaveButtonState()\n"
        "                }",
    )
    write(path, content)


def assert_contracts() -> None:
    main = read("app/src/main/java/org/oxycblt/auxio/MainActivity.kt")
    assert main.count("import org.oxycblt.auxio.music.service.StartupScanAuthorityPolicy") == 1
    assert main.count("issueTrustedUserVisibleStart()") == 1
    assert "INTENT_KEY_TRUSTED_SCAN_NONCE" in main

    service = read("app/src/main/java/org/oxycblt/auxio/AuxioService.kt")
    assert service.count("import org.oxycblt.auxio.music.service.StartupScanOrigin") == 1
    assert "ACTION_EARLY_PRESTART" not in service
    assert "allowTrustedUserVisible = false" in service
    assert "consumeTrustedUserVisibleStart(" in service

    authority = read(
        "app/src/main/java/org/oxycblt/auxio/music/service/StartupScanAuthorityPolicy.kt"
    )
    for required in (
        "UUID.randomUUID()",
        "sources.size != settings.configuredSourceCount",
        "file.isDirectory",
        "permission.uri == uri",
    ):
        assert required in authority, required

    holder = read("app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt")
    assert holder.index("indexScope.launch") < holder.index("hasCurrentSourceAuthority")
    assert "startupJob?.isActive == true" in holder
    assert "currentIndexJob?.isActive == true" in holder

    dialog = read("app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt")
    assert "appSettingsLauncher" in dialog
    assert "StoragePermissionPolicy.markRequested" in dialog
    assert "if (rawRootCandidate) return ManualPathValidation.UNREADABLE" in dialog
    assert "ROOT_UNAVAILABLE(R.string.set_path_root_unavailable)" not in dialog

    permission = read(
        "app/src/main/java/org/oxycblt/auxio/music/locations/StoragePermissionPolicy.kt"
    )
    assert 'path == "/sdcard"' in permission
    assert 'path?.startsWith("/sdcard/")' in permission
    assert 'path?.startsWith("/storage/")' in permission

    layout = read("app/src/main/res/layout/fragment_home_list.xml")
    assert layout.count('android:id="@+id/home_no_music"') == 1
    assert layout.index("<androidx.core.widget.NestedScrollView") < layout.index(
        'android:id="@+id/home_no_music"'
    )

    assert not Path(
        "app/src/main/java/org/oxycblt/auxio/headunit/prestart/EarlyPrestartNotification.kt"
    ).exists()
    assert not Path(
        "app/src/main/java/org/oxycblt/auxio/headunit/prestart/EarlyPrestartSettings.kt"
    ).exists()


def main() -> int:
    finalizer = extract_finalizer()
    subprocess.run([sys.executable, "-m", "py_compile", str(finalizer)], check=True)
    subprocess.run([sys.executable, str(finalizer)], check=True)
    deduplicate_imports()
    fix_recovery_container()
    fix_dialog_callback()
    assert_contracts()
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${repo_root}"

failures=0
warnings=0
fail() { printf 'ERROR: %s\n' "$*" >&2; failures=$((failures + 1)); }
warn() { printf 'WARN: %s\n' "$*" >&2; warnings=$((warnings + 1)); }
ok() { printf 'OK: %s\n' "$*"; }

printf 'Checking Topway manifest-declared component classes...\n\n'

set +e
python3 - <<'PY'
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ANDROID = "{http://schemas.android.com/apk/res/android}"
REPO = Path.cwd()
SOURCE_ROOTS = [
    REPO / "app/src/main/java",
    REPO / "app/src/main/kotlin",
    REPO / "app/src/topwayCompat/java",
    REPO / "app/src/topwayCompat/kotlin",
]
LOADABLE_TAGS = {"activity", "service", "receiver", "provider"}
TOPWAY_VARIANTS = {
    "topwayTwMusicDebug": "com.tw.music.debug",
    "topwayTwMusicRelease": "com.tw.music",
    "topwayTwMediaDebug": "com.tw.media.debug",
    "topwayTwMediaRelease": "com.tw.media",
}
# Activity aliases are manifest components but Android resolves them to targetActivity; it does not
# instantiate a class with the alias name. Keep the stock alias in the manifest, but require the real
# target activity class to be packaged.
REQUIRED_TOPWAY_LOADABLE_CLASSES = {
    "com.tw.music.MusicService",
    "com.tw.music.view.MusicWidgetProvider",
    "org.oxycblt.auxio.MainActivity",
    "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity",
    "org.oxycblt.auxio.car.overlay.CarFloatingControlsService",
    "org.oxycblt.auxio.car.overlay.CarOverlayBootReceiver",
    "org.oxycblt.auxio.car.overlay.CarOverlayPermissionActivity",
}
REQUIRED_TOPWAY_ALIAS = "com.tw.music.MusicActivity"
EXPECTED_TOPWAY_ALIAS_TARGET = "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity"

failures = 0
warnings = 0

def ok(message):
    print(f"OK: {message}")

def warn(message):
    global warnings
    warnings += 1
    print(f"WARN: {message}", file=sys.stderr)

def fail(message):
    global failures
    failures += 1
    print(f"ERROR: {message}", file=sys.stderr)

def attr(element, name):
    return element.attrib.get(ANDROID + name)

def normalize_class(name, manifest_package):
    if not name:
        return None
    if name.startswith("."):
        return f"{manifest_package}{name}"
    if "." not in name and manifest_package:
        return f"{manifest_package}.{name}"
    return name

def descriptor(class_name):
    return "L" + class_name.replace(".", "/") + ";"

def source_exists(class_name):
    rel = Path(*class_name.split("."))
    return any((root / rel.with_suffix(ext)).is_file() for root in SOURCE_ROOTS for ext in (".kt", ".java"))

def is_test_like_manifest(path):
    return any("test" in part.lower() or "androidtest" in part.lower() for part in path.parts)

def merged_manifest_paths():
    bases = [REPO / "app/build/intermediates/merged_manifest", REPO / "app/build/intermediates/merged_manifests"]
    paths = {}
    for variant in TOPWAY_VARIANTS:
        matches = []
        for base in bases:
            if base.is_dir():
                matches.extend(
                    path
                    for path in base.glob(f"**/{variant}/**/AndroidManifest.xml")
                    if not is_test_like_manifest(path)
                )
        if matches:
            paths[variant] = sorted(set(matches))[0]
    return paths

def classes_from_manifest(path, fallback_package):
    root = ET.parse(path).getroot()
    manifest_package = root.attrib.get("package") or fallback_package
    app = root.find("application")
    loadable = set()
    aliases = {}
    if app is None:
        return manifest_package, loadable, aliases
    for tag in LOADABLE_TAGS:
        for element in app.findall(tag):
            name = normalize_class(attr(element, "name"), manifest_package)
            if name:
                loadable.add(name)
    for alias in app.findall("activity-alias"):
        alias_name = normalize_class(attr(alias, "name"), manifest_package)
        target = normalize_class(attr(alias, "targetActivity"), manifest_package)
        if alias_name:
            aliases[alias_name] = target
            if target:
                loadable.add(target)
    return manifest_package, loadable, aliases

def check_source_manifest():
    manifest = REPO / "app/src/topwayCompat/AndroidManifest.xml"
    if not manifest.is_file():
        fail("missing app/src/topwayCompat/AndroidManifest.xml")
        return
    _pkg, loadable, aliases = classes_from_manifest(manifest, "org.oxycblt.auxio")
    missing_decl = REQUIRED_TOPWAY_LOADABLE_CLASSES - loadable
    if missing_decl:
        fail(f"topwayCompat source manifest lacks required loadable classes: {sorted(missing_decl)}")
    else:
        ok("topwayCompat source manifest declares all required loadable wrapper/overlay classes")
    if aliases.get(REQUIRED_TOPWAY_ALIAS) == EXPECTED_TOPWAY_ALIAS_TARGET:
        ok("topwayCompat source manifest routes com.tw.music.MusicActivity through TopwayMusicEntryActivity")
    else:
        fail(f"topwayCompat source manifest alias mismatch for {REQUIRED_TOPWAY_ALIAS}: {aliases.get(REQUIRED_TOPWAY_ALIAS)!r}")
    for class_name in sorted(loadable & REQUIRED_TOPWAY_LOADABLE_CLASSES):
        if source_exists(class_name):
            ok(f"source exists for manifest loadable class {class_name}")
        else:
            fail(f"source missing for manifest loadable class {class_name}")


def check_merged_manifests():
    found = merged_manifest_paths()
    if not found:
        warn("no Topway merged manifests found; run :app:processTopwayTwMusicReleaseMainManifest and :app:processTopwayTwMediaReleaseMainManifest for output-level manifest checks")
        return
    for variant, expected_package in TOPWAY_VARIANTS.items():
        path = found.get(variant)
        if not path:
            warn(f"merged manifest absent for {variant}")
            continue
        package_name, loadable, aliases = classes_from_manifest(path, expected_package)
        if package_name == expected_package:
            ok(f"{variant} merged manifest package is {expected_package}")
        else:
            fail(f"{variant} merged manifest package expected {expected_package}, got {package_name!r}")
        missing = REQUIRED_TOPWAY_LOADABLE_CLASSES - loadable
        if missing:
            fail(f"{variant} merged manifest is missing required loadable classes: {sorted(missing)}")
        else:
            ok(f"{variant} merged manifest declares all required loadable classes")
        if aliases.get(REQUIRED_TOPWAY_ALIAS) == EXPECTED_TOPWAY_ALIAS_TARGET:
            ok(f"{variant} merged manifest aliases {REQUIRED_TOPWAY_ALIAS} to TopwayMusicEntryActivity")
        else:
            fail(f"{variant} merged manifest alias mismatch for {REQUIRED_TOPWAY_ALIAS}: {aliases.get(REQUIRED_TOPWAY_ALIAS)!r}")


def apk_paths():
    paths = {}
    for variant, build_type in [
        ("topwayTwMusicDebug", "debug"),
        ("topwayTwMusicRelease", "release"),
        ("topwayTwMediaDebug", "debug"),
        ("topwayTwMediaRelease", "release"),
    ]:
        # Explicit map avoids case mangling surprises.
        dir_name = "topwayTwMusic" if "Music" in variant else "topwayTwMedia"
        apk_dir = REPO / "app/build/outputs/apk" / dir_name / build_type
        if apk_dir.is_dir():
            candidates = sorted([p for p in apk_dir.glob("*.apk") if "unsigned" not in p.name]) or sorted(apk_dir.glob("*.apk"))
            if candidates:
                paths[variant] = candidates[0]
    return paths

def dex_contains(zf, dex_names, needle):
    # Stream each DEX entry separately so the guardrail does not concatenate all DEX files into memory.
    overlap = max(len(needle) - 1, 0)
    for dex_name in dex_names:
        tail = b""
        with zf.open(dex_name) as dex_file:
            while True:
                chunk = dex_file.read(1024 * 1024)
                if not chunk:
                    break
                data = tail + chunk
                if needle in data:
                    return True
                tail = data[-overlap:] if overlap else b""
    return False

def check_apks():
    import zipfile
    paths = apk_paths()
    if not paths:
        warn("no Topway APKs found; run Topway assemble tasks for APK DEX component checks")
        return
    for variant, apk in paths.items():
        try:
            with zipfile.ZipFile(apk) as zf:
                dex_names = [name for name in zf.namelist() if name.startswith("classes") and name.endswith(".dex")]
                if not dex_names:
                    fail(f"{variant} APK has no classes*.dex entries: {apk}")
                    continue
                ok(f"{variant} APK contains DEX entries: {', '.join(dex_names)}")
                for class_name in sorted(REQUIRED_TOPWAY_LOADABLE_CLASSES):
                    needle = descriptor(class_name).encode()
                    if dex_contains(zf, dex_names, needle):
                        ok(f"{variant} APK DEX contains loadable class {class_name}")
                    else:
                        fail(f"{variant} APK DEX is missing manifest loadable class {class_name}")
                alias_descriptor = descriptor(REQUIRED_TOPWAY_ALIAS).encode()
                if dex_contains(zf, dex_names, alias_descriptor):
                    ok(f"{variant} APK also contains optional alias class {REQUIRED_TOPWAY_ALIAS}")
                else:
                    ok(f"{variant} APK uses {REQUIRED_TOPWAY_ALIAS} as an activity-alias; no class is required")
        except Exception as exc:
            fail(f"unable to read {variant} APK {apk}: {exc}")
            continue

check_source_manifest()
check_merged_manifests()
check_apks()

if failures:
    sys.exit(1)
if warnings:
    sys.exit(2)
PY
status=$?
set -e
case "$status" in
  0) ;;
  2) warnings=$((warnings + 1)) ;;
  *) failures=$((failures + 1)) ;;
esac

printf '\nResult: '
if (( failures == 0 )); then
  if (( warnings == 0 )); then
    printf 'PASS\n'
  else
    printf 'PASS with warnings (%d warning(s))\n' "$warnings"
  fi
else
  printf 'FAIL (%d issue(s), %d warning(s))\n' "$failures" "$warnings"
  exit 1
fi

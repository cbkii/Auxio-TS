#!/usr/bin/env bash
# Validate stock-compatible component declarations and any built single-product outputs.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)
cd -- "$repo_root"

python3 - <<'PY'
import sys
import xml.etree.ElementTree as ET
import zipfile
from pathlib import Path

ANDROID = "{http://schemas.android.com/apk/res/android}"
ROOT = Path.cwd()
SOURCE_ROOTS = [ROOT / "app/src/main/java", ROOT / "app/src/main/kotlin", ROOT / "app/src/topwayCompat/java", ROOT / "app/src/topwayCompat/kotlin"]
REQUIRED = {
    "com.tw.music.MusicService",
    "com.tw.music.view.MusicWidgetProvider",
    "org.oxycblt.auxio.MainActivity",
    "org.oxycblt.auxio.car.overlay.CarOverlayActivity",
    "org.oxycblt.auxio.car.overlay.CarFloatingControlsService",
    "org.oxycblt.auxio.car.overlay.CarOverlayBootReceiver",
    "org.oxycblt.auxio.car.overlay.CarOverlayPermissionActivity",
}
ALIAS = "com.tw.music.MusicActivity"
TARGET = "org.oxycblt.auxio.MainActivity"
failures = []

def normalise(name, package):
    if not name: return None
    if name.startswith("."): return package + name
    if "." not in name: return package + "." + name
    return name

def parse(path, package="org.oxycblt.auxio"):
    root = ET.parse(path).getroot()
    package = root.attrib.get("package") or package
    app = root.find("application")
    classes, aliases = set(), {}
    if app is None: return classes, aliases
    for tag in ("activity", "service", "receiver", "provider"):
        for node in app.findall(tag):
            name = normalise(node.attrib.get(ANDROID + "name"), package)
            if name: classes.add(name)
    for node in app.findall("activity-alias"):
        name = normalise(node.attrib.get(ANDROID + "name"), package)
        target = normalise(node.attrib.get(ANDROID + "targetActivity"), package)
        if name: aliases[name] = target
        if target: classes.add(target)
    return classes, aliases

manifest = ROOT / "app/src/main/AndroidManifest.xml"
classes, aliases = parse(manifest)
missing = REQUIRED - classes
if missing: failures.append(f"main manifest lacks required classes: {sorted(missing)}")
if aliases.get(ALIAS) != TARGET: failures.append(f"{ALIAS} must target {TARGET}")
for cls in sorted(REQUIRED):
    rel = Path(*cls.split("."))
    if not any((base / rel.with_suffix(ext)).is_file() for base in SOURCE_ROOTS for ext in (".kt", ".java")):
        failures.append(f"source missing for {cls}")

for build_type, expected in (("debug", "com.tw.media.debug"), ("release", "com.tw.media")):
    merged = list((ROOT / "app/build/intermediates").glob(f"merged_manifest/**/{build_type}/**/AndroidManifest.xml"))
    if not merged: merged = list((ROOT / "app/build/intermediates").glob(f"merged_manifests/**/{build_type}/**/AndroidManifest.xml"))
    for path in merged[:1]:
        out_classes, out_aliases = parse(path, expected)
        if REQUIRED - out_classes: failures.append(f"{build_type} merged manifest lacks {sorted(REQUIRED - out_classes)}")
        if out_aliases.get(ALIAS) != TARGET: failures.append(f"{build_type} merged manifest alias mismatch")

    apk_dir = ROOT / "app/build/outputs/apk" / build_type
    apks = sorted(p for p in apk_dir.glob("*.apk") if "unsigned" not in p.name) if apk_dir.is_dir() else []
    for apk in apks[:1]:
        with zipfile.ZipFile(apk) as zf:
            dex = [name for name in zf.namelist() if name.startswith("classes") and name.endswith(".dex")]
            if not dex: failures.append(f"{apk} has no DEX entries")

if failures:
    for failure in failures: print(f"ERROR: {failure}", file=sys.stderr)
    raise SystemExit(1)
print("Stock-compatible component checks: PASS")
PY

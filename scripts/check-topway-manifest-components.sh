#!/usr/bin/env bash
# Validate neutral Standard and stock-compatible Topway manifest/component boundaries.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)
cd -- "$repo_root"

python3 - <<'PY'
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ANDROID = "{http://schemas.android.com/apk/res/android}"
ROOT = Path.cwd()
TOPWAY_REQUIRED = {
    "com.tw.music.MusicService",
    "com.tw.music.view.MusicWidgetProvider",
    "org.oxycblt.auxio.MainActivity",
    "org.oxycblt.auxio.car.overlay.CarOverlayActivity",
    "org.oxycblt.auxio.car.overlay.CarFloatingControlsService",
    "org.oxycblt.auxio.car.overlay.CarOverlayBootReceiver",
    "org.oxycblt.auxio.car.overlay.CarOverlayPermissionActivity",
}
TOPWAY_ONLY = TOPWAY_REQUIRED - {"org.oxycblt.auxio.MainActivity"}
ALIAS = "com.tw.music.MusicActivity"
TARGET = "org.oxycblt.auxio.MainActivity"
FLOATING_ALIAS = "org.oxycblt.auxio.car.overlay.FloatingControlsLauncher"
FLOATING_TARGET = "org.oxycblt.auxio.car.overlay.CarOverlayActivity"
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

main = ROOT / "app/src/main/AndroidManifest.xml"
topway = ROOT / "app/src/topwayCompat/AndroidManifest.xml"
main_classes, main_aliases = parse(main)
topway_classes, topway_aliases = parse(topway)

leaked = TOPWAY_ONLY & main_classes
if leaked: failures.append(f"Standard/main manifest contains Topway-only components: {sorted(leaked)}")
if ALIAS in main_aliases or FLOATING_ALIAS in main_aliases:
    failures.append("Standard/main manifest contains Topway-only activity aliases")
missing = TOPWAY_REQUIRED - topway_classes
if missing: failures.append(f"Topway overlay lacks required classes: {sorted(missing)}")
if topway_aliases.get(ALIAS) != TARGET: failures.append(f"{ALIAS} must target {TARGET}")
if topway_aliases.get(FLOATING_ALIAS) != FLOATING_TARGET:
    failures.append(f"{FLOATING_ALIAS} must target {FLOATING_TARGET}")

source_roots = [ROOT / "app/src/main/java", ROOT / "app/src/main/kotlin", ROOT / "app/src/topwayCompat/java", ROOT / "app/src/topwayCompat/kotlin"]
for cls in sorted(TOPWAY_REQUIRED):
    rel = Path(*cls.split("."))
    if not any((base / rel.with_suffix(ext)).is_file() for base in source_roots for ext in (".kt", ".java")):
        failures.append(f"source missing for {cls}")

# Inspect any merged manifests already produced by Gradle. Standard must remain neutral; both Topway
# identities must expose the same thin compatibility surface.
for variant, topway_variant in (("standardDebug", False), ("topwayTwMediaDebug", True), ("topwayTwMusicDebug", True), ("standardRelease", False), ("topwayTwMediaRelease", True), ("topwayTwMusicRelease", True)):
    candidates = list((ROOT / "app/build/intermediates").glob(f"merged_manifest/**/{variant}/**/AndroidManifest.xml"))
    candidates += list((ROOT / "app/build/intermediates").glob(f"merged_manifests/**/{variant}/**/AndroidManifest.xml"))
    for path in candidates[:1]:
        classes, aliases = parse(path)
        if topway_variant:
            missing = TOPWAY_REQUIRED - classes
            if missing: failures.append(f"{variant} merged manifest lacks {sorted(missing)}")
            if aliases.get(ALIAS) != TARGET: failures.append(f"{variant} alias mismatch")
            if aliases.get(FLOATING_ALIAS) != FLOATING_TARGET: failures.append(f"{variant} floating alias mismatch")
        else:
            leaked = TOPWAY_ONLY & classes
            if leaked or ALIAS in aliases or FLOATING_ALIAS in aliases:
                failures.append(f"{variant} merged manifest leaked Topway-only components")

if failures:
    for failure in failures: print(f"ERROR: {failure}", file=sys.stderr)
    raise SystemExit(1)
print("Variant manifest/component isolation: PASS")
PY

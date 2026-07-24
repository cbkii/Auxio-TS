#!/usr/bin/env bash
set -u

fail() {
  echo "TS18 root storage guard: $*" >&2
  exit 1
}

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)" ||
  fail 'cannot resolve script directory'
cd "$script_dir/.." || fail 'cannot change to repository root'

root_gate=musikr/src/main/java/org/oxycblt/musikr/fs/RootGate.kt
root_holder=app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt
direct_fs=musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt
resolver=app/src/main/java/org/oxycblt/auxio/headunit/root/dofun/Ts18DofunIntegrationResolver.kt
index_store=app/src/main/java/org/oxycblt/auxio/headunit/root/storage/PreparedVolumeIndexStore.kt
locations=app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt
helper=tools/ts18-root-storage-fastpath/magisk-module/service.sh
module_prop=tools/ts18-root-storage-fastpath/magisk-module/module.prop

for file in \
  "$root_gate" "$root_holder" "$direct_fs" "$resolver" "$index_store" \
  "$locations" "$helper" "$module_prop"; do
  [ -f "$file" ] || fail "missing $file"
done

grep -Fq 'snapshotTreeSync' "$root_gate" || fail 'typed snapshot API missing'
if grep -Fq 'runRootCommandSync' "$root_gate"; then
  fail 'free-form root command escaped into Musikr'
fi
grep -Fq 'BuildConfig.TOPWAY_COMPAT_FLAVOR' "$root_holder" ||
  fail 'Topway variant gate missing'
grep -Fq '/data/adb/modules/auxio_ts_root_storage/service.sh' "$root_holder" ||
  fail 'fixed prepared-helper command missing'

# Scan every production Java/Kotlin source set while ignoring comments. This catches command strings
# and executable source without false positives from documentation such as "adb shell setprop ...".
python3 - <<'PY' || fail 'protected mutation command remains in production runtime source'
from pathlib import Path
import re
import sys

PATTERN = re.compile(
    r"(?ix)"
    r"\bTs18RootMutation\b|"
    r"\bpm\s+(?:disable-user|enable|uninstall|clear)\b|"
    r"\bcmd\s+package\s+(?:uninstall|clear|set-enabled-setting)\b|"
    r"\bcontent\s+(?:insert|update|delete)\b|"
    r"\bsettings\s+put\b|"
    r"\bsetprop\s+"
)


def strip_comments(text: str) -> str:
    out: list[str] = []
    i = 0
    block_depth = 0
    state = "code"
    while i < len(text):
        if state == "code":
            if text.startswith("//", i):
                state = "line"
                out.extend("  ")
                i += 2
            elif text.startswith("/*", i):
                state = "block"
                block_depth = 1
                out.extend("  ")
                i += 2
            elif text.startswith('"""', i):
                state = "triple"
                out.extend('"""')
                i += 3
            elif text[i] == '"':
                state = "double"
                out.append(text[i])
                i += 1
            elif text[i] == "'":
                state = "single"
                out.append(text[i])
                i += 1
            else:
                out.append(text[i])
                i += 1
        elif state == "line":
            if text[i] == "\n":
                out.append("\n")
                state = "code"
            else:
                out.append(" ")
            i += 1
        elif state == "block":
            if text.startswith("/*", i):
                block_depth += 1
                out.extend("  ")
                i += 2
            elif text.startswith("*/", i):
                block_depth -= 1
                out.extend("  ")
                i += 2
                if block_depth == 0:
                    state = "code"
            else:
                out.append("\n" if text[i] == "\n" else " ")
                i += 1
        elif state == "triple":
            if text.startswith('"""', i):
                out.extend('"""')
                i += 3
                state = "code"
            else:
                out.append(text[i])
                i += 1
        elif state in {"double", "single"}:
            quote = '"' if state == "double" else "'"
            out.append(text[i])
            if text[i] == "\\" and i + 1 < len(text):
                out.append(text[i + 1])
                i += 2
            elif text[i] == quote:
                i += 1
                state = "code"
            else:
                i += 1
    return "".join(out)


files: list[Path] = []
for root in (Path("app/src"), Path("musikr/src")):
    if not root.is_dir():
        raise SystemExit(f"missing production source root: {root}")
    for path in root.rglob("*"):
        if path.suffix not in {".kt", ".java"} or not path.is_file():
            continue
        parts = set(path.parts)
        source_set = path.parts[2] if len(path.parts) > 2 else ""
        if (
            parts.intersection({"test", "androidTest", "benchmark"})
            or source_set.endswith("Test")
        ):
            continue
        files.append(path)
if not files:
    raise SystemExit("no production Java/Kotlin files found")

matches: list[str] = []
for path in sorted(files):
    cleaned = strip_comments(path.read_text(encoding="utf-8", errors="strict"))
    for line_number, line in enumerate(cleaned.splitlines(), start=1):
        match = PATTERN.search(line)
        if match:
            matches.append(f"{path}:{line_number}: {match.group(0)!r}")
if matches:
    print("\n".join(matches), file=sys.stderr)
    raise SystemExit(1)
print(f"Production mutation scan passed ({len(files)} source files)")
PY

if grep -Fq 'buildRootListCommand' "$direct_fs"; then
  fail 'per-directory root listing remains in DirectFS'
fi
grep -Fq 'root_snapshot_without_app_uid_media_access' "$index_store" ||
  fail 'snapshot-only classification missing'
grep -Fq 'RootStorageAccelerationPolicy.choose' "$index_store" ||
  fail 'cost-aware storage ordering missing'
grep -Fq 'isAllowedCanonicalStorageRoot(clean)' "$index_store" ||
  fail 'canonical storage containment gate missing'
grep -Fq 'TopwaySourcePolicy.isAllowedSourceCandidate(path)' "$locations" ||
  fail 'Topway DirectFS path boundary missing'
grep -Fq 'LocationMode.defaultForFlavor' \
  app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt ||
  fail 'DirectFS fresh default missing'
grep -Fq '/data/adb/auxio-ts-root/volumes.tsv' "$root_holder" ||
  fail 'prepared manifest contract missing'
grep -q '^description=' "$module_prop" || fail 'Magisk module description metadata missing'

if grep -Eq 'pm (disable-user|enable)|/system/(app|priv-app)|/vendor/(app|priv-app)' "$helper"; then
  fail 'helper contains forbidden package mutation or protected APK write'
fi
if find tools/ts18-root-storage-fastpath/magisk-module -path '*/service.d/*' -type f | grep -q .; then
  fail 'Magisk module must use module-root service.sh, not nested service.d'
fi
grep -Fq 'MAX_SAMPLED_VOLUMES=2' "$helper" ||
  fail 'bounded representative-sample count missing'
grep -Fq 'SAMPLE_TIMEOUT_SECONDS=1' "$helper" ||
  fail 'bounded representative-sample timeout missing'
if grep -Fq 'MAX_VOLUMES=' "$helper"; then
  fail 'helper must not silently omit detected volumes'
fi
grep -Fq 'volumes=$processed sampled=$sampled' "$helper" ||
  fail 'helper volume/sample accounting missing'

shell_count=0
while IFS= read -r -d '' shell_file; do
  bash -n "$shell_file" || fail "shell syntax failed: $shell_file"
  shell_count=$((shell_count + 1))
done < <(find scripts tools/ts18-root-storage-fastpath -type f -name '*.sh' -print0)
[ "$shell_count" -gt 0 ] || fail 'no shell scripts found for syntax validation'

echo "TS18 root storage fast-path checks passed (shell_files=$shell_count)"

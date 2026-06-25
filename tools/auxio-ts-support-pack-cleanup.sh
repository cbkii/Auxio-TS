#!/usr/bin/env bash
# Remove old Auxio-TS TS18 Codex support-pack contents from v1-v3 and extract v4.
# Run from cbkii/Auxio-TS repo root.
set -u

die() { printf '
ERROR: %s
' "$*" >&2; exit 1; }
run() { printf '
==> %s
' "$*"; "$@"; local rc=$?; [ "$rc" -eq 0 ] || die "Command failed with exit $rc: $*"; }

[ -d .git ] || die "Run from the Auxio-TS repo root"
origin_url="$(git remote get-url origin 2>/dev/null || true)"
case "$origin_url" in *cbkii/Auxio-TS*) ;; *) die "origin does not look like cbkii/Auxio-TS: $origin_url" ;; esac

zip_path="${1:-/mnt/data/auxio_ts18_codex_support_pack_v4.zip}"
[ -f "$zip_path" ] || die "Zip not found: $zip_path"

tracked_dirty="$(git status --porcelain)"
if [ -n "$tracked_dirty" ]; then
  bad="$(printf '%s
' "$tracked_dirty" | awk '{print $2}' | grep -Ev '^(CODEX_START_HERE\.md|codex_ts18_auxio_full_scope_prompt(_v[0-9]+)?\.md|SHA256SUMS\.txt|evidence/|docs/evidence/ts18_auxio_v5_0_6/|docs/prompts/(CODEX_START_HERE\.md|codex_ts18_auxio_full_scope_prompt(_v[0-9]+)?\.md)|docs/research/STOCK_T_MUSIC_AND_VLC_MEDIA_CONTROL_EVIDENCE\.md|tools/ts18-auxio-media-diag-pack-v3-|tools/auxio-ts-support-pack-cleanup\.sh|README\.md$)' || true)"
  if [ -n "$bad" ]; then
    printf '%s
' "$tracked_dirty"
    die "Working tree has unrelated changes; commit/stash first"
  fi
fi

old_paths=(
  "CODEX_START_HERE.md"
  "codex_ts18_auxio_full_scope_prompt.md"
  "codex_ts18_auxio_full_scope_prompt_v2.md"
  "codex_ts18_auxio_full_scope_prompt_v3.md"
  "SHA256SUMS.txt"
  "evidence/ts18_auxio_v5_0_6"
  "docs/evidence/ts18_auxio_v5_0_6"
  "docs/prompts/CODEX_START_HERE.md"
  "docs/prompts/codex_ts18_auxio_full_scope_prompt.md"
  "docs/prompts/codex_ts18_auxio_full_scope_prompt_v4.md"
  "docs/research/STOCK_T_MUSIC_AND_VLC_MEDIA_CONTROL_EVIDENCE.md"
  "tools/ts18-auxio-media-diag-pack-v3-original"
  "tools/ts18-auxio-media-diag-pack-v3-recommended"
  "tools/auxio-ts-support-pack-cleanup.sh"
)

printf '
==> Removing old support-pack paths
'
for p in "${old_paths[@]}"; do
  if git ls-files --error-unmatch "$p" >/dev/null 2>&1; then
    git rm -r --ignore-unmatch -- "$p"
  else
    rm -rf -- "$p"
  fi
done

if [ -f README.md ] && grep -q 'Auxio-TS TS18 evidence support pack' README.md; then
  printf '
==> Restoring overwritten repo README.md
'
  git checkout -- README.md 2>/dev/null || rm -f README.md
fi

printf '
==> Checking v4 zip has no top-level entries outside docs/ or tools/
'
python3 - "$zip_path" <<'PYZIPCHECK'
import sys, zipfile
zp=sys.argv[1]
with zipfile.ZipFile(zp) as z:
    bad=[n for n in z.namelist() if n and not n.endswith('/') and not (n.startswith('docs/') or n.startswith('tools/'))]
    if bad:
        print('Bad top-level entries:', *bad, sep='
  ')
        sys.exit(2)
PYZIPCHECK

run unzip -o "$zip_path" -d .

printf '
==> Staging support-pack replacement
'
run git add docs/evidence/ts18_auxio_v5_0_6 docs/prompts docs/research/STOCK_T_MUSIC_AND_VLC_MEDIA_CONTROL_EVIDENCE.md tools/ts18-auxio-media-diag-pack-v3-original tools/ts18-auxio-media-diag-pack-v3-recommended tools/auxio-ts-support-pack-cleanup.sh

printf '
==> Current staged diff summary
'
git diff --cached --stat

if git diff --cached --quiet; then
  die "No staged changes after cleanup/extract"
fi

printf '
Review the staged diff. If correct, commit with:
'
printf '  git commit -m "docs: refresh TS18 Auxio evidence support pack" && git push
'

# com.tw.music — TS18 Native Music App
**Source APK:** `com.tw.music.apk`

| Field | Value |
|---|---|
| package | `com.tw.music` |
| versionName | `TW_THEME.20240715` |
| versionCode | `118` |
| minSdkVersion | `29` |
| targetSdkVersion | `29` |
| sharedUserId | android.uid.system |
| smali dex dirs | 4 |

## Repo layout

| Path | Purpose |
|---|---|
| `app/apktool/` | **Canonical editable/buildable project** |
| `reference/jadx-raw/` | JADX Java reference export (read-only) |
| `reference/jadx-aliased/` | JADX export with persistent renames applied |
| `reference/jadx-mt/` | Optional export from MT-normalised APK (currently absent unless generated) |
| `reference/firstparty-jadx/` | Filtered: first-party (com.tw.music) classes |
| `reference/vendor-jadx/` | Filtered: vendor/TW integration classes |
| `mappings/jadx/` | JADX-generated JOBF alias caches |
| `mappings/manual-enigma/` | Human-reviewed persistent renames |
| `docs/` | Reports, maps, manual gates, release notes |
| `scripts/` | Repeatable helper scripts |

> JADX alias caveat: packages like `com.p060tw.music` / `com.p073tw.music` in `reference/` are decompiler alias artefacts; runtime package identity remains `com.tw.music`.

## Quick start

```bash
# Rebuild APK (needs external aapt2)
bash scripts/02_build_unsigned.sh

# Refresh all maps and reports
bash scripts/01_refresh_reports.sh

# Re-export JADX reference (pass original APK path)
bash scripts/03_jadx_export_raw.sh /path/to/com.tw.music.apk
```

## Planning and execution authority

For current phase sequencing, gate status, blockers, and next actions, use `docs/migration-blueprint.md` as the single live execution tracker. Other planning documents in `docs/` are supporting narrative/evidence references.

## CI / Actions usage

For workflow inputs, dispatch examples, and commit/push guardrails, see `README_ACTIONS.md`.  
If running `maintenance.yml`, keep `allow_push=false` unless you explicitly want generated docs committed back to the branch.

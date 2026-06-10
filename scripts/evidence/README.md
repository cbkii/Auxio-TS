# Evidence helper scripts

All scripts are repo-local helpers or TS18 collectors. They are read-only unless explicitly run with a mutation flag.

## `collect-ts18-dofun-runtime-validation-v3.sh`

Corrected TS18 TermOnePlus collector. Safe/read-only by default. It uses `ANDROID_USER_ID=0` by default and intentionally ignores the shell `USER_ID` environment variable, because TermOnePlus may set `USER_ID` to the app UID (for example `10177`) rather than Android user `0`.

Recommended baseline:

```sh
cd /sdcard/Download
ANDROID_USER_ID=0 sh collect-ts18-dofun-runtime-validation-v3.sh baseline
```

Optional mutation tests:

```sh
ANDROID_USER_ID=0 TRY_DISABLE_STOCK=1 sh collect-ts18-dofun-runtime-validation-v3.sh user0-disable-stock-test
ANDROID_USER_ID=0 RESTORE_STOCK=1 sh collect-ts18-dofun-runtime-validation-v3.sh restore-stock-user0
```

## Other scripts

- `grep-topway-contract.sh`: read-only grep for exact contract strings.
- `compare-evidence-contract.sh`: read-only static presence check for key terms in this evidence directory.

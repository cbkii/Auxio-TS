#!/usr/bin/env bash
set -u
fail() {
  echo "TS18 root storage guard: $*" >&2
  exit 1
}

root_gate=musikr/src/main/java/org/oxycblt/musikr/fs/RootGate.kt
root_holder=app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt
direct_fs=musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt
helper=tools/ts18-root-storage-fastpath/magisk-module/service.sh

for file in "$root_gate" "$root_holder" "$direct_fs" "$helper"; do
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
if grep -Eq 'pm (disable-user|enable)|Ts18RootMutation' \
  "$root_holder" \
  app/src/main/java/org/oxycblt/auxio/headunit/root/dofun/Ts18DofunIntegrationResolver.kt; then
  fail 'protected-package mutation remains in runtime root code'
fi
if grep -Fq 'buildRootListCommand' "$direct_fs"; then
  fail 'per-directory root listing remains in DirectFS'
fi
grep -Fq 'root_snapshot_without_app_uid_media_access' \
  app/src/main/java/org/oxycblt/auxio/headunit/root/storage/PreparedVolumeIndexStore.kt ||
  fail 'snapshot-only classification missing'
grep -Fq 'LocationMode.defaultForFlavor' \
  app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt ||
  fail 'DirectFS fresh default missing'
grep -Fq '/data/adb/auxio-ts-root/volumes.tsv' "$root_holder" ||
  fail 'prepared manifest contract missing'
if grep -Eq 'pm (disable-user|enable)|/system/|/vendor/' "$helper"; then
  fail 'helper contains forbidden mutation/write'
fi
if find tools/ts18-root-storage-fastpath/magisk-module -path '*/service.d/*' -type f | grep -q .; then
  fail 'Magisk module must use module-root service.sh, not nested service.d'
fi
bash -n "$helper" || fail 'helper shell syntax failed'
bash -n scripts/package-ts18-root-storage-helper.sh || fail 'packager shell syntax failed'
bash -n tools/ts18-root-storage-fastpath/tier3/stock-music-selection-test.sh ||
  fail 'Tier 3 script syntax failed'
echo "TS18 root storage fast-path checks passed"

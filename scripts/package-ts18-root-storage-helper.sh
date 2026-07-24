#!/usr/bin/env bash
set -u
root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source_dir="$root_dir/tools/ts18-root-storage-fastpath/magisk-module"
output="${1:-$root_dir/Auxio-TS-ts18-root-storage-helper.zip}"

for required in module.prop customize.sh service.d/55-auxio-root-storage-prepare.sh; do
  if [ ! -f "$source_dir/$required" ]; then
    echo "Missing helper module file: $required" >&2
    exit 2
  fi
done
if ! command -v zip >/dev/null 2>&1; then
  echo "zip is required" >&2
  exit 2
fi
rm -f "$output"
(
  cd "$source_dir" || exit 2
  zip -qr "$output" module.prop customize.sh service.d
) || exit 1
unzip -l "$output" | grep -Fq 'service.d/55-auxio-root-storage-prepare.sh' || exit 1
echo "$output"

#!/usr/bin/env bash
set -u
root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source_dir="$root_dir/tools/ts18-root-storage-fastpath/magisk-module"
output="${1:-$root_dir/Auxio-TS-ts18-root-storage-helper.zip}"

output_dir="$(dirname -- "$output")"
output_name="$(basename -- "$output")"
if ! output_dir="$(cd -- "$output_dir" && pwd)"; then
  echo "Output directory does not exist: $(dirname -- "$output")" >&2
  exit 2
fi
output="$output_dir/$output_name"

for required in module.prop customize.sh service.sh; do
  if [ ! -f "$source_dir/$required" ]; then
    echo "Missing helper module file: $required" >&2
    exit 2
  fi
done
if ! command -v zip >/dev/null 2>&1 || ! command -v unzip >/dev/null 2>&1; then
  echo "zip and unzip are required" >&2
  exit 2
fi
if ! rm -f -- "$output"; then
  echo "Unable to remove existing output ZIP: $output" >&2
  exit 1
fi
(
  cd "$source_dir" || exit 2
  zip -qr "$output" module.prop customize.sh service.sh
) || exit 1
entries="$(unzip -Z1 "$output")" || exit 1
printf '%s\n' "$entries" | grep -Fxq 'service.sh' || exit 1
if printf '%s\n' "$entries" | grep -Fq 'service.d/'; then
  echo "Nested service.d is not a valid module late-start entrypoint" >&2
  exit 1
fi
echo "$output"

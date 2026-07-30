#!/usr/bin/env bash
# Shared parser for apksigner verify --print-certs output.
# Callers must capture stdout and stderr from apksigner before invoking this function.

extract_apksigner_certificate_sha256() {
  local report=${1-}
  local line digest
  local pattern='^[[:space:]]*Signer[[:space:]]+(#[0-9]+|\([^)]*\))[[:space:]]+certificate[[:space:]]+SHA-256[[:space:]]+digest:[[:space:]]*([0-9A-Fa-f:]+)[[:space:]]*$'
  local -A unique_digests=()

  while IFS= read -r line; do
    line=${line%$'\r'}
    [[ $line =~ $pattern ]] || continue

    digest=${BASH_REMATCH[2]//:/}
    digest=${digest^^}
    if [[ ! $digest =~ ^[0-9A-F]{64}$ ]]; then
      printf '::error::apksigner returned a malformed certificate SHA-256 digest.\n' >&2
      return 2
    fi
    unique_digests["$digest"]=1
  done <<< "$report"

  case ${#unique_digests[@]} in
    0)
      printf '::error::apksigner output did not contain a signer certificate SHA-256 digest.\n' >&2
      return 1
      ;;
    1)
      for digest in "${!unique_digests[@]}"; do
        printf '%s\n' "$digest"
      done
      ;;
    *)
      printf '::error::apksigner output contained %d distinct signer certificate SHA-256 digests.\n' \
        "${#unique_digests[@]}" >&2
      return 1
      ;;
  esac
}

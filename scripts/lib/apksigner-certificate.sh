#!/usr/bin/env bash
# Shared parser for apksigner verify --print-certs output.
# Callers must capture stdout and stderr from apksigner before invoking this function.

summarise_apksigner_certificate_records() {
  local report=${1-}
  local line prefix
  local count=0

  while IFS= read -r line; do
    line=${line%$'\r'}
    case $line in
      *"certificate SHA-256 digest:"*)
        prefix=${line%%certificate SHA-256 digest:*}
        printf '[INFO] apksigner certificate record: %scertificate SHA-256 digest: <redacted>\n' \
          "$prefix" >&2
        count=$((count + 1))
        ((count >= 20)) && break
        ;;
    esac
  done <<< "$report"

  if ((count == 0)); then
    printf '[INFO] apksigner report contained no SHA-256 certificate record lines.\n' >&2
  fi
}

extract_apksigner_certificate_sha256() {
  local report=${1-}
  local line label digest existing seen
  # apksigner output varies by Build Tools release. Accept the historical
  # aggregate labels and the scheme-qualified labels emitted by newer tools,
  # while rejecting source stamps and every other certificate record.
  local record_pattern='^[[:space:]]*(.+)[[:space:]]+certificate[[:space:]]+SHA-256[[:space:]]+digest:[[:space:]]*(.*)[[:space:]]*$'
  local numbered_label_pattern='^Signer[[:space:]]+#[1-9][0-9]*$'
  local sdk_range_label_pattern='^Signer[[:space:]]+\(minSdkVersion=[0-9]+([[:space:]]+\(dev[[:space:]]+release=true\))?,[[:space:]]+maxSdkVersion=[0-9]+\)$'
  local scheme_label_pattern='^V[1-9][0-9]*(\.[0-9]+)?[[:space:]]+Signer:$'
  local source_stamp_label='Source Stamp Signer'
  local unique_digests=()

  while IFS= read -r line; do
    line=${line%$'\r'}
    [[ $line =~ $record_pattern ]] || continue

    label=${BASH_REMATCH[1]}
    digest=${BASH_REMATCH[2]}

    # A source stamp identifies the distributor, not the APK signing identity.
    [[ $label == "$source_stamp_label" ]] && continue

    if [[ ! $label =~ $numbered_label_pattern &&
          ! $label =~ $sdk_range_label_pattern &&
          ! $label =~ $scheme_label_pattern ]]; then
      printf '::error::apksigner returned an unsupported signer certificate label.\n' >&2
      summarise_apksigner_certificate_records "$report"
      return 2
    fi

    digest=$(
      printf '%s' "$digest" |
        tr -d '[:space:]:' |
        tr -d '-' |
        tr '[:lower:]' '[:upper:]'
    )
    if [[ ! $digest =~ ^[0-9A-F]{64}$ ]]; then
      printf '::error::apksigner returned a malformed certificate SHA-256 digest.\n' >&2
      summarise_apksigner_certificate_records "$report"
      return 2
    fi

    seen=false
    for existing in "${unique_digests[@]}"; do
      if [[ $existing == "$digest" ]]; then
        seen=true
        break
      fi
    done
    [[ $seen == true ]] || unique_digests+=("$digest")
  done <<< "$report"

  case ${#unique_digests[@]} in
    0)
      printf '::error::apksigner output did not contain a signer certificate SHA-256 digest.\n' >&2
      summarise_apksigner_certificate_records "$report"
      return 1
      ;;
    1)
      printf '%s\n' "${unique_digests[0]}"
      ;;
    *)
      printf '::error::apksigner output contained %d distinct signer certificate SHA-256 digests.\n' \
        "${#unique_digests[@]}" >&2
      summarise_apksigner_certificate_records "$report"
      return 1
      ;;
  esac
}

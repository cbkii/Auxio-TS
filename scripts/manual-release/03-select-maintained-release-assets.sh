#!/usr/bin/env bash
set -euo pipefail

bool() {
  case "$2" in true|false) ;; *) echo "::error::$1 must resolve to true or false." >&2; exit 1 ;; esac
}
bool INCLUDE_STANDARD "${INCLUDE_STANDARD:-}"
bool INCLUDE_TOPWAY_TWMEDIA "${INCLUDE_TOPWAY_TWMEDIA:-}"
bool INCLUDE_TOPWAY_TWMUSIC_MAGISK "${INCLUDE_TOPWAY_TWMUSIC_MAGISK:-}"
bool INCLUDE_LSPOSED_BRIDGE "${INCLUDE_LSPOSED_BRIDGE:-}"
bool PUBLISH_DEBUG_APKS "${PUBLISH_DEBUG_APKS:-}"

if [[ "${PUBLISH_DEBUG_APKS}" == true ]]; then DEBUG_DESTINATION=release_assets; else DEBUG_DESTINATION=workflow_artifacts; fi
if [[ "${INCLUDE_LSPOSED_BRIDGE}" == true && "${INCLUDE_TOPWAY_TWMEDIA}" != true ]]; then
  echo "::error::LSPosed Track C requires the maintained topwayTwMedia target to be selected." >&2
  exit 1
fi

selected=()
if [[ "${INCLUDE_STANDARD}" == true ]]; then selected+=(standard standard_debug); fi
if [[ "${INCLUDE_TOPWAY_TWMEDIA}" == true ]]; then selected+=(topway_twmedia topway_twmedia_debug); fi
if [[ "${INCLUDE_TOPWAY_TWMUSIC_MAGISK}" == true ]]; then selected+=(topway_twmusic_magisk); fi
if [[ "${INCLUDE_LSPOSED_BRIDGE}" == true ]]; then selected+=(lsposed_bridge lsposed_bridge_debug); fi
if ((${#selected[@]} == 0)); then
  echo "::error::At least one maintained release asset must be selected." >&2
  exit 1
fi

selected_file="${RUNNER_TEMP}/selected-variants.txt"
printf '%s\n' "${selected[@]}" > "${selected_file}"
{
  echo "selected_file=${selected_file}"
  printf 'selected_variants<<EOF\n%s\nEOF\n' "$(cat "${selected_file}")"
  printf 'selected_summary=%s\n' "$(IFS=', '; echo "${selected[*]}")"
  echo "debug_destination=${DEBUG_DESTINATION}"
} >> "${GITHUB_OUTPUT}"

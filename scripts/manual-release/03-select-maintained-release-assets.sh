set -euo pipefail
case "${PUBLISH_DEBUG_APKS}" in
  true) DEBUG_DESTINATION=release_assets ;;
  false) DEBUG_DESTINATION=workflow_artifacts ;;
  *)
    echo "::error::publish_debug_apks must resolve to true or false."
    exit 1
    ;;
esac

selected=()
if [[ "${INCLUDE_TWMEDIA}" == true ]]; then
  selected+=(topway_twmedia topway_twmedia_debug)
fi
if [[ "${INCLUDE_LSPOSED_BRIDGE}" == true ]]; then
  selected+=(lsposed_bridge lsposed_bridge_debug)
fi
if ((${#selected[@]} == 0)); then
  echo "::error::At least one maintained release asset must be selected."
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

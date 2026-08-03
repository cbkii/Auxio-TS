#!/usr/bin/env bash
# Audit or reset repository protections that can block Auxio-TS Manual Release.
# Default mode is read-only. Pass --apply to delete repository rulesets and
# classic protection on dev, restoring the simple GITHUB_TOKEN release model.

SCRIPT_NAME=${0##*/}
REPO='cbkii/Auxio-TS'
APPLY=0
WARNINGS=0
TMP_DIR=''
TMP_PARENT=''
TIMEOUT_CMD=''

log() { printf '[INFO] %s\n' "$*" >&2; }
warn() { WARNINGS=$((WARNINGS + 1)); printf '[WARN] %s\n' "$*" >&2; }
stop() { printf 'STOP: %s\n' "$*" >&2; exit 1; }
lowercase() { printf '%s' "$1" | LC_ALL=C tr '[:upper:]' '[:lower:]'; }
usage() {
  cat <<USAGE
Usage: ${SCRIPT_NAME} [--apply] [-R owner/repo]

Without --apply, performs a bounded read-only audit.
With --apply, deletes all repository rulesets and classic protection on dev.
It does not change APK-signing secrets or the repository's default Actions
workflow-permission setting.
USAGE
}

cleanup() {
  local rc=$?
  trap - EXIT INT TERM
  if [[ -n ${TMP_DIR:-} && -d ${TMP_DIR:-} ]]; then
    # Best-effort cleanup only; preserve the script's original exit status.
    rm -rf -- "$TMP_DIR" 2>/dev/null || :
  fi
  exit "$rc"
}
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

while (($#)); do
  case $1 in
    --apply) APPLY=1; shift ;;
    -R|--repo)
      (($# >= 2)) || stop "$1 requires owner/repo"
      REPO=$2
      shift 2
      ;;
    -h|--help) usage; exit 0 ;;
    *) stop "unknown argument: $1" ;;
  esac
done

[[ $REPO =~ ^[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+$ ]] || stop "invalid repository: $REPO"
for command_name in gh jq mktemp tr grep; do
  command -v "$command_name" >/dev/null 2>&1 || stop "required command unavailable: $command_name"
done
if command -v timeout >/dev/null 2>&1; then
  TIMEOUT_CMD=$(command -v timeout)
elif command -v gtimeout >/dev/null 2>&1; then
  TIMEOUT_CMD=$(command -v gtimeout)
else
  stop 'timeout or gtimeout is required; refusing unbounded GitHub API calls'
fi

TMP_PARENT=${TMPDIR:-${HOME}/tmp}
mkdir -p -- "$TMP_PARENT" || stop "could not create private temporary parent: $TMP_PARENT"
TMP_DIR=$(mktemp -d "${TMP_PARENT}/${SCRIPT_NAME}.XXXXXXXX") ||
  stop 'could not create private temporary directory'

api_to_file() {
  local output_file=$1
  shift
  local error_file="${output_file}.error"
  local rc

  : > "$output_file"
  : > "$error_file"
  "$TIMEOUT_CMD" --foreground 30s gh api "$@" >"$output_file" 2>"$error_file"
  rc=$?
  if ((rc != 0)); then
    printf 'STOP: GitHub API request failed: gh api' >&2
    printf ' %q' "$@" >&2
    printf '\n' >&2
    cat "$error_file" >&2
    return "$rc"
  fi
  return 0
}

log '[1/6] Verifying GitHub identity and repository'
actor_file="$TMP_DIR/actor.json"
repo_file="$TMP_DIR/repo.json"
api_to_file "$actor_file" user || exit 1
api_to_file "$repo_file" "repos/$REPO" || exit 1
actor=$(jq -er '.login | select(type == "string" and length > 0)' "$actor_file") ||
  stop 'GitHub identity response was invalid'
resolved_repo=$(jq -er '.full_name | select(type == "string" and length > 0)' "$repo_file") ||
  stop 'repository response was invalid'
resolved_repo_lower=$(lowercase "$resolved_repo")
requested_repo_lower=$(lowercase "$REPO")
[[ $resolved_repo_lower == "$requested_repo_lower" ]] ||
  stop "resolved repository mismatch: $resolved_repo"
owner=${REPO%%/*}
actor_lower=$(lowercase "$actor")
owner_lower=$(lowercase "$owner")
[[ $actor_lower == "$owner_lower" ]] ||
  stop "authenticated GitHub user is $actor; repository owner $owner is required for this reset"
printf 'Repository: %s\nAuthenticated owner: %s\n' "$resolved_repo" "$actor"

log '[2/6] Checking existing signing secrets and Actions token default'
secrets_file="$TMP_DIR/secrets.json"
workflow_permissions_file="$TMP_DIR/workflow-permissions.json"
api_to_file "$secrets_file" --paginate --slurp \
  "repos/$REPO/actions/secrets?per_page=100" || exit 1
api_to_file "$workflow_permissions_file" \
  "repos/$REPO/actions/permissions/workflow" || exit 1

jq -e '
  type == "array" and
  all(.[]; type == "object" and (.secrets | type) == "array")
' "$secrets_file" >/dev/null || stop 'Actions secret-name response had an unexpected schema'
secret_names_file="$TMP_DIR/secret-names.txt"
jq -r '
  [.[] | .secrets[]? | .name | select(type == "string" and length > 0)]
  | sort
  | unique[]
' "$secrets_file" > "$secret_names_file" || stop 'Actions secret names could not be parsed'

jq -e '
  type == "object" and
  (.default_workflow_permissions | type) == "string"
' "$workflow_permissions_file" >/dev/null ||
  stop 'Actions workflow-permission response had an unexpected schema'

missing_secrets=''
for required in KEYSTORE_BASE64 KEYSTORE_PASSWORD KEY_ALIAS KEY_PASSWORD; do
  if ! grep -Fxq -- "$required" "$secret_names_file"; then
    if [[ -n $missing_secrets ]]; then
      missing_secrets="$missing_secrets $required"
    else
      missing_secrets=$required
    fi
  fi
done
if [[ -n $missing_secrets ]]; then
  warn "missing APK-signing secret names: $missing_secrets"
  if ((APPLY == 1)); then
    stop 'refusing to remove protections while required APK-signing secret names are missing'
  fi
else
  printf 'Signing secret names: complete\n'
fi
printf 'Default Actions token permissions:\n'
jq '{default_workflow_permissions, can_approve_pull_request_reviews}' \
  "$workflow_permissions_file"
if grep -Fxq -- RELEASE_PUSH_TOKEN "$secret_names_file"; then
  warn 'RELEASE_PUSH_TOKEN exists but the simple release workflow does not use it; delete it manually when no other workflow depends on it'
fi

log '[3/6] Inspecting repository rulesets'
rulesets_file="$TMP_DIR/rulesets.json"
api_to_file "$rulesets_file" --paginate --slurp \
  "repos/$REPO/rulesets?per_page=100" || exit 1
jq -e '
  type == "array" and
  all(.[]; type == "array") and
  all(.[][];
    type == "object" and
    (.id | type) == "number" and
    (.name | type) == "string" and
    (.target | type) == "string" and
    (.enforcement | type) == "string"
  )
' "$rulesets_file" >/dev/null || stop 'ruleset response had an unexpected schema'
ruleset_rows_file="$TMP_DIR/ruleset-rows.tsv"
jq -r '.[][] | [.id, .name, .target, .enforcement] | @tsv' \
  "$rulesets_file" > "$ruleset_rows_file" || stop 'ruleset rows could not be parsed'

ruleset_count=0
while IFS= read -r row; do
  [[ -n $row ]] || continue
  ruleset_count=$((ruleset_count + 1))
done < "$ruleset_rows_file"
if ((ruleset_count == 0)); then
  printf 'Repository rulesets: none\n'
else
  printf 'Repository rulesets:\n'
  while IFS= read -r row; do
    [[ -n $row ]] && printf '  %s\n' "$row"
  done < "$ruleset_rows_file"
fi

log '[4/6] Inspecting classic dev protection'
classic_file="$TMP_DIR/classic.json"
classic_error="$TMP_DIR/classic.error"
: > "$classic_file"
: > "$classic_error"
"$TIMEOUT_CMD" --foreground 30s gh api "repos/$REPO/branches/dev/protection" \
  >"$classic_file" 2>"$classic_error"
classic_rc=$?
classic_present=0
if ((classic_rc == 0)); then
  jq -e 'type == "object"' "$classic_file" >/dev/null ||
    stop 'classic protection response was invalid JSON'
  classic_present=1
  printf 'Classic dev protection: present\n'
elif grep -Fq 'Branch not protected' "$classic_error"; then
  printf 'Classic dev protection: none\n'
else
  printf 'STOP: could not inspect classic dev protection\n' >&2
  cat "$classic_error" >&2
  exit 1
fi

if ((APPLY == 0)); then
  log '[5/6] Audit only; no settings changed'
  printf '\nRESULT: AUDIT COMPLETE\n'
  printf 'Rulesets found: %d\n' "$ruleset_count"
  if ((classic_present == 1)); then
    printf 'Classic dev protection: present\n'
  else
    printf 'Classic dev protection: none\n'
  fi
  printf 'Warnings: %d\n' "$WARNINGS"
  printf 'Run again with --apply only when you intend to remove these protections.\n'
  exit 0
fi

log '[5/6] Applying explicit release-settings reset'
while IFS=$'\t' read -r ruleset_id ruleset_name ruleset_target ruleset_enforcement; do
  [[ -n $ruleset_id ]] || continue
  [[ $ruleset_id =~ ^[0-9]+$ ]] || stop "invalid ruleset id: $ruleset_id"
  log "Deleting ruleset $ruleset_id ($ruleset_name; $ruleset_target; $ruleset_enforcement)"
  delete_output="$TMP_DIR/delete-ruleset-$ruleset_id.out"
  api_to_file "$delete_output" --method DELETE \
    "repos/$REPO/rulesets/$ruleset_id" || exit 1
done < "$ruleset_rows_file"
if ((classic_present == 1)); then
  log 'Deleting classic protection on dev'
  delete_classic_output="$TMP_DIR/delete-classic.out"
  api_to_file "$delete_classic_output" --method DELETE \
    "repos/$REPO/branches/dev/protection" || exit 1
fi

log '[6/6] Verifying reset'
verify_rulesets="$TMP_DIR/verify-rulesets.json"
api_to_file "$verify_rulesets" --paginate --slurp \
  "repos/$REPO/rulesets?per_page=100" || exit 1
jq -e 'type == "array" and all(.[]; type == "array")' \
  "$verify_rulesets" >/dev/null || stop 'ruleset verification response had an unexpected schema'
remaining=$(jq -er '[.[][]] | length' "$verify_rulesets") ||
  stop 'could not verify ruleset deletion'
((remaining == 0)) || stop "$remaining repository ruleset(s) remain after reset"

verify_classic="$TMP_DIR/verify-classic.json"
verify_classic_error="$TMP_DIR/verify-classic.error"
: > "$verify_classic"
: > "$verify_classic_error"
"$TIMEOUT_CMD" --foreground 30s gh api "repos/$REPO/branches/dev/protection" \
  >"$verify_classic" 2>"$verify_classic_error"
verify_classic_rc=$?
if ((verify_classic_rc == 0)); then
  stop 'classic dev protection still exists after reset'
elif ! grep -Fq 'Branch not protected' "$verify_classic_error"; then
  printf 'STOP: could not verify classic dev protection removal\n' >&2
  cat "$verify_classic_error" >&2
  exit 1
fi

printf '\nRESULT: RESET COMPLETE\n'
printf 'Repository: %s\n' "$REPO"
printf 'Repository rulesets remaining: 0\n'
printf 'Classic dev protection: none\n'
printf 'APK-signing secrets changed: no\n'
printf 'Default Actions token permissions changed: no\n'
printf 'Warnings: %d\n' "$WARNINGS"

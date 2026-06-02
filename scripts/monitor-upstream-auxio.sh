#!/usr/bin/env bash
set -euo pipefail

UPSTREAM_REPO_URL="${UPSTREAM_REPO_URL:-https://github.com/OxygenCobalt/Auxio.git}"
BASELINE_FILE="${BASELINE_FILE:-.github/upstream-auxio-baseline.json}"
REPORT_DIR="${REPORT_DIR:-build/upstream-auxio-monitor}"
GITHUB_REPOSITORY="${GITHUB_REPOSITORY:-}"
GITHUB_RUN_ID="${GITHUB_RUN_ID:-}"
GITHUB_SERVER_URL="${GITHUB_SERVER_URL:-https://github.com}"
GITHUB_EVENT_NAME="${GITHUB_EVENT_NAME:-local}"
UPDATE_BASELINE="${UPDATE_BASELINE:-true}"
DRY_RUN="${DRY_RUN:-false}"
REMOTE_NAME="upstream-auxio"

if [[ -n "${GITHUB_TOKEN:-}" && -z "${GH_TOKEN:-}" ]]; then
  export GH_TOKEN="${GITHUB_TOKEN}"
fi

log() {
  printf '%s\n' "$*"
}

append_summary() {
  if [[ -n "${GITHUB_STEP_SUMMARY:-}" ]]; then
    printf '%s\n' "$*" >> "${GITHUB_STEP_SUMMARY}"
  fi
}

set_output() {
  local name="$1"
  local value="$2"
  if [[ -n "${GITHUB_OUTPUT:-}" ]]; then
    printf '%s=%s\n' "${name}" "${value}" >> "${GITHUB_OUTPUT}"
  fi
}

github_error() {
  printf '::error::%s\n' "$*" >&2
}

fail_with_summary() {
  local message="$1"
  github_error "${message}"
  append_summary "## Upstream Auxio monitor"
  append_summary ""
  append_summary "Monitor failed before creating or updating any upstream report."
  append_summary ""
  append_summary "${message}"
  exit 1
}

json_get() {
  local field="$1"
  local file="$2"
  python3 - "$field" "$file" <<'PY'
import json
import sys
field = sys.argv[1]
path = sys.argv[2]
try:
    with open(path, "r", encoding="utf-8") as handle:
        data = json.load(handle)
except FileNotFoundError:
    data = {}
value = data.get(field, "")
print("" if value is None else value)
PY
}

validate_baseline_json() {
  python3 -m json.tool "${BASELINE_FILE}" >/dev/null
}

write_baseline() {
  local sha="$1"
  local branch="$2"
  local timestamp="$3"
  mkdir -p "$(dirname "${BASELINE_FILE}")"
  python3 - "${BASELINE_FILE}" "${UPSTREAM_REPO_URL}" "${branch}" "${sha}" "${timestamp}" <<'PY'
import json
import os
import sys
path, url, branch, sha, timestamp = sys.argv[1:]
try:
    with open(path, "r", encoding="utf-8") as handle:
        data = json.load(handle)
except (FileNotFoundError, ValueError):
    data = {}
data["upstream_repository"] = "OxygenCobalt/Auxio"
data["upstream_url"] = url
data["upstream_branch"] = branch
data["last_seen_sha"] = sha
data["last_seen_at"] = timestamp
data["notes"] = "Updated only when upstream is first baselined or after an upstream-change issue is created/acknowledged."
tmp = f"{path}.tmp"
with open(tmp, "w", encoding="utf-8") as handle:
    json.dump(data, handle, indent=2)
    handle.write("\n")
os.replace(tmp, path)
PY
  validate_baseline_json
}

is_full_commit_sha() {
  [[ "$1" =~ ^[0-9a-fA-F]{40}$ ]]
}

ensure_baseline_sha_is_reachable() {
  local sha="$1"

  if [[ -z "${sha}" ]]; then
    return 0
  fi

  if ! is_full_commit_sha "${sha}"; then
    fail_with_summary "Baseline last_seen_sha '${sha}' is not a full 40-character git SHA. Reseed ${BASELINE_FILE} with a valid upstream ${branch} commit SHA, then rerun the monitor."
  fi

  if git cat-file -e "${sha}^{commit}" >/dev/null 2>&1; then
    return 0
  fi

  log "Baseline SHA ${sha} was not present after fetching ${REMOTE_NAME}/${branch}; attempting a direct best-effort fetch."
  git fetch --no-tags "${REMOTE_NAME}" "${sha}" >/dev/null 2>&1 || true

  if git cat-file -e "${sha}^{commit}" >/dev/null 2>&1; then
    return 0
  fi

  fail_with_summary "Baseline SHA ${sha} was not found in fetched upstream history for ${UPSTREAM_REPO_URL} (${branch}). Reseed ${BASELINE_FILE} with a reachable upstream commit SHA; see docs/UPSTREAM_AUXIO_MONITORING.md."
}

resolve_default_branch() {
  git ls-remote --symref "${UPSTREAM_REPO_URL}" HEAD | awk '/^ref:/ { sub("refs/heads/", "", $2); print $2; exit }'
}

remove_remote() {
  if git remote get-url "${REMOTE_NAME}" >/dev/null 2>&1; then
    git remote remove "${REMOTE_NAME}" >/dev/null 2>&1 || true
  fi
}
trap remove_remote EXIT

if [[ "${DRY_RUN}" != "true" && "${DRY_RUN}" != "false" ]]; then
  echo "::error::DRY_RUN must be true or false, got ${DRY_RUN}."
  exit 1
fi
if [[ "${UPDATE_BASELINE}" != "true" && "${UPDATE_BASELINE}" != "false" ]]; then
  echo "::error::UPDATE_BASELINE must be true or false, got ${UPDATE_BASELINE}."
  exit 1
fi

set_output "upstream_changed" "false"
set_output "reports_generated" "false"
set_output "baseline_updated" "false"
set_output "issue_created" "false"
set_output "no_changes" "false"
set_output "issue_url" ""

baseline_branch=""
old_sha=""
if [[ -f "${BASELINE_FILE}" ]]; then
  validate_baseline_json
  baseline_branch="$(json_get upstream_branch "${BASELINE_FILE}")"
  old_sha="$(json_get last_seen_sha "${BASELINE_FILE}")"
fi

branch="${UPSTREAM_BRANCH:-${baseline_branch}}"
if [[ -z "${branch}" ]]; then
  log "Resolving upstream default branch from ${UPSTREAM_REPO_URL}."
  branch="$(resolve_default_branch)"
fi
if [[ -z "${branch}" ]]; then
  echo "::error::Unable to resolve upstream branch for ${UPSTREAM_REPO_URL}."
  exit 1
fi

log "Monitoring upstream Auxio repository: ${UPSTREAM_REPO_URL}"
log "Upstream branch: ${branch}"
log "Baseline file: ${BASELINE_FILE}"

remove_remote
git remote add "${REMOTE_NAME}" "${UPSTREAM_REPO_URL}"
git fetch --no-tags --prune "${REMOTE_NAME}" "+refs/heads/${branch}:refs/remotes/${REMOTE_NAME}/${branch}"
new_sha="$(git rev-parse "refs/remotes/${REMOTE_NAME}/${branch}")"
ensure_baseline_sha_is_reachable "${old_sha}"
old_short="${old_sha:0:12}"
new_short="${new_sha:0:12}"

workflow_run_url=""
if [[ -n "${GITHUB_REPOSITORY}" && -n "${GITHUB_RUN_ID}" ]]; then
  workflow_run_url="${GITHUB_SERVER_URL}/${GITHUB_REPOSITORY}/actions/runs/${GITHUB_RUN_ID}"
fi

if [[ -z "${old_sha}" ]]; then
  log "No upstream baseline SHA is recorded. Initialising silently to ${new_sha}."
  append_summary "## Upstream Auxio monitor"
  append_summary ""
  append_summary "Initialised the stored upstream baseline silently. No issue was created."
  append_summary ""
  append_summary "- Upstream: ${UPSTREAM_REPO_URL}"
  append_summary "- Branch: ${branch}"
  append_summary "- Baseline SHA: ${new_sha}"
  if [[ "${DRY_RUN}" == "true" ]]; then
    log "DRY_RUN=true: baseline file was not updated."
  else
    timestamp="$(date -u +%Y-%m-%dT%H:%M:%SZ)"
    write_baseline "${new_sha}" "${branch}" "${timestamp}"
    set_output "baseline_updated" "true"
  fi
  exit 0
fi

if [[ "${old_sha}" == "${new_sha}" ]]; then
  set_output "no_changes" "true"
  log "No upstream changes detected: ${old_sha} == ${new_sha}."
  append_summary "## Upstream Auxio monitor"
  append_summary ""
  append_summary "No upstream changes detected."
  append_summary ""
  append_summary "- Upstream: ${UPSTREAM_REPO_URL}"
  append_summary "- Branch: ${branch}"
  append_summary "- Baseline SHA: ${old_sha}"
  append_summary "- Current upstream SHA: ${new_sha}"
  append_summary ""
  append_summary "No issue, comments, PRs, artifacts, commits, or timestamp updates were produced."
  exit 0
fi

set_output "upstream_changed" "true"
set_output "reports_generated" "true"
mkdir -p "${REPORT_DIR}"
commits_file="${REPORT_DIR}/upstream-auxio-commits.txt"
files_file="${REPORT_DIR}/upstream-auxio-files.txt"
stat_file="${REPORT_DIR}/upstream-auxio-stat.txt"
diff_file="${REPORT_DIR}/upstream-auxio.diff"
patch_file="${REPORT_DIR}/upstream-auxio.patch"
summary_file="${REPORT_DIR}/upstream-auxio-summary.md"
issue_body_file="${REPORT_DIR}/upstream-auxio-issue-body.md"

log "Upstream changes detected: ${old_sha}..${new_sha}. Generating reports in ${REPORT_DIR}."
git log --oneline --decorate "${old_sha}..${new_sha}" > "${commits_file}"
git diff --stat "${old_sha}..${new_sha}" > "${stat_file}"
git diff --name-status "${old_sha}..${new_sha}" > "${files_file}"
git diff --find-renames "${old_sha}..${new_sha}" > "${diff_file}"
git format-patch --stdout "${old_sha}..${new_sha}" > "${patch_file}"
commit_count="$(git rev-list --count "${old_sha}..${new_sha}")"

artifact_note="Workflow artifacts: upstream-auxio-monitor-report (contains summary, commit list, changed files, diff, and patch)."
trigger_note="${GITHUB_EVENT_NAME}"

{
  echo "# Upstream Auxio monitor report"
  echo
  echo "## Summary"
  echo
  echo "- Upstream repo URL: ${UPSTREAM_REPO_URL}"
  echo "- Upstream branch: ${branch}"
  echo "- Old baseline SHA: ${old_sha}"
  echo "- New upstream SHA: ${new_sha}"
  echo "- Comparison range: ${old_sha}..${new_sha}"
  if [[ -n "${workflow_run_url}" ]]; then
    echo "- Workflow run URL: ${workflow_run_url}"
  fi
  echo "- Trigger: ${trigger_note}"
  echo "- Commit count: ${commit_count}"
  echo "- Report directory: ${REPORT_DIR}"
  echo "- Artifact name: upstream-auxio-monitor-report"
  echo
  echo "## Commit list"
  echo
  echo '```text'
  cat "${commits_file}"
  echo '```'
  echo
  echo "## Changed files"
  echo
  echo '```text'
  cat "${files_file}"
  echo '```'
  echo
  echo "## Diff stat"
  echo
  echo '```text'
  cat "${stat_file}"
  echo '```'
} > "${summary_file}"

inline_diff=""
if [[ "$(wc -c < "${diff_file}")" -le 45000 ]]; then
  inline_diff="true"
else
  inline_diff="false"
fi

{
  cat "${summary_file}"
  echo
  echo "## Full diff location"
  echo
  if [[ "${inline_diff}" == "true" ]]; then
    echo "Full diff is included below and is also available in the workflow artifact."
    echo
    echo '```diff'
    cat "${diff_file}"
    echo '```'
  else
    echo "The full diff is too large for a concise issue body. ${artifact_note}"
    if [[ -n "${workflow_run_url}" ]]; then
      echo
      echo "Workflow run: ${workflow_run_url}"
    fi
  fi
  cat <<'MARKDOWN'

## Agent task: adapt upstream Auxio changes to Auxio-TS

Please review the upstream Auxio changes in this issue and determine which changes should be adapted into Auxio-TS.

Do not blindly sync or apply upstream patches. Auxio-TS is a Topway/DoFun/TS18-focused variant and has deliberate downstream changes.

Start by classifying each upstream change as:

- directly applicable;
- applicable with Auxio-TS adaptation;
- already present in Auxio-TS;
- superseded by Auxio-TS-specific implementation;
- conflicts with TS18/Topway/DoFun requirements;
- not relevant.

Preserve these Auxio-TS invariants:

- standard package remains `org.oxycblt.auxio`;
- Topway/DoFun package variants and wrapper source sets remain intact;
- TS18 runtime/device docs and validation flows remain accurate;
- release workflows continue to build the intended standard and Topway APKs;
- private/native Topway surfaces remain evidence-gated only;
- no automatic fork sync, merge, or cross-repo PR behaviour.

Implementation requirements:

1. Inspect the upstream commits and file diffs.
2. Inspect the corresponding Auxio-TS files before editing.
3. Apply only safe, intentional changes.
4. Adapt conflicts to Auxio-TS architecture instead of overwriting local behaviour.
5. Update tests, docs, scripts, and workflows when affected.
6. Run relevant checks.
7. Document intentionally skipped upstream changes and why.

Final response must include:

- upstream range reviewed;
- changes applied;
- changes intentionally skipped;
- files changed;
- checks run and results;
- remaining manual validation.

## Safety notes

- This issue is a monitoring report only.
- No upstream code was merged automatically.
- No PR was created automatically.
- The baseline is advanced only after this upstream head is reported or acknowledged, to avoid duplicate monitoring issues.
- Implementation still requires human/agent review.
MARKDOWN
} > "${issue_body_file}"

append_summary "## Upstream Auxio monitor"
append_summary ""
append_summary "Upstream changes detected."
append_summary ""
append_summary "- Upstream: ${UPSTREAM_REPO_URL}"
append_summary "- Branch: ${branch}"
append_summary "- Range: ${old_sha}..${new_sha}"
append_summary "- Commit count: ${commit_count}"
append_summary "- Reports: ${REPORT_DIR}"

if [[ "${DRY_RUN}" == "true" ]]; then
  log "DRY_RUN=true: no issue will be created and baseline will not be updated."
  append_summary "- Dry run: no issue created; baseline unchanged."
  append_summary "- Issue body preview: ${issue_body_file}"
  exit 0
fi

if [[ -z "${GITHUB_REPOSITORY}" ]]; then
  echo "::error::GITHUB_REPOSITORY is required to search/create issues when upstream changed."
  exit 1
fi
if ! command -v gh >/dev/null 2>&1; then
  echo "::error::GitHub CLI (gh) is required to search/create issues when upstream changed."
  exit 1
fi
if [[ -z "${GH_TOKEN:-}" ]]; then
  echo "::error::GITHUB_TOKEN or GH_TOKEN is required to search/create issues when upstream changed."
  exit 1
fi

duplicate_json="$(gh issue list --repo "${GITHUB_REPOSITORY}" --state open --limit 100 --search "\"${new_sha}\" in:title,body repo:${GITHUB_REPOSITORY}" --json number,title,url)"
duplicate_url="$(printf '%s' "${duplicate_json}" | python3 -c 'import json, sys
try:
    items = json.load(sys.stdin)
except Exception:
    items = []
print(items[0].get("url", "") if items else "")')"

reported="false"
issue_url=""
if [[ -n "${duplicate_url}" ]]; then
  issue_url="${duplicate_url}"
  reported="true"
  log "An open issue already reports upstream SHA ${new_sha}: ${issue_url}"
  append_summary "- Existing issue: ${issue_url}"
else
  title="Upstream Auxio monitor: OxygenCobalt/Auxio changed ${old_short}..${new_short}"
  label_csv=""
  existing_labels="$(gh label list --repo "${GITHUB_REPOSITORY}" --limit 1000 --json name --jq '.[].name' || true)"
  for label in upstream auxio-sync agent-ready; do
    if printf '%s\n' "${existing_labels}" | grep -Fxq "${label}"; then
      if [[ -z "${label_csv}" ]]; then
        label_csv="${label}"
      else
        label_csv="${label_csv},${label}"
      fi
    fi
  done
  if [[ -n "${label_csv}" ]]; then
    issue_url="$(gh issue create --repo "${GITHUB_REPOSITORY}" --title "${title}" --body-file "${issue_body_file}" --label "${label_csv}" | tail -n 1)"
  else
    issue_url="$(gh issue create --repo "${GITHUB_REPOSITORY}" --title "${title}" --body-file "${issue_body_file}" | tail -n 1)"
  fi
  reported="true"
  set_output "issue_created" "true"
  log "Created upstream monitor issue: ${issue_url}"
  append_summary "- Created issue: ${issue_url}"
fi
set_output "issue_url" "${issue_url}"

if [[ "${reported}" == "true" && "${UPDATE_BASELINE}" == "true" ]]; then
  timestamp="$(date -u +%Y-%m-%dT%H:%M:%SZ)"
  write_baseline "${new_sha}" "${branch}" "${timestamp}"
  set_output "baseline_updated" "true"
  log "Updated baseline to ${new_sha}."
elif [[ "${UPDATE_BASELINE}" != "true" ]]; then
  log "UPDATE_BASELINE=false: baseline not updated after reporting upstream change."
  append_summary "- Baseline update: skipped because UPDATE_BASELINE=false."
fi

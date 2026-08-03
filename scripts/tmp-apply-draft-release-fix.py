#!/usr/bin/env python3
from pathlib import Path

WORKFLOW = Path('.github/workflows/manual-release.yml')
CHECKER = Path('scripts/check-manual-release-workflow.sh')


def fail(message: str) -> None:
    raise SystemExit(message)


def unique_index(lines: list[str], value: str, label: str, start: int = 0) -> int:
    matches = [index for index in range(start, len(lines)) if lines[index] == value]
    if len(matches) != 1:
        fail(f'{label}: expected one line match, found {len(matches)}')
    return matches[0]


def next_index(lines: list[str], value: str, label: str, start: int) -> int:
    for index in range(start, len(lines)):
        if lines[index] == value:
            return index
    fail(f'{label}: line not found after index {start}')


workflow_lines = WORKFLOW.read_text(encoding='utf-8').splitlines()

# Replace published-only tag lookup with authenticated draft-aware ID lookup.
plan_start = unique_index(
    workflow_lines,
    '          git tag --list > "${git_tags}"',
    'release planning start',
)
old_lookup = unique_index(
    workflow_lines,
    '          if gh api "repos/${GITHUB_REPOSITORY}/releases/tags/${release_tag}" > "${target_release}" 2>/dev/null; then',
    'published-only release lookup',
    start=plan_start,
)
plan_end = next_index(workflow_lines, '          fi', 'release planning end', old_lookup)
new_plan = '''          git tag --list > "${git_tags}"
          # List releases includes authenticated draft, prerelease and published releases.
          # Keep an ID/tag index so draft lookup never uses the published-only tag endpoint.
          release_index="${RUNNER_TEMP}/github-releases.tsv"
          timeout 60s gh api --paginate "repos/${GITHUB_REPOSITORY}/releases?per_page=100" \\
            --jq '.[] | [.id, .tag_name] | @tsv' > "${release_index}"
          cut -f2 "${release_index}" > "${release_tags}"
          printf '{}\\n' > "${target_release}"

          python3 "${TOOL}" resolve \\
            --mode "${RELEASE_MODE}" \\
            --input-tag "${INPUT_TAG}" \\
            --source-gradle app/build.gradle \\
            --git-tags-file "${git_tags}" \\
            --release-tags-file "${release_tags}" \\
            --target-release-json "${target_release}" \\
            --output "${preliminary}"

          release_tag="$(jq -r .release_tag "${preliminary}")"
          release_ids="${RUNNER_TEMP}/target-release-ids.txt"
          awk -F '\\t' -v tag="${release_tag}" '$2 == tag { print $1 }' \\
            "${release_index}" > "${release_ids}"
          release_count="$(awk 'NF { count += 1 } END { print count + 0 }' "${release_ids}")"
          case "${release_count}" in
            0)
              printf '{}\\n' > "${target_release}"
              ;;
            1)
              release_id="$(cat "${release_ids}")"
              [[ "${release_id}" =~ ^[0-9]+$ ]] || {
                echo "::error::Release ${release_tag} resolved to invalid ID ${release_id}."
                exit 1
              }
              timeout 60s gh api "repos/${GITHUB_REPOSITORY}/releases/${release_id}" > "${target_release}"
              jq -e --arg tag "${release_tag}" --arg id "${release_id}" \\
                '(.id == ($id | tonumber)) and (.tag_name == $tag)' \\
                "${target_release}" >/dev/null || {
                  echo "::error::Release ID ${release_id} did not resolve back to ${release_tag}."
                  exit 1
                }
              echo "Found existing GitHub Release ${release_tag} (ID ${release_id})."
              ;;
            *)
              echo "::error::Multiple GitHub Releases unexpectedly use tag ${release_tag}."
              exit 1
              ;;
          esac'''.splitlines()
workflow_lines[plan_start : plan_end + 1] = new_plan

# Create drafts with REST and retain the returned release ID. Existing drafts use
# the exact object already resolved during planning.
release_step = unique_index(
    workflow_lines,
    '      - name: Ensure draft release transaction exists',
    'draft release step',
)
run_line = next_index(workflow_lines, '        run: |', 'draft release run block', release_step)
env_insert = next_index(
    workflow_lines,
    '          DEBUG_DESTINATION: ${{ steps.selection.outputs.debug_destination }}',
    'draft release environment',
    release_step,
)
if workflow_lines[env_insert + 1] != '        run: |':
    fail('draft release environment layout changed unexpectedly')
workflow_lines.insert(
    env_insert + 1,
    '          TARGET_RELEASE_FILE: ${{ steps.plan.outputs.target_release_file }}',
)
run_line += 1
created_line = next_index(workflow_lines, '          created=false', 'draft release created flag', run_line)
output_block = next_index(workflow_lines, '          {', 'draft release output block', created_line + 1)
# Select the output block after the old tag lookup, not the notes block.
while output_block < len(workflow_lines) and not (
    output_block > created_line
    and workflow_lines[output_block - 1]
    == '          gh api "repos/${GITHUB_REPOSITORY}/releases/tags/${RELEASE_TAG}" > "${release_json}"'
):
    output_block = next_index(
        workflow_lines,
        '          {',
        'draft release output block after lookup',
        output_block + 1,
    )
new_release = '''          created=false
          release_json="${RUNNER_TEMP}/active-release.json"
          if [[ "${RELEASE_EXISTS}" != true ]]; then
            notes="${RUNNER_TEMP}/release-notes.md"
            {
              echo "## What's changed"
              echo
              if [[ -n "${PREVIOUS_TAG}" ]]; then
                git log --reverse --pretty='- %s' "${PREVIOUS_TAG}..${RELEASE_TAG}"
              else
                echo '- Initial maintained Topway release.'
              fi
              echo
              echo '## Assets'
              echo '- topwayTwMedia is the primary signed APK.'
              echo '- The LSPosed API 100 bridge is the signed TS18 addon for the genuine stock `com.tw.music` process.'
              if [[ "${DEBUG_DESTINATION}" == release_assets ]]; then
                echo '- Debug APKs were explicitly selected as GitHub Release assets.'
                echo '- Debug APKs are diagnostic companions with separate application IDs; they are not recommended normal installs.'
              else
                echo '- Debug APKs remain short-lived workflow artifacts and are not GitHub Release assets.'
              fi
              echo '- The former exact-package Auxio Magisk overlay is retired and is not published.'
              echo '- Exact TS18 runtime behaviour requires device validation.'
            } > "${notes}"
            # The create endpoint returns the draft object, including its stable release ID.
            # Do not query the published-only /releases/tags/{tag} endpoint afterwards.
            timeout 60s gh api --method POST "repos/${GITHUB_REPOSITORY}/releases" \\
              -f tag_name="${RELEASE_TAG}" \\
              -f target_commitish="${RELEASE_TAG}" \\
              -f name="${RELEASE_TAG}" \\
              -F body=@"${notes}" \\
              -F draft=true \\
              -F prerelease=false > "${release_json}"
            created=true
          else
            [[ -s "${TARGET_RELEASE_FILE}" ]] || {
              echo "::error::Existing release state file is missing for ${RELEASE_TAG}."
              exit 1
            }
            cp "${TARGET_RELEASE_FILE}" "${release_json}"
          fi
          jq -e --arg tag "${RELEASE_TAG}" '
            (.id | type == "number") and
            (.tag_name == $tag) and
            (.html_url | type == "string" and length > 0) and
            (.draft | type == "boolean") and
            (.prerelease | type == "boolean")
          ' "${release_json}" >/dev/null || {
            echo "::error::Active release object is invalid or does not match ${RELEASE_TAG}."
            exit 1
          }'''.splitlines()
workflow_lines[created_line:output_block] = new_release

# Verify the exact draft or published release by stable ID.
verify_step = unique_index(
    workflow_lines,
    '      - name: Verify remote release asset manifest',
    'remote verification step',
)
verify_tag = next_index(
    workflow_lines,
    '          RELEASE_TAG: ${{ steps.plan.outputs.release_tag }}',
    'verification tag environment',
    verify_step,
)
workflow_lines.insert(
    verify_tag + 1,
    '          RELEASE_ID: ${{ steps.release.outputs.release_id }}',
)
old_verify = next_index(
    workflow_lines,
    '          gh api "repos/${GITHUB_REPOSITORY}/releases/tags/${RELEASE_TAG}" > "${remote_json}"',
    'published-only verification lookup',
    verify_step,
)
new_verify = '''          [[ "${RELEASE_ID}" =~ ^[0-9]+$ ]] || {
            echo "::error::Release ID is missing or invalid for ${RELEASE_TAG}."
            exit 1
          }
          timeout 60s gh api "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}" > "${remote_json}"
          jq -e --arg tag "${RELEASE_TAG}" --arg id "${RELEASE_ID}" \\
            '(.id == ($id | tonumber)) and (.tag_name == $tag)' \\
            "${remote_json}" >/dev/null || {
              echo "::error::Remote release ID ${RELEASE_ID} no longer matches ${RELEASE_TAG}."
              exit 1
            }'''.splitlines()
workflow_lines[old_verify : old_verify + 1] = new_verify

workflow = '\n'.join(workflow_lines) + '\n'
if '/releases/tags/' in workflow:
    fail('published-only release tag endpoint remains after patch')
WORKFLOW.write_text(workflow, encoding='utf-8')

checker = CHECKER.read_text(encoding='utf-8')
replacements = [
    (
        "    'github.actor == github.repository_owner',\n",
        "    'github.actor == github.repository_owner',\n    '/releases/tags/',\n",
        'published-only endpoint regression guard',
    ),
    (
        "    'gh api --paginate \"repos/${GITHUB_REPOSITORY}/releases?per_page=100\"',\n    \"--jq '.[].tag_name'\",\n",
        "    'timeout 60s gh api --paginate \"repos/${GITHUB_REPOSITORY}/releases?per_page=100\"',\n    \"--jq '.[] | [.id, .tag_name] | @tsv'\",\n",
        'draft-aware listing contract',
    ),
    (
        "    '--draft',\n",
        "    'gh api --method POST \"repos/${GITHUB_REPOSITORY}/releases\"',\n    '-F draft=true',\n    'TARGET_RELEASE_FILE: ${{ steps.plan.outputs.target_release_file }}',\n    'repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}',\n",
        'draft creation and ID contract',
    ),
]
for old, new, label in replacements:
    count = checker.count(old)
    if count != 1:
        fail(f'{label}: expected one checker match, found {count}')
    checker = checker.replace(old, new, 1)
CHECKER.write_text(checker, encoding='utf-8')

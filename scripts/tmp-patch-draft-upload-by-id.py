#!/usr/bin/env python3
from pathlib import Path

workflow_path = Path('.repair-staging/manual-release.yml')
checker_path = Path('.repair-staging/check-manual-release-workflow.sh')
workflow = workflow_path.read_text(encoding='utf-8')
checker = checker_path.read_text(encoding='utf-8')

start_marker = '      - name: Upload or replace planned release assets\n'
end_marker = '      - name: Verify remote release asset manifest\n'
start = workflow.find(start_marker)
end = workflow.find(end_marker, start + len(start_marker))
if start < 0 or end < 0 or workflow.find(start_marker, start + 1) >= 0:
    raise SystemExit('Unable to isolate the release asset upload step exactly once.')

new_upload = '''      - name: Upload or replace planned release assets
        shell: bash
        env:
          GH_TOKEN: ${{ github.token }}
          RELEASE_TAG: ${{ steps.plan.outputs.release_tag }}
          RELEASE_ID: ${{ steps.release.outputs.release_id }}
          UPLOAD_TSV: ${{ steps.assets.outputs.upload_tsv }}
          REPLACE: ${{ inputs.replace_existing_assets }}
        run: |
          set -euo pipefail
          if [[ ! -s "${UPLOAD_TSV}" ]]; then
            echo 'No remote asset upload is required; selected triplets are already complete.'
            exit 0
          fi
          [[ "${RELEASE_ID}" =~ ^[0-9]+$ ]] || {
            echo "::error::Release ID is missing or invalid for ${RELEASE_TAG}."
            exit 1
          }

          duplicate_names="${RUNNER_TEMP}/duplicate-upload-asset-names.txt"
          cut -f1 "${UPLOAD_TSV}" | awk 'NF' | sort | uniq -d > "${duplicate_names}"
          [[ ! -s "${duplicate_names}" ]] || {
            echo '::error::The upload plan contains duplicate asset names.'
            cat "${duplicate_names}" >&2
            exit 1
          }

          remote_json="${RUNNER_TEMP}/upload-release.json"
          timeout 60s gh api "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}" > "${remote_json}"
          jq -e --arg tag "${RELEASE_TAG}" --arg id "${RELEASE_ID}" \\
            '(.id == ($id | tonumber)) and (.tag_name == $tag)' \\
            "${remote_json}" >/dev/null || {
              echo "::error::Upload release ID ${RELEASE_ID} does not match ${RELEASE_TAG}."
              exit 1
            }
          upload_template="$(jq -r .upload_url "${remote_json}")"
          upload_base="${upload_template%%\{*}"
          [[ "${upload_base}" == https://* && "${upload_base}" == */releases/${RELEASE_ID}/assets ]] || {
            echo "::error::Release ${RELEASE_ID} returned an invalid upload URL."
            exit 1
          }

          while IFS=$'\\t' read -r name path; do
            [[ -n "${name}" ]] || continue
            [[ -f "${path}" ]] || {
              echo "::error::Staged upload path is missing for ${name}: ${path}"
              exit 1
            }

            mapfile -t existing_ids < <(
              jq -r --arg name "${name}" '.assets[] | select(.name == $name) | .id' "${remote_json}"
            )
            ((${#existing_ids[@]} <= 1)) || {
              echo "::error::Release ${RELEASE_ID} contains duplicate assets named ${name}."
              exit 1
            }
            if ((${#existing_ids[@]} == 1)); then
              existing_id="${existing_ids[0]}"
              [[ "${existing_id}" =~ ^[0-9]+$ ]] || {
                echo "::error::Asset ${name} resolved to invalid ID ${existing_id}."
                exit 1
              }
              [[ "${REPLACE}" == true ]] || {
                echo "::error::Asset ${name} already exists; explicit replacement was not selected."
                exit 1
              }
              timeout 60s gh api --method DELETE \\
                "repos/${GITHUB_REPOSITORY}/releases/assets/${existing_id}"
            fi

            encoded_name="$(jq -rn --arg value "${name}" '$value | @uri')"
            response="${RUNNER_TEMP}/uploaded-${encoded_name}.json"
            curl --fail-with-body --silent --show-error \\
              --connect-timeout 15 \\
              --max-time 300 \\
              --request POST \\
              --header 'Accept: application/vnd.github+json' \\
              --header "Authorization: Bearer ${GH_TOKEN}" \\
              --header 'X-GitHub-Api-Version: 2022-11-28' \\
              --header 'Content-Type: application/octet-stream' \\
              --data-binary "@${path}" \\
              "${upload_base}?name=${encoded_name}&label=${encoded_name}" > "${response}"
            jq -e --arg name "${name}" \\
              '(.id | type == "number") and (.name == $name) and (.state == "uploaded")' \\
              "${response}" >/dev/null || {
                echo "::error::GitHub returned an invalid upload result for ${name}."
                exit 1
              }
          done < "${UPLOAD_TSV}"

'''
workflow = workflow[:start] + new_upload + workflow[end:]
workflow = workflow.replace(
    '          gh api --method PATCH "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}" \\\n',
    '          timeout 60s gh api --method PATCH "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}" \\\n',
    1,
)
if 'gh release upload' in workflow:
    raise SystemExit('Tag-based gh release upload remains after patch.')
if workflow.count('timeout 60s gh api --method PATCH') != 1:
    raise SystemExit('Final release status update was not bounded exactly once.')
workflow_path.write_text(workflow, encoding='utf-8')

forbidden_anchor = "    '/releases/tags/',\n"
if checker.count(forbidden_anchor) != 1:
    raise SystemExit('Checker forbidden-token anchor changed unexpectedly.')
checker = checker.replace(
    forbidden_anchor,
    forbidden_anchor + "    'gh release upload',\n",
    1,
)
required_old = "    'Upload or replace planned release assets',\n    '--clobber',\n    'Verify remote release asset manifest',\n"
required_new = "    'Upload or replace planned release assets',\n    'RELEASE_ID: ${{ steps.release.outputs.release_id }}',\n    'repos/${GITHUB_REPOSITORY}/releases/assets/${existing_id}',\n    '--connect-timeout 15',\n    '--max-time 300',\n    '--data-binary \"@${path}\"',\n    'Verify remote release asset manifest',\n"
if checker.count(required_old) != 1:
    raise SystemExit('Checker upload contract anchor changed unexpectedly.')
checker = checker.replace(required_old, required_new, 1)
patch_token = 'timeout 60s gh api --method PATCH "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}"'
required_end = "    'persist-credentials: false',\n"
if checker.count(required_end) != 1:
    raise SystemExit('Checker required-token tail changed unexpectedly.')
checker = checker.replace(required_end, required_end + f"    '{patch_token}',\n", 1)
checker_path.write_text(checker, encoding='utf-8')

#!/usr/bin/env bash
set -euo pipefail
timeout 120s git fetch origin dev
remote_dev="$(git rev-parse origin/dev)"
status='blocked'
if [[ "${remote_dev}" == "${RELEASE_SHA}" ]] || git merge-base --is-ancestor "${RELEASE_SHA}" "${remote_dev}"; then
  status='already_synced'
elif [[ "$(git rev-parse "${RELEASE_SHA}^")" == "${remote_dev}" ]]; then
  timeout 30s gh auth setup-git
  if timeout 120s git push origin "${RELEASE_SHA}:refs/heads/dev"; then
    status='fast_forwarded'
  else
    echo "::warning::Dev metadata push did not return success; checking the remote postcondition."
    if timeout 120s git fetch origin dev; then
      remote_after_failure="$(git rev-parse origin/dev)"
      if [[ "${remote_after_failure}" == "${RELEASE_SHA}" ]] ||
        git merge-base --is-ancestor "${RELEASE_SHA}" "${remote_after_failure}"; then
        status='fast_forwarded'
      else
        echo "::warning::Release assets are verified, but the protected dev metadata fast-forward was rejected."
        status='push_rejected'
      fi
    else
      echo "::warning::Release assets are verified, but the failed dev push postcondition could not be read."
      status='push_rejected'
    fi
  fi
else
  echo "::warning::Release assets are verified, but dev moved and cannot be fast-forwarded to the release metadata commit."
  status='dev_moved'
fi
echo "status=${status}" >> "${GITHUB_OUTPUT}"


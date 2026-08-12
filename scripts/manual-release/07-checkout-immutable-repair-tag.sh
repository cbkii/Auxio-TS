#!/usr/bin/env bash
set -euo pipefail
tag_sha="$(git rev-list -n1 "${RELEASE_TAG}")"
[[ -n "${tag_sha}" ]] || {
  echo "::error::Repair tag ${RELEASE_TAG} cannot be resolved."
  exit 1
}
git checkout --detach "${tag_sha}"
[[ "$(git rev-parse HEAD)" == "${tag_sha}" ]] || {
  echo "::error::Repair checkout does not match immutable tag ${RELEASE_TAG}."
  exit 1
}


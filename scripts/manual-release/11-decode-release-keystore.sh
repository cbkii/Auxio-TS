set -euo pipefail
for name in KEYSTORE_BASE64 KEYSTORE_PASSWORD KEY_ALIAS KEY_PASSWORD; do
  [[ -n "${!name:-}" ]] || {
    echo "::error::Missing signing secret ${name}."
    exit 1
  }
done
echo "::add-mask::${KEYSTORE_PASSWORD}"
echo "::add-mask::${KEY_ALIAS}"
echo "::add-mask::${KEY_PASSWORD}"
path="${RUNNER_TEMP}/release.keystore"
printf '%s' "${KEYSTORE_BASE64}" | base64 --decode > "${path}"
chmod 600 "${path}"
echo "path=${path}" >> "${GITHUB_OUTPUT}"

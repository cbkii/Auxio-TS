set -euo pipefail
[[ -x scripts/bootstrap-dependencies.sh ]] || {
  echo "::error::Selected immutable source lacks scripts/bootstrap-dependencies.sh; safe rebuild is unsupported."
  exit 1
}
bash ./scripts/bootstrap-dependencies.sh --profile release

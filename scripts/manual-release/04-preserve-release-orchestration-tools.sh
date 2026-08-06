set -euo pipefail
tools_dir="${RUNNER_TEMP}/release-tools"
mkdir -p "${tools_dir}/lib"
cp scripts/release-orchestrator.py "${tools_dir}/release-orchestrator.py"
cp scripts/lib/apksigner-certificate.sh "${tools_dir}/lib/apksigner-certificate.sh"
chmod +x "${tools_dir}/release-orchestrator.py"
python3 "${tools_dir}/release-orchestrator.py" self-test
echo "tools_dir=${tools_dir}" >> "${GITHUB_OUTPUT}"

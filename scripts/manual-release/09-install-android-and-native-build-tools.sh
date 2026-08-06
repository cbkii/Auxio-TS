set -euo pipefail
timeout 20m sdkmanager --install platform-tools 'build-tools;36.0.0' 'platforms;android-36' 'ndk;28.2.13676358'
if ! command -v ninja >/dev/null 2>&1; then
  timeout 10m sudo apt-get update
  timeout 10m sudo apt-get install -y ninja-build
fi

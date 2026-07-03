timeout 720s ./gradlew --no-daemon --console=plain :app:testStandardDebugUnitTest
timeout 180s bash scripts/check-dofun-topway-compat.sh
timeout 180s bash scripts/check-headunit-compat-safety.sh
timeout 180s bash scripts/check-ts18-apk-reference-contracts.sh

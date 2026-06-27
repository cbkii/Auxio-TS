bash ./scripts/bootstrap-dependencies.sh --profile jvm-tests
./gradlew --no-daemon --console=plain --stacktrace :app:testStandardDebugUnitTest :musikr:testDebugUnitTest

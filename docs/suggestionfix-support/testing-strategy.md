# Testing strategy notes for Jules

**Observed in external docs:** Android local unit tests run on the JVM and are fast. They are suitable for logic with minimal Android framework dependence, but direct Android framework calls require fakes, mocks, Robolectric, or instrumentation.

**Observed in external docs:** Local tests should generally exercise public APIs and use fakes/test doubles for dependencies.

## High-confidence JVM/local test candidates

- `PlaybackUtil.kt` conversion and formatting helpers.
- `M3U.kt` relative path/backtracking logic.
- `BetterShuffleOrder.kt` clone/traversal behaviour.
- Any extracted QueueAdapter affected-range helper.
- Root path validation / command-generation helper if root command surface is narrowed.
- `StartupLibraryPolicy` regressions if touched.
- `FilteredFS` regressions if touched.

## Medium-confidence tests

- `MediaSessionInterface` query failure handling, if fakes for library/context/commands already exist.
- `PlaybackViewModel` play/shuffle actions, if command factory and playback manager fakes already exist.
- `PersistenceRepository`, if database or storage fakes exist or can be added without heavy framework dependencies.
- `SystemPlaybackReceiver` and `MediaButtonReceiver`, likely with Robolectric or existing receiver test helpers.

## Avoid

- New heavy dependencies just to satisfy a suggestion.
- Flaky tests that require physical TS18 hardware.
- Claims of TS18 runtime compatibility from JVM/unit tests.
- Broad snapshot tests that fail on unrelated UI text changes.

## Validation wording

If full Gradle tests cannot run, Jules should report exact blocker labels such as `SDK_BLOCKER`, `NDK_BLOCKER`, `SUBMODULE_BLOCKER`, `DEPENDENCY_PIN_MISMATCH`, `ENVIRONMENT_BLOCKER`, or `DEGRADED_STATIC_ONLY`.

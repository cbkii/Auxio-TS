# Media3 playback contract notes

## MediaSession query failure

**Observed in external docs:** `SessionResult` exposes structured result codes, including bad-value and invalid-state errors.

**Inferred for Auxio-TS:** For an unresolvable query, fallback-to-all playback is a poor safety default. Prefer an existing no-op/error path if the method signature supports it. If current callback does not permit a `SessionResult`, return an empty/no-op command rather than starting broad playback.

**Requires repo validation:** The exact return type and caller expectations in `MediaSessionInterface` decide the minimal safe fix.

## ShuffleOrder semantics

**Observed in external docs:** Media3 `ShuffleOrder` defines clone operations and index traversal contracts:

- `cloneAndInsert(insertionIndex, insertionCount)` inserts new item indices in the range `[insertionIndex, insertionIndex + insertionCount)`.
- `cloneAndRemove(indexFrom, indexToExclusive)` removes item indices in `[indexFrom, indexToExclusive)`.
- `getFirstIndex`, `getLastIndex`, `getNextIndex`, and `getPreviousIndex` return `C.INDEX_UNSET` when no valid item exists.

**Inferred for Auxio-TS:** BetterShuffleOrder fixes should be test-first. Avoid cosmetic refactors to shuffle arrays unless tests cover insertions, removals, next/previous traversal, first/last, empty orders, and preservation of play-next placement.

## AudioProcessor / ReplayGain

**Observed in external docs:** `AudioProcessor` implementations configure accepted formats, flush state, receive input via `queueInput`, and expose output via `getOutput`. Unsupported formats are rejected with `UnhandledAudioFormatException`.

**Inferred for Auxio-TS:** The ReplayGain suggestion about converting to a lower-level processor appears conditional/future-looking. If current Media3 APIs and current app audio pipeline only safely support 16-bit PCM, defer the broad conversion.

**Performance guard:** Do not rewrite low-level audio processing without tests and without checking CPU/latency impact on TS18-class hardware.

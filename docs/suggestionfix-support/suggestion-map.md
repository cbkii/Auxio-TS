# Suggestion-to-research map

This map lists external research Jules may need for each consolidated suggestion group. It is not a solution plan; it is a support map.

## 1. Navigate to newly created playlist

External research need: low. Android Navigation docs can help if current navigation graph patterns are unclear, but repo navigation patterns should be authoritative.

Recommended stance: **Requires repo validation**. Use existing NavController/navigation graph conventions; avoid new runtime scans or repository refreshes.

## 2. CoverProvider exported provider security

External research need: high. See `android-component-security.md`.

Recommended stance: **Observed in external docs** that `android:exported` and provider permissions control cross-app access. **Requires repo validation** for artwork consumers.

## 3. PlaylistPickerViewModel guards and playlist-state syncing

External research need: low. This is repo state-management logic.

Recommended stance: **Requires repo validation**. Add equality/null guards only where semantics are unchanged. Avoid broad state-sync architecture changes.

## 4. MediaSession unresolved query handling

External research need: medium. See `media3-playback-contracts.md`.

Recommended stance: **Inferred for Auxio-TS**: use structured no-op/error behaviour rather than fallback-to-all playback. Validate against existing MediaSession callback return types.

## 5. M3U path backtracking

External research need: low. Pure path logic; repo tests/history should dominate.

Recommended stance: **Requires repo validation**. Add tests before refactor.

## 6. PlaybackUtil tests

External research need: low. See `testing-strategy.md` for local unit-test guidance.

Recommended stance: pure JVM tests should be feasible.

## 7. QueueAdapter range updates

External research need: medium. See `ui-accessibility-recyclerview.md`.

Recommended stance: **Observed in external docs** that specific range notifications are more efficient, but payloads may be dropped. Tests should verify adapter helper logic, not Android rendering internals.

## 8. Playback button accessibility

External research need: medium. See `ui-accessibility-recyclerview.md`.

Recommended stance: add useful action/state labels using string resources; keep binding lightweight.

## 9. Media3 demo lowercase optimization

External research need: low. The issue is straightforward, but repository ownership/submodule status matters more.

Recommended stance: **Requires repo validation**. Skip if vendored/submodule/pinned.

## 10. Foreground-service behaviour

External research need: high. See `foreground-bluetooth-media-buttons.md`.

Recommended stance: **Observed in external docs** that FGS launch restrictions vary by Android version and service type. Do not blindly force foreground; align with notification and playback state.

## 11. Bluetooth connect service initialisation

External research need: high. See `foreground-bluetooth-media-buttons.md`.

Recommended stance: **Observed in external docs** that Android 12+ Bluetooth permissions include `BLUETOOTH_CONNECT`. **Inferred for Auxio-TS**: avoid unexpected playback/background starts.

## 12. Original and normal dates side-by-side

External research need: low. Tag model and UI architecture are repo-specific.

Recommended stance: skip/defer unless raw date data already survives parsing and exposure is small.

## 13. Hard vs soft reloads

External research need: medium. External storage docs support avoiding large directory work; repo cache lifecycle remains authoritative.

Recommended stance: preserve PR #120 manual-only scan/cached startup. Avoid cache-destructive reloads for non-source settings.

## 14. BetterShuffleOrder correctness

External research need: high. See `media3-playback-contracts.md`.

Recommended stance: test against Media3 `ShuffleOrder` semantics before refactor.

## 15–17. PlaybackViewModel/SystemPlaybackReceiver/PersistenceRepository tests

External research need: medium. See `testing-strategy.md`.

Recommended stance: use JVM local tests for pure/public API logic; use Robolectric/mocks only if already present or low-risk.

## 18. SelectionFragment automatic selection handling

External research need: low. Needs repo history; external docs are unlikely to solve it.

Recommended stance: skip if behaviour is not recoverable from current code/history.

## 19. Root command execution hardening

External research need: high. See `android-component-security.md`.

Recommended stance: replace arbitrary shell string surface with narrow validated API if feasible; otherwise keep generated commands internal and validate allowed roots.

## 20. ReplayGainAudioProcessor low-level conversion

External research need: high. See `media3-playback-contracts.md`.

Recommended stance: likely defer unless current Media3 APIs support a small, testable migration.

## 21. Replace Hilt

External research need: none for this task.

Recommended stance: skip as broad architecture migration.

## 22. Custom overscroll effect

External research need: low.

Recommended stance: skip unless a concrete bug exists.

## 23. MediaButtonReceiver behaviour

External research need: medium. See `foreground-bluetooth-media-buttons.md` and `media3-playback-contracts.md`.

Recommended stance: Media3 already handles many media-button behaviours; verify repo-specific forwarding before changing.

## 24. Third-party filters failure mode

External research need: medium. See `storage-saf-mediastore-overlay.md`.

Recommended stance: preserve manual path fallback and never treat unsupported filters/source failures as empty successful scans.

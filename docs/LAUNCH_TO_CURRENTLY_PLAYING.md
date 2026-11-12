# Launch to Currently Playing

## Behaviour contract

`Launch to Now Playing` controls navigation only; it does not imply autoplay.

- Generic cold launches target **Now Playing**, never Queue.
- Explicit Queue launcher/deep-link requests target playback plus Queue.
- Explicit destinations supersede generic startup routing.
- Requests wait through transient library, restore, raw-resume, and sheet states.
- Source-required, empty/recovery, no-session, failed, and cancelled states terminate the
  matching request rather than leaving a stale future route.
- A route is consumed only after the requested final sheet state is reached.
- Manual sheet drag/back/panel changes and Settings/About navigation cancel pending routes.
- Persisted-state restore publishes `WAITING_FOR_PLAYER` before its asynchronous read; a later
  manual playback or queue mutation cancels the active restore and publishes `CANCELLED` before
  it can overwrite the user's newer choice.

## Automated coverage

`StartupPanelCoordinatorTest` verifies the pure policy boundary for generic Now Playing routing,
transient restore waits, terminal library and restore outcomes, raw-fast-resume reconciliation,
and explicit Queue routing. Repository CI additionally exercises formatting, lint, the full unit
suite, Android builds, workflow/script syntax, and head-unit compatibility guardrails.

Bottom-sheet animation timing, launcher ordering, process death, and ACC sleep/wake remain physical
runtime concerns rather than unit-test claims.

## Fast Resume queue context and shuffle

- **Handoff trigger.** The automatic raw/primitive → hydrated handoff runs as soon as a coherent,
  non-empty device library is published, including the committed cache that TS18 cold start
  publishes without a source scan. Hydration stays non-lossy and identity-checked: every persisted
  item, including the current one, must resolve or primitive authority is retained. A forced full
  rescan is requested only when no usable library is available, or after hydration has failed
  against a library that no complete enumeration produced. Building a *new* All Songs membership still requires `SourceScanOutcome.Success`.
- **Playback context.** The persisted parent (album/artist/genre/playlist, or none for All Songs)
  belongs to the whole queue-session lineage written by `saveState`, so it is restored even after
  pre-hydration queue edits. Queue membership, parent, shuffle scope and current item remain
  separate facts.
- **Shuffle scope.** Raw Fast Resume publishes the persisted scope immediately
  (`PlaybackStateHolder.shuffleScopeHint`), so a saved `GENRE` scope survives the handoff.
- **Shuffle control** (`ShuffleControlPolicy`). `OFF → ALL` and `ALL → OFF` reorder the existing
  queue in place and are available during Fast Resume. `ALL → GENRE` and `GENRE → OFF` need the
  hydrated song and replace membership *around* the decoding item
  (`replaceQueuePreservingCurrent`: no `setMediaItems`, `prepare`, seek or parent change; published
  as a queue reorder, not new playback). New playback or a queue edit discards a genre selection
  still being computed. Genre
  shuffle keeps the parent, so `GENRE → OFF` restores the parent's (or All Songs') unshuffled
  membership; if the current song is outside that context the queue is unshuffled in place
  instead, and if the owner refuses the seamless install `GENRE` is kept rather than discarded. Without genre data the cycle skips `GENRE` rather than sticking in `ALL`.

## Exact-device validation

**Requires TS18 validation** on `s9863a1h10_Natv`:

1. Cold launch with saved playing and paused sessions.
2. No saved session, empty library, recovery, and source-required states.
3. Raw fast resume followed by normal `Song` reconciliation, including saved playlist, genre-filtered,
   genre-shuffle and All Songs shuffle contexts, with no source scan at boot.
4. Shuffle `OFF → ALL → GENRE → OFF` during Fast Resume and after handoff, listening for any gap.
5. Explicit Now Playing and Queue launch during cold and warm app states.
6. Manual close, drag, back, Settings, and Queue mutation during restore/sheet settling.
7. DoFun launcher restart, process death, reboot, and real ACC sleep/wake.

Collect route/restore logs and final sheet state. CI does not prove physical TS18 timing.

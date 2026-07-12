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
- Manual playback or queue mutations cancel an in-flight persisted-state restore before it
  can overwrite the user's newer choice.

## Automated coverage

`StartupPanelCoordinatorTest` verifies the pure policy boundary for generic Now Playing routing,
transient restore waits, terminal library and restore outcomes, raw-fast-resume reconciliation,
and explicit Queue routing. Repository CI additionally exercises formatting, lint, the full unit
suite, Android builds, workflow/script syntax, and head-unit compatibility guardrails.

Bottom-sheet animation timing, launcher ordering, process death, and ACC sleep/wake remain physical
runtime concerns rather than unit-test claims.

## Exact-device validation

**Requires TS18 validation** on `s9863a1h10_Natv`:

1. Cold launch with saved playing and paused sessions.
2. No saved session, empty library, recovery, and source-required states.
3. Raw fast resume followed by normal `Song` reconciliation.
4. Explicit Now Playing and Queue launch during cold and warm app states.
5. Manual close, drag, back, Settings, and Queue mutation during restore/sheet settling.
6. DoFun launcher restart, process death, reboot, and real ACC sleep/wake.

Collect route/restore logs and final sheet state. CI does not prove physical TS18 timing.

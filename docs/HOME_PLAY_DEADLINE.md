# Bounded HOME Play

The canonical session advertises `org.oxycblt.auxio.action.HOME_PLAY_V1` in initial, empty and playable states. Clients send `protocol=1` and a future `deadline_elapsed_ms` on the device's elapsedRealtime clock, within the existing maximum window.

The receiver rejects invalid/expired requests. Deferred queue/raw restoration retains the deadline and has no detached ShuffleAll fallback. An expired or passive restore cannot be promoted by launch-autoplay preferences. The restored player remains paused until decoder STATE_READY; a one-use deadline gate then admits playback only inside the original window. A later explicit Pause/Play, replacement, cancellation or holder release invalidates the pending gate. Timeout in the client is not an acknowledgement or cancellation; the source's own gate supplies the playback-commit bound.

Normal warm Play remains available. One player, queue, session, notification and focus authority are preserved. A timely playback commit is not a hard real-time guarantee about the first audible hardware sample; TS18 audibility and audio handover remain physical acceptance items.

Tests cover arbiter expiry/fallback/cancellation, decoder readiness before/at expiry, one-use admission, replacement and stale expiry. The launcher companion also tests actual action delivery against an API29 cross-process fixture; that fixture is not this application's complete restore engine.

# UI accessibility and RecyclerView update notes

## Playback controls accessibility

**Observed in external docs:** Android accessibility guidance emphasizes useful, descriptive labels for UI elements; screen readers announce those labels.

**Inferred for Auxio-TS:** Playback buttons should expose action/state labels such as play/pause, shuffle on/off, repeat mode, previous, next, and overflow/options. Use string resources and update dynamic labels when state changes.

**Performance guard:** Accessibility label updates should occur in existing state-binding paths only; avoid new observers or heavy recomputation on startup.

## QueueAdapter range updates

**Observed in external docs:** RecyclerView specific change events are more efficient than broad data-set refreshes. `notifyItemRangeChanged` marks a positional range as changed while preserving item identity. Payloads may be dropped for unattached views.

**Inferred for Auxio-TS:** Use targeted range updates, but assume full re-bind may still happen. Adapter logic must be correct without relying solely on payload delivery.

**Suggested test focus:** Pure helper tests for old index, new index, playing-state changes, no-op cases, empty queue/out-of-range handling, and affected ranges. Do not attempt to unit-test RecyclerView rendering unless existing test infrastructure already supports it.

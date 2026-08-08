/*
 * Copyright (c) 2026 Auxio Project
 * BridgeDispatchCorrelation.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Correlates a very short duplicate observation after a command has already been accepted.
 *
 * <p>The stock receiver and private presenter can expose the same physical action. We only reuse a
 * command ID after the first observation has positively completed, and only inside a 50 ms window.
 * Failed or in-flight observations never suppress a later stock path.
 */
final class BridgeDispatchCorrelation {
    static final long CORRELATION_WINDOW_MS = 50L;

    private final AtomicLong sequence = new AtomicLong();
    private long latestDispatchedCommandId;
    private long lastCommandId;
    private int lastCommandCode;
    private long lastSeekPosition;
    private long lastCompletedAtMs = Long.MIN_VALUE;
    private boolean lastAccepted;

    synchronized Decision begin(int commandCode, long seekPosition, long nowMs) {
        if (lastAccepted
                && lastCommandId != 0L
                && lastCommandCode == commandCode
                && lastSeekPosition == seekPosition
                && nowMs >= lastCompletedAtMs
                && nowMs - lastCompletedAtMs <= CORRELATION_WINDOW_MS) {
            return Decision.reuse(lastCommandId);
        }
        long id = sequence.incrementAndGet();
        if (id == 0L) id = sequence.incrementAndGet();
        latestDispatchedCommandId = id;
        return Decision.dispatch(id, commandCode, seekPosition);
    }

    synchronized void complete(Decision decision, boolean accepted, long nowMs) {
        if (!decision.shouldDispatch || decision.commandId != latestDispatchedCommandId) return;
        lastCommandId = decision.commandId;
        lastCommandCode = decision.commandCode;
        lastSeekPosition = decision.seekPosition;
        lastCompletedAtMs = nowMs;
        lastAccepted = accepted;
    }

    static final class Decision {
        final long commandId;
        final int commandCode;
        final long seekPosition;
        final boolean shouldDispatch;
        final boolean alreadyAccepted;

        private Decision(
                long commandId,
                int commandCode,
                long seekPosition,
                boolean shouldDispatch,
                boolean alreadyAccepted) {
            this.commandId = commandId;
            this.commandCode = commandCode;
            this.seekPosition = seekPosition;
            this.shouldDispatch = shouldDispatch;
            this.alreadyAccepted = alreadyAccepted;
        }

        static Decision dispatch(long id, int commandCode, long seekPosition) {
            return new Decision(id, commandCode, seekPosition, true, false);
        }

        static Decision reuse(long id) {
            return new Decision(id, 0, 0L, false, true);
        }
    }
}

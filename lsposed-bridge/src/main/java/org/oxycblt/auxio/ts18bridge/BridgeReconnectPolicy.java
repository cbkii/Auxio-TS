/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

/** Pure retry policy for long-lived Track-C MediaBrowser and command endpoints. */
final class BridgeReconnectPolicy {
    static final int MAX_BURST_ATTEMPTS = 5;
    static final long COOLDOWN_DELAY_MS = 30_000L;
    private static final long BASE_DELAY_MS = 2_000L;
    private static final long MAX_BURST_DELAY_MS = 30_000L;

    private BridgeReconnectPolicy() {}

    static Decision next(int attempts) {
        int safeAttempts = Math.max(0, attempts);
        if (safeAttempts >= MAX_BURST_ATTEMPTS) {
            return new Decision(COOLDOWN_DELAY_MS, 0, true);
        }
        int nextAttempt = safeAttempts + 1;
        long delayMs = Math.min(MAX_BURST_DELAY_MS, BASE_DELAY_MS * nextAttempt);
        return new Decision(delayMs, nextAttempt, false);
    }

    static final class Decision {
        final long delayMs;
        final int nextAttempt;
        final boolean cooldown;

        Decision(long delayMs, int nextAttempt, boolean cooldown) {
            this.delayMs = delayMs;
            this.nextAttempt = nextAttempt;
            this.cooldown = cooldown;
        }
    }
}

/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class BridgeReconnectPolicyTest {
    @Test
    public void transientFailuresUseBoundedFastBurst() {
        int attempt = 0;
        long[] expected = {2_000L, 4_000L, 6_000L, 8_000L, 10_000L};
        for (long expectedDelay : expected) {
            BridgeReconnectPolicy.Decision decision = BridgeReconnectPolicy.next(attempt);
            assertEquals(expectedDelay, decision.delayMs);
            assertFalse(decision.cooldown);
            attempt = decision.nextAttempt;
        }
        assertEquals(BridgeReconnectPolicy.MAX_BURST_ATTEMPTS, attempt);
    }

    @Test
    public void exhaustedBurstRearmsAfterQuietCooldown() {
        BridgeReconnectPolicy.Decision cooldown =
                BridgeReconnectPolicy.next(BridgeReconnectPolicy.MAX_BURST_ATTEMPTS);
        assertEquals(BridgeReconnectPolicy.COOLDOWN_DELAY_MS, cooldown.delayMs);
        assertEquals(0, cooldown.nextAttempt);
        assertTrue(cooldown.cooldown);

        BridgeReconnectPolicy.Decision restarted =
                BridgeReconnectPolicy.next(cooldown.nextAttempt);
        assertEquals(2_000L, restarted.delayMs);
        assertEquals(1, restarted.nextAttempt);
        assertFalse(restarted.cooldown);
    }

    @Test
    public void negativeAttemptIsTreatedAsFirstRetry() {
        BridgeReconnectPolicy.Decision decision = BridgeReconnectPolicy.next(-1);
        assertEquals(2_000L, decision.delayMs);
        assertEquals(1, decision.nextAttempt);
        assertFalse(decision.cooldown);
    }
}

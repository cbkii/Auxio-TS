/*
 * Copyright (c) 2026 Auxio Project
 * BridgeDispatchCorrelationTest.java is part of Auxio-TS.
 */
package org.oxycblt.auxio.ts18bridge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class BridgeDispatchCorrelationTest {
    @Test
    public void acceptedObservationIsReusedInsideCorrelationWindow() {
        BridgeDispatchCorrelation correlation = new BridgeDispatchCorrelation();
        BridgeDispatchCorrelation.Decision first = correlation.begin(1, -1L, 100L);
        assertTrue(first.shouldDispatch);
        correlation.complete(first, true, 110L);

        BridgeDispatchCorrelation.Decision duplicate = correlation.begin(1, -1L, 140L);
        assertFalse(duplicate.shouldDispatch);
        assertTrue(duplicate.alreadyAccepted);
    }

    @Test
    public void failedObservationDoesNotSuppressLaterPath() {
        BridgeDispatchCorrelation correlation = new BridgeDispatchCorrelation();
        BridgeDispatchCorrelation.Decision first = correlation.begin(2, -1L, 100L);
        correlation.complete(first, false, 110L);

        BridgeDispatchCorrelation.Decision retry = correlation.begin(2, -1L, 120L);
        assertTrue(retry.shouldDispatch);
        assertFalse(retry.alreadyAccepted);
        assertNotEquals(first.commandId, retry.commandId);
    }

    @Test
    public void inFlightObservationDoesNotSuppressLaterPath() {
        BridgeDispatchCorrelation correlation = new BridgeDispatchCorrelation();
        BridgeDispatchCorrelation.Decision first = correlation.begin(2, -1L, 100L);
        assertTrue(first.shouldDispatch);

        BridgeDispatchCorrelation.Decision concurrent = correlation.begin(2, -1L, 110L);
        assertTrue(concurrent.shouldDispatch);
        assertFalse(concurrent.alreadyAccepted);
        assertNotEquals(first.commandId, concurrent.commandId);
    }

    @Test
    public void sameCommandOutsideWindowGetsNewId() {
        BridgeDispatchCorrelation correlation = new BridgeDispatchCorrelation();
        BridgeDispatchCorrelation.Decision first = correlation.begin(3, -1L, 100L);
        correlation.complete(first, true, 110L);

        BridgeDispatchCorrelation.Decision later =
                correlation.begin(3, -1L, 111L + BridgeDispatchCorrelation.CORRELATION_WINDOW_MS);
        assertTrue(later.shouldDispatch);
        assertNotEquals(first.commandId, later.commandId);
    }

    @Test
    public void differentSeekValueNeverCorrelates() {
        BridgeDispatchCorrelation correlation = new BridgeDispatchCorrelation();
        BridgeDispatchCorrelation.Decision first = correlation.begin(6, 1_000L, 100L);
        correlation.complete(first, true, 110L);

        BridgeDispatchCorrelation.Decision second = correlation.begin(6, 2_000L, 120L);
        assertTrue(second.shouldDispatch);
        assertNotEquals(first.commandId, second.commandId);
    }
}

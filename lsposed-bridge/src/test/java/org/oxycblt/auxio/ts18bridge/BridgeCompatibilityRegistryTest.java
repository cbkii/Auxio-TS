/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Regression tests for the deliberately small Track-C routing boundary. */
public final class BridgeCompatibilityRegistryTest {
    @Test
    public void stockMainProcessIsInScope() {
        assertTrue(BridgeContract.isScopedProcess("com.tw.music", "com.tw.music"));
    }

    @Test
    public void otherPackagesAndSecondaryProcessesAreOutOfScope() {
        assertFalse(BridgeContract.isScopedProcess("com.tw.media", "com.tw.media"));
        assertFalse(BridgeContract.isScopedProcess("com.dofun.variety", "com.dofun.variety"));
        assertFalse(BridgeContract.isScopedProcess("com.tw.music", "com.tw.music:remote"));
    }

    @Test
    public void updateAndUnknownCannotCrossCommandBinder() {
        assertFalse(BridgeWireContract.isSupportedCommand(0));
        assertFalse(BridgeWireContract.isSupportedCommand(7));
    }
}

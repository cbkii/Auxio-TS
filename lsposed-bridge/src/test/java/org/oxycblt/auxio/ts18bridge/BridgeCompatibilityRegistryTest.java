/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class BridgeCompatibilityRegistryTest {
    @Test
    public void reviewedBuildsCarryExplicitPresenterCapability() {
        BridgeContract.RegistryEntry first = BridgeContract.reviewedStockApk(
                "4F5495E270A7C86BAB232E2B7EE2ECD2D71F3450F6F20ED5F36FEAA4229C1518");
        BridgeContract.RegistryEntry second = BridgeContract.reviewedStockApk(
                "3A14ED3B330723A7F88AE3911804858D370CA673E17D67098CCE6C9A543C6B49");
        assertNotNull(first);
        assertNotNull(second);
        assertTrue(first.has(BridgeContract.CAP_COMMAND_RECEIVER));
        assertTrue(first.has(BridgeContract.CAP_PRIVATE_PRESENTER));
        assertTrue(second.has(BridgeContract.CAP_COMMAND_RECEIVER));
        assertTrue(second.has(BridgeContract.CAP_PRIVATE_PRESENTER));
    }

    @Test
    public void unknownApkNeverGetsFunctionalCapabilities() {
        BridgeContract.RegistryEntry unknown = BridgeContract.reviewedStockApk(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        assertNull(unknown);
    }

    @Test
    public void updateAndUnknownCannotCrossCommandBinder() {
        assertFalse(BridgeWireContract.isSupportedCommand(0));
        assertFalse(BridgeWireContract.isSupportedCommand(7));
    }
}

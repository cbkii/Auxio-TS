/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MediaMirrorCommandTest {
    @Test
    public void mapsBridgeCommandsToVersionedWireCodes() {
        assertEquals(BridgeWireContract.COMMAND_PREVIOUS, BridgeContract.commandCode(BridgeCommand.PREVIOUS));
        assertEquals(BridgeWireContract.COMMAND_NEXT, BridgeContract.commandCode(BridgeCommand.NEXT));
        assertEquals(BridgeWireContract.COMMAND_PLAY_PAUSE, BridgeContract.commandCode(BridgeCommand.PLAY_PAUSE));
        assertEquals(BridgeWireContract.COMMAND_PLAY, BridgeContract.commandCode(BridgeCommand.PLAY));
        assertEquals(BridgeWireContract.COMMAND_PAUSE, BridgeContract.commandCode(BridgeCommand.PAUSE));
        assertEquals(BridgeWireContract.COMMAND_SEEK, BridgeContract.commandCode(BridgeCommand.SEEK));
    }

    @Test
    public void nonTransportCommandsAreNotSentAcrossCommandBinder() {
        assertEquals(0, BridgeContract.commandCode(BridgeCommand.UPDATE));
        assertEquals(0, BridgeContract.commandCode(BridgeCommand.UNKNOWN));
    }
}

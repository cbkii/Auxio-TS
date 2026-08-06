package org.oxycblt.auxio.ts18bridge;

interface IAuxioBridgeCommand {
    int dispatchCommand(int protocolVersion, int commandId, String commandType, long seekPos, String sourceAdapter, long clientGeneration, long clientTimestamp);
}

/*
 * Copyright (c) 2026 Auxio Project
 * BridgeWireContract.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

/**
 * Primitive, versioned Binder wire contract shared by the Topway-compatible app and Track-C shim.
 *
 * <p>Keep this class Android-framework-light. The bridge module compiles this exact source file so
 * there cannot be two independently edited protocol definitions.
 */
public final class BridgeWireContract {
    public static final int PROTOCOL_VERSION = 2;

    public static final String ACTION_BIND_COMMAND =
            "org.oxycblt.auxio.ts18bridge.ACTION_BIND_COMMAND";
    public static final String BINDER_DESCRIPTOR = "org.oxycblt.auxio.ts18bridge.Command";
    public static final int TRANSACTION_DISPATCH = 1;

    public static final int COMMAND_PREVIOUS = 1;
    public static final int COMMAND_NEXT = 2;
    public static final int COMMAND_PLAY_PAUSE = 3;
    public static final int COMMAND_PLAY = 4;
    public static final int COMMAND_PAUSE = 5;
    public static final int COMMAND_SEEK = 6;

    public static final int SOURCE_STOCK_SHIM = 1;

    public static final int RESULT_ACCEPTED = 1;
    public static final int RESULT_DUPLICATE_ACCEPTED = 2;
    public static final int RESULT_NOT_READY = 3;
    public static final int RESULT_UNSUPPORTED = 4;
    public static final int RESULT_INVALID = 5;
    public static final int RESULT_UNTRUSTED = 6;
    public static final int RESULT_BUSY = 7;
    public static final int RESULT_VERSION_MISMATCH = 8;
    public static final int RESULT_EXPIRED = 9;
    public static final int RESULT_ERROR = 10;

    private BridgeWireContract() {}

    public static boolean isSupportedCommand(int command) {
        return command >= COMMAND_PREVIOUS && command <= COMMAND_SEEK;
    }

    public static boolean isAcceptedResult(int result) {
        return result == RESULT_ACCEPTED || result == RESULT_DUPLICATE_ACCEPTED;
    }
}

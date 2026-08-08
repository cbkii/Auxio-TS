/*
 * Copyright (c) 2026 Auxio Project
 * BridgeContract.java is part of Auxio-TS.
 */
package org.oxycblt.auxio.ts18bridge;

/** Stock package routing and primitive command mapping for the Track-C shim. */
final class BridgeContract {
    static final String STOCK_PACKAGE = "com.tw.music";

    static final String ACTION_COMMAND = "com.tw.music.action.cmd";
    static final String ACTION_PREVIOUS = "com.tw.music.action.prev";
    static final String ACTION_NEXT = "com.tw.music.action.next";
    static final String ACTION_PLAY_PAUSE = "com.tw.music.action.pp";
    static final String ACTION_WIDGET_SEEK = "com.android.launcher.widget_music_progress";

    private BridgeContract() {}

    /**
     * LSPosed's static package scope is the application-selection authority. The captured stock
     * manifest does not assign the hooked application/activity/service to a secondary process, so
     * Track C handles only the package's default process.
     */
    static boolean isScopedProcess(String packageName, String processName) {
        return STOCK_PACKAGE.equals(packageName) && STOCK_PACKAGE.equals(processName);
    }

    static int commandCode(BridgeCommand command) {
        return switch (command) {
            case PREVIOUS -> BridgeWireContract.COMMAND_PREVIOUS;
            case NEXT -> BridgeWireContract.COMMAND_NEXT;
            case PLAY_PAUSE -> BridgeWireContract.COMMAND_PLAY_PAUSE;
            case PLAY -> BridgeWireContract.COMMAND_PLAY;
            case PAUSE -> BridgeWireContract.COMMAND_PAUSE;
            case SEEK -> BridgeWireContract.COMMAND_SEEK;
            case UPDATE, UNKNOWN -> 0;
        };
    }
}

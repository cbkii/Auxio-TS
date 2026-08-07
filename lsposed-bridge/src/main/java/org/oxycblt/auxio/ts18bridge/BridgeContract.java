/*
 * Copyright (c) 2026 Auxio Project
 * BridgeContract.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

/** Stock contracts and the exact-build capability registry for the Track-C shim. */
final class BridgeContract {
    static final String STOCK_PACKAGE = "com.tw.music";

    static final String ACTION_COMMAND = "com.tw.music.action.cmd";
    static final String ACTION_PREVIOUS = "com.tw.music.action.prev";
    static final String ACTION_NEXT = "com.tw.music.action.next";
    static final String ACTION_PLAY_PAUSE = "com.tw.music.action.pp";
    static final String ACTION_WIDGET_SEEK = "com.android.launcher.widget_music_progress";

    static final int CAP_ACTIVITY_REDIRECT = 1 << 0;
    static final int CAP_SERVICE_OBSERVATION = 1 << 1;
    static final int CAP_COMMAND_RECEIVER = 1 << 2;
    static final int CAP_SEEK_RECEIVER = 1 << 3;
    static final int CAP_PRIVATE_PRESENTER = 1 << 4;
    static final int CAP_STATE_MIRROR = 1 << 5;
    private static final int REVIEWED_CAPABILITIES =
            CAP_ACTIVITY_REDIRECT
                    | CAP_SERVICE_OBSERVATION
                    | CAP_COMMAND_RECEIVER
                    | CAP_SEEK_RECEIVER
                    | CAP_PRIVATE_PRESENTER
                    | CAP_STATE_MIRROR;

    private static final RegistryEntry[] REGISTRY = {
        new RegistryEntry(
                "4F5495E270A7C86BAB232E2B7EE2ECD2D71F3450F6F20ED5F36FEAA4229C1518",
                "AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3",
                118L,
                REVIEWED_CAPABILITIES,
                "Captured TW_THEME stock build; presenter rb/pb/ba/fa/seekTo reviewed."),
        new RegistryEntry(
                "3A14ED3B330723A7F88AE3911804858D370CA673E17D67098CCE6C9A543C6B49",
                "AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3",
                0L,
                REVIEWED_CAPABILITIES,
                "Second reviewed stock capture; exact runtime version code remains device-qualified evidence.")
    };

    private BridgeContract() {}

    static RegistryEntry reviewedStockApk(String sha256) {
        if (sha256 == null || sha256.isEmpty()) return null;
        for (RegistryEntry entry : REGISTRY) {
            if (entry.apkSha256.equals(sha256)) return entry;
        }
        return null;
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

    static final class RegistryEntry {
        final String apkSha256;
        final String signerSha256;
        final long versionCode;
        final int capabilities;
        final String limitations;

        RegistryEntry(
                String apkSha256,
                String signerSha256,
                long versionCode,
                int capabilities,
                String limitations) {
            this.apkSha256 = apkSha256;
            this.signerSha256 = signerSha256;
            this.versionCode = versionCode;
            this.capabilities = capabilities;
            this.limitations = limitations;
        }

        boolean has(int capability) {
            return (capabilities & capability) == capability;
        }
    }
}

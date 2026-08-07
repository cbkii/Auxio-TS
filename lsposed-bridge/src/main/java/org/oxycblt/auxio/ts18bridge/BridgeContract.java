/*
 * Copyright (c) 2026 Auxio Project
 * BridgeContract.java is part of Auxio-TS.
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
        // Captured TW_THEME stock build; rb/pb/ba/fa/seekTo presenter methods were reviewed.
        new RegistryEntry(
                "4F5495E270A7C86BAB232E2B7EE2ECD2D71F3450F6F20ED5F36FEAA4229C1518",
                BridgeWireContract.STOCK_CERT_SHA256,
                118L,
                REVIEWED_CAPABILITIES),
        // Second reviewed stock capture. Its exact APK hash is authoritative; no reliable runtime
        // versionCode was retained, so 0 means the registry does not impose a redundant version gate.
        new RegistryEntry(
                "3A14ED3B330723A7F88AE3911804858D370CA673E17D67098CCE6C9A543C6B49",
                BridgeWireContract.STOCK_CERT_SHA256,
                0L,
                REVIEWED_CAPABILITIES)
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

        RegistryEntry(String apkSha256, String signerSha256, long versionCode, int capabilities) {
            this.apkSha256 = apkSha256;
            this.signerSha256 = signerSha256;
            this.versionCode = versionCode;
            this.capabilities = capabilities;
        }

        boolean acceptsVersion(long actualVersionCode) {
            return versionCode == 0L || versionCode == actualVersionCode;
        }

        boolean has(int capability) {
            return (capabilities & capability) == capability;
        }
    }
}

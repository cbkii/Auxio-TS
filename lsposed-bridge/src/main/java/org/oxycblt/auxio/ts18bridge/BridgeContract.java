package org.oxycblt.auxio.ts18bridge;

import java.util.Arrays;
import java.util.List;

public final class BridgeContract {
    static final String STOCK_PACKAGE = "com.tw.music";

    static final String ACTION_PREVIOUS = "com.tw.music.action.prev";
    static final String ACTION_NEXT = "com.tw.music.action.next";
    static final String ACTION_PLAY_PAUSE = "com.tw.music.action.pp";
    static final String ACTION_COMMAND = "com.tw.music.action.cmd";

    static final String ACTION_WIDGET_SEEK = "com.android.launcher.widget_music_progress";

    static final String EXTRA_COMMAND = "cmd";
    static final String EXTRA_WIDGET_PROGRESS = "music_progress";

    static final String COMMAND_PREVIOUS = "prev";
    static final String COMMAND_NEXT = "next";
    static final String COMMAND_PLAY_PAUSE = "pp";
    static final String COMMAND_UPDATE = "update";

    static final String STOCK_PRESENTER = "com.eckom.xtlibrary.b.f.e.a";

    public static final String ACTION_AUXIO_BRIDGE_BIND = "org.oxycblt.auxio.ts18bridge.ACTION_BIND_COMMAND";

    public static final int RESULT_ACCEPTED = 1;
    public static final int RESULT_DUPLICATE = 2;
    public static final int RESULT_NOT_READY = 3;
    public static final int RESULT_UNSUPPORTED = 4;
    public static final int RESULT_INVALID = 5;
    public static final int RESULT_UNTRUSTED = 6;
    public static final int RESULT_BUSY = 7;
    public static final int RESULT_VERSION_MISMATCH = 8;
    public static final int RESULT_ERROR = 9;

    public static final int PROTOCOL_VERSION = 1;

    // Explicit registry of compatible verified stock APKs and exact hooks
    static final List<RegistryEntry> REGISTRY = Arrays.asList(
            new RegistryEntry("4F5495E270A7C86BAB232E2B7EE2ECD2D71F3450F6F20ED5F36FEAA4229C1518", true),
            new RegistryEntry("3A14ED3B330723A7F88AE3911804858D370CA673E17D67098CCE6C9A543C6B49", true)
    );

    private BridgeContract() {}

    static boolean isReviewedStockApk(String actualApkHash) {
        if (actualApkHash == null || actualApkHash.isEmpty()) {
            return false;
        }
        for (RegistryEntry entry : REGISTRY) {
            if (entry.sha256.equals(actualApkHash)) {
                return true;
            }
        }
        return false;
    }

    static boolean isReviewedStockApkForPrivateHooks(String actualApkHash) {
        if (actualApkHash == null || actualApkHash.isEmpty()) {
            return false;
        }
        for (RegistryEntry entry : REGISTRY) {
            if (entry.sha256.equals(actualApkHash) && entry.supportsPrivateHooks) {
                return true;
            }
        }
        return false;
    }

    static final class RegistryEntry {
        final String sha256;
        final boolean supportsPrivateHooks;

        RegistryEntry(String sha256, boolean supportsPrivateHooks) {
            this.sha256 = sha256;
            this.supportsPrivateHooks = supportsPrivateHooks;
        }
    }
}

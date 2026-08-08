/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/** Lightweight runtime state for the LSPosed-scoped Track-C shim. */
final class BridgeEnvironment {
    interface LogSink {
        void log(String message, Throwable error);
    }

    private static final String KILL_SWITCH_DIRECTORY = "Auxio-TS";
    private static final String KILL_SWITCH_MARKER = "disable-lsposed-bridge";

    private final LogSink log;
    private final AtomicBoolean killSwitchChecked = new AtomicBoolean();
    private final AtomicBoolean disabled = new AtomicBoolean();
    private final AtomicReference<WeakReference<Context>> context =
            new AtomicReference<>(new WeakReference<>(null));

    BridgeEnvironment(LogSink log) {
        this.log = log;
    }

    void remember(Context value) {
        Context app = value.getApplicationContext();
        Context retained = app != null ? app : value;
        context.set(new WeakReference<>(retained));
        checkKillSwitchOnce();
    }

    Context currentContext() {
        return context.get().get();
    }

    boolean canBridge(Context value, String reason) {
        remember(value);
        return !disabled.get();
    }

    boolean canPublish(Context value) {
        return canBridge(value, "publish");
    }

    /**
     * The marker is an optional emergency convenience, not an LSPosed scope or identity gate.
     *
     * <p>It is inspected once per stock-process lifetime. If storage is unavailable or unreadable,
     * normal LSPosed-scoped operation continues. Creating or removing the marker therefore takes
     * effect after restarting {@code com.tw.music}; LSPosed Manager remains the primary disable path.
     */
    private void checkKillSwitchOnce() {
        if (!killSwitchChecked.compareAndSet(false, true)) return;
        try {
            File shared = Environment.getExternalStorageDirectory();
            if (shared == null || !shared.isDirectory() || !shared.canRead()) {
                log.log(
                        "optional bridge kill switch is unavailable; continuing with LSPosed scope",
                        null);
                return;
            }
            String storageState = Environment.getExternalStorageState(shared);
            if (!Environment.MEDIA_MOUNTED.equals(storageState)
                    && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState)) {
                log.log(
                        "optional bridge kill switch storage is not mounted; continuing with LSPosed scope",
                        null);
                return;
            }

            File marker =
                    new File(new File(shared, KILL_SWITCH_DIRECTORY), KILL_SWITCH_MARKER);
            if (marker.isFile()) {
                disabled.set(true);
                log.log("bridge disabled by optional kill switch; stock path retained", null);
            }
        } catch (RuntimeException error) {
            log.log(
                    "optional bridge kill switch is unreadable; continuing with LSPosed scope",
                    error);
        }
    }
}

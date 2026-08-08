/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

/** Lightweight runtime state for the LSPosed-scoped Track-C shim. */
final class BridgeEnvironment {
    interface LogSink {
        void log(String message, Throwable error);
    }

    private enum KillSwitchState {
        ENABLED,
        DISABLED,
        UNKNOWN
    }

    private static final String KILL_SWITCH_DIRECTORY = "Auxio-TS";
    private static final String KILL_SWITCH_MARKER = "disable-lsposed-bridge";
    private static final long KILL_SWITCH_RETRY_MS = 3_000L;

    private final LogSink log;
    private final AtomicReference<KillSwitchState> killSwitch =
            new AtomicReference<>(KillSwitchState.UNKNOWN);
    private final AtomicReference<WeakReference<Context>> context =
            new AtomicReference<>(new WeakReference<>(null));
    private long nextKillSwitchRetryElapsedMs;

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
        return killSwitch.get() == KillSwitchState.ENABLED;
    }

    boolean canPublish(Context value) {
        return canBridge(value, "publish");
    }

    /**
     * The marker is an optional emergency convenience, not an LSPosed scope or identity gate.
     *
     * <p>A confirmed marker disables bridge actions for the rest of the stock-process lifetime. A
     * confirmed readable/mounted state with no marker enables them. UNKNOWN is fail-safe: bridge
     * actions remain disabled and later calls retry the read on a bounded cadence so transient
     * storage failures can recover without restarting {@code com.tw.music}. LSPosed Manager remains
     * the primary disable path.
     */
    private synchronized void checkKillSwitchOnce() {
        KillSwitchState current = killSwitch.get();
        if (current == KillSwitchState.ENABLED || current == KillSwitchState.DISABLED) return;

        long now = SystemClock.elapsedRealtime();
        if (now < nextKillSwitchRetryElapsedMs) return;

        try {
            File shared = Environment.getExternalStorageDirectory();
            if (shared == null || !shared.isDirectory() || !shared.canRead()) {
                unknownUntilRetry(
                        now,
                        "optional bridge kill switch storage is unavailable; bridge remains disabled until a confirmed read",
                        null);
                return;
            }
            String storageState = Environment.getExternalStorageState(shared);
            if (!Environment.MEDIA_MOUNTED.equals(storageState)
                    && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState)) {
                unknownUntilRetry(
                        now,
                        "optional bridge kill switch storage is not mounted; bridge remains disabled until a confirmed read",
                        null);
                return;
            }

            File directory = new File(shared, KILL_SWITCH_DIRECTORY);
            if (directory.exists() && (!directory.isDirectory() || !directory.canRead())) {
                unknownUntilRetry(
                        now,
                        "optional bridge kill switch directory is unreadable or malformed; bridge remains disabled until a confirmed read",
                        null);
                return;
            }

            File marker = new File(directory, KILL_SWITCH_MARKER);
            if (marker.exists()) {
                if (!marker.isFile()) {
                    unknownUntilRetry(
                            now,
                            "optional bridge kill switch marker is malformed; bridge remains disabled until a confirmed read",
                            null);
                    return;
                }
                killSwitch.set(KillSwitchState.DISABLED);
                log.log("bridge disabled by optional kill switch; stock path retained", null);
            } else {
                killSwitch.set(KillSwitchState.ENABLED);
            }
        } catch (RuntimeException error) {
            // A confirmed DISABLED state is terminal for this process; UNKNOWN remains fail-safe and
            // retryable. The early return above means a later read failure cannot erase DISABLED.
            if (killSwitch.get() != KillSwitchState.DISABLED) {
                killSwitch.set(KillSwitchState.UNKNOWN);
                deferUnknownRetry(now);
            }
            log.log(
                    "optional bridge kill switch is unreadable; bridge remains disabled until a confirmed read",
                    error);
        }
    }

    private void unknownUntilRetry(long nowElapsedMs, String message, Throwable error) {
        killSwitch.set(KillSwitchState.UNKNOWN);
        deferUnknownRetry(nowElapsedMs);
        log.log(message, error);
    }

    private void deferUnknownRetry(long nowElapsedMs) {
        nextKillSwitchRetryElapsedMs = nowElapsedMs + KILL_SWITCH_RETRY_MS;
    }
}

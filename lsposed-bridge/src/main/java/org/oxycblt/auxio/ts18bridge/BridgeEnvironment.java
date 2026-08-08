/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/** Exact-build, signer, target and kill-switch authority for the optional Track-C shim. */
final class BridgeEnvironment {
    interface LogSink {
        void log(String message, Throwable error);
    }

    enum KillSwitchState {
        ENABLED,
        DISABLED,
        UNKNOWN
    }

    private static final long CACHE_MS = 3_000L;
    private static final long LOG_REPEAT_MS = 30_000L;
    private static final String KILL_SWITCH_DIRECTORY = "Auxio-TS";
    private static final String KILL_SWITCH_MARKER = "disable-lsposed-bridge";

    private final LogSink log;
    private final ExecutorService probe =
            Executors.newSingleThreadExecutor(
                    task -> {
                        Thread thread = new Thread(task, "AuxioTsBridgeProbe");
                        thread.setDaemon(true);
                        return thread;
                    });
    private final AtomicBoolean pending = new AtomicBoolean();
    private final AtomicReference<State> state = new AtomicReference<>(State.unknown());
    private final AtomicReference<WeakReference<Context>> context =
            new AtomicReference<>(new WeakReference<>(null));
    private String digestKey = "";
    private String digestValue = "";
    private KillSwitchState cachedKillSwitch = KillSwitchState.UNKNOWN;
    private String lastLogKey = "";
    private long lastLogAtMs = Long.MIN_VALUE;

    BridgeEnvironment(LogSink log) {
        this.log = log;
    }

    void remember(Context value) {
        Context app = value.getApplicationContext();
        context.set(new WeakReference<>(app != null ? app : value));
    }

    Context currentContext() {
        return context.get().get();
    }

    boolean canBridge(Context value, String reason) {
        remember(value);
        State current = state.get();
        refreshIfNeeded(value, current);
        return current.canBridge();
    }

    boolean canPublish(Context value) {
        return canBridge(value, "publish");
    }

    boolean canUseObservedPrivateHooks() {
        return canUseCapability(BridgeContract.CAP_PRIVATE_PRESENTER);
    }

    boolean canUseCapability(int capability) {
        State current = state.get();
        return current.canBridge() && current.entry != null && current.entry.has(capability);
    }

    void refreshAsync(Context value, Consumer<Boolean> completion) {
        remember(value);
        Context app = currentContext();
        if (app == null) {
            complete(completion, false);
            return;
        }
        if (!pending.compareAndSet(false, true)) {
            complete(completion, state.get().canBridge());
            return;
        }
        try {
            probe.execute(
                    () -> {
                        boolean trusted = false;
                        try {
                            State updated = inspect(app);
                            state.set(updated);
                            trusted = updated.canBridge();
                            logState(updated);
                        } catch (Throwable error) {
                            state.set(State.unknown(SystemClock.elapsedRealtime()));
                            logBounded(
                                    "readiness-probe-failed",
                                    "bridge readiness probe failed; stock path retained",
                                    error);
                        } finally {
                            pending.set(false);
                            complete(completion, trusted);
                        }
                    });
        } catch (RuntimeException error) {
            state.set(State.unknown(SystemClock.elapsedRealtime()));
            pending.set(false);
            logBounded(
                    "readiness-probe-unscheduled",
                    "bridge readiness probe could not be scheduled; stock path retained",
                    error);
            complete(completion, false);
        }
    }

    private void refreshIfNeeded(Context value, State current) {
        long now = SystemClock.elapsedRealtime();
        if (current.checkedAtMs == 0L
                || now < current.checkedAtMs
                || now - current.checkedAtMs >= CACHE_MS) {
            refreshAsync(value, null);
        }
    }

    private State inspect(Context value) {
        long now = SystemClock.elapsedRealtime();
        KillSwitchState killSwitch = effectiveKillSwitch(readKillSwitch());
        Identity identity = inspectStock(value);
        boolean targetReady = identity.functional && inspectTarget(value.getPackageManager());
        return new State(
                true,
                killSwitch,
                identity.identity,
                identity.functional,
                identity.entry,
                targetReady,
                identity.versionCode,
                now);
    }

    private KillSwitchState effectiveKillSwitch(KillSwitchState observed) {
        if (observed != KillSwitchState.UNKNOWN) {
            cachedKillSwitch = observed;
            return observed;
        }
        if (cachedKillSwitch == KillSwitchState.DISABLED) return KillSwitchState.DISABLED;
        cachedKillSwitch = KillSwitchState.UNKNOWN;
        return KillSwitchState.UNKNOWN;
    }

    private Identity inspectStock(Context value) {
        try {
            PackageInfo info =
                    value.getPackageManager()
                            .getPackageInfo(
                                    BridgeContract.STOCK_PACKAGE,
                                    PackageManager.GET_SIGNING_CERTIFICATES);
            long version = info.getLongVersionCode();
            if (info.applicationInfo == null || info.applicationInfo.uid != Process.SYSTEM_UID) {
                return Identity.untrusted(version);
            }
            if (!matchesCurrentSigner(info.signingInfo, BridgeWireContract.STOCK_CERT_SHA256)) {
                return Identity.untrusted(version);
            }

            BridgeContract.RegistryEntry entry =
                    BridgeContract.reviewedStockApk(apkDigest(info.applicationInfo.sourceDir));
            boolean functional =
                    entry != null
                            && BridgeWireContract.STOCK_CERT_SHA256.equals(entry.signerSha256)
                            && entry.acceptsVersion(version);
            return new Identity(true, functional, entry, version);
        } catch (PackageManager.NameNotFoundException | RuntimeException error) {
            logBounded(
                    "stock-identity-unavailable",
                    "stock identity unavailable; bridge remains inactive",
                    error);
            return Identity.untrusted(0L);
        }
    }

    private boolean inspectTarget(PackageManager manager) {
        try {
            String expected = normalise(BuildConfig.TARGET_EXPECTED_SIGNER);
            if (!isDigest(expected)) return false;
            ApplicationInfo app = manager.getApplicationInfo(BuildConfig.TARGET_PACKAGE, 0);
            if (!app.enabled || app.uid == Process.SYSTEM_UID) return false;

            ActivityInfo activity = manager.getActivityInfo(component(BuildConfig.TARGET_ACTIVITY), 0);
            ServiceInfo service =
                    manager.getServiceInfo(component(BuildConfig.TARGET_MEDIA_BROWSER_SERVICE), 0);
            if (!activity.enabled || !activity.exported || !service.enabled || !service.exported) {
                return false;
            }

            PackageInfo info =
                    manager.getPackageInfo(
                            BuildConfig.TARGET_PACKAGE,
                            PackageManager.GET_SIGNING_CERTIFICATES);
            return matchesCurrentSigner(info.signingInfo, expected);
        } catch (PackageManager.NameNotFoundException | RuntimeException error) {
            return false;
        }
    }

    private static boolean matchesCurrentSigner(SigningInfo info, String expected) {
        if (info == null || !isDigest(expected)) return false;
        Signature[] signers = info.getApkContentsSigners();
        return signers != null
                && signers.length == 1
                && expected.equals(sha256(signers[0].toByteArray()));
    }

    private static ComponentName component(String className) {
        return new ComponentName(BuildConfig.TARGET_PACKAGE, className);
    }

    /**
     * Read the emergency marker only after proving the directory can actually be enumerated.
     *
     * <p>{@link File#exists()} returning false is not sufficient evidence of absence under an OEM
     * mount/permission failure. A null directory listing is therefore UNKNOWN, never ENABLED.
     */
    private KillSwitchState readKillSwitch() {
        try {
            File shared = Environment.getExternalStorageDirectory();
            if (shared == null || !shared.isDirectory() || !shared.canRead()) {
                return KillSwitchState.UNKNOWN;
            }
            String storageState = Environment.getExternalStorageState(shared);
            if (!Environment.MEDIA_MOUNTED.equals(storageState)
                    && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState)) {
                return KillSwitchState.UNKNOWN;
            }

            String[] rootEntries = shared.list();
            if (rootEntries == null) return KillSwitchState.UNKNOWN;
            boolean parentListed = contains(rootEntries, KILL_SWITCH_DIRECTORY);
            if (!parentListed) return KillSwitchState.ENABLED;

            File parent = new File(shared, KILL_SWITCH_DIRECTORY);
            if (!parent.isDirectory() || !parent.canRead()) return KillSwitchState.UNKNOWN;
            String[] markerEntries = parent.list();
            if (markerEntries == null) return KillSwitchState.UNKNOWN;
            if (!contains(markerEntries, KILL_SWITCH_MARKER)) return KillSwitchState.ENABLED;

            File marker = new File(parent, KILL_SWITCH_MARKER);
            return marker.isFile() ? KillSwitchState.DISABLED : KillSwitchState.UNKNOWN;
        } catch (RuntimeException error) {
            return KillSwitchState.UNKNOWN;
        }
    }

    private static boolean contains(String[] entries, String expected) {
        for (String entry : entries) {
            if (expected.equals(entry)) return true;
        }
        return false;
    }

    private String apkDigest(String path) {
        if (path == null || path.isEmpty()) return "";
        File file = new File(path);
        String key = path + '\u0000' + file.length() + '\u0000' + file.lastModified();
        if (key.equals(digestKey)) return digestValue;
        String digest = sha256(file);
        if (!digest.isEmpty()) {
            digestKey = key;
            digestValue = digest;
        }
        return digest;
    }

    private void logState(State value) {
        if (!value.identityTrusted) {
            logBounded(
                    "stock-identity-untrusted",
                    "STOP: com.tw.music UID or current signer differs from stock identity",
                    null);
        } else if (!value.functionalIdentityTrusted) {
            logBounded(
                    "stock-build-unreviewed",
                    "stock identity verified but APK hash/version is not reviewed; functional hooks inactive",
                    null);
        } else if (value.killSwitch == KillSwitchState.DISABLED) {
            logBounded(
                    "kill-switch-disabled",
                    "bridge disabled by kill switch; stock path retained",
                    null);
        } else if (value.killSwitch == KillSwitchState.UNKNOWN) {
            logBounded(
                    "kill-switch-unreadable",
                    "bridge kill switch is unreadable; stock path retained",
                    null);
        } else if (!value.targetReady) {
            logBounded(
                    "target-not-ready",
                    "paired Auxio target unavailable or signer mismatch; stock path retained",
                    null);
        } else {
            clearLogState();
        }
    }

    private synchronized void logBounded(String key, String message, Throwable error) {
        long now = SystemClock.elapsedRealtime();
        if (!key.equals(lastLogKey)
                || lastLogAtMs == Long.MIN_VALUE
                || now < lastLogAtMs
                || now - lastLogAtMs >= LOG_REPEAT_MS) {
            lastLogKey = key;
            lastLogAtMs = now;
            log.log(message, error);
        }
    }

    private synchronized void clearLogState() {
        lastLogKey = "";
        lastLogAtMs = Long.MIN_VALUE;
    }

    private static String sha256(byte[] value) {
        try {
            return hex(MessageDigest.getInstance("SHA-256").digest(value));
        } catch (Exception error) {
            return "";
        }
    }

    private static String sha256(File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[64 * 1024];
            int count;
            while ((count = input.read(buffer)) != -1) digest.update(buffer, 0, count);
            return hex(digest.digest());
        } catch (Exception error) {
            return "";
        }
    }

    private static String hex(byte[] value) {
        StringBuilder result = new StringBuilder(value.length * 2);
        for (byte item : value) {
            result.append(String.format(Locale.ROOT, "%02X", item & 0xff));
        }
        return result.toString();
    }

    private static String normalise(String value) {
        return value == null ? "" : value.replace(":", "").trim().toUpperCase(Locale.ROOT);
    }

    private static boolean isDigest(String value) {
        return value != null && value.matches("[0-9A-F]{64}");
    }

    private void complete(Consumer<Boolean> completion, boolean value) {
        if (completion == null) return;
        try {
            completion.accept(value);
        } catch (RuntimeException error) {
            logBounded(
                    "readiness-completion-failed",
                    "bridge readiness completion failed safely",
                    error);
        }
    }

    private static final class Identity {
        final boolean identity;
        final boolean functional;
        final BridgeContract.RegistryEntry entry;
        final long versionCode;

        Identity(
                boolean identity,
                boolean functional,
                BridgeContract.RegistryEntry entry,
                long versionCode) {
            this.identity = identity;
            this.functional = functional;
            this.entry = entry;
            this.versionCode = versionCode;
        }

        static Identity untrusted(long version) {
            return new Identity(false, false, null, version);
        }
    }

    private static final class State {
        final boolean known;
        final KillSwitchState killSwitch;
        final boolean identityTrusted;
        final boolean functionalIdentityTrusted;
        final BridgeContract.RegistryEntry entry;
        final boolean targetReady;
        final long versionCode;
        final long checkedAtMs;

        State(
                boolean known,
                KillSwitchState killSwitch,
                boolean identityTrusted,
                boolean functionalIdentityTrusted,
                BridgeContract.RegistryEntry entry,
                boolean targetReady,
                long versionCode,
                long checkedAtMs) {
            this.known = known;
            this.killSwitch = killSwitch;
            this.identityTrusted = identityTrusted;
            this.functionalIdentityTrusted = functionalIdentityTrusted;
            this.entry = entry;
            this.targetReady = targetReady;
            this.versionCode = versionCode;
            this.checkedAtMs = checkedAtMs;
        }

        static State unknown() {
            return unknown(0L);
        }

        static State unknown(long checkedAtMs) {
            return new State(
                    false,
                    KillSwitchState.UNKNOWN,
                    false,
                    false,
                    null,
                    false,
                    0L,
                    checkedAtMs);
        }

        boolean canBridge() {
            return known
                    && functionalIdentityTrusted
                    && killSwitch == KillSwitchState.ENABLED
                    && targetReady;
        }
    }
}

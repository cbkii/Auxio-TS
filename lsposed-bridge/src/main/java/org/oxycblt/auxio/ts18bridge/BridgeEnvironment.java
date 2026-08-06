package org.oxycblt.auxio.ts18bridge;

import android.app.Activity;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

final class BridgeEnvironment {

    private static final long READINESS_CACHE_MS = 2_000L;

    private final Executor probeExecutor;
    private final LogSink log;

    private final AtomicReference<WeakReference<Context>> context =
            new AtomicReference<>(new WeakReference<>(null));
    private final AtomicReference<RuntimeState> state =
            new AtomicReference<>(RuntimeState.unknown());
    private final AtomicBoolean refreshPending = new AtomicBoolean(false);

    private String cachedApkDigestKey = "";
    private String cachedApkDigest = "";

    // Store tri-state explicitly
    enum KillSwitchState {
        DISABLED, ENABLED, UNKNOWN
    }

    private KillSwitchState cachedKillSwitchState = KillSwitchState.UNKNOWN;

    BridgeEnvironment(LogSink log) {
        this.probeExecutor = Executors.newSingleThreadExecutor();
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
        RuntimeState current = state.get();
        refreshIfNeeded(value, current);
        return current.canBridge();
    }

    boolean canPublish(Context value) {
        remember(value);
        RuntimeState current = state.get();
        refreshIfNeeded(value, current);
        return current.canBridge();
    }

    boolean canUseObservedPrivateHooks() {
        RuntimeState current = state.get();
        return current.known && current.identityTrusted && current.privateSurfaceTrusted;
    }

    void refreshAsync(Context value, Consumer<Boolean> completion) {
        remember(value);
        Context app = currentContext();
        if (app == null) {
            if (completion != null) completion.accept(false);
            return;
        }
        if (!refreshPending.compareAndSet(false, true)) {
            RuntimeState current = state.get();
            complete(completion, current.known && current.identityTrusted);
            return;
        }
        try {
            probeExecutor.execute(
                    () -> {
                        boolean trusted = false;
                        try {
                            RuntimeState previous = state.get();
                            RuntimeState updated = probe(app);
                            state.set(updated);
                            trusted = updated.identityTrusted;
                            logTransition(previous, updated);
                        } catch (Throwable error) {
                            state.set(RuntimeState.unknown());
                            log.log("bridge readiness probe failed; stock path retained", error);
                        } finally {
                            refreshPending.set(false);
                            complete(completion, trusted);
                        }
                    });
        } catch (RuntimeException error) {
            state.set(RuntimeState.unknown());
            refreshPending.set(false);
            log.log("bridge readiness probe could not be scheduled; stock path retained", error);
            complete(completion, false);
        }
    }

    private void refreshIfNeeded(Context value, RuntimeState current) {
        long now = SystemClock.elapsedRealtime();
        if (!current.known || now - current.checkedAtMs >= READINESS_CACHE_MS) {
            refreshAsync(value, null);
        }
    }

    private RuntimeState probe(Context value) {
        long checkedAtMs = SystemClock.elapsedRealtime();
        KillSwitchState killSwitch = readKillSwitch();

        // Use the explicitly defined state machine rules for kill switch transitions
        if (killSwitch == KillSwitchState.UNKNOWN) {
            if (cachedKillSwitchState == KillSwitchState.DISABLED) {
                // If it was explicitly disabled, retain it on read errors
                killSwitch = KillSwitchState.DISABLED;
            } else if (cachedKillSwitchState == KillSwitchState.ENABLED) {
                // If it was explicitly enabled, but is now unknown, do not retain ENABLED
                // This correctly falls back to stock behaviour on read errors.
                killSwitch = KillSwitchState.UNKNOWN;
                cachedKillSwitchState = KillSwitchState.UNKNOWN;
            }
        } else {
            cachedKillSwitchState = killSwitch;
        }

        boolean disabled = killSwitch != KillSwitchState.ENABLED; // disabled unless explicitly enabled
        IdentityResult identity = queryStockIdentity(value);
        boolean targetReady = identity.trusted && queryTargetReady(value.getPackageManager());
        return new RuntimeState(
                true,
                disabled,
                identity.trusted,
                identity.privateSurfaceTrusted,
                targetReady,
                identity.versionCode,
                checkedAtMs);
    }

    private IdentityResult queryStockIdentity(Context value) {
        try {
            PackageInfo info =
                    value.getPackageManager()
                            .getPackageInfo(
                                    BridgeContract.STOCK_PACKAGE,
                                    PackageManager.GET_SIGNING_CERTIFICATES);
            if (info.applicationInfo == null || info.applicationInfo.uid != Process.SYSTEM_UID) {
                return new IdentityResult(false, false, info.getLongVersionCode());
            }

            long versionCode = info.getLongVersionCode();
            SigningInfo signingInfo = info.signingInfo;
            Signature[] signatures =
                    signingInfo == null
                            ? new Signature[0]
                            : (signingInfo.hasMultipleSigners()
                                    ? signingInfo.getApkContentsSigners()
                                    : signingInfo.getSigningCertificateHistory());
            String expectedCertificate = normalisedDigest(BuildConfig.STOCK_CERT_SHA256);
            if (expectedCertificate.isEmpty()) {
                return new IdentityResult(false, false, versionCode);
            }
            for (Signature signature : signatures) {
                String actualCertificate = sha256(signature.toByteArray());
                if (!actualCertificate.isEmpty()
                        && expectedCertificate.equals(actualCertificate)) {
                    String actualApk = cachedApkDigest(info.applicationInfo.sourceDir);
                    boolean privateSurfaceTrusted =
                            BridgeContract.isReviewedStockApkForPrivateHooks(actualApk);
                    return new IdentityResult(true, privateSurfaceTrusted, versionCode);
                }
            }
        } catch (PackageManager.NameNotFoundException | RuntimeException error) {
            log.log("stock identity query unavailable; bridge remains inactive", error);
        }
        return new IdentityResult(false, false, 0L);
    }

    private String cachedApkDigest(String sourceDir) {
        if (sourceDir == null || sourceDir.isEmpty()) return "";
        File apk = new File(sourceDir);
        String key =
                sourceDir
                        + '\u0000'
                        + apk.length()
                        + '\u0000'
                        + apk.lastModified();
        if (key.equals(cachedApkDigestKey)) return cachedApkDigest;
        String digest = sha256(apk);
        if (!digest.isEmpty()) {
            cachedApkDigest = digest;
            cachedApkDigestKey = key;
        }
        return digest;
    }

    private boolean queryTargetReady(PackageManager manager) {
        try {
            ApplicationInfo target = manager.getApplicationInfo(BuildConfig.TARGET_PACKAGE, 0);
            if (!target.enabled || target.uid == Process.SYSTEM_UID) return false;

            ActivityInfo activity = manager.getActivityInfo(component(BuildConfig.TARGET_ACTIVITY), 0);
            ServiceInfo mediaBrowser =
                    manager.getServiceInfo(component(BuildConfig.TARGET_MEDIA_BROWSER_SERVICE), 0);
            if (!isCrossPackageCallable(activity) || !isCrossPackageCallable(mediaBrowser)) return false;

            // Expected target signer verification
            PackageInfo info = manager.getPackageInfo(BuildConfig.TARGET_PACKAGE, PackageManager.GET_SIGNING_CERTIFICATES);
            SigningInfo signingInfo = info.signingInfo;
            Signature[] signatures =
                    signingInfo == null
                            ? new Signature[0]
                            : (signingInfo.hasMultipleSigners()
                                    ? signingInfo.getApkContentsSigners()
                                    : signingInfo.getSigningCertificateHistory());
            String expectedCertificate = normalisedDigest(BuildConfig.TARGET_EXPECTED_SIGNER);
            if (expectedCertificate.isEmpty()) {
                log.log("Target expected signer is missing.", null);
                return false;
            }

            for (Signature signature : signatures) {
                String actualCertificate = sha256(signature.toByteArray());
                if (!actualCertificate.isEmpty() && expectedCertificate.equals(actualCertificate)) {
                    return true;
                }
            }

            log.log("Target signer verification failed.", null);
            return false;
        } catch (PackageManager.NameNotFoundException | RuntimeException error) {
            return false;
        }
    }

    private static boolean isCrossPackageCallable(android.content.pm.ComponentInfo info) {
        return info.enabled && info.exported;
    }

    private static ComponentName component(String className) {
        return new ComponentName(BuildConfig.TARGET_PACKAGE, className);
    }

    private static KillSwitchState readKillSwitch() {
        try {
            // Intentional on API 29: the UID-1000 host must read the documented shared-storage
            // kill switch without depending on app-scoped storage owned by either APK.
            File shared = Environment.getExternalStorageDirectory();
            if (shared == null) return KillSwitchState.UNKNOWN;

            File killSwitchFile = new File(shared, "Auxio-TS/disable-lsposed-bridge");

            if (killSwitchFile.exists()) {
                return killSwitchFile.isFile() ? KillSwitchState.DISABLED : KillSwitchState.UNKNOWN;
            } else {
                return KillSwitchState.ENABLED;
            }
        } catch (SecurityException error) {
            return KillSwitchState.UNKNOWN;
        } catch (RuntimeException error) {
            return KillSwitchState.UNKNOWN;
        }
    }

    private void logTransition(RuntimeState previous, RuntimeState updated) {
        if (!updated.identityTrusted) {
            if (!previous.known || previous.identityTrusted) {
                log.log(
                        "STOP: com.tw.music UID or signer differs from the captured stock identity",
                        null);
            }
            return;
        }
        if (!previous.known || !previous.identityTrusted) {
            String versionStatus =
                    updated.versionCode == BuildConfig.KNOWN_TESTED_STOCK_VERSION_CODE
                            ? "captured/tested version"
                            : "version not yet device-tested";
            log.log(
                    "verified exact Topway signer and UID 1000; stock version code "
                            + updated.versionCode
                            + " ("
                            + versionStatus
                            + "). Public hook surfaces are capability-probed at runtime",
                    null);
        }
        if (!previous.known
                || previous.privateSurfaceTrusted != updated.privateSurfaceTrusted) {
            log.log(
                    updated.privateSurfaceTrusted
                            ? "captured stock APK fingerprint verified; private presenter hooks eligible"
                            : "stock APK fingerprint is not the captured build; private presenter hooks disabled",
                    null);
        }
        if (!previous.known || previous.disabled != updated.disabled) {
            log.log(
                    updated.disabled
                            ? "bridge disabled by kill switch; stock path retained"
                            : "bridge kill switch is clear",
                    null);
        }
        if (!previous.known || previous.targetReady != updated.targetReady) {
            log.log(
                    updated.targetReady
                            ? "Auxio target components are ready"
                            : "Auxio target unavailable; stock path retained",
                    null);
        }
    }

    private static String sha256(byte[] value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value);
            return hex(digest);
        } catch (Exception error) {
            return "";
        }
    }

    private static String sha256(File value) {
        try (FileInputStream input = new FileInputStream(value)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[64 * 1024];
            int read;
            while ((read = input.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
            return hex(digest.digest());
        } catch (Exception error) {
            return "";
        }
    }

    private static String hex(byte[] value) {
        StringBuilder builder = new StringBuilder(value.length * 2);
        for (byte item : value) {
            builder.append(String.format(Locale.ROOT, "%02X", item & 0xff));
        }
        return builder.toString();
    }

    private static String normalisedDigest(String value) {
        if (value == null) return "";
        return value.replace(":", "").trim().toUpperCase(Locale.ROOT);
    }

    private void complete(Consumer<Boolean> completion, boolean trusted) {
        if (completion == null) return;
        try {
            completion.accept(trusted);
        } catch (RuntimeException error) {
            log.log("bridge readiness completion failed safely", error);
        }
    }

    static final class IdentityResult {
        final boolean trusted;
        final boolean privateSurfaceTrusted;
        final long versionCode;

        IdentityResult(boolean trusted, boolean privateSurfaceTrusted, long versionCode) {
            this.trusted = trusted;
            this.privateSurfaceTrusted = privateSurfaceTrusted;
            this.versionCode = versionCode;
        }
    }

    static final class RuntimeState {
        final boolean known;
        final boolean disabled;
        final boolean identityTrusted;
        final boolean privateSurfaceTrusted;
        final boolean targetReady;
        final long versionCode;
        final long checkedAtMs;

        RuntimeState(
                boolean known,
                boolean disabled,
                boolean identityTrusted,
                boolean privateSurfaceTrusted,
                boolean targetReady,
                long versionCode,
                long checkedAtMs) {
            this.known = known;
            this.disabled = disabled;
            this.identityTrusted = identityTrusted;
            this.privateSurfaceTrusted = privateSurfaceTrusted;
            this.targetReady = targetReady;
            this.versionCode = versionCode;
            this.checkedAtMs = checkedAtMs;
        }

        static RuntimeState unknown() {
            return new RuntimeState(false, false, false, false, false, 0L, 0L);
        }

        boolean canBridge() {
            return known && identityTrusted && !disabled && targetReady;
        }
    }

    @FunctionalInterface
    interface LogSink {
        void log(String message, Throwable error);
    }
}

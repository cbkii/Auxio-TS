/*
 * Copyright (c) 2026 Auxio Project
 * BridgeEnvironment.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
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
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/** Runtime identity, target-readiness and kill-switch authority for the bridge. */
final class BridgeEnvironment {
    interface LogSink {
        void log(String message, Throwable error);
    }

    private static final long READINESS_CACHE_MS = 3_000L;

    private final LogSink log;
    private final AtomicBoolean identityVerified = new AtomicBoolean();
    private final AtomicBoolean identityRejected = new AtomicBoolean();
    private final AtomicReference<WeakReference<Context>> context =
            new AtomicReference<>(new WeakReference<>(null));

    private volatile long readinessCheckedAtMs;
    private volatile boolean cachedTargetReady;

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
        if (isDisabled()) {
            log.log("bridge disabled by kill switch during " + reason + "; stock path retained", null);
            return false;
        }
        if (!verifyStockIdentity(value)) return false;
        if (!isTargetReady(value)) {
            log.log("Auxio target unavailable during " + reason + "; stock path retained", null);
            return false;
        }
        return true;
    }

    boolean canPublish(Context value) {
        if (isDisabled()) return false;
        return verifyStockIdentity(value) && isTargetReady(value);
    }

    boolean verifyStockIdentity(Context value) {
        if (identityVerified.get()) return true;
        if (identityRejected.get()) return false;
        try {
            PackageInfo info =
                    value.getPackageManager()
                            .getPackageInfo(
                                    BridgeContract.STOCK_PACKAGE,
                                    PackageManager.GET_SIGNING_CERTIFICATES);
            if (info.applicationInfo == null || info.applicationInfo.uid != Process.SYSTEM_UID) {
                identityRejected.set(true);
                log.log("STOP: stock com.tw.music is not UID 1000", null);
                return false;
            }

            long versionCode = info.getLongVersionCode();
            SigningInfo signingInfo = info.signingInfo;
            Signature[] signatures =
                    signingInfo == null
                            ? new Signature[0]
                            : (signingInfo.hasMultipleSigners()
                                    ? signingInfo.getApkContentsSigners()
                                    : signingInfo.getSigningCertificateHistory());
            for (Signature signature : signatures) {
                if (BuildConfig.STOCK_CERT_SHA256.equals(sha256(signature.toByteArray()))) {
                    identityVerified.set(true);
                    String versionStatus =
                            versionCode == BuildConfig.KNOWN_TESTED_STOCK_VERSION_CODE
                                    ? "captured/tested version"
                                    : "version not yet device-tested";
                    log.log(
                            "verified exact Topway signer and UID 1000; stock version code "
                                    + versionCode
                                    + " ("
                                    + versionStatus
                                    + "). Exact hook surfaces are capability-probed at runtime",
                            null);
                    return true;
                }
            }
            identityRejected.set(true);
            log.log("STOP: stock signer differs from the captured Topway platform certificate", null);
        } catch (PackageManager.NameNotFoundException | RuntimeException error) {
            log.log("stock identity query unavailable; bridge remains inactive", error);
        }
        return false;
    }

    boolean isTargetReady(Context value) {
        long now = SystemClock.elapsedRealtime();
        if (readinessCheckedAtMs != 0L && now - readinessCheckedAtMs < READINESS_CACHE_MS) {
            return cachedTargetReady;
        }
        synchronized (this) {
            now = SystemClock.elapsedRealtime();
            if (readinessCheckedAtMs != 0L && now - readinessCheckedAtMs < READINESS_CACHE_MS) {
                return cachedTargetReady;
            }
            cachedTargetReady = queryTargetReady(value.getPackageManager());
            readinessCheckedAtMs = now;
            return cachedTargetReady;
        }
    }

    private boolean queryTargetReady(PackageManager manager) {
        try {
            ApplicationInfo target = manager.getApplicationInfo(BuildConfig.TARGET_PACKAGE, 0);
            if (!target.enabled || target.uid == Process.SYSTEM_UID) return false;

            ActivityInfo activity = manager.getActivityInfo(component(BuildConfig.TARGET_ACTIVITY), 0);
            ServiceInfo mediaBrowser =
                    manager.getServiceInfo(component(BuildConfig.TARGET_MEDIA_BROWSER_SERVICE), 0);

            return isCrossPackageCallable(activity) && isCrossPackageCallable(mediaBrowser);
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

    private static boolean isDisabled() {
        try {
            File shared = Environment.getExternalStorageDirectory();
            return new File(shared, "Auxio-TS/disable-lsposed-bridge").isFile();
        } catch (RuntimeException error) {
            return false;
        }
    }

    private static String sha256(byte[] value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value);
            StringBuilder builder = new StringBuilder(digest.length * 2);
            for (byte item : digest) {
                builder.append(String.format(Locale.ROOT, "%02X", item & 0xff));
            }
            return builder.toString();
        } catch (Exception error) {
            return "";
        }
    }
}

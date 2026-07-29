/*
 * Copyright (c) 2026 Auxio Project
 * Ts18LsposedBridgeModule.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import io.github.libxposed.XposedContext;
import io.github.libxposed.XposedInterface;
import io.github.libxposed.XposedModule;
import io.github.libxposed.XposedModuleInterface;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Narrow API-100 LSPosed bridge for the genuine Topway {@code com.tw.music} process.
 *
 * <p>The module preserves the stock platform signer and UID 1000. It does not hook Package Manager
 * or system_server, edit shared-UID state, replace the stock APK, or grant Auxio privileged identity.
 */
public final class Ts18LsposedBridgeModule extends XposedModule {
    private static final String STOCK_APPLICATION = "com.tw.music.MusicApplication";
    private static final String STOCK_ACTIVITY = "com.tw.music.MusicActivity";
    private static final String STOCK_SERVICE = "com.tw.music.MusicService";
    private static final String STOCK_COMMAND_RECEIVER = "com.tw.music.k";
    private static final String STOCK_SEEK_RECEIVER = "com.tw.music.j";
    private static final String STOCK_PRESENTER = "com.eckom.xtlibrary.b.f.e.a";
    private static final String EXTRA_COMMAND = "cmd";
    private static final String EXTRA_WIDGET_PROGRESS = "music_progress";
    private static final long LOG_WINDOW_MS = 10_000L;
    private static final int MAX_LOGS_PER_WINDOW = 4;

    private final AtomicBoolean hooksInstalled = new AtomicBoolean();
    private final AtomicReference<MediaMirror> mediaMirror = new AtomicReference<>();
    private final Map<String, LogWindow> logWindows = new ConcurrentHashMap<>();
    private final BridgeEnvironment environment =
            new BridgeEnvironment(
                    (message, error) -> {
                        if (error == null) safeLog(message);
                        else safeLog(message, error);
                    });

    public Ts18LsposedBridgeModule(
            XposedContext base, XposedModuleInterface.ModuleLoadedParam param) {
        super(base, param);
        safeLog(
                "loaded API="
                        + getAPIVersion()
                        + " framework="
                        + safeFrameworkName()
                        + " process="
                        + param.getProcessName());
    }

    @Override
    public void onPackageLoaded(XposedModuleInterface.PackageLoadedParam param) {
        try {
            if (!BridgeContract.STOCK_PACKAGE.equals(param.getPackageName())) return;
            if (!param.isFirstApplication()) return;
            if (!BridgeContract.STOCK_PACKAGE.equals(param.getProcessName())) {
                safeLog("skip non-main process " + param.getProcessName());
                return;
            }
            ApplicationInfo appInfo = param.getAppInfo();
            if (appInfo.uid != Process.SYSTEM_UID) {
                safeLog("STOP: com.tw.music is not UID 1000; no hooks installed");
                return;
            }
            if (!hooksInstalled.compareAndSet(false, true)) return;

            ClassLoader loader = param.getClassLoader();
            installSafely("application", () -> installApplicationHook(loader));
            installSafely("activity", () -> installActivityHooks(loader));
            installSafely("service", () -> installServiceHooks(loader));
            installSafely("receivers", () -> installReceiverHooks(loader));
            installSafely("presenter", () -> installPresenterHooks(loader));
            safeLog(
                    "hook capability-probe pass complete; unmatched stock-version surfaces remain stock-controlled");
        } catch (Throwable error) {
            safeLog("hook installation failed closed", error);
        }
    }

    private void installSafely(String group, ThrowingInstall install) {
        try {
            install.run();
            safeLog("installed " + group + " hook group");
        } catch (Throwable error) {
            safeLog(group + " hook group unavailable; preserving stock behaviour", error);
        }
    }

    private void installApplicationHook(ClassLoader loader) throws ReflectiveOperationException {
        Method onCreate = requiredMethod(loader, STOCK_APPLICATION, "onCreate");
        hookAfter(
                onCreate,
                callback -> {
                    try {
                        Object target = callback.getThis();
                        if (target instanceof Application application) captureAndStart(application);
                    } catch (Throwable error) {
                        safeLog("application hook failed open", error);
                    }
                });
    }

    private void installActivityHooks(ClassLoader loader) throws ReflectiveOperationException {
        Method onCreate = requiredMethod(loader, STOCK_ACTIVITY, "onCreate", Bundle.class);
        hookAfter(
                onCreate,
                callback -> {
                    try {
                        Object target = callback.getThis();
                        if (target instanceof Activity activity) redirectActivity(activity, "onCreate");
                    } catch (Throwable error) {
                        safeLog("activity onCreate redirect failed open", error);
                    }
                });

        try {
            Method onNewIntent = inheritedMethod(loader, STOCK_ACTIVITY, "onNewIntent", Intent.class);
            hookAfter(
                    onNewIntent,
                    callback -> {
                        try {
                            Object target = callback.getThis();
                            if (target instanceof Activity activity
                                    && STOCK_ACTIVITY.equals(activity.getClass().getName())) {
                                redirectActivity(activity, "onNewIntent");
                            }
                        } catch (Throwable error) {
                            safeLog("activity onNewIntent redirect failed open", error);
                        }
                    });
        } catch (ReflectiveOperationException error) {
            safeLog("optional onNewIntent hook unavailable", error);
        }
    }

    private void installServiceHooks(ClassLoader loader) throws ReflectiveOperationException {
        Method onCreate = requiredMethod(loader, STOCK_SERVICE, "onCreate");
        hookAfter(
                onCreate,
                callback -> {
                    try {
                        Object target = callback.getThis();
                        if (target instanceof Service service) captureAndStart(service);
                    } catch (Throwable error) {
                        safeLog("service context capture failed open", error);
                    }
                });

        Method onStartCommand =
                requiredMethod(
                        loader,
                        STOCK_SERVICE,
                        "onStartCommand",
                        Intent.class,
                        int.class,
                        int.class);
        hookBefore(
                onStartCommand,
                callback -> {
                    try {
                        Object target = callback.getThis();
                        Intent intent = argument(callback, 0, Intent.class);
                        if (target instanceof Service service
                                && intent != null
                                && forwardObservedIntent(service, intent, "service-onStartCommand")) {
                            callback.returnAndSkip(Service.START_NOT_STICKY);
                        }
                    } catch (Throwable error) {
                        safeLog("service command hook failed open", error);
                    }
                });
    }

    private void installReceiverHooks(ClassLoader loader) throws ReflectiveOperationException {
        hookReceiver(loader, STOCK_COMMAND_RECEIVER, "stock-command-receiver");
        hookReceiver(loader, STOCK_SEEK_RECEIVER, "stock-seek-receiver");
    }

    private void hookReceiver(ClassLoader loader, String className, String source)
            throws ReflectiveOperationException {
        Method onReceive =
                requiredMethod(loader, className, "onReceive", Context.class, Intent.class);
        hookBefore(
                onReceive,
                callback -> {
                    try {
                        Context context = argument(callback, 0, Context.class);
                        Intent intent = argument(callback, 1, Intent.class);
                        if (context != null
                                && intent != null
                                && forwardObservedIntent(context, intent, source)) {
                            callback.returnAndSkip(null);
                        }
                    } catch (Throwable error) {
                        safeLog(source + " hook failed open", error);
                    }
                });
    }

    private void installPresenterHooks(ClassLoader loader) {
        installPresenterMethod(loader, "rb");
        installPresenterMethod(loader, "pb");
        installPresenterMethod(loader, "ba");
        installPresenterMethod(loader, "fa");
        installPresenterMethod(loader, "seekTo", int.class);
    }

    private void installPresenterMethod(
            ClassLoader loader, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = requiredMethod(loader, STOCK_PRESENTER, methodName, parameterTypes);
            try {
                deoptimize(method);
            } catch (RuntimeException error) {
                safeLog("presenter deoptimisation unavailable for " + methodName, error);
            }
            hookBefore(
                    method,
                    callback -> {
                        try {
                            Context context = environment.currentContext();
                            if (context == null) return;
                            BridgeCommand command = BridgeCommand.fromPresenterMethod(methodName);
                            Integer seek =
                                    command == BridgeCommand.SEEK
                                            ? safeIntArgument(callback, 0)
                                            : null;
                            if (forwardCommand(
                                    context, command, seek, "presenter-" + methodName)) {
                                callback.returnAndSkip(null);
                            }
                        } catch (Throwable error) {
                            safeLog("presenter hook " + methodName + " failed open", error);
                        }
                    });
        } catch (Throwable error) {
            safeLog(
                    "presenter method "
                            + methodName
                            + " unavailable; stock fallback retained",
                    error);
        }
    }

    private void captureAndStart(Context context) {
        environment.remember(context);
        if (environment.canBridge(context, "process-start")) ensureMediaMirror(context);
    }

    private void redirectActivity(Activity activity, String source) {
        if (!environment.canBridge(activity, "activity-" + source)) return;
        ensureMediaMirror(activity);
        Intent target =
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_LAUNCHER)
                        .setComponent(
                                new ComponentName(
                                        BuildConfig.TARGET_PACKAGE, BuildConfig.TARGET_ACTIVITY))
                        .addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        try {
            activity.startActivity(target);
            activity.finish();
            rateLimitedLog("activity", "redirected stock MusicActivity via " + source);
        } catch (RuntimeException error) {
            safeLog("Auxio activity redirect failed open", error);
        }
    }

    private boolean forwardObservedIntent(Context context, Intent intent, String source) {
        BridgeCommand command =
                BridgeCommand.fromIntent(intent.getAction(), safeStringExtra(intent, EXTRA_COMMAND));
        if (command == BridgeCommand.UNKNOWN) return false;
        Integer seek =
                command == BridgeCommand.SEEK
                        ? Math.max(0, safeIntExtra(intent, EXTRA_WIDGET_PROGRESS, 0))
                        : null;
        return forwardCommand(context, command, seek, source);
    }

    private boolean forwardCommand(
            Context context, BridgeCommand command, Integer seekPosition, String source) {
        if (!environment.canBridge(context, source)) {
            MediaMirror mirror = mediaMirror.get();
            if (mirror != null) mirror.pauseUntilRetried();
            return false;
        }

        ensureMediaMirror(context);
        MediaMirror mirror = mediaMirror.get();
        if (mirror == null) return false;

        try {
            boolean dispatched = mirror.dispatchCommand(command, seekPosition);
            if (dispatched) {
                rateLimitedLog(
                        "command-" + command,
                        "dispatched " + command + " to Auxio MediaSession from " + source);
            } else {
                rateLimitedLog(
                        "command-not-ready-" + command,
                        "Auxio MediaSession not ready for "
                                + command
                                + " from "
                                + source
                                + "; stock path retained");
            }
            return dispatched;
        } catch (RuntimeException error) {
            safeLog("command forwarding failed open for " + command, error);
            return false;
        }
    }

    private void ensureMediaMirror(Context context) {
        MediaMirror existing = mediaMirror.get();
        if (existing != null) {
            existing.startOrRetry();
            return;
        }
        MediaMirror candidate =
                new MediaMirror(
                        context,
                        environment,
                        (message, error) -> {
                            if (error == null) safeLog(message);
                            else safeLog(message, error);
                        });
        if (mediaMirror.compareAndSet(null, candidate)) candidate.startOrRetry();
        else {
            MediaMirror winner = mediaMirror.get();
            if (winner != null) winner.startOrRetry();
        }
    }

    private static Method requiredMethod(
            ClassLoader loader, String className, String methodName, Class<?>... parameterTypes)
            throws ReflectiveOperationException {
        Class<?> type = Class.forName(className, false, loader);
        Method method = type.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private static Method inheritedMethod(
            ClassLoader loader, String className, String methodName, Class<?>... parameterTypes)
            throws ReflectiveOperationException {
        Class<?> current = Class.forName(className, false, loader);
        while (current != null) {
            try {
                Method method = current.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchMethodException(className + "#" + methodName);
    }

    private static <T> T argument(
            XposedInterface.BeforeHookCallback<Method> callback, int index, Class<T> type) {
        Object[] arguments = callback.getArgs();
        if (index < 0 || index >= arguments.length || !type.isInstance(arguments[index])) return null;
        return type.cast(arguments[index]);
    }

    private static Integer safeIntArgument(
            XposedInterface.BeforeHookCallback<Method> callback, int index) {
        Object[] arguments = callback.getArgs();
        if (index < 0 || index >= arguments.length || !(arguments[index] instanceof Number number)) {
            return null;
        }
        return Math.max(0, number.intValue());
    }

    private static String safeStringExtra(Intent intent, String key) {
        try {
            return intent.getStringExtra(key);
        } catch (RuntimeException error) {
            return null;
        }
    }

    private static int safeIntExtra(Intent intent, String key, int fallback) {
        try {
            return intent.getIntExtra(key, fallback);
        } catch (RuntimeException error) {
            return fallback;
        }
    }

    private String safeFrameworkName() {
        try {
            return getFrameworkName();
        } catch (RuntimeException error) {
            return "unknown";
        }
    }

    private void rateLimitedLog(String key, String message) {
        long now = SystemClock.elapsedRealtime();
        LogWindow window = logWindows.computeIfAbsent(key, unused -> new LogWindow(now));
        synchronized (window) {
            if (now - window.startedAtMs >= LOG_WINDOW_MS) {
                window.startedAtMs = now;
                window.count = 0;
            }
            if (window.count >= MAX_LOGS_PER_WINDOW) return;
            window.count += 1;
        }
        safeLog(message);
    }

    private void safeLog(String message) {
        try {
            log("Auxio-TS LSPosed bridge: " + message);
        } catch (RuntimeException ignored) {
            // Logging must never make the platform-signed host process fail.
        }
    }

    private void safeLog(String message, Throwable error) {
        try {
            log("Auxio-TS LSPosed bridge: " + message, error);
        } catch (RuntimeException ignored) {
            // Logging must never make the platform-signed host process fail.
        }
    }

    @FunctionalInterface
    private interface ThrowingInstall {
        void run() throws Exception;
    }

    private static final class LogWindow {
        long startedAtMs;
        int count;

        LogWindow(long startedAtMs) {
            this.startedAtMs = startedAtMs;
        }
    }
}

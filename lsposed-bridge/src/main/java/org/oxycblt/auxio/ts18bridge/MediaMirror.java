/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.ts18bridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/** Fail-open state mirror plus bounded acknowledged command transport into Auxio. */
final class MediaMirror {
    interface LogSink {
        void log(String message, Throwable error);
    }

    private static final String ACTION_MUSIC_INFO = "com.tw.music.info";
    private static final String ACTION_PROGRESS = "com.tw.launcher.music_progress_duration";
    private static final String ACTION_LEGACY_METADATA = "com.android.music.metachanged";
    private static final String ACTION_LEGACY_PLAYSTATE = "com.android.music.playstatechanged";
    private static final long TICK_MS = 1_000L;
    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final int MAX_METADATA_CHARS = 1_024;
    private static final long COMMAND_WAIT_TIMEOUT_MS = 120L;
    private static final long SERVER_DEADLINE_MS = 75L;
    private static final long COMMAND_BIND_TIMEOUT_MS = 5_000L;
    private static final long CLIENT_GENERATION = createClientGeneration();

    private final Context context;
    private final BridgeEnvironment environment;
    private final LogSink log;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final AtomicBoolean started = new AtomicBoolean();
    private final AtomicBoolean stopped = new AtomicBoolean();
    private final AtomicBoolean commandCircuitOpen = new AtomicBoolean();
    private final AtomicBoolean loggedMainThreadFallback = new AtomicBoolean();
    private final BridgeDispatchCorrelation correlation = new BridgeDispatchCorrelation();
    private final ThreadPoolExecutor commandExecutor =
            new ThreadPoolExecutor(
                    1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1),
                    task -> {
                        Thread thread = new Thread(task, "AuxioTsBridgeCommand");
                        thread.setDaemon(true);
                        return thread;
                    },
                    new ThreadPoolExecutor.AbortPolicy());

    private MediaBrowser browser;
    private volatile MediaController controller;
    private boolean browserConnectionPending;
    private int reconnectAttempts;
    private int commandReconnectAttempts;
    private boolean commandBindRequested;
    private volatile IBinder commandBinder;

    private final IBinder.DeathRecipient commandDeathRecipient =
            () -> handler.post(() -> commandEndpointFailed("command Binder died", null));

    private final Runnable reconnect = this::connect;
    private final Runnable commandReconnect = this::connectCommandService;
    private final Runnable commandBindTimeout =
            () -> {
                if (!commandBindRequested || commandBinder != null) return;
                commandEndpointFailed("command endpoint bind timed out", null);
            };

    private final ServiceConnection commandConnection =
            new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    handler.removeCallbacks(commandBindTimeout);
                    clearCommandBinder();
                    if (stopped.get() || commandCircuitOpen.get()) {
                        disconnectCommandService();
                        return;
                    }
                    try {
                        service.linkToDeath(commandDeathRecipient, 0);
                        commandBinder = service;
                        commandReconnectAttempts = 0;
                        handler.removeCallbacks(commandReconnect);
                    } catch (RemoteException | RuntimeException error) {
                        commandEndpointFailed(
                                "command Binder died during connection", error);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    commandEndpointFailed("command endpoint disconnected", null);
                }

                @Override
                public void onBindingDied(ComponentName name) {
                    commandEndpointFailed("command endpoint binding died", null);
                }

                @Override
                public void onNullBinding(ComponentName name) {
                    commandEndpointFailed("command endpoint returned null binding", null);
                }
            };

    private final Runnable progressTick =
            new Runnable() {
                @Override
                public void run() {
                    if (stopped.get() || controller == null) return;
                    publishProgress();
                    if (isPlaying(controller.getPlaybackState())) {
                        handler.postDelayed(this, TICK_MS);
                    }
                }
            };

    private final MediaController.Callback controllerCallback =
            new MediaController.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadata metadata) {
                    publishMetadata(metadata);
                    publishProgress();
                }

                @Override
                public void onPlaybackStateChanged(PlaybackState state) {
                    publishPlayState(state);
                    publishProgress();
                    handler.removeCallbacks(progressTick);
                    if (isPlaying(state)) handler.postDelayed(progressTick, TICK_MS);
                }

                @Override
                public void onSessionDestroyed() {
                    clearController();
                    scheduleReconnect();
                }
            };

    private final MediaBrowser.ConnectionCallback browserCallback =
            new MediaBrowser.ConnectionCallback() {
                @Override
                public void onConnected() {
                    browserConnectionPending = false;
                    if (stopped.get() || browser == null || !browser.isConnected()) return;
                    try {
                        clearController();
                        controller = new MediaController(context, browser.getSessionToken());
                        controller.registerCallback(controllerCallback, handler);
                        reconnectAttempts = 0;
                        publishSnapshot();
                        connectCommandService();
                    } catch (RuntimeException error) {
                        log.log("MediaController creation failed", error);
                        clearController();
                        scheduleReconnect();
                    }
                }

                @Override
                public void onConnectionSuspended() {
                    browserConnectionPending = false;
                    clearController();
                    scheduleReconnect();
                }

                @Override
                public void onConnectionFailed() {
                    browserConnectionPending = false;
                    clearController();
                    scheduleReconnect();
                }
            };

    MediaMirror(Context context, BridgeEnvironment environment, LogSink log) {
        Context app = context.getApplicationContext();
        this.context = app != null ? app : context;
        this.environment = environment;
        this.log = log;
        commandExecutor.prestartCoreThread();
    }

    void startOrRetry() {
        if (!stopped.get()) handler.post(this::startOrRetryOnHandler);
    }

    private void startOrRetryOnHandler() {
        if (stopped.get()) return;
        if (started.compareAndSet(false, true)) {
            reconnectAttempts = 0;
            commandReconnectAttempts = 0;
            connect();
        } else if (!browserConnectionPending
                && controller == null
                && (browser == null || !browser.isConnected())) {
            reconnectAttempts = 0;
            handler.removeCallbacks(reconnect);
            connect();
        } else if (!commandCircuitOpen.get()) {
            connectCommandService();
        }
    }

    void pauseUntilRetried() {
        handler.post(
                () -> {
                    handler.removeCallbacks(reconnect);
                    handler.removeCallbacks(commandReconnect);
                    handler.removeCallbacks(commandBindTimeout);
                    handler.removeCallbacks(progressTick);
                    disconnectCommandService();
                    disconnectBrowser();
                    clearController();
                    started.set(false);
                });
    }

    void stop() {
        stopped.set(true);
        commandCircuitOpen.set(true);
        commandExecutor.shutdownNow();
        handler.post(
                () -> {
                    handler.removeCallbacksAndMessages(null);
                    disconnectCommandService();
                    disconnectBrowser();
                    clearController();
                    started.set(false);
                });
    }

    boolean dispatchCommand(BridgeCommand command, Integer seekPosition) {
        if (command == BridgeCommand.UNKNOWN
                || stopped.get()
                || !environment.canPublish(context)) return false;
        if (command == BridgeCommand.UPDATE) return publishSnapshot();

        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (loggedMainThreadFallback.compareAndSet(false, true)) {
                log.log(
                        "command hook is on stock main looper; command suppression remains fail-open",
                        null);
            }
            return false;
        }
        if (commandCircuitOpen.get()) return false;

        int commandCode = BridgeContract.commandCode(command);
        if (commandCode == 0 || (command == BridgeCommand.SEEK && seekPosition == null)) {
            return false;
        }
        long seekMs =
                command == BridgeCommand.SEEK ? Math.max(0L, seekPosition.longValue()) : -1L;
        IBinder target = commandBinder;
        if (target == null || !target.isBinderAlive()) {
            startOrRetry();
            return false;
        }

        long now = SystemClock.elapsedRealtime();
        BridgeDispatchCorrelation.Decision decision = correlation.begin(commandCode, seekMs, now);
        if (decision.alreadyAccepted) return true;

        Future<Integer> future;
        try {
            future =
                    commandExecutor.submit(
                            () -> transact(target, decision.commandId, commandCode, seekMs, now));
        } catch (RejectedExecutionException error) {
            correlation.complete(decision, false, SystemClock.elapsedRealtime());
            return false;
        }

        try {
            int result = future.get(COMMAND_WAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            boolean accepted = BridgeWireContract.isAcceptedResult(result);
            correlation.complete(decision, accepted, SystemClock.elapsedRealtime());
            return accepted;
        } catch (TimeoutException error) {
            correlation.complete(decision, false, SystemClock.elapsedRealtime());
            future.cancel(true);
            tripCircuit(
                    "command Binder timed out; suppression disabled for this stock process", error);
            return false;
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            correlation.complete(decision, false, SystemClock.elapsedRealtime());
            future.cancel(true);
            return false;
        } catch (ExecutionException error) {
            correlation.complete(decision, false, SystemClock.elapsedRealtime());
            handler.post(
                    () ->
                            commandEndpointFailed(
                                    "command Binder execution failed", error.getCause()));
            return false;
        }
    }

    private int transact(
            IBinder target, long commandId, int commandCode, long seekMs, long createdAtMs)
            throws RemoteException {
        if (commandCircuitOpen.get() || !target.isBinderAlive()) {
            return BridgeWireContract.RESULT_NOT_READY;
        }
        long deadlineMs = createdAtMs + SERVER_DEADLINE_MS;
        if (SystemClock.elapsedRealtime() >= deadlineMs) return BridgeWireContract.RESULT_EXPIRED;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(BridgeWireContract.BINDER_DESCRIPTOR);
            data.writeInt(BridgeWireContract.PROTOCOL_VERSION);
            data.writeLong(commandId);
            data.writeInt(commandCode);
            data.writeLong(seekMs);
            data.writeInt(BridgeWireContract.SOURCE_STOCK_SHIM);
            data.writeLong(CLIENT_GENERATION);
            data.writeLong(createdAtMs);
            data.writeLong(deadlineMs);
            if (!target.transact(BridgeWireContract.TRANSACTION_DISPATCH, data, reply, 0)) {
                return BridgeWireContract.RESULT_NOT_READY;
            }
            reply.readException();
            return reply.readInt();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    private void tripCircuit(String message, Throwable error) {
        if (!commandCircuitOpen.compareAndSet(false, true)) return;
        log.log(message, error);
        commandExecutor.shutdownNow();
        handler.post(
                () -> {
                    handler.removeCallbacks(commandReconnect);
                    handler.removeCallbacks(commandBindTimeout);
                    disconnectCommandService();
                });
    }

    private void connect() {
        if (stopped.get() || !environment.canPublish(context)) {
            started.set(false);
            return;
        }
        disconnectBrowser();
        try {
            browserConnectionPending = true;
            browser =
                    new MediaBrowser(
                            context,
                            new ComponentName(
                                    BuildConfig.TARGET_PACKAGE,
                                    BuildConfig.TARGET_MEDIA_BROWSER_SERVICE),
                            browserCallback,
                            null);
            browser.connect();
            connectCommandService();
        } catch (RuntimeException error) {
            browserConnectionPending = false;
            log.log("MediaBrowser connect failed", error);
            scheduleReconnect();
        }
    }

    private void connectCommandService() {
        if (stopped.get()
                || commandCircuitOpen.get()
                || commandBindRequested
                || commandBinder != null
                || !environment.canPublish(context)) return;
        Intent intent =
                new Intent(BridgeWireContract.ACTION_BIND_COMMAND)
                        .setComponent(
                                new ComponentName(
                                        BuildConfig.TARGET_PACKAGE,
                                        BuildConfig.TARGET_MEDIA_BROWSER_SERVICE));
        try {
            commandBindRequested =
                    context.bindService(intent, commandConnection, Context.BIND_AUTO_CREATE);
            if (commandBindRequested) {
                handler.removeCallbacks(commandBindTimeout);
                handler.postDelayed(commandBindTimeout, COMMAND_BIND_TIMEOUT_MS);
            } else {
                log.log("command endpoint bind rejected; stock path retained", null);
                scheduleCommandReconnect();
            }
        } catch (RuntimeException error) {
            commandBindRequested = false;
            log.log("command endpoint bind failed; stock path retained", error);
            scheduleCommandReconnect();
        }
    }

    private void commandEndpointFailed(String message, Throwable error) {
        log.log(message, error);
        disconnectCommandService();
        scheduleCommandReconnect();
    }

    private void scheduleCommandReconnect() {
        if (stopped.get() || commandCircuitOpen.get()) return;
        if (commandReconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
            log.log("command endpoint reconnect limit reached; stock path retained", null);
            return;
        }
        commandReconnectAttempts += 1;
        handler.removeCallbacks(commandReconnect);
        handler.postDelayed(
                commandReconnect, Math.min(30_000L, 2_000L * commandReconnectAttempts));
    }

    private void scheduleReconnect() {
        if (stopped.get()) return;
        disconnectBrowser();
        disconnectCommandService();
        handler.removeCallbacks(commandReconnect);
        handler.removeCallbacks(commandBindTimeout);
        commandReconnectAttempts = 0;
        if (reconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
            started.set(false);
            return;
        }
        reconnectAttempts += 1;
        handler.removeCallbacks(reconnect);
        handler.postDelayed(reconnect, Math.min(30_000L, 2_000L * reconnectAttempts));
    }

    private void disconnectBrowser() {
        browserConnectionPending = false;
        MediaBrowser current = browser;
        browser = null;
        if (current != null) {
            try {
                current.disconnect();
            } catch (RuntimeException ignored) {
                // Best-effort host-process cleanup.
            }
        }
    }

    private void disconnectCommandService() {
        handler.removeCallbacks(commandBindTimeout);
        clearCommandBinder();
        if (!commandBindRequested) return;
        commandBindRequested = false;
        try {
            context.unbindService(commandConnection);
        } catch (IllegalArgumentException | IllegalStateException error) {
            log.log("command endpoint was already unbound", error);
        }
    }

    private void clearCommandBinder() {
        IBinder current = commandBinder;
        commandBinder = null;
        if (current != null) {
            try {
                current.unlinkToDeath(commandDeathRecipient, 0);
            } catch (RuntimeException ignored) {
                // Binder was already dead or detached.
            }
        }
    }

    private void clearController() {
        handler.removeCallbacks(progressTick);
        MediaController current = controller;
        controller = null;
        if (current != null) {
            try {
                current.unregisterCallback(controllerCallback);
            } catch (RuntimeException ignored) {
                // Best-effort host-process cleanup.
            }
        }
    }

    private boolean publishSnapshot() {
        MediaController current = controller;
        if (current == null || stopped.get()) return false;
        return publishMetadata(current.getMetadata())
                & publishPlayState(current.getPlaybackState())
                & publishProgress();
    }

    private boolean canPublish() {
        return !stopped.get() && environment.canPublish(context);
    }

    private boolean publishMetadata(MediaMetadata metadata) {
        if (!canPublish()) return false;
        String title = "";
        String artist = "";
        String album = "";
        String mediaUri = "";
        long duration = 0L;
        if (metadata != null) {
            CharSequence displayTitle = metadata.getText(MediaMetadata.METADATA_KEY_DISPLAY_TITLE);
            title =
                    boundedText(
                            displayTitle != null
                                    ? displayTitle
                                    : metadata.getText(MediaMetadata.METADATA_KEY_TITLE));
            artist = boundedText(metadata.getText(MediaMetadata.METADATA_KEY_ARTIST));
            album = boundedText(metadata.getText(MediaMetadata.METADATA_KEY_ALBUM));
            mediaUri = boundedText(metadata.getString(MediaMetadata.METADATA_KEY_MEDIA_URI));
            duration = Math.max(0L, metadata.getLong(MediaMetadata.METADATA_KEY_DURATION));
        }
        MediaController current = controller;
        boolean playing = isPlaying(current != null ? current.getPlaybackState() : null);
        try {
            context.sendBroadcast(
                    new Intent(ACTION_MUSIC_INFO)
                            .putExtra("musicTitle", title)
                            .putExtra("musicaArtist", artist)
                            .putExtra("musicAlbum", album)
                            .putExtra("musicPath", mediaUri));
            context.sendBroadcast(
                    new Intent(ACTION_LEGACY_METADATA)
                            .putExtra("track", title)
                            .putExtra("artist", artist)
                            .putExtra("album", album)
                            .putExtra("duration", duration)
                            .putExtra("playing", playing)
                            .putExtra("package", BridgeContract.STOCK_PACKAGE));
            return true;
        } catch (RuntimeException error) {
            log.log("metadata publish failed", error);
            return false;
        }
    }

    private boolean publishPlayState(PlaybackState state) {
        if (!canPublish()) return false;
        try {
            context.sendBroadcast(
                    new Intent(ACTION_LEGACY_PLAYSTATE)
                            .putExtra("playing", isPlaying(state))
                            .putExtra("package", BridgeContract.STOCK_PACKAGE));
            return true;
        } catch (RuntimeException error) {
            log.log("play-state publish failed", error);
            return false;
        }
    }

    private boolean publishProgress() {
        if (!canPublish() || controller == null) return false;
        MediaMetadata metadata = controller.getMetadata();
        PlaybackState state = controller.getPlaybackState();
        long duration =
                metadata != null
                        ? Math.max(0L, metadata.getLong(MediaMetadata.METADATA_KEY_DURATION))
                        : 0L;
        long position = calculatedPosition(state, duration);
        try {
            context.sendBroadcast(
                    new Intent(ACTION_PROGRESS)
                            .putExtra("msg_music_progress", boundedInt(position))
                            .putExtra("msg_music_duration", boundedInt(duration)));
            return true;
        } catch (RuntimeException error) {
            log.log("progress publish failed", error);
            return false;
        }
    }

    private static boolean isPlaying(PlaybackState state) {
        return state != null && state.getState() == PlaybackState.STATE_PLAYING;
    }

    private static long calculatedPosition(PlaybackState state, long duration) {
        if (state == null) return 0L;
        long position = Math.max(0L, state.getPosition());
        if (isPlaying(state) && state.getLastPositionUpdateTime() > 0L) {
            position +=
                    (long)
                            (Math.max(
                                            0L,
                                            SystemClock.elapsedRealtime()
                                                    - state.getLastPositionUpdateTime())
                                    * state.getPlaybackSpeed());
        }
        return duration > 0L
                ? Math.max(0L, Math.min(position, duration))
                : Math.max(0L, position);
    }

    private static int boundedInt(long value) {
        return (int) Math.max(0L, Math.min(value, Integer.MAX_VALUE));
    }

    private static String boundedText(CharSequence value) {
        if (value == null) return "";
        String text = value.toString();
        return text.length() <= MAX_METADATA_CHARS
                ? text
                : text.substring(0, MAX_METADATA_CHARS);
    }

    private static long createClientGeneration() {
        long value =
                SystemClock.elapsedRealtimeNanos()
                        ^ System.nanoTime()
                        ^ (((long) Process.myPid()) << 32);
        return value != 0L ? value : 1L;
    }
}

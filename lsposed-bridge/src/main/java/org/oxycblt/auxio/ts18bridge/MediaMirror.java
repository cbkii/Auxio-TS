package org.oxycblt.auxio.ts18bridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.os.SystemClock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

final class MediaMirror {

    private static final String ACTION_MUSIC_INFO = "com.tw.music.info";
    private static final String ACTION_LEGACY_METADATA = "com.android.music.metachanged";
    private static final String ACTION_LEGACY_PLAYSTATE = "com.android.music.playstatechanged";
    private static final String ACTION_PROGRESS = "com.tw.launcher.music_progress_duration";

    private static final int MAX_METADATA_CHARS = 1024;
    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final long UNSUPPORTED_TRANSPORT_ACTION = -1L;
    private static final long NO_TRANSPORT_ACTION = 0L;

    private final Context context;
    private final BridgeEnvironment environment;
    private final LogSink log;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final AtomicBoolean started = new AtomicBoolean(false);

    // Dedicated background worker for synchronous IPC calls
    private final ExecutorService commandExecutor = Executors.newSingleThreadExecutor();

    private MediaBrowser browser;
    private MediaController controller;
    private IAuxioBridgeCommand commandService;
    private boolean connectionPending;
    private boolean serviceConnectionPending;
    private int reconnectAttempts;

    private final Runnable reconnect = this::connect;
    private final Runnable progressTick =
            new Runnable() {
                @Override
                public void run() {
                    if (stopped.get() || controller == null) return;
                    publishProgress();
                    handler.postDelayed(this, 1000L);
                }
            };

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceConnectionPending = false;
            commandService = IAuxioBridgeCommand.Stub.asInterface(service);
            log.log("CommandService connected", null);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            commandService = null;
            log.log("CommandService disconnected", null);
            scheduleReconnect();
        }
    };

    private final MediaBrowser.ConnectionCallback browserCallback =
            new MediaBrowser.ConnectionCallback() {
                @Override
                public void onConnected() {
                    connectionPending = false;
                    reconnectAttempts = 0;
                    try {
                        controller = new MediaController(context, browser.getSessionToken());
                        controller.registerCallback(controllerCallback);
                        publishNow();
                        handler.post(progressTick);
                    } catch (RuntimeException error) {
                        log.log("MediaBrowser session token unavailable", error);
                        scheduleReconnect();
                    }
                }

                @Override
                public void onConnectionSuspended() {
                    connectionPending = false;
                    clearController();
                    log.log("MediaBrowser suspended", null);
                    scheduleReconnect();
                }

                @Override
                public void onConnectionFailed() {
                    connectionPending = false;
                    clearController();
                    log.log("MediaBrowser failed", null);
                    scheduleReconnect();
                }
            };

    private final MediaController.Callback controllerCallback =
            new MediaController.Callback() {
                @Override
                public void onSessionDestroyed() {
                    log.log("MediaController session destroyed", null);
                    clearController();
                    scheduleReconnect();
                }

                @Override
                public void onPlaybackStateChanged(PlaybackState state) {
                    publishPlayState(state);
                    publishProgress();
                }

                @Override
                public void onMetadataChanged(MediaMetadata metadata) {
                    publishMetadata(metadata);
                    publishProgress();
                }
            };

    MediaMirror(Context context, BridgeEnvironment environment, LogSink log) {
        this.context = context;
        this.environment = environment;
        this.log = log;
    }

    void startOrRetry() {
        if (stopped.get()) return;
        handler.post(this::startOrRetryOnHandler);
    }

    private void startOrRetryOnHandler() {
        if (stopped.get()) return;
        if (started.compareAndSet(false, true)) {
            reconnectAttempts = 0;
            connect();
            return;
        }
        if (controller == null && !connectionPending) {
            connect();
        }
    }

    void pauseUntilRetried() {
        handler.post(
                () -> {
                    stopped.set(true);
                    started.set(false);
                    clearController();
                    disconnectBrowser();
                    handler.removeCallbacks(reconnect);
                    stopped.set(false);
                });
    }

    private void publishNow() {
        MediaController current = controller;
        if (current == null) return;
        publishMetadata(current.getMetadata());
        publishPlayState(current.getPlaybackState());
        publishProgress();
    }

    /**
     * Attempts to send a command to the connected session.
     *
     * <p>Returns true and suppresses stock callbacks only if the target controller exists, advertises the required
     * transport action and accepts the transport call without throwing. Callers may suppress the
     * corresponding stock path only for this result.
     */
    boolean dispatchCommand(BridgeCommand command, Integer seekPosition) {
        if (command == BridgeCommand.UNKNOWN || stopped.get()) return false;
        if (!environment.canPublish(context)) return false;

        MediaController current = controller;
        IAuxioBridgeCommand currentCommandService = commandService;

        if (current == null || currentCommandService == null) {
            startOrRetry();
            return false;
        }

        try {
            if (command == BridgeCommand.UPDATE) {
                publishNow();
                return true;
            }

            PlaybackState state = current.getPlaybackState();
            boolean playing = isPlaying(state);
            long requiredAction = requiredActionFor(command, playing);
            if (requiredAction == UNSUPPORTED_TRANSPORT_ACTION) return false;
            if (state == null || (state.getActions() & requiredAction) == 0L) {
                return false;
            }

            long seekPosParam = (command == BridgeCommand.SEEK && seekPosition != null) ? Math.max(0L, seekPosition.longValue()) : -1L;

            return dispatchCommandSynchronously(currentCommandService, command, seekPosParam);

        } catch (RuntimeException error) {
            log.log("MediaController command dispatch failed; stock path retained", error);
            handler.post(
                    () -> {
                        clearController();
                        scheduleReconnect();
                    });
            return false;
        }
    }

    private boolean dispatchCommandSynchronously(IAuxioBridgeCommand targetService, BridgeCommand command, long seekPosParam) {
        // Execute synchronous command on the dedicated single-thread executor with a very short timeout
        Future<Boolean> commandResult = commandExecutor.submit(() -> {
            try {
                int result = targetService.dispatchCommand(
                    BridgeContract.PROTOCOL_VERSION,
                    0, // Use 0 for now as commandId
                    command.name(),
                    seekPosParam,
                    "lsposed-bridge",
                    0, // clientGeneration
                    SystemClock.elapsedRealtime()
                );
                return result == BridgeContract.RESULT_ACCEPTED || result == BridgeContract.RESULT_DUPLICATE;
            } catch (Exception e) {
                return false;
            }
        });

        // Wait for up to 100ms
        try {
            return commandResult.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // Time out circuit breaker, disconnect Service
            log.log("Command dispatch timed out, tripping circuit breaker", e);
            commandResult.cancel(true);
            handler.post(() -> {
                clearController();
                scheduleReconnect();
            });
            return false;
        } catch (ExecutionException | InterruptedException e) {
            log.log("Command dispatch execution failed", e);
            return false;
        }
    }

    static long requiredActionFor(BridgeCommand command, boolean currentlyPlaying) {
        return switch (command) {
            case PREVIOUS -> PlaybackState.ACTION_SKIP_TO_PREVIOUS;
            case NEXT -> PlaybackState.ACTION_SKIP_TO_NEXT;
            case PLAY_PAUSE ->
                    currentlyPlaying ? PlaybackState.ACTION_PAUSE : PlaybackState.ACTION_PLAY;
            case PLAY -> PlaybackState.ACTION_PLAY;
            case PAUSE -> PlaybackState.ACTION_PAUSE;
            case SEEK -> PlaybackState.ACTION_SEEK_TO;
            case UPDATE -> NO_TRANSPORT_ACTION;
            case UNKNOWN -> UNSUPPORTED_TRANSPORT_ACTION;
        };
    }

    private void connect() {
        if (stopped.get() || !environment.canPublish(context)) {
            started.set(false);
            return;
        }
        disconnectBrowser();
        try {
            connectionPending = true;
            browser =
                    new MediaBrowser(
                            context,
                            new ComponentName(
                                    BuildConfig.TARGET_PACKAGE,
                                    BuildConfig.TARGET_MEDIA_BROWSER_SERVICE),
                            browserCallback,
                            null);
            browser.connect();

            // Bind to the narrow command endpoint
            serviceConnectionPending = true;
            Intent bindIntent = new Intent(BridgeContract.ACTION_AUXIO_BRIDGE_BIND);
            bindIntent.setComponent(new ComponentName(BuildConfig.TARGET_PACKAGE, BuildConfig.TARGET_MEDIA_BROWSER_SERVICE));
            context.bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        } catch (RuntimeException error) {
            connectionPending = false;
            serviceConnectionPending = false;
            log.log("MediaBrowser connect failed", error);
            scheduleReconnect();
        }
    }

    private void scheduleReconnect() {
        if (stopped.get()) return;
        disconnectBrowser();
        if (reconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
            started.set(false);
            log.log("MediaBrowser reconnect limit reached; next user action will retry", null);
            return;
        }
        reconnectAttempts += 1;
        long delayMs = Math.min(30_000L, 2_000L * reconnectAttempts);
        handler.removeCallbacks(reconnect);
        handler.postDelayed(reconnect, delayMs);
    }

    private void disconnectBrowser() {
        connectionPending = false;
        MediaBrowser current = browser;
        browser = null;
        if (current == null) return;
        try {
            current.disconnect();
            if (commandService != null || serviceConnectionPending) {
                context.unbindService(serviceConnection);
                commandService = null;
                serviceConnectionPending = false;
            }
        } catch (RuntimeException ignored) {
            // Best-effort host-process cleanup.
        }
    }

    private void clearController() {
        handler.removeCallbacks(progressTick);
        MediaController current = controller;
        controller = null;
        if (current == null) return;
        try {
            current.unregisterCallback(controllerCallback);
            if (commandService != null || serviceConnectionPending) {
                context.unbindService(serviceConnection);
                commandService = null;
                serviceConnectionPending = false;
            }
        } catch (RuntimeException ignored) {
            // Best-effort host-process cleanup.
        }
    }

    private boolean canPublish() {
        return !stopped.get() && environment.canPublish(context);
    }

    private void publishMetadata(MediaMetadata metadata) {
        if (!canPublish()) return;
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
            // These implicit actions and the musicPath extra reproduce the captured stock TW Music
            // contract consumed by fixed launcher/panel receivers. They intentionally expose the
            // same bounded metadata to broadcast receivers as stock; do not add unrelated fields.
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
        } catch (RuntimeException error) {
            log.log("stock-identity metadata publish failed", error);
        }
    }

    private void publishPlayState(PlaybackState state) {
        if (!canPublish()) return;
        try {
            context.sendBroadcast(
                    new Intent(ACTION_LEGACY_PLAYSTATE)
                            .putExtra("playing", isPlaying(state))
                            .putExtra("package", BridgeContract.STOCK_PACKAGE));
        } catch (RuntimeException error) {
            log.log("stock-identity play-state publish failed", error);
        }
    }

    private void publishProgress() {
        if (!canPublish()) return;
        MediaController current = controller;
        MediaMetadata metadata = current != null ? current.getMetadata() : null;
        PlaybackState state = current != null ? current.getPlaybackState() : null;
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
        } catch (RuntimeException error) {
            log.log("stock-identity progress publish failed", error);
        }
    }

    private static boolean isPlaying(PlaybackState state) {
        return state != null && state.getState() == PlaybackState.STATE_PLAYING;
    }

    private static long calculatedPosition(PlaybackState state, long duration) {
        if (state == null) return 0L;
        long position = Math.max(0L, state.getPosition());
        if (isPlaying(state) && state.getLastPositionUpdateTime() > 0L) {
            long elapsed =
                    Math.max(
                            0L,
                            SystemClock.elapsedRealtime() - state.getLastPositionUpdateTime());
            position += (long) (elapsed * state.getPlaybackSpeed());
        }
        if (duration > 0L) position = Math.min(position, duration);
        return Math.max(0L, position);
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

    @FunctionalInterface
    interface LogSink {
        void log(String message, Throwable error);
    }
}

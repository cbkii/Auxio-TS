/*
 * Copyright (c) 2026 Auxio Project
 * MediaMirror.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import java.util.concurrent.atomic.AtomicBoolean;

/** Mirrors and controls Auxio's public Android MediaSession through the genuine stock process. */
final class MediaMirror {
    interface LogSink {
        void log(String message, Throwable error);
    }

    static final long NO_TRANSPORT_ACTION = 0L;
    static final long UNSUPPORTED_TRANSPORT_ACTION = -1L;

    private static final String ACTION_MUSIC_INFO = "com.tw.music.info";
    private static final String ACTION_PROGRESS = "com.tw.launcher.music_progress_duration";
    private static final String ACTION_LEGACY_METADATA = "com.android.music.metachanged";
    private static final String ACTION_LEGACY_PLAYSTATE = "com.android.music.playstatechanged";
    private static final long TICK_MS = 1_000L;
    private static final int MAX_RECONNECT_ATTEMPTS = 12;
    private static final int MAX_METADATA_CHARS = 8_192;

    private final Context context;
    private final BridgeEnvironment environment;
    private final LogSink log;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final AtomicBoolean started = new AtomicBoolean();
    private final AtomicBoolean stopped = new AtomicBoolean();

    private MediaBrowser browser;
    private volatile MediaController controller;
    private boolean connectionPending;
    private int reconnectAttempts;

    private final Runnable reconnect = this::connect;
    private final Runnable progressTick =
            new Runnable() {
                @Override
                public void run() {
                    if (stopped.get()) return;
                    publishProgress();
                    MediaController current = controller;
                    PlaybackState state = current != null ? current.getPlaybackState() : null;
                    if (isPlaying(state)) handler.postDelayed(this, TICK_MS);
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
                    connectionPending = false;
                    if (stopped.get() || browser == null || !browser.isConnected()) return;
                    try {
                        clearController();
                        controller = new MediaController(context, browser.getSessionToken());
                        controller.registerCallback(controllerCallback, handler);
                        reconnectAttempts = 0;
                        log.log(
                                "MediaBrowser connected; bidirectional stock-identity bridge active",
                                null);
                        publishNow();
                    } catch (RuntimeException error) {
                        log.log("MediaController creation failed", error);
                        clearController();
                        scheduleReconnect();
                    }
                }

                @Override
                public void onConnectionSuspended() {
                    connectionPending = false;
                    clearController();
                    scheduleReconnect();
                }

                @Override
                public void onConnectionFailed() {
                    connectionPending = false;
                    clearController();
                    scheduleReconnect();
                }
            };

    MediaMirror(Context context, BridgeEnvironment environment, LogSink log) {
        Context app = context.getApplicationContext();
        this.context = app != null ? app : context;
        this.environment = environment;
        this.log = log;
    }

    void startOrRetry() {
        stopped.set(false);
        handler.post(this::startOrRetryOnHandler);
    }

    private void startOrRetryOnHandler() {
        if (started.compareAndSet(false, true)) {
            connect();
        } else if (!connectionPending
                && controller == null
                && (browser == null || !browser.isConnected())) {
            reconnectAttempts = 0;
            handler.removeCallbacks(reconnect);
            connect();
        }
    }

    void pauseUntilRetried() {
        handler.post(
                () -> {
                    handler.removeCallbacksAndMessages(null);
                    disconnectBrowser();
                    clearController();
                    started.set(false);
                });
    }

    void stop() {
        stopped.set(true);
        handler.post(
                () -> {
                    handler.removeCallbacksAndMessages(null);
                    disconnectBrowser();
                    clearController();
                    started.set(false);
                });
    }

    void publishNow() {
        handler.post(
                () -> {
                    MediaController current = controller;
                    if (current == null || stopped.get()) return;
                    publishMetadata(current.getMetadata());
                    publishPlayState(current.getPlaybackState());
                    publishProgress();
                });
    }

    /**
     * Sends a launcher/stock command directly to Auxio's connected MediaSession.
     *
     * <p>Returns {@code true} only after the target controller exists, advertises the required
     * transport action and accepts the transport call without throwing. Callers may suppress the
     * corresponding stock path only for this result.
     */
    boolean dispatchCommand(BridgeCommand command, Integer seekPosition) {
        if (command == BridgeCommand.UNKNOWN || stopped.get()) return false;
        if (!environment.canPublish(context)) return false;

        MediaController current = controller;
        if (current == null) {
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

            MediaController.TransportControls controls = current.getTransportControls();
            switch (command) {
                case PREVIOUS -> controls.skipToPrevious();
                case NEXT -> controls.skipToNext();
                case PLAY_PAUSE -> {
                    if (playing) controls.pause();
                    else controls.play();
                }
                case PLAY -> controls.play();
                case PAUSE -> controls.pause();
                case SEEK -> {
                    if (seekPosition == null) return false;
                    controls.seekTo(Math.max(0L, seekPosition.longValue()));
                }
                case UPDATE, UNKNOWN -> {
                    return false;
                }
            }
            return true;
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
        } catch (RuntimeException error) {
            connectionPending = false;
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
}

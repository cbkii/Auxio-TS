package com.tw.music.media;

import android.app.Service;
import android.content.Intent;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Maintainable bridge scaffold between MusicService lifecycle and Android media controls.
 *
 * <p>This class intentionally delegates all transport commands to the existing TW command
 * broadcast path so vendor and widget compatibility remains authoritative.
 */
public final class MediaControlBridge {
    private final Service service;
    private final MediaSessionCompat mediaSession;
    private final MediaNotificationController notificationController;

    public MediaControlBridge(Service service) {
        this.service = service;
        this.mediaSession = new MediaSessionCompat(service, "com.tw.music.MediaControlBridge");
        // Required so that media-button and steering-wheel control events are reliably routed to
        // this session by the system and vendor transport-control infrastructure on TS18.
        this.mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        this.notificationController = new MediaNotificationController(service);
        this.mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onSkipToPrevious() {
                dispatchTwAction("com.tw.music.action.prev", "prev");
            }

            @Override
            public void onSkipToNext() {
                dispatchTwAction("com.tw.music.action.next", "next");
            }

            @Override
            public void onPlay() {
                // The TW vendor command surface exposes only a play/pause toggle (action.pp);
                // there are no separate explicit play or pause actions in the current firmware.
                // Both onPlay() and onPause() therefore dispatch the same toggle.  If the TW
                // service is later extended with explicit actions, update these dispatches.
                dispatchTwAction("com.tw.music.action.pp", "pp");
            }

            @Override
            public void onPause() {
                // See onPlay() comment: the TW surface is toggle-only.
                dispatchTwAction("com.tw.music.action.pp", "pp");
            }

        });
    }

    public void setActive(boolean active) {
        mediaSession.setActive(active);
    }

    public void publishState(PlaybackStateCompat playbackStateCompat) {
        mediaSession.setPlaybackState(playbackStateCompat);
    }

    public void publishMetadata(MediaMetadataCompat mediaMetadataCompat) {
        mediaSession.setMetadata(mediaMetadataCompat);
    }

    public void updateNotification(boolean isPlaying) {
        notificationController.update(mediaSession.getSessionToken(), isPlaying);
    }

    public void release() {
        notificationController.cancel();
        mediaSession.setActive(false);
        mediaSession.release();
    }

    /**
     * Sends the given TW command action via broadcast, which is the authoritative TW command path
     * for {@code com.tw.music.action.*} intents.  Broadcast routing also avoids the need for an
     * explicit component name that {@code startService} would require on modern Android.
     */
    private void dispatchTwAction(String action, String cmd) {
        Intent intent = new Intent(action);
        intent.setPackage("com.tw.music");
        intent.putExtra("cmd", cmd);
        service.sendBroadcast(intent);
    }
}

package com.tw.music.media;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;

/**
 * Scaffold for MediaStyle notification wiring.
 *
 * <p>Notification channel {@value #CHANNEL_ID} must be created before posting any notification
 * on Android 8.0+ (API 26+). {@link #createNotificationChannel()} is called automatically from
 * {@link #update} and is safe to call repeatedly (no-op if the channel already exists).
 *
 * <p>Transport-control PendingIntents are broadcast-based so they route through the existing TW
 * command broadcast path (see {@code MediaControlBridge.dispatchTwAction}).
 */
public final class MediaNotificationController {
    /** Notification channel ID for the media playback notification (Android 8.0+ / API 26+). */
    static final String CHANNEL_ID = "com.tw.music.playback";
    private static final int NOTIFICATION_ID = 1180;
    private final Service service;

    public MediaNotificationController(Service service) {
        this.service = service;
    }

    public void update(MediaSessionCompat.Token token, boolean isPlaying) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle("Music")
                .setOngoing(isPlaying)
                .addAction(new NotificationCompat.Action(
                        android.R.drawable.ic_media_previous,
                        "Prev",
                        commandPendingIntent("com.tw.music.action.prev", "prev")))
                .addAction(new NotificationCompat.Action(
                        isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play,
                        isPlaying ? "Pause" : "Play",
                        commandPendingIntent("com.tw.music.action.pp", "pp")))
                .addAction(new NotificationCompat.Action(
                        android.R.drawable.ic_media_next,
                        "Next",
                        commandPendingIntent("com.tw.music.action.next", "next")))
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(token)
                        .setShowActionsInCompactView(0, 1, 2));
        Notification notification = builder.build();
        service.startForeground(NOTIFICATION_ID, notification);
    }

    public void cancel() {
        service.stopForeground(true);
        NotificationManager nm = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.cancel(NOTIFICATION_ID);
    }

    /**
     * Creates the playback notification channel on Android 8.0+ (API 26+). Safe to call
     * repeatedly; the system ignores the call if the channel already exists.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm =
                    (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Music Playback",
                        NotificationManager.IMPORTANCE_LOW);
                channel.setDescription("Media playback controls");
                nm.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Returns a broadcast {@link PendingIntent} that fires the given TW command action.
     * Using {@code getBroadcast} keeps the intent routed through the TW command broadcast path
     * and avoids the requirement for an explicit component name that {@code getService} would need
     * on Android 12+.
     */
    private PendingIntent commandPendingIntent(String action, String cmd) {
        Intent intent = new Intent(action);
        intent.setPackage("com.tw.music");
        intent.putExtra("cmd", cmd);
        // FLAG_IMMUTABLE required for apps targeting Android 12+ (API 31+).
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        return PendingIntent.getBroadcast(service, action.hashCode(), intent, flags);
    }
}

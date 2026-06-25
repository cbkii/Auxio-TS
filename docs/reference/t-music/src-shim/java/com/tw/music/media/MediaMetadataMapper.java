package com.tw.music.media;

import android.graphics.Bitmap;
import android.support.v4.media.MediaMetadataCompat;

/** Maps existing MusicInfo/PlaybackInfo fields into MediaMetadataCompat. */
public final class MediaMetadataMapper {
    private MediaMetadataMapper() {}

    public static MediaMetadataCompat map(
            String title,
            String artist,
            String album,
            long durationMs,
            Bitmap artwork,
            String mediaId,
            String sourcePath) {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        if (title != null) builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, title);
        if (artist != null) builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist);
        if (album != null) builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album);
        if (durationMs > 0) builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, durationMs);
        if (artwork != null) builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, artwork);
        if (mediaId != null) builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId);
        if (sourcePath != null) {
            builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, sourcePath);
        }
        return builder.build();
    }
}

package com.tw.music.media;

import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Maps existing service/model playback state holders to PlaybackStateCompat.
 */
public final class PlaybackStateMapper {
    private PlaybackStateMapper() {}

    /**
     * Builds a {@link PlaybackStateCompat} from the given playback signals.
     *
     * <p>{@code positionMs} is the current track position in milliseconds.  The update timestamp
     * used for position extrapolation is computed internally using
     * {@link SystemClock#elapsedRealtime()} as required by
     * {@link PlaybackStateCompat.Builder#setState}.
     */
    public static PlaybackStateCompat map(
            boolean isPlaying,
            boolean isBuffering,
            boolean hasError,
            long positionMs,
            float speed,
            boolean canSeek) {
        int state = PlaybackStateCompat.STATE_STOPPED;
        if (hasError) {
            state = PlaybackStateCompat.STATE_ERROR;
        } else if (isBuffering) {
            state = PlaybackStateCompat.STATE_BUFFERING;
        } else if (isPlaying) {
            state = PlaybackStateCompat.STATE_PLAYING;
        } else if (positionMs > 0) {
            state = PlaybackStateCompat.STATE_PAUSED;
        }

        long actions = PlaybackStateCompat.ACTION_PLAY
                | PlaybackStateCompat.ACTION_PAUSE
                | PlaybackStateCompat.ACTION_PLAY_PAUSE
                | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                | PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        if (canSeek) {
            actions |= PlaybackStateCompat.ACTION_SEEK_TO;
        }

        return new PlaybackStateCompat.Builder()
                .setActions(actions)
                // setState requires elapsedRealtime() as the update timestamp so that position
                // extrapolation in MediaSession consumers (e.g. TLink / CarPlay) is correct.
                .setState(state, Math.max(positionMs, 0), speed, SystemClock.elapsedRealtime())
                .build();
    }
}

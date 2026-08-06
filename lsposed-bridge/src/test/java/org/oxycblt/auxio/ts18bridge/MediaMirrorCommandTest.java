/*
 * Copyright (c) 2026 Auxio Project
 * MediaMirrorCommandTest.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

import static org.junit.Assert.assertEquals;

import android.media.session.PlaybackState;
import org.junit.Test;

public class MediaMirrorCommandTest {
    @Test
    public void mapsCommandsToAdvertisedMediaSessionActions() {
        assertEquals(
                PlaybackState.ACTION_SKIP_TO_PREVIOUS,
                MediaMirror.requiredActionFor(BridgeCommand.PREVIOUS, false));
        assertEquals(
                PlaybackState.ACTION_SKIP_TO_NEXT,
                MediaMirror.requiredActionFor(BridgeCommand.NEXT, false));
        assertEquals(
                PlaybackState.ACTION_PLAY,
                MediaMirror.requiredActionFor(BridgeCommand.PLAY, false));
        assertEquals(
                PlaybackState.ACTION_PAUSE,
                MediaMirror.requiredActionFor(BridgeCommand.PAUSE, true));
        assertEquals(
                PlaybackState.ACTION_SEEK_TO,
                MediaMirror.requiredActionFor(BridgeCommand.SEEK, false));
    }

    @Test
    public void playPauseResolvesAgainstCurrentAuxioState() {
        assertEquals(
                PlaybackState.ACTION_PLAY,
                MediaMirror.requiredActionFor(BridgeCommand.PLAY_PAUSE, false));
        assertEquals(
                PlaybackState.ACTION_PAUSE,
                MediaMirror.requiredActionFor(BridgeCommand.PLAY_PAUSE, true));
    }

    @Test
    public void updateAndUnknownDoNotPretendToBeTransportActions() {
        assertEquals(
                0L,
                MediaMirror.requiredActionFor(BridgeCommand.UPDATE, false));
        assertEquals(
                -1L,
                MediaMirror.requiredActionFor(BridgeCommand.UNKNOWN, false));
    }
}

/*
 * Copyright (c) 2026 Auxio Project
 * BridgeCommandTest.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

import static org.junit.Assert.assertEquals;

import android.view.KeyEvent;
import org.junit.Test;

public class BridgeCommandTest {
    @Test
    public void mapsObservedTopwayActions() {
        assertEquals(
                BridgeCommand.PREVIOUS,
                BridgeCommand.fromIntent(BridgeContract.ACTION_PREVIOUS, null));
        assertEquals(
                BridgeCommand.NEXT,
                BridgeCommand.fromIntent(BridgeContract.ACTION_NEXT, null));
        assertEquals(
                BridgeCommand.PLAY_PAUSE,
                BridgeCommand.fromIntent(BridgeContract.ACTION_PLAY_PAUSE, null));
        assertEquals(
                BridgeCommand.SEEK,
                BridgeCommand.fromIntent(BridgeContract.ACTION_WIDGET_SEEK, null));
    }

    @Test
    public void mapsObservedCommandExtras() {
        assertEquals(
                BridgeCommand.PREVIOUS,
                BridgeCommand.fromIntent(BridgeContract.ACTION_COMMAND, "prev"));
        assertEquals(
                BridgeCommand.NEXT,
                BridgeCommand.fromIntent(BridgeContract.ACTION_COMMAND, "next"));
        assertEquals(
                BridgeCommand.PLAY_PAUSE,
                BridgeCommand.fromIntent(BridgeContract.ACTION_COMMAND, "pp"));
        assertEquals(
                BridgeCommand.UPDATE,
                BridgeCommand.fromIntent(BridgeContract.ACTION_COMMAND, "update"));
        assertEquals(
                BridgeCommand.UNKNOWN,
                BridgeCommand.fromIntent(BridgeContract.ACTION_COMMAND, "delete"));
    }

    @Test
    public void mapsObservedPresenterMethodsWithoutGuessingUnknownMethods() {
        assertEquals(BridgeCommand.PREVIOUS, BridgeCommand.fromPresenterMethod("rb"));
        assertEquals(BridgeCommand.NEXT, BridgeCommand.fromPresenterMethod("pb"));
        assertEquals(BridgeCommand.PAUSE, BridgeCommand.fromPresenterMethod("ba"));
        assertEquals(BridgeCommand.PLAY, BridgeCommand.fromPresenterMethod("fa"));
        assertEquals(BridgeCommand.SEEK, BridgeCommand.fromPresenterMethod("seekTo"));
        assertEquals(BridgeCommand.UNKNOWN, BridgeCommand.fromPresenterMethod("unknown"));
    }

    @Test
    public void exposesExactMediaKeysForNonSeekCommands() {
        assertEquals(
                Integer.valueOf(KeyEvent.KEYCODE_MEDIA_PREVIOUS),
                BridgeCommand.PREVIOUS.mediaKeyCode());
        assertEquals(
                Integer.valueOf(KeyEvent.KEYCODE_MEDIA_NEXT), BridgeCommand.NEXT.mediaKeyCode());
        assertEquals(
                Integer.valueOf(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE),
                BridgeCommand.PLAY_PAUSE.mediaKeyCode());
        assertEquals(
                Integer.valueOf(KeyEvent.KEYCODE_MEDIA_PLAY), BridgeCommand.PLAY.mediaKeyCode());
        assertEquals(
                Integer.valueOf(KeyEvent.KEYCODE_MEDIA_PAUSE), BridgeCommand.PAUSE.mediaKeyCode());
    }
}

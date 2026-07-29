/*
 * Copyright (c) 2026 Auxio Project
 * BridgeCommand.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

import android.view.KeyEvent;
import java.util.Locale;

/** Pure mapping for the observed stock Topway service, receiver and presenter control surface. */
enum BridgeCommand {
    PREVIOUS(KeyEvent.KEYCODE_MEDIA_PREVIOUS),
    NEXT(KeyEvent.KEYCODE_MEDIA_NEXT),
    PLAY_PAUSE(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE),
    PLAY(KeyEvent.KEYCODE_MEDIA_PLAY),
    PAUSE(KeyEvent.KEYCODE_MEDIA_PAUSE),
    UPDATE(null),
    SEEK(null),
    UNKNOWN(null);

    private final Integer mediaKeyCode;

    BridgeCommand(Integer mediaKeyCode) {
        this.mediaKeyCode = mediaKeyCode;
    }

    Integer mediaKeyCode() {
        return mediaKeyCode;
    }

    static BridgeCommand fromIntent(String action, String cmd) {
        if (BridgeContract.ACTION_PREVIOUS.equals(action)) return PREVIOUS;
        if (BridgeContract.ACTION_NEXT.equals(action)) return NEXT;
        if (BridgeContract.ACTION_PLAY_PAUSE.equals(action)) return PLAY_PAUSE;
        if (BridgeContract.ACTION_WIDGET_SEEK.equals(action)) return SEEK;
        if (!BridgeContract.ACTION_COMMAND.equals(action)) return UNKNOWN;
        if (cmd == null) return UNKNOWN;
        return switch (cmd.toLowerCase(Locale.ROOT)) {
            case "prev" -> PREVIOUS;
            case "next" -> NEXT;
            case "pp" -> PLAY_PAUSE;
            case "update" -> UPDATE;
            default -> UNKNOWN;
        };
    }

    static BridgeCommand fromPresenterMethod(String methodName) {
        return switch (methodName) {
            case "rb" -> PREVIOUS;
            case "pb" -> NEXT;
            case "fa" -> PLAY;
            case "ba" -> PAUSE;
            case "seekTo" -> SEEK;
            default -> UNKNOWN;
        };
    }
}

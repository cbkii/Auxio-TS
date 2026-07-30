/*
 * Copyright (c) 2026 Auxio Project
 * BridgeContract.java is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.ts18bridge;

/** Stable strings observed in the exact stock and Auxio-TS compatibility surfaces. */
final class BridgeContract {
    static final String STOCK_PACKAGE = "com.tw.music";

    static final String ACTION_COMMAND = "com.tw.music.action.cmd";
    static final String ACTION_PREVIOUS = "com.tw.music.action.prev";
    static final String ACTION_NEXT = "com.tw.music.action.next";
    static final String ACTION_PLAY_PAUSE = "com.tw.music.action.pp";
    static final String ACTION_WIDGET_SEEK = "com.android.launcher.widget_music_progress";

    private BridgeContract() {}
}

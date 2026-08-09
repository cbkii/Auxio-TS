/*
 * Copyright (c) 2026 Auxio Project
 * Ts18DofunIntegrationResolverTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.headunit.root.dofun

import org.junit.Assert.assertEquals
import org.junit.Test

class Ts18DofunIntegrationResolverTest {
    @Test
    fun `root integration probes match the explicit read only allowlist`() {
        // The Kotlin map assertion fixes every approved command.
        // The shell safety guard fixes the exact com.tw.* identifier set extracted here.
        // Adding a probe or vendor identifier requires explicit review of both boundaries.
        val approved =
            mapOf(
                Ts18RootProbe.Id to "id",
                Ts18RootProbe.PackageSummary to
                    "pm list packages -f -U | grep -E 'com\\.tw\\.music|com\\.tw\\.media|com\\.dofun\\.variety'",
                Ts18RootProbe.ResolveMusicComponents to
                    "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN com.tw.media",
                Ts18RootProbe.ResolveTopwayAlias to
                    "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN -n com.tw.media/com.tw.music.MusicActivity",
                Ts18RootProbe.OverlayRuntime to
                    "appops get com.tw.media SYSTEM_ALERT_WINDOW 2>&1; dumpsys activity services com.tw.media 2>&1 | head -n 160; dumpsys window windows 2>&1 | grep -E 'com.tw.media|CarFloatingControls' | head -n 80",
                Ts18RootProbe.EqualizerComponents to
                    "dumpsys package com.tw.eq 2>&1 | grep -E 'EQChoiceActivity|DSPActivity|EQActivity|enabledComponents|disabledComponents' | head -n 160",
                Ts18RootProbe.VisualizerEffects to
                    "dumpsys media.audio_flinger 2>&1 | grep -i -E 'visualizer|session|com.tw.media' | head -n 200",
                Ts18RootProbe.PackageDumpMedia to "dumpsys package com.tw.media",
                Ts18RootProbe.PackageDumpMusic to "dumpsys package com.tw.music",
                Ts18RootProbe.AppWidgetSummary to "dumpsys appwidget",
                Ts18RootProbe.MediaSessionSummary to "dumpsys media_session",
                Ts18RootProbe.NotificationSummary to
                    "dumpsys notification --noredact 2>&1 | grep -i -E 'com.tw.media|channel.PLAYBACK|NotifyService|notification listener' | head -n 320",
                Ts18RootProbe.DofunServiceSummary to
                    "dumpsys activity services com.dofun.variety 2>&1 | grep -i -E 'NotifyService|Media|music|listener' | head -n 240",
                Ts18RootProbe.ActivityBroadcastSummary to "dumpsys activity broadcasts",
                Ts18RootProbe.DofunDataHintsReadOnly to
                    "content query --uri content://com.dofun.variety.ExportedProvider/hotseat_app_music",
            )

        assertEquals(approved.keys, Ts18RootProbe.entries.toSet())
        Ts18RootProbe.entries.forEach { probe ->
            assertEquals(approved.getValue(probe), probe.command)
        }
    }
}

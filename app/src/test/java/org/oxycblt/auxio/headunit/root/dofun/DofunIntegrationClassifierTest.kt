/*
 * Copyright (c) 2026 Auxio Project
 * DofunIntegrationClassifierTest.kt is part of Auxio.
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DofunIntegrationClassifierTest {
    @Test
    fun `package topology never implies launcher selection`() {
        val topology =
            DofunIntegrationClassifier.topology(
                listOf("com.tw.media", "com.tw.music", "com.dofun.variety")
            )

        assertTrue(topology.releaseAuxioPresent)
        assertTrue(topology.stockMusicPresent)
        assertTrue(topology.dofunPresent)
        assertFalse(topology.debugAuxioPresent)
        assertEquals(
            DofunSelectedMusicTarget.UNKNOWN,
            DofunIntegrationClassifier.selectedMusicTarget(null),
        )
    }

    @Test
    fun `explicit provider component selects com tw media`() {
        assertEquals(
            DofunSelectedMusicTarget.COM_TW_MEDIA,
            DofunIntegrationClassifier.selectedMusicTarget(
                "Row: 0 component=com.tw.media/com.tw.music.MusicActivity"
            ),
        )
    }

    @Test
    fun `explicit provider package selects stock music`() {
        assertEquals(
            DofunSelectedMusicTarget.COM_TW_MUSIC,
            DofunIntegrationClassifier.selectedMusicTarget("Row: 0 package=com.tw.music"),
        )
    }

    @Test
    fun `ambiguous or failed provider evidence stays unknown`() {
        assertEquals(
            DofunSelectedMusicTarget.UNKNOWN,
            DofunIntegrationClassifier.selectedMusicTarget(
                "Row: 0 component=com.tw.media/com.tw.music.MusicActivity, fallback=com.tw.music/com.tw.music.MusicActivity"
            ),
        )
        assertEquals(
            DofunSelectedMusicTarget.UNKNOWN,
            DofunIntegrationClassifier.selectedMusicTarget(
                "Row: 0 component=com.tw.media/com.tw.music.MusicActivity, package=org.videolan.vlc"
            ),
        )
        assertEquals(
            DofunSelectedMusicTarget.UNKNOWN,
            DofunIntegrationClassifier.selectedMusicTarget("No result found."),
        )
        assertEquals(
            DofunSelectedMusicTarget.UNKNOWN,
            DofunIntegrationClassifier.selectedMusicTarget(
                "Permission Denial: opening provider com.dofun.variety.ExportedProvider"
            ),
        )
    }

    @Test
    fun `explicit other package is classified without guessing identity`() {
        assertEquals(
            DofunSelectedMusicTarget.OTHER,
            DofunIntegrationClassifier.selectedMusicTarget(
                "Row: 0 component=org.videolan.vlc/org.videolan.vlc.StartActivity"
            ),
        )
    }

    @Test
    fun `root observation cannot replace failed app uid selection authority`() {
        val selection =
            DofunIntegrationClassifier.authoritativeSelection(
                appProviderOutput =
                    "SecurityException: Permission Denial opening com.dofun.variety.ExportedProvider",
                rootProviderOutput = "Row: 0 component=com.tw.media/com.tw.music.MusicActivity",
            )

        assertEquals(DofunSelectedMusicTarget.UNKNOWN, selection.target)
        assertEquals("APP_UID_EXPORTED_PROVIDER", selection.source)
        assertTrue(selection.evidence.orEmpty().contains("SecurityException"))
    }

    @Test
    fun `root provider output remains observation when app uid surface is absent`() {
        val selection =
            DofunIntegrationClassifier.authoritativeSelection(
                appProviderOutput = null,
                rootProviderOutput = "Row: 0 package=com.tw.music",
            )

        assertEquals(DofunSelectedMusicTarget.UNKNOWN, selection.target)
        assertEquals("ROOT_OBSERVATION_ONLY", selection.source)
        assertEquals("Row: 0 package=com.tw.music", selection.evidence)
    }

    @Test
    fun `proven stock selection outranks missing Auxio package recommendation`() {
        val recommendation =
            DofunIntegrationClassifier.recommendation(
                topology =
                    DofunPackageTopology(
                        releaseAuxioPresent = false,
                        debugAuxioPresent = false,
                        stockMusicPresent = true,
                        dofunPresent = true,
                    ),
                selectedTarget = DofunSelectedMusicTarget.COM_TW_MUSIC,
            )

        assertTrue(recommendation.contains("Preserve stock"))
    }

    @Test
    fun `missing release recommendation names the current product identity`() {
        val recommendation =
            DofunIntegrationClassifier.recommendation(
                topology =
                    DofunPackageTopology(
                        releaseAuxioPresent = false,
                        debugAuxioPresent = false,
                        stockMusicPresent = false,
                        dofunPresent = true,
                    ),
                selectedTarget = DofunSelectedMusicTarget.UNKNOWN,
            )

        assertTrue(recommendation.contains("signed com.tw.media release APK"))
    }
}

/*
 * Copyright (c) 2026 Auxio Project
 * ConfiguredSourcePolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.oxycblt.auxio.music.locations.LocationMode

class ConfiguredSourcePolicyTest {
    @Test
    fun configuredPathMatchingRequiresDirectoryBoundary() {
        val snapshot = snapshot("/storage/emulated/0/Music")

        assertTrue(snapshot.containsPath("/storage/emulated/0/Music"))
        assertTrue(snapshot.containsPath("/storage/emulated/0/Music/Artist/song.flac"))
        assertFalse(snapshot.containsPath("/storage/emulated/0/Music_Private/song.flac"))
        assertFalse(snapshot.containsPath("/storage/emulated/0/Music/../Download/song.flac"))
    }

    @Test
    fun removableClassificationIsExposedWithoutChangingRootOrder() {
        val sources =
            listOf(
                source("internal", "/storage/emulated/0/Music", ConfiguredSourcePolicy.SourceKind.INTERNAL),
                source("usb", "/storage/usbdisk1/Music", ConfiguredSourcePolicy.SourceKind.REMOVABLE),
            )
        val snapshot = ConfiguredSourcePolicy.Snapshot(LocationMode.DIRECT_FS, sources, 42)

        assertEquals(
            listOf("/storage/emulated/0/Music", "/storage/usbdisk1/Music"),
            snapshot.configuredRoots,
        )
        assertTrue(snapshot.hasConfiguredUsb)
    }

    @Test
    fun unavailableRootsAreExcludedFromDirectBrowsing() {
        val unavailable =
            ConfiguredSourcePolicy.Source(
                id = "source:missing",
                uri = "file:///storage/usbdisk0/Music",
                appPath = "/storage/usbdisk0/Music",
                kind = ConfiguredSourcePolicy.SourceKind.REMOVABLE,
                availability = ConfiguredSourcePolicy.Availability.UNAVAILABLE,
            )
        val snapshot =
            ConfiguredSourcePolicy.Snapshot(LocationMode.DIRECT_FS, listOf(unavailable), 42)

        assertTrue(snapshot.rootFiles().isEmpty())
    }

    private fun snapshot(path: String) =
        ConfiguredSourcePolicy.Snapshot(
            locationMode = LocationMode.DIRECT_FS,
            sources = listOf(source("configured", path, ConfiguredSourcePolicy.SourceKind.INTERNAL)),
            configurationRevision = 42,
        )

    private fun source(id: String, path: String, kind: ConfiguredSourcePolicy.SourceKind) =
        ConfiguredSourcePolicy.Source(
            id = "source:$id",
            uri = "file://$path",
            appPath = path,
            kind = kind,
            availability = ConfiguredSourcePolicy.Availability.READY,
        )
}

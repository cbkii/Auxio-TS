/*
 * Copyright (c) 2026 Auxio Project
 * GeneratedPlaylistCompilerTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class GeneratedPlaylistCompilerTest {
    @Test
    fun compilationIsStableAcrossInputOrder() {
        val entries =
            listOf(
                entry("old", addedMs = 10, year = 1998, album = "B"),
                entry("new", addedMs = 30, year = 2001, album = "A"),
                entry("middle", addedMs = 20, year = 1992, album = "C"),
            )

        assertEquals(
            GeneratedPlaylistCompiler.compile(entries),
            GeneratedPlaylistCompiler.compile(entries.reversed()),
        )
    }

    @Test
    fun recentlyAddedAndDecadesHaveDeterministicMembership() {
        val definitions =
            GeneratedPlaylistCompiler.compile(
                listOf(
                    entry("1998", addedMs = 10, year = 1998, album = "B"),
                    entry("2001", addedMs = 30, year = 2001, album = "A"),
                    entry("1992", addedMs = 20, year = 1992, album = "C"),
                    entry("unknown", addedMs = 40, year = null, album = "D"),
                )
            )

        assertEquals("recently-added", definitions[0].id)
        assertEquals(listOf("unknown", "2001", "1992", "1998"), definitions[0].values)
        assertEquals(listOf("decade:2000", "decade:1990"), definitions.drop(1).map { it.id })
        assertEquals(listOf("1992", "1998"), definitions.last().values)
    }

    @Test
    fun generatedUidIsStableAndNamespacedByDefinition() {
        val first = GeneratedPlaylistCompiler.stableUid("decade:1990")
        val second = GeneratedPlaylistCompiler.stableUid("decade:1990")
        val other = GeneratedPlaylistCompiler.stableUid("decade:2000")

        assertEquals(first, second)
        assertNotEquals(first, other)
        assertTrue(first.toString().startsWith("uap"))
    }

    @Test
    fun emptyLibraryProducesNoGeneratedRows() {
        assertTrue(GeneratedPlaylistCompiler.compile<String>(emptyList()).isEmpty())
    }

    private fun entry(
        value: String,
        addedMs: Long,
        year: Int?,
        album: String,
    ) =
        GeneratedPlaylistCompiler.Entry(
            value = value,
            stableKey = value,
            addedMs = addedMs,
            year = year,
            albumSort = album,
            disc = 0,
            track = 0,
            titleSort = value,
        )
}

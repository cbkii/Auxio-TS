/*
 * Copyright (c) 2026 Auxio Project
 * DatabaseFirstLibraryArchitectureTest.kt is part of Auxio.
 */
package org.oxycblt.auxio.music

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DatabaseFirstLibraryArchitectureTest {
    private val root =
        Path.of(System.getProperty("user.dir")).let { cwd ->
            if (Files.exists(cwd.resolve("settings.gradle"))) cwd else cwd.parent
        }

    @Test
    fun cacheReadDoesNotHydrateFullLibraryForPointLookup() {
        val source =
            root.resolve("musikr/src/main/java/org/oxycblt/musikr/cache/db/DBCache.kt").readText()
        val readBody =
            source
                .substringAfter("override suspend fun read(file: File)")
                .substringBefore("override suspend fun snapshot")
        assertTrue(readBody.contains("selectSongByUri"))
        assertFalse(readBody.contains("selectAllSongs"))
        assertFalse(readBody.contains("associateBy"))
    }

    @Test
    fun normalizedLibrarySchemaHasRequiredTablesAndIndexes() {
        val schema =
            root
                .resolve("musikr/src/main/java/org/oxycblt/musikr/cache/db/CacheDatabase.kt")
                .readText()
        listOf(
                "LibraryVolumeData",
                "LibrarySongData",
                "LibraryAlbumData",
                "LibraryArtistData",
                "LibraryGenreData",
                "SongArtistCrossRefData",
                "SongGenreCrossRefData",
                "AlbumArtistCrossRefData",
                "LibraryPlaylistData",
                "PlaylistItemData",
                "ScanGenerationData",
                "SourceStateData",
                "MetadataRevisionData",
                "index_LibrarySongData_uri",
                "index_LibrarySongData_volumeId_relativePath",
                "index_LibrarySongData_stableUid",
                "index_LibrarySongData_titleSort",
                "index_LibrarySongData_albumId_discNumber_trackNumber",
                "index_LibrarySongData_primaryArtistSort",
                "index_LibrarySongData_available",
                "index_SongArtistCrossRefData_artistId",
                "index_SongGenreCrossRefData_genreId",
                "index_AlbumArtistCrossRefData_artistId",
                "index_PlaylistItemData_songId",
            )
            .forEach { assertTrue(schema.contains(it), "missing $it") }
        assertFalse(schema.contains("fallbackToDestructiveMigration"))
        // Redundant composite-primary-key-prefix indexes must stay removed.
        listOf(
                "index_SongArtistCrossRefData_songId_artistId",
                "index_SongGenreCrossRefData_songId_genreId",
                "index_PlaylistItemData_playlistId_position",
            )
            .forEach { assertFalse(schema.contains(it), "unexpected redundant $it") }
    }

    @Test
    fun migrationMapIsCheckedIn() {
        val doc = root.resolve("docs/architecture/database-first-library.md")
        assertTrue(Files.exists(doc))
        assertTrue(doc.readText().contains("DBCache.snapshot()"))
    }
}

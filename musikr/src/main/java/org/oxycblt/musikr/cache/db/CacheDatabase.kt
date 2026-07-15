/*
 * Copyright (c) 2023 Auxio Project
 * CacheDatabase.kt is part of Auxio.
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

package org.oxycblt.musikr.cache.db

import android.content.Context
import android.net.Uri
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import org.oxycblt.musikr.tag.Date
import org.oxycblt.musikr.util.correctWhitespace
import org.oxycblt.musikr.util.splitEscaped

@Database(
    entities =
        [
            CachedFileData::class,
            LibraryVolumeData::class,
            LibrarySongData::class,
            LibraryAlbumData::class,
            LibraryArtistData::class,
            LibraryGenreData::class,
            SongArtistCrossRefData::class,
            SongGenreCrossRefData::class,
            AlbumArtistCrossRefData::class,
            LibraryPlaylistData::class,
            PlaylistItemData::class,
            ScanGenerationData::class,
            SourceStateData::class,
            MetadataRevisionData::class,
        ],
    version = 71,
    exportSchema = false,
)
@TypeConverters(CachedFileData.Converters::class)
internal abstract class CacheDatabase : RoomDatabase() {
    abstract fun readDao(): CacheReadDao

    abstract fun writeDao(): CacheWriteDao

    abstract fun libraryDao(): LibraryReadDao

    abstract fun backfillDao(): LibraryBackfillDao

    companion object {
        val MIGRATION_70_71 =
            Migration(70, 71) { database ->
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS LibraryVolumeData (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, stableSourceKey TEXT NOT NULL, displayName TEXT NOT NULL, sourceType TEXT NOT NULL, rootUri TEXT, rootPath TEXT, available INTEGER NOT NULL, lastCommittedGeneration INTEGER, pendingGeneration INTEGER, lastSuccessfulScanMs INTEGER, lastSeenMs INTEGER, configurationRevision INTEGER NOT NULL)"
                )
                database.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_LibraryVolumeData_stableSourceKey ON LibraryVolumeData(stableSourceKey)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS LibraryAlbumData (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL, titleSort TEXT NOT NULL, scanGeneration INTEGER NOT NULL, metadataRevision INTEGER NOT NULL, available INTEGER NOT NULL)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibraryAlbumData_titleSort ON LibraryAlbumData(titleSort)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS LibraryArtistData (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, nameSort TEXT NOT NULL, scanGeneration INTEGER NOT NULL, metadataRevision INTEGER NOT NULL, available INTEGER NOT NULL)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibraryArtistData_nameSort ON LibraryArtistData(nameSort)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS LibraryGenreData (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, nameSort TEXT NOT NULL, scanGeneration INTEGER NOT NULL, metadataRevision INTEGER NOT NULL, available INTEGER NOT NULL)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibraryGenreData_nameSort ON LibraryGenreData(nameSort)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS LibrarySongData (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, stableUid TEXT NOT NULL, volumeId INTEGER NOT NULL, albumId INTEGER, uri TEXT NOT NULL, relativePath TEXT, displayPath TEXT, fileName TEXT NOT NULL, title TEXT NOT NULL, titleSort TEXT NOT NULL, primaryArtistName TEXT, primaryArtistSort TEXT, albumName TEXT, albumSort TEXT, trackNumber INTEGER, discNumber INTEGER, durationMs INTEGER, sizeBytes INTEGER NOT NULL, modifiedTimeMs INTEGER NOT NULL, dateAddedMs INTEGER NOT NULL, mimeType TEXT, codecHint TEXT, embeddedArtworkRef TEXT, externalArtworkRef TEXT, replayGainTrack REAL, replayGainAlbum REAL, musicBrainzIds TEXT, scanGeneration INTEGER NOT NULL, metadataRevision INTEGER NOT NULL, available INTEGER NOT NULL)"
                )
                database.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_LibrarySongData_uri ON LibrarySongData(uri)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibrarySongData_volumeId_relativePath ON LibrarySongData(volumeId, relativePath)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibrarySongData_stableUid ON LibrarySongData(stableUid)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibrarySongData_titleSort ON LibrarySongData(titleSort)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibrarySongData_albumId_discNumber_trackNumber ON LibrarySongData(albumId, discNumber, trackNumber)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibrarySongData_primaryArtistSort ON LibrarySongData(primaryArtistSort)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibrarySongData_dateAddedMs ON LibrarySongData(dateAddedMs)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibrarySongData_modifiedTimeMs ON LibrarySongData(modifiedTimeMs)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_LibrarySongData_available ON LibrarySongData(available)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS SongArtistCrossRefData (songId INTEGER NOT NULL, artistId INTEGER NOT NULL, role TEXT NOT NULL, roleOrder INTEGER NOT NULL, PRIMARY KEY(songId, artistId, role))"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_SongArtistCrossRefData_artistId ON SongArtistCrossRefData(artistId)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS SongGenreCrossRefData (songId INTEGER NOT NULL, genreId INTEGER NOT NULL, genreOrder INTEGER NOT NULL, PRIMARY KEY(songId, genreId))"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_SongGenreCrossRefData_genreId ON SongGenreCrossRefData(genreId)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS AlbumArtistCrossRefData (albumId INTEGER NOT NULL, artistId INTEGER NOT NULL, artistOrder INTEGER NOT NULL, PRIMARY KEY(albumId, artistId))"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_AlbumArtistCrossRefData_artistId ON AlbumArtistCrossRefData(artistId)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS LibraryPlaylistData (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, stableUid TEXT NOT NULL, name TEXT NOT NULL, nameSort TEXT NOT NULL, sourceUri TEXT, scanGeneration INTEGER NOT NULL, metadataRevision INTEGER NOT NULL, available INTEGER NOT NULL)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS PlaylistItemData (playlistId INTEGER NOT NULL, position INTEGER NOT NULL, songId INTEGER, stableSongUid TEXT, uri TEXT, titleFallback TEXT, PRIMARY KEY(playlistId, position))"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_PlaylistItemData_songId ON PlaylistItemData(songId)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS ScanGenerationData (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, volumeId INTEGER NOT NULL, generation INTEGER NOT NULL, state TEXT NOT NULL, startedAtMs INTEGER NOT NULL, completedAtMs INTEGER)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS SourceStateData (sourceKey TEXT NOT NULL PRIMARY KEY, available INTEGER NOT NULL, lastSeenMs INTEGER NOT NULL, lastCommittedGeneration INTEGER)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS MetadataRevisionData (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, targetType TEXT NOT NULL, targetId INTEGER NOT NULL, revision INTEGER NOT NULL, profile TEXT NOT NULL, updatedAtMs INTEGER NOT NULL)"
                )
            }

        fun from(context: Context) =
            Room.databaseBuilder(
                    context.applicationContext,
                    CacheDatabase::class.java,
                    "music_cache.db",
                )
                .addMigrations(MIGRATION_70_71)
                .build()
    }
}

@Dao
internal interface CacheReadDao {
    @Query("SELECT * FROM CachedFileData") suspend fun selectAllSongs(): List<CachedFileData>

    @Query("SELECT * FROM CachedFileData WHERE uri = :uri LIMIT 1")
    suspend fun selectSongByUri(uri: Uri): CachedFileData?

    @Query("SELECT * FROM CachedFileData ORDER BY uri LIMIT :limit OFFSET :offset")
    suspend fun selectSongsPage(limit: Int, offset: Int): List<CachedFileData>
}

@Dao
internal interface CacheWriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun updateSong(data: CachedFileData)

    @Transaction
    suspend fun deleteExcludingUris(uris: Set<String>) {
        val delete = selectAllUris().toSet() - uris
        for (chunk in delete.chunked(999)) {
            deleteExcludingUriChunk(chunk)
        }
    }

    @Query("SELECT uri FROM CachedFileData") suspend fun selectAllUris(): List<String>

    @Query("DELETE FROM CachedFileData WHERE uri IN (:uris)")
    suspend fun deleteExcludingUriChunk(uris: List<String>)
}

@Entity
@TypeConverters(CachedFileData.Converters::class)
internal data class CachedFileData(
    @PrimaryKey val uri: Uri,
    val modifiedMs: Long,
    val addedMs: Long,
    val mimeType: String?,
    val durationMs: Long?,
    val bitrateKbps: Int?,
    val sampleRateHz: Int?,
    val musicBrainzId: String?,
    val name: String?,
    val sortName: String?,
    val track: Int?,
    val disc: Int?,
    val subtitle: String?,
    val date: Date?,
    val albumMusicBrainzId: String?,
    val albumName: String?,
    val albumSortName: String?,
    val releaseTypes: List<String>?,
    val artistMusicBrainzIds: List<String>?,
    val artistNames: List<String>?,
    val artistSortNames: List<String>?,
    val albumArtistMusicBrainzIds: List<String>?,
    val albumArtistNames: List<String>?,
    val albumArtistSortNames: List<String>?,
    val genreNames: List<String>?,
    val replayGainTrackAdjustment: Float?,
    val replayGainAlbumAdjustment: Float?,
    val coverId: String?,
) {
    object Converters {
        @TypeConverter
        fun fromMultiValue(values: List<String>) =
            values.joinToString(";") { it.replace(";", "\\;") }

        @TypeConverter
        fun toMultiValue(string: String) = string.splitEscaped { it == ';' }.correctWhitespace()

        @TypeConverter fun fromDate(date: Date?) = date?.toString()

        @TypeConverter fun toDate(string: String?) = string?.let(Date::from)

        @TypeConverter fun toUri(string: String) = Uri.parse(string)

        @TypeConverter fun fromUri(uri: Uri) = uri.toString()
    }
}

@Entity(indices = [Index(value = ["stableSourceKey"], unique = true)])
internal data class LibraryVolumeData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stableSourceKey: String,
    val displayName: String,
    val sourceType: String,
    val rootUri: String?,
    val rootPath: String?,
    val available: Boolean,
    val lastCommittedGeneration: Long?,
    val pendingGeneration: Long?,
    val lastSuccessfulScanMs: Long?,
    val lastSeenMs: Long?,
    val configurationRevision: Long,
)

@Entity(indices = [Index(value = ["titleSort"])])
internal data class LibraryAlbumData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val titleSort: String,
    val scanGeneration: Long,
    val metadataRevision: Long,
    val available: Boolean,
)

@Entity(indices = [Index(value = ["nameSort"])])
internal data class LibraryArtistData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val nameSort: String,
    val scanGeneration: Long,
    val metadataRevision: Long,
    val available: Boolean,
)

@Entity(indices = [Index(value = ["nameSort"])])
internal data class LibraryGenreData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val nameSort: String,
    val scanGeneration: Long,
    val metadataRevision: Long,
    val available: Boolean,
)

@Entity(
    indices =
        [
            Index(value = ["uri"], unique = true),
            Index(value = ["volumeId", "relativePath"]),
            Index(value = ["stableUid"]),
            Index(value = ["titleSort"]),
            Index(value = ["albumId", "discNumber", "trackNumber"]),
            Index(value = ["primaryArtistSort"]),
            Index(value = ["dateAddedMs"]),
            Index(value = ["modifiedTimeMs"]),
            Index(value = ["available"]),
        ]
)
internal data class LibrarySongData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stableUid: String,
    val volumeId: Long,
    val albumId: Long?,
    val uri: String,
    val relativePath: String?,
    val displayPath: String?,
    val fileName: String,
    val title: String,
    val titleSort: String,
    val primaryArtistName: String?,
    val primaryArtistSort: String?,
    val albumName: String?,
    val albumSort: String?,
    val trackNumber: Int?,
    val discNumber: Int?,
    val durationMs: Long?,
    val sizeBytes: Long,
    val modifiedTimeMs: Long,
    val dateAddedMs: Long,
    val mimeType: String?,
    val codecHint: String?,
    val embeddedArtworkRef: String?,
    val externalArtworkRef: String?,
    val replayGainTrack: Float?,
    val replayGainAlbum: Float?,
    val musicBrainzIds: String?,
    val scanGeneration: Long,
    val metadataRevision: Long,
    val available: Boolean,
)

@Entity(primaryKeys = ["songId", "artistId", "role"], indices = [Index(value = ["artistId"])])
internal data class SongArtistCrossRefData(
    val songId: Long,
    val artistId: Long,
    val role: String,
    val roleOrder: Int,
)

@Entity(primaryKeys = ["songId", "genreId"], indices = [Index(value = ["genreId"])])
internal data class SongGenreCrossRefData(val songId: Long, val genreId: Long, val genreOrder: Int)

@Entity(primaryKeys = ["albumId", "artistId"], indices = [Index(value = ["artistId"])])
internal data class AlbumArtistCrossRefData(
    val albumId: Long,
    val artistId: Long,
    val artistOrder: Int,
)

@Entity
internal data class LibraryPlaylistData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stableUid: String,
    val name: String,
    val nameSort: String,
    val sourceUri: String?,
    val scanGeneration: Long,
    val metadataRevision: Long,
    val available: Boolean,
)

@Entity(primaryKeys = ["playlistId", "position"], indices = [Index(value = ["songId"])])
internal data class PlaylistItemData(
    val playlistId: Long,
    val position: Int,
    val songId: Long?,
    val stableSongUid: String?,
    val uri: String?,
    val titleFallback: String?,
)

@Entity
internal data class ScanGenerationData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val volumeId: Long,
    val generation: Long,
    val state: String,
    val startedAtMs: Long,
    val completedAtMs: Long?,
)

@Entity
internal data class SourceStateData(
    @PrimaryKey val sourceKey: String,
    val available: Boolean,
    val lastSeenMs: Long,
    val lastCommittedGeneration: Long?,
)

@Entity
internal data class MetadataRevisionData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetType: String,
    val targetId: Long,
    val revision: Long,
    val profile: String,
    val updatedAtMs: Long,
)

@Dao
internal interface LibraryReadDao {
    @Query(
        "SELECT id, stableUid, uri, displayPath, title, primaryArtistName, albumName, durationMs, embeddedArtworkRef, externalArtworkRef, available FROM LibrarySongData WHERE available = 1 ORDER BY titleSort, id LIMIT :limit OFFSET :offset"
    )
    suspend fun songsPage(limit: Int, offset: Int): List<SongListRow>

    @Query(
        "SELECT id, stableUid, uri, displayPath, title, primaryArtistName, albumName, durationMs, embeddedArtworkRef, externalArtworkRef, available FROM LibrarySongData WHERE available = 1 ORDER BY dateAddedMs DESC, id DESC LIMIT :limit"
    )
    suspend fun recentlyAdded(limit: Int): List<SongListRow>

    @Query(
        "SELECT id, title, titleSort FROM LibraryAlbumData WHERE available = 1 ORDER BY titleSort, id LIMIT :limit OFFSET :offset"
    )
    suspend fun albumsPage(limit: Int, offset: Int): List<AlbumListRow>

    @Query(
        "SELECT id, name, nameSort FROM LibraryArtistData WHERE available = 1 ORDER BY nameSort, id LIMIT :limit OFFSET :offset"
    )
    suspend fun artistsPage(limit: Int, offset: Int): List<ArtistListRow>

    @Query("SELECT COUNT(*) FROM LibrarySongData WHERE available = 1") suspend fun songCount(): Int

    /**
     * Database-backed, bounded, paged song search.
     *
     * [pattern] must already be a fully escaped `LIKE` contains-pattern (see [LikeQuery.contains]);
     * the `ESCAPE '\'` clause makes any `%`, `_` or `\` originating from user input match literally
     * rather than as wildcards. Ordering is deterministic (`titleSort` then the stable primary key)
     * so that [offset]/[limit] paging never skips or repeats rows.
     */
    @Query(
        "SELECT id, stableUid, uri, title, primaryArtistName, albumName, durationMs, embeddedArtworkRef, externalArtworkRef FROM LibrarySongData WHERE available = 1 AND (titleSort LIKE :pattern ESCAPE '\\' OR primaryArtistSort LIKE :pattern ESCAPE '\\' OR albumSort LIKE :pattern ESCAPE '\\') ORDER BY titleSort, id LIMIT :limit OFFSET :offset"
    )
    suspend fun searchSongs(pattern: String, limit: Int, offset: Int): List<SongListRow>
}

internal data class SongListRow(
    val id: Long,
    val stableUid: String,
    val uri: String,
    val title: String,
    val primaryArtistName: String?,
    val albumName: String?,
    val durationMs: Long?,
    val embeddedArtworkRef: String?,
    val externalArtworkRef: String?,
    val displayPath: String? = null,
    val available: Boolean = true,
)

internal data class AlbumListRow(val id: Long, val title: String, val titleSort: String)

internal data class ArtistListRow(val id: Long, val name: String, val nameSort: String)

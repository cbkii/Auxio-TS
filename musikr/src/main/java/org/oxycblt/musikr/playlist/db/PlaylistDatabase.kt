/*
 * Copyright (c) 2023 Auxio Project
 * PlaylistDatabase.kt is part of Auxio.
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

package org.oxycblt.musikr.playlist.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.oxycblt.musikr.Music

/**
 * Allows persistence of all user-created music information.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@Database(
    entities = [PlaylistInfo::class, PlaylistSong::class, PlaylistSongCrossRef::class],
    version = 31,
    exportSchema = false,
)
@TypeConverters(Music.UID.TypeConverters::class)
internal abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    companion object {
        /**
         * Canonicalise the UID string format introduced after schema 30.
         *
         * [Music.UID.fromString] deliberately accepts both the legacy namespace form and the
         * current compact form, while the Room converter always writes the current form. Without
         * this migration a row read from a v30 database can therefore be rebound under a different
         * primary-key string during rename/rewrite/delete operations.
         *
         * The migration validates every stored UID before mutating anything, moves source keys to
         * temporary collision-free values, merges duplicate legacy/current representations of the
         * same logical UID, and then publishes only canonical keys. Cross-reference rows are never
         * deduplicated because duplicate songs can be meaningful playlist content.
         */
        internal val MIGRATION_30_31 = Migration(30, 31) { db -> canonicaliseStoredUids(db) }

        fun from(context: Context) =
            Room.databaseBuilder(
                    context.applicationContext,
                    PlaylistDatabase::class.java,
                    "user_music.db",
                )
                .addMigrations(MIGRATION_30_31)
                .build()

        private fun canonicaliseStoredUids(db: SupportSQLiteDatabase) {
            val playlistRows = readUidRows(db, "PlaylistInfo", "playlistUid")
            val songRows = readUidRows(db, "PlaylistSong", "songUid")
            val playlistSources =
                linkedSetOf<String>().apply {
                    addAll(playlistRows.map { it.raw })
                    addAll(readDistinctUids(db, "PlaylistSongCrossRef", "playlistUid"))
                }
            val songSources =
                linkedSetOf<String>().apply {
                    addAll(songRows.map { it.raw })
                    addAll(readDistinctUids(db, "PlaylistSongCrossRef", "songUid"))
                }

            // Validate the entire database before the first mutation. A malformed row must abort the
            // migration transaction rather than trigger destructive fallback or a partial rewrite.
            val playlistTargets =
                playlistSources.associateWith { raw -> canonicalUid(raw, "playlistUid") }
            val songTargets = songSources.associateWith { raw -> canonicalUid(raw, "songUid") }

            val playlistLosers = duplicateRepresentations(playlistRows, playlistTargets)
            val songLosers = duplicateRepresentations(songRows, songTargets)
            val playlistTemps = temporaryKeys("playlist", playlistSources)
            val songTemps = temporaryKeys("song", songSources)

            // First move every source key out of the target namespace so legacy/current swaps and
            // duplicate representations cannot trip primary-key constraints halfway through.
            playlistSources.forEach { source ->
                val temporary = playlistTemps.getValue(source)
                updateUid(db, "PlaylistInfo", "playlistUid", source, temporary)
                updateUid(db, "PlaylistSongCrossRef", "playlistUid", source, temporary)
            }
            songSources.forEach { source ->
                val temporary = songTemps.getValue(source)
                updateUid(db, "PlaylistSong", "songUid", source, temporary)
                updateUid(db, "PlaylistSongCrossRef", "songUid", source, temporary)
            }

            // Two textual representations of one Music.UID are one logical entity. Keep one entity
            // row but preserve every cross-reference so playlist contents survive historical mixed
            // databases created by the old read-legacy/write-compact behaviour.
            playlistLosers.forEach { source ->
                deleteUid(db, "PlaylistInfo", "playlistUid", playlistTemps.getValue(source))
            }
            songLosers.forEach { source ->
                deleteUid(db, "PlaylistSong", "songUid", songTemps.getValue(source))
            }

            playlistSources.forEach { source ->
                val temporary = playlistTemps.getValue(source)
                val target = playlistTargets.getValue(source)
                updateUid(db, "PlaylistInfo", "playlistUid", temporary, target)
                updateUid(db, "PlaylistSongCrossRef", "playlistUid", temporary, target)
            }
            songSources.forEach { source ->
                val temporary = songTemps.getValue(source)
                val target = songTargets.getValue(source)
                updateUid(db, "PlaylistSong", "songUid", temporary, target)
                updateUid(db, "PlaylistSongCrossRef", "songUid", temporary, target)
            }
        }

        private fun duplicateRepresentations(
            rows: List<StoredUidRow>,
            targets: Map<String, String>,
        ): Set<String> =
            rows
                .groupBy { targets.getValue(it.raw) }
                .values
                .flatMap { group ->
                    if (group.size <= 1) {
                        emptyList()
                    } else {
                        val canonical = targets.getValue(group.first().raw)
                        val winner =
                            group.firstOrNull { it.raw == canonical }
                                ?: group.maxBy { it.rowId }
                        group.filterNot { it.raw == winner.raw }.map { it.raw }
                    }
                }
                .toSet()

        private fun canonicalUid(raw: String, column: String): String =
            requireNotNull(Music.UID.fromString(raw)) {
                    "Playlist database contains an invalid $column UID"
                }
                .toString()

        private fun temporaryKeys(prefix: String, sources: Collection<String>): Map<String, String> =
            sources.withIndex().associate { (index, source) ->
                source to "__auxio_uid_migration_31_${prefix}_$index"
            }

        private fun readUidRows(
            db: SupportSQLiteDatabase,
            table: String,
            column: String,
        ): List<StoredUidRow> =
            db.query("SELECT rowid, `$column` FROM `$table` ORDER BY rowid ASC").use { cursor ->
                val rowIdIndex = cursor.getColumnIndexOrThrow("rowid")
                val uidIndex = cursor.getColumnIndexOrThrow(column)
                buildList {
                    while (cursor.moveToNext()) {
                        add(
                            StoredUidRow(
                                rowId = cursor.getLong(rowIdIndex),
                                raw =
                                    requireNotNull(cursor.getString(uidIndex)) {
                                        "Playlist database contains null $column"
                                    },
                            )
                        )
                    }
                }
            }

        private fun readDistinctUids(
            db: SupportSQLiteDatabase,
            table: String,
            column: String,
        ): List<String> =
            db.query("SELECT DISTINCT `$column` FROM `$table`").use { cursor ->
                val uidIndex = cursor.getColumnIndexOrThrow(column)
                buildList {
                    while (cursor.moveToNext()) {
                        add(
                            requireNotNull(cursor.getString(uidIndex)) {
                                "Playlist database contains null $column"
                            }
                        )
                    }
                }
            }

        private fun updateUid(
            db: SupportSQLiteDatabase,
            table: String,
            column: String,
            from: String,
            to: String,
        ) {
            db.execSQL(
                "UPDATE `$table` SET `$column` = ? WHERE `$column` = ?",
                arrayOf(to, from),
            )
        }

        private fun deleteUid(
            db: SupportSQLiteDatabase,
            table: String,
            column: String,
            value: String,
        ) {
            db.execSQL("DELETE FROM `$table` WHERE `$column` = ?", arrayOf(value))
        }

        private data class StoredUidRow(val rowId: Long, val raw: String)
    }
}

// TODO: Handle playlist defragmentation? I really don't want dead songs to accumulate in this
//  database.

/**
 * The DAO for persisted playlist information.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@Dao
internal abstract class PlaylistDao {
    /**
     * Read out all playlists stored in the database.
     *
     * @return A list of [RawPlaylist] representing each [RawPlaylist] representing each playlist stored.
     */
    @Transaction
    @Query("SELECT * FROM PlaylistInfo")
    abstract suspend fun readRawPlaylists(): List<RawPlaylist>

    /** Create a new playlist entry. */
    @Transaction
    open suspend fun insertPlaylist(rawPlaylist: RawPlaylist) {
        insertInfo(rawPlaylist.playlistInfo)
        insertSongs(rawPlaylist.songs)
        insertRefs(
            rawPlaylist.songs.map {
                PlaylistSongCrossRef(
                    playlistUid = rawPlaylist.playlistInfo.playlistUid,
                    songUid = it.songUid,
                )
            }
        )
    }

    /** Replace the currently-stored [PlaylistInfo] for a playlist entry. */
    @Transaction
    open suspend fun replacePlaylistInfo(playlistInfo: PlaylistInfo) {
        deleteInfo(playlistInfo.playlistUid)
        insertInfo(playlistInfo)
    }

    /** Delete a playlist entry and its cross-references. */
    @Transaction
    open suspend fun deletePlaylist(playlistUid: Music.UID) {
        deleteInfo(playlistUid)
        deleteRefs(playlistUid)
    }

    /** Insert songs into an existing playlist. */
    @Transaction
    open suspend fun insertPlaylistSongs(playlistUid: Music.UID, songs: List<PlaylistSong>) {
        insertSongs(songs)
        insertRefs(
            songs.map { PlaylistSongCrossRef(playlistUid = playlistUid, songUid = it.songUid) }
        )
    }

    /** Replace the currently stored songs of the given playlist entry. */
    @Transaction
    open suspend fun replacePlaylistSongs(playlistUid: Music.UID, songs: List<PlaylistSong>) {
        deleteRefs(playlistUid)
        insertSongs(songs)
        insertRefs(
            songs.map { PlaylistSongCrossRef(playlistUid = playlistUid, songUid = it.songUid) }
        )
    }

    /** Internal, do not use. */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertInfo(info: PlaylistInfo)

    /** Internal, do not use. */
    @Query("DELETE FROM PlaylistInfo where playlistUid = :playlistUid")
    abstract suspend fun deleteInfo(playlistUid: Music.UID)

    /** Internal, do not use. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertSongs(songs: List<PlaylistSong>)

    /** Internal, do not use. */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertRefs(refs: List<PlaylistSongCrossRef>)

    /** Internal, do not use. */
    @Query("DELETE FROM PlaylistSongCrossRef where playlistUid = :playlistUid")
    abstract suspend fun deleteRefs(playlistUid: Music.UID)
}

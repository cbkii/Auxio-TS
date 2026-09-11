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

@Database(
    entities = [PlaylistInfo::class, PlaylistSong::class, PlaylistSongCrossRef::class],
    version = 31,
    exportSchema = false,
)
@TypeConverters(Music.UID.TypeConverters::class)
internal abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    companion object {
        internal val MIGRATION_30_31 =
            object : Migration(30, 31) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    canonicalizeUidColumn(db, "PlaylistInfo", "playlistUid", collapsePrimaryKey = true)
                    canonicalizeUidColumn(
                        db,
                        "PlaylistSongCrossRef",
                        "playlistUid",
                        collapsePrimaryKey = false,
                    )
                }

                private fun canonicalizeUidColumn(
                    db: SupportSQLiteDatabase,
                    table: String,
                    column: String,
                    collapsePrimaryKey: Boolean,
                ) {
                    val values =
                        db.query("SELECT DISTINCT `$column` FROM `$table`").use { cursor ->
                            val index = cursor.getColumnIndexOrThrow(column)
                            buildList {
                                while (cursor.moveToNext()) {
                                    cursor.getString(index)?.let(::add)
                                }
                            }
                        }

                    val pending = mutableListOf<Pair<String, String>>()
                    values.forEachIndexed { index, oldValue ->
                        val canonical = Music.UID.fromString(oldValue)?.toString() ?: return@forEachIndexed
                        if (canonical == oldValue) return@forEachIndexed
                        val temporary = "__auxio_uid_31_${index}__${canonical}"
                        db.execSQL(
                            "UPDATE `$table` SET `$column` = ? WHERE `$column` = ?",
                            arrayOf<Any?>(temporary, oldValue),
                        )
                        pending += temporary to canonical
                    }

                    for ((temporary, canonical) in pending) {
                        if (collapsePrimaryKey && rowExists(db, table, column, canonical)) {
                            db.execSQL(
                                "DELETE FROM `$table` WHERE `$column` = ?",
                                arrayOf<Any?>(temporary),
                            )
                        } else {
                            db.execSQL(
                                "UPDATE `$table` SET `$column` = ? WHERE `$column` = ?",
                                arrayOf<Any?>(canonical, temporary),
                            )
                        }
                    }
                }

                private fun rowExists(
                    db: SupportSQLiteDatabase,
                    table: String,
                    column: String,
                    value: String,
                ): Boolean =
                    db.query(
                            "SELECT 1 FROM `$table` WHERE `$column` = ? LIMIT 1",
                            arrayOf<Any?>(value),
                        )
                        .use { it.moveToFirst() }
            }

        fun from(context: Context) =
            Room.databaseBuilder(
                    context.applicationContext,
                    PlaylistDatabase::class.java,
                    "user_music.db",
                )
                .addMigrations(MIGRATION_30_31)
                .fallbackToDestructiveMigration(true)
                .build()
    }
}

@Dao
internal abstract class PlaylistDao {
    @Transaction
    @Query("SELECT * FROM PlaylistInfo")
    abstract suspend fun readRawPlaylists(): List<RawPlaylist>

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

    @Transaction
    open suspend fun replacePlaylistInfo(playlistInfo: PlaylistInfo) {
        deleteInfo(playlistInfo.playlistUid)
        insertInfo(playlistInfo)
    }

    @Transaction
    open suspend fun deletePlaylist(playlistUid: Music.UID) {
        deleteInfo(playlistUid)
        deleteRefs(playlistUid)
    }

    @Transaction
    open suspend fun insertPlaylistSongs(playlistUid: Music.UID, songs: List<PlaylistSong>) {
        insertSongs(songs)
        insertRefs(
            songs.map { PlaylistSongCrossRef(playlistUid = playlistUid, songUid = it.songUid) }
        )
    }

    @Transaction
    open suspend fun replacePlaylistSongs(playlistUid: Music.UID, songs: List<PlaylistSong>) {
        deleteRefs(playlistUid)
        insertSongs(songs)
        insertRefs(
            songs.map { PlaylistSongCrossRef(playlistUid = playlistUid, songUid = it.songUid) }
        )
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertInfo(info: PlaylistInfo)

    @Query("DELETE FROM PlaylistInfo where playlistUid = :playlistUid")
    abstract suspend fun deleteInfo(playlistUid: Music.UID)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertSongs(songs: List<PlaylistSong>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertRefs(refs: List<PlaylistSongCrossRef>)

    @Query("DELETE FROM PlaylistSongCrossRef where playlistUid = :playlistUid")
    abstract suspend fun deleteRefs(playlistUid: Music.UID)
}

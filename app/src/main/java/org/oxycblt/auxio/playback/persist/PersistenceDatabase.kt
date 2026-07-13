/*
 * Copyright (c) 2023 Auxio Project
 * PersistenceDatabase.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.persist

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Music

/** Provides raw access to the database storing persisted playback state. */
@Database(
    entities =
        [
            PlaybackState::class,
            QueueHeapItem::class,
            QueueShuffledMappingItem::class,
            QueueSessionEntity::class,
            QueueItemRefEntity::class,
        ],
    version = 40,
    exportSchema = false,
)
@TypeConverters(Music.UID.TypeConverters::class)
abstract class PersistenceDatabase : RoomDatabase() {
    abstract fun playbackStateDao(): PlaybackStateDao

    abstract fun queueDao(): QueueDao

    companion object {
        val MIGRATION_27_32 =
            Migration(27, 32) {
                it.execSQL("ALTER TABLE playback_state RENAME TO PlaybackState")
                it.execSQL("ALTER TABLE queue_heap RENAME TO QueueHeapItem")
                it.execSQL("ALTER TABLE queue_mapping RENAME TO QueueMappingItem")
            }

        val MIGRATION_38_39 =
            Migration(38, 39) {
                it.execSQL(
                    "ALTER TABLE PlaybackState ADD COLUMN shuffleScope TEXT NOT NULL DEFAULT 'OFF'"
                )
                it.execSQL(
                    "UPDATE PlaybackState SET shuffleScope = 'ALL' " +
                        "WHERE id = 0 AND EXISTS (SELECT 1 FROM QueueShuffledMappingItem LIMIT 1)"
                )
            }

        val MIGRATION_39_40 =
            Migration(39, 40) { database ->
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS QueueSessionEntity (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "currentLogicalPosition INTEGER NOT NULL, " +
                        "positionMs INTEGER NOT NULL, " +
                        "repeatMode TEXT NOT NULL, " +
                        "shuffleScope TEXT NOT NULL, " +
                        "totalCount INTEGER NOT NULL, " +
                        "revision INTEGER NOT NULL, " +
                        "updatedAtMs INTEGER NOT NULL)"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS QueueItemRefEntity (" +
                        "sessionId INTEGER NOT NULL, " +
                        "logicalPosition INTEGER NOT NULL, " +
                        "stableSongUid TEXT, " +
                        "uri TEXT, " +
                        "pathFallback TEXT, " +
                        "titleFallback TEXT, " +
                        "artistFallback TEXT, " +
                        "albumFallback TEXT, " +
                        "durationMs INTEGER NOT NULL, " +
                        "PRIMARY KEY(sessionId, logicalPosition))"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_QueueItemRefEntity_sessionId_logicalPosition " +
                        "ON QueueItemRefEntity(sessionId, logicalPosition)"
                )
                database.execSQL(
                    "INSERT INTO QueueSessionEntity " +
                        "(id, currentLogicalPosition, positionMs, repeatMode, shuffleScope, " +
                        "totalCount, revision, updatedAtMs) " +
                        "SELECT 1, " +
                        "CASE WHEN (SELECT COUNT(*) FROM QueueShuffledMappingItem) > 0 " +
                        "THEN COALESCE((SELECT id FROM QueueShuffledMappingItem " +
                        "WHERE `index` = PlaybackState.`index` LIMIT 1), PlaybackState.`index`) " +
                        "ELSE PlaybackState.`index` END, " +
                        "PlaybackState.positionMs, PlaybackState.repeatMode, " +
                        "PlaybackState.shuffleScope, (SELECT COUNT(*) FROM QueueHeapItem), " +
                        "1, strftime('%s', 'now') * 1000 " +
                        "FROM PlaybackState WHERE PlaybackState.id = 0"
                )
                database.execSQL(
                    "INSERT INTO QueueItemRefEntity " +
                        "(sessionId, logicalPosition, stableSongUid, uri, pathFallback, " +
                        "titleFallback, artistFallback, albumFallback, durationMs) " +
                        "SELECT 1, QueueHeapItem.id, QueueHeapItem.uid, NULL, NULL, NULL, NULL, NULL, 0 " +
                        "FROM QueueHeapItem " +
                        "WHERE (SELECT COUNT(*) FROM QueueShuffledMappingItem) = 0"
                )
                database.execSQL(
                    "INSERT INTO QueueItemRefEntity " +
                        "(sessionId, logicalPosition, stableSongUid, uri, pathFallback, " +
                        "titleFallback, artistFallback, albumFallback, durationMs) " +
                        "SELECT 1, QueueShuffledMappingItem.id, QueueHeapItem.uid, " +
                        "NULL, NULL, NULL, NULL, NULL, 0 " +
                        "FROM QueueShuffledMappingItem " +
                        "JOIN QueueHeapItem ON QueueHeapItem.id = QueueShuffledMappingItem.`index`"
                )
            }
    }
}

@Dao
interface PlaybackStateDao {
    @Query("SELECT * FROM PlaybackState WHERE id = 0") suspend fun getState(): PlaybackState?

    @Query("DELETE FROM PlaybackState") suspend fun nukeState()

    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertState(state: PlaybackState)
}

@Dao
interface QueueDao {
    @Query("SELECT * FROM QueueHeapItem") suspend fun getHeap(): List<QueueHeapItem>

    @Query("SELECT * FROM QueueShuffledMappingItem")
    suspend fun getShuffledMapping(): List<QueueShuffledMappingItem>

    @Query("DELETE FROM QueueHeapItem") suspend fun nukeHeap()

    @Query("DELETE FROM QueueShuffledMappingItem") suspend fun nukeShuffledMapping()

    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertHeap(heap: List<QueueHeapItem>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertShuffledMapping(mapping: List<QueueShuffledMappingItem>)

    @Query("SELECT * FROM QueueSessionEntity WHERE id = 1")
    suspend fun getQueueSession(): QueueSessionEntity?

    @Query(
        "SELECT * FROM QueueItemRefEntity WHERE sessionId = :sessionId " +
            "AND logicalPosition >= :startInclusive AND logicalPosition < :endExclusive " +
            "ORDER BY logicalPosition ASC"
    )
    suspend fun getQueueWindow(
        sessionId: Long,
        startInclusive: Int,
        endExclusive: Int,
    ): List<QueueItemRefEntity>

    @Query("SELECT COUNT(*) FROM QueueItemRefEntity WHERE sessionId = :sessionId")
    suspend fun countQueueItems(sessionId: Long): Int

    @Query("DELETE FROM QueueSessionEntity") suspend fun nukeQueueSessions()

    @Query("DELETE FROM QueueItemRefEntity") suspend fun nukeQueueItemRefs()

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertQueueSession(session: QueueSessionEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertQueueItemRefs(items: List<QueueItemRefEntity>)


}

@Entity
data class PlaybackState(
    @PrimaryKey val id: Int,
    val index: Int,
    val positionMs: Long,
    val repeatMode: RepeatMode,
    val songUid: Music.UID,
    val parentUid: Music.UID?,
    val shuffleScope: ShuffleScope,
)

@Entity data class QueueHeapItem(@PrimaryKey val id: Int, val uid: Music.UID)

@Entity data class QueueShuffledMappingItem(@PrimaryKey val id: Int, val index: Int)

@Entity
data class QueueSessionEntity(
    @PrimaryKey val id: Long,
    val currentLogicalPosition: Int,
    val positionMs: Long,
    val repeatMode: RepeatMode,
    val shuffleScope: ShuffleScope,
    val totalCount: Int,
    val revision: Long,
    val updatedAtMs: Long,
)

@Entity(
    primaryKeys = ["sessionId", "logicalPosition"],
    indices = [Index(value = ["sessionId", "logicalPosition"])],
)
data class QueueItemRefEntity(
    val sessionId: Long,
    val logicalPosition: Int,
    val stableSongUid: Music.UID?,
    val uri: String?,
    val pathFallback: String?,
    val titleFallback: String?,
    val artistFallback: String?,
    val albumFallback: String?,
    val durationMs: Long,
)

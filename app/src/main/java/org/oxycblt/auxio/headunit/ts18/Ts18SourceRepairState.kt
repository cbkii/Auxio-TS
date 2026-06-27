/*
 * Copyright (c) 2026 Auxio Project
 * Ts18SourceRepairState.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.ts18

import java.io.File

/** Minimal bounded source-state model for TS18 USB/removable storage. */
object Ts18SourceRepairStatePolicy {
    val defaultUsbRoots = listOf("/storage/usbdisk0", "/storage/usbdisk1")

    enum class Kind {
        ALL_SOURCES_READY,
        MOUNT_MISSING,
        DIRECT_PATH_INACCESSIBLE,
        SAF_PERMISSION_MISSING,
        SAF_PROVIDER_FAILURE,
        SOURCE_EMPTY,
        SOURCE_CONTAINS_NO_SUPPORTED_AUDIO,
        MIXED_MULTIPLE_VOLUME_STATE,
        UNKNOWN_FAILURE,
    }

    data class SourceState(
        val path: String,
        val kind: Kind,
        val evidence: String,
        val recommendedAction: Action,
    )

    enum class Action {
        NONE,
        REINSERT_USB,
        RESCAN,
        CHOOSE_SOURCE,
        SWITCH_TO_DIRECT_PATH_MODE,
        CLEAR_INACCESSIBLE_SOURCE,
        KEEP_CACHED_LIBRARY,
    }

    fun classifyDirectPaths(paths: List<String> = defaultUsbRoots): List<SourceState> {
        return paths.map(::classifyDirectPath)
    }

    fun summarise(states: List<SourceState>): Kind {
        if (states.isEmpty()) return Kind.UNKNOWN_FAILURE
        if (states.all { it.kind == Kind.ALL_SOURCES_READY }) return Kind.ALL_SOURCES_READY
        if (states.map { it.kind }.distinct().size > 1) return Kind.MIXED_MULTIPLE_VOLUME_STATE
        return states.first().kind
    }

    fun classifyDirectPath(path: String): SourceState {
        if (!RawFastResumeValidator.isAllowedDirectPath(path.trimEnd('/') + "/probe.mp3")) {
            return SourceState(
                path,
                Kind.DIRECT_PATH_INACCESSIBLE,
                "path is outside the accepted normal-app-readable TS18 media roots",
                Action.CHOOSE_SOURCE,
            )
        }

        val file = File(path)
        return try {
            when {
                !file.exists() ->
                    SourceState(
                        path,
                        Kind.MOUNT_MISSING,
                        "mount path does not exist",
                        Action.REINSERT_USB,
                    )
                !file.isDirectory || !file.canRead() ->
                    SourceState(
                        path,
                        Kind.DIRECT_PATH_INACCESSIBLE,
                        "mount path exists but is not readable as a directory",
                        Action.KEEP_CACHED_LIBRARY,
                    )
                else -> classifyReadableDirectory(path, file)
            }
        } catch (e: SecurityException) {
            SourceState(
                path,
                Kind.DIRECT_PATH_INACCESSIBLE,
                e.message.orEmpty(),
                Action.KEEP_CACHED_LIBRARY,
            )
        } catch (e: RuntimeException) {
            SourceState(path, Kind.UNKNOWN_FAILURE, e.message.orEmpty(), Action.KEEP_CACHED_LIBRARY)
        }
    }

    private fun classifyReadableDirectory(path: String, directory: File): SourceState {
        val rootEntries =
            try {
                directory.listFiles()?.filterNot { it.name.startsWith(".") }?.take(BOUNDED_ENTRY_LIMIT)
            } catch (e: SecurityException) {
                null
            } catch (e: RuntimeException) {
                null
            }
                ?: return SourceState(
                    path,
                    Kind.UNKNOWN_FAILURE,
                    "listFiles failed or returned null",
                    Action.RESCAN,
                )
        if (rootEntries.isEmpty()) {
            return SourceState(
                path,
                Kind.SOURCE_EMPTY,
                "directory has no visible non-hidden entries",
                Action.RESCAN,
            )
        }

        val hasAudioLike = hasAudioLikeWithinBoundedProbe(directory)
        return if (hasAudioLike) {
            SourceState(
                path,
                Kind.ALL_SOURCES_READY,
                "found at least one audio-like file within bounded recursive probe",
                Action.NONE,
            )
        } else {
            SourceState(
                path,
                Kind.SOURCE_CONTAINS_NO_SUPPORTED_AUDIO,
                "first $BOUNDED_ENTRY_LIMIT entries contain no supported audio-like files",
                Action.CHOOSE_SOURCE,
            )
        }
    }

    internal fun hasAudioLikeWithinBoundedProbe(directory: File): Boolean {
        val pending = ArrayDeque<File>()
        pending.add(directory)
        var visited = 0

        while (pending.isNotEmpty() && visited < BOUNDED_ENTRY_LIMIT) {
            val current = pending.removeFirst()
            val entries =
                try {
                    current.listFiles()?.asList()
                } catch (e: SecurityException) {
                    null
                } catch (e: RuntimeException) {
                    null
                } ?: continue
            for (entry in entries) {
                if (entry.name.startsWith(".")) continue
                if (visited >= BOUNDED_ENTRY_LIMIT) break
                visited += 1
                try {
                    when {
                        entry.isFile && RawFastResumeValidator.hasAudioExtension(entry.name) ->
                            return true
                        entry.isDirectory && entry.canRead() -> pending.add(entry)
                    }
                } catch (e: SecurityException) {
                    // Skip restricted USB/system entries and keep probing other readable paths.
                } catch (e: RuntimeException) {
                    // Skip unstable/removing USB entries and keep the bounded probe alive.
                }
            }
        }

        return false
    }

    private const val BOUNDED_ENTRY_LIMIT = 64
}

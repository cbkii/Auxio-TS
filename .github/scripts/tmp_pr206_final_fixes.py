from pathlib import Path
import re


def read(path: str) -> str:
    return Path(path).read_text(encoding="utf-8")


def write(path: str, text: str) -> None:
    Path(path).write_text(text, encoding="utf-8", newline="\n")


def replace_once(path: str, old: str, new: str) -> None:
    replace_exact_count(path, old, new, expected=1)


def replace_exact_count(path: str, old: str, new: str, *, expected: int) -> None:
    text = read(path)
    count = text.count(old)
    if count == 0 and new in text:
        print(f"already applied: {path}")
        return
    if count != expected:
        raise SystemExit(f"{path}: expected {expected} exact matches, found {count}")
    write(path, text.replace(old, new))
    print(f"updated: {path}")


def replace_regex_once(path: str, pattern: str, replacement: str, *, flags: int = 0) -> None:
    text = read(path)
    matches = list(re.finditer(pattern, text, flags))
    if len(matches) != 1:
        raise SystemExit(f"{path}: expected one regex match, found {len(matches)} for {pattern!r}")
    write(path, re.sub(pattern, replacement, text, count=1, flags=flags))
    print(f"updated: {path}")


def write_if_changed(path: str, content: str) -> None:
    current = read(path)
    if current == content:
        print(f"already applied: {path}")
        return
    write(path, content)
    print(f"rewrote: {path}")


music_settings = "app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt"
replace_once(
    music_settings,
    "    /** Consume a durable first-scan marker after the indexing worker is attached. */\n"
    "    fun consumePendingInitialScan(): Boolean = false",
    "    /** Atomically consume a first-scan marker and return its source generation. */\n"
    "    fun consumePendingInitialScan(): Long? = null",
)
replace_once(
    music_settings,
    "    @Synchronized\n"
    "    override fun consumePendingInitialScan(): Boolean {\n"
    "        if (!sharedPreferences.getBoolean(KEY_PENDING_INITIAL_SCAN, false)) return false\n"
    "        sharedPreferences.edit(commit = true) { putBoolean(KEY_PENDING_INITIAL_SCAN, false) }\n"
    "        return true\n"
    "    }",
    "    @Synchronized\n"
    "    override fun consumePendingInitialScan(): Long? {\n"
    "        if (!sharedPreferences.getBoolean(KEY_PENDING_INITIAL_SCAN, false)) return null\n"
    "        val generation = sourceConfigurationGeneration\n"
    "        sharedPreferences.edit(commit = true) { putBoolean(KEY_PENDING_INITIAL_SCAN, false) }\n"
    "        return generation\n"
    "    }",
)

indexing_holder = "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt"
replace_once(indexing_holder, "import org.oxycblt.auxio.headunit.root.RootStateHolder\n", "")
replace_once(indexing_holder, "import org.oxycblt.auxio.music.RootAccessPolicy\n", "")
replace_exact_count(
    indexing_holder,
    "        private val musicSettings: MusicSettings,\n"
    "        private val rootGate: RootStateHolder,\n",
    "        private val musicSettings: MusicSettings,\n",
    expected=2,
)
replace_once(
    indexing_holder,
    "                musicRepository,\n"
    "                musicSettings,\n"
    "                rootGate,\n"
    "            )",
    "                musicRepository,\n"
    "                musicSettings,\n"
    "            )",
)
replace_once(
    indexing_holder,
    "                        val pendingInitialScan = musicSettings.consumePendingInitialScan()\n"
    "                        if (pendingInitialScan) {\n"
    "                            synchronized(this@IndexingHolder) {\n"
    "                                lastHandledSourceConfigurationGeneration =\n"
    "                                    musicSettings.sourceConfigurationGeneration\n"
    "                            }\n"
    "                            L.i(\"Consuming durable source configuration with a simple initial scan\")\n"
    "                            requestIndex(false)\n",
    "                        val pendingInitialScanGeneration =\n"
    "                            synchronized(this@IndexingHolder) {\n"
    "                                musicSettings.consumePendingInitialScan()?.also { generation ->\n"
    "                                    lastHandledSourceConfigurationGeneration = generation\n"
    "                                }\n"
    "                            }\n"
    "                        if (pendingInitialScanGeneration != null) {\n"
    "                            L.i(\n"
    "                                \"Consuming durable source configuration with a simple initial scan \" +\n"
    "                                    \"[generation=$pendingInitialScanGeneration]\"\n"
    "                            )\n"
    "                            requestIndex(false)\n",
)
replace_regex_once(
    indexing_holder,
    r"                LocationMode\.DIRECT_FS ->\n"
    r"                    DirectFS\(\n"
    r"                        musicSettings\.safQuery\.source,\n"
    r"                        rootGate\.takeIf \{\n"
    r"                            musicSettings\.rootAccessPolicy == RootAccessPolicy\.ON_DEMAND\n"
    r"                        \},\n"
    r"                    \)",
    "                LocationMode.DIRECT_FS -> DirectFS(musicSettings.safQuery.source)",
)
replace_regex_once(
    indexing_holder,
    r"    override fun onMusicLocationsChanged\(\) \{\n"
    r"        super\.onMusicLocationsChanged\(\)\n"
    r"        val generation = musicSettings\.sourceConfigurationGeneration\n"
    r"        val shouldHandle =\n"
    r"            synchronized\(this\) \{\n"
    r"                if \(generation == lastHandledSourceConfigurationGeneration\) \{\n"
    r"                    false\n"
    r"                \} else \{\n"
    r"                    lastHandledSourceConfigurationGeneration = generation\n"
    r"                    true\n"
    r"                \}\n"
    r"            \}\n"
    r"        if \(!shouldHandle\) \{\n"
    r"            L\.d\(\"Ignoring duplicate source callback \[generation=\$generation\]\"\)\n"
    r"            return\n"
    r"        \}\n"
    r"        if \(musicSettings\.shouldBeObserving\) startTracking\(\) else stopTracking\(\)\n"
    r"        val initialScan = musicSettings\.consumePendingInitialScan\(\)\n"
    r"        indexScope\.launch \{\n"
    r"            musicRepository\.invalidateSource\(\)\n"
    r"            musicRepository\.requestIndex\(withCache = !initialScan\)\n"
    r"        \}\n"
    r"    \}",
    """    override fun onMusicLocationsChanged() {
        super.onMusicLocationsChanged()
        val (generation, initialScan, shouldHandle) =
            synchronized(this) {
                val pendingGeneration = musicSettings.consumePendingInitialScan()
                val currentGeneration =
                    pendingGeneration ?: musicSettings.sourceConfigurationGeneration
                if (currentGeneration == lastHandledSourceConfigurationGeneration) {
                    Triple(currentGeneration, pendingGeneration != null, false)
                } else {
                    lastHandledSourceConfigurationGeneration = currentGeneration
                    Triple(currentGeneration, pendingGeneration != null, true)
                }
            }
        if (!shouldHandle) {
            L.d("Ignoring duplicate source callback [generation=$generation]")
            return
        }
        if (musicSettings.shouldBeObserving) startTracking() else stopTracking()
        indexScope.launch {
            musicRepository.invalidateSource()
            musicRepository.requestIndex(withCache = !initialScan)
        }
    }""",
)

music_repository = "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt"
replace_once(
    music_repository,
    "                    } catch (e: CancellationException) {\n"
    "                        withContext(NonCancellable) {\n"
    "                            emitIndexingCompletion(Exception(\"Music-source preflight cancelled\", e))\n"
    "                        }\n"
    "                        throw e\n",
    "                    } catch (e: CancellationException) {\n"
    "                        throw e\n",
)
replace_once(
    music_repository,
    "                musicSettings.revision = newRevision\n"
    "                val publishedLibrary =",
    "                val publishedLibrary =",
)
replace_once(
    music_repository,
    "                    } else {\n"
    "                        throw SourceScanFailureException(result.failedSources)\n"
    "                    }\n"
    "                emitLibrary(publishedLibrary)\n",
    "                    } else {\n"
    "                        throw SourceScanFailureException(result.failedSources)\n"
    "                    }\n"
    "                musicSettings.revision = newRevision\n"
    "                emitLibrary(publishedLibrary)\n",
)
replace_regex_once(
    music_repository,
    r"            LocationMode\.DIRECT_FS ->\n"
    r"                DirectFS\(\n"
    r"                    musicSettings\.safQuery\.source,\n"
    r"                    rootGate\.takeIf \{ musicSettings\.rootAccessPolicy == RootAccessPolicy\.ON_DEMAND \},\n"
    r"                \)",
    "            LocationMode.DIRECT_FS -> DirectFS(musicSettings.safQuery.source)",
)

direct_fs = "musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt"
replace_once(direct_fs, "import org.oxycblt.musikr.fs.RootGate\n", "")
replace_once(direct_fs, "import org.oxycblt.musikr.fs.RootTreeSnapshot\n", "")
replace_once(
    direct_fs,
    "class DirectFS(private val roots: List<Location.Opened>, private val rootGate: RootGate? = null) :\n"
    "    SourceAwareFS {",
    "class DirectFS(private val roots: List<Location.Opened>) : SourceAwareFS {",
)
replace_once(
    direct_fs,
    "    private val sourceFailures = ConcurrentHashMap<String, String>()\n"
    "    private val rootSnapshotChecked = ConcurrentHashMap.newKeySet<String>()\n"
    "    private val rootSnapshotEntries = ConcurrentHashMap<String, Map<String, List<DirectEntry>>>()",
    "    private val sourceFailures = ConcurrentHashMap<String, String>()",
)
replace_once(
    direct_fs,
    "    override fun selectSources(sourceKeys: Set<String>): FS =\n"
    "        DirectFS(roots.filter { SourceIdentity.forLocation(it) in sourceKeys }, rootGate)",
    "    override fun selectSources(sourceKeys: Set<String>): FS =\n"
    "        DirectFS(roots.filter { SourceIdentity.forLocation(it) in sourceKeys })",
)
replace_once(
    direct_fs,
    "        if (task.depth > MAX_DEPTH) {\n"
    "            val detail = \"DirectFS maximum depth exceeded at ${task.directory.path}\"\n"
    "            if (task.configuredRootTask) recordFailure(task.sourceKey, detail)\n"
    "            else Log.w(TAG, detail)\n"
    "            return\n"
    "        }",
    "        if (task.depth > MAX_DEPTH) {\n"
    "            Log.w(TAG, \"DirectFS maximum depth exceeded at ${task.directory.path}\")\n"
    "            return\n"
    "        }",
)
replace_once(
    direct_fs,
    "            if (current >= MAX_VISITED_DIRECTORIES) {\n"
    "                Log.w(TAG, \"DirectFS directory limit reached at ${task.directory.path}\")\n"
    "                return EnqueueResult.LimitExceeded\n"
    "            }",
    "            if (current >= MAX_VISITED_DIRECTORIES) {\n"
    "                recordFailure(\n"
    "                    task.sourceKey,\n"
    "                    \"DirectFS directory limit reached at ${task.directory.path}\",\n"
    "                )\n"
    "                return EnqueueResult.LimitExceeded\n"
    "            }",
)
replace_regex_once(
    direct_fs,
    r"\n        val canonicalRoot = configuredRootFor\(directory\).*?"
    r"\n        Log\.w\(TAG, \"DirectFS source is unavailable or inaccessible: \$\{directory\.path\}\"\)",
    "\n        Log.w(TAG, \"DirectFS source is unavailable or inaccessible: ${directory.path}\")",
    flags=re.DOTALL,
)
replace_regex_once(
    direct_fs,
    r"\n    private fun indexRootSnapshot\(.*?\n    private data class RootSnapshot\(",
    "\n    private data class RootSnapshot(",
    flags=re.DOTALL,
)

volume_manager = "musikr/src/main/java/org/oxycblt/musikr/fs/path/VolumeManager.kt"
write_if_changed(
    volume_manager,
    """/*
 * Copyright (c) 2024 Auxio Project
 * VolumeManager.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.path

import android.content.Context
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import java.io.File
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.Volume

/** A wrapper around [StorageManager] that provides instances of the [Volume] interface. */
internal interface VolumeManager {
    /**
     * The internal storage volume of the device.
     *
     * @see StorageManager.getPrimaryStorageVolume
     */
    fun getInternalVolume(): Volume.Internal

    /**
     * The list of [Volume]s currently recognized by [StorageManager].
     *
     * @see StorageManager.getStorageVolumes
     */
    fun getVolumes(): List<Volume>

    companion object {
        fun from(context: Context): VolumeManager =
            VolumeManagerImpl(context.getSystemService(StorageManager::class.java))
    }
}

private class VolumeManagerImpl(private val storageManager: StorageManager) : VolumeManager {
    override fun getInternalVolume(): Volume.Internal =
        try {
            InternalVolumeImpl(storageManager.primaryStorageVolume)
        } catch (_: RuntimeException) {
            FallbackInternalVolume
        }

    override fun getVolumes() =
        storageManager.storageVolumesCompat.map {
            if (it.isInternalCompat) {
                InternalVolumeImpl(it)
            } else {
                ExternalVolumeImpl(it)
            }
        }

    private data class InternalVolumeImpl(val storageVolume: StorageVolume) : Volume.Internal {
        override val mediaStoreName
            get() = storageVolume.mediaStoreVolumeNameCompat

        override val components
            get() = storageVolume.directoryCompat?.let(Components.Companion::parseUnix)

        override fun resolveName(context: Context) = storageVolume.getDescriptionCompat(context)

        override fun isAccessible(): Boolean {
            return storageVolume.stateCompat == android.os.Environment.MEDIA_MOUNTED ||
                storageVolume.stateCompat == android.os.Environment.MEDIA_MOUNTED_READ_ONLY
        }
    }

    private data class ExternalVolumeImpl(val storageVolume: StorageVolume) : Volume.External {
        override val id
            get() = storageVolume.uuidCompat

        override val mediaStoreName
            get() = storageVolume.mediaStoreVolumeNameCompat

        override val components
            get() = storageVolume.directoryCompat?.let(Components.Companion::parseUnix)

        override fun resolveName(context: Context) = storageVolume.getDescriptionCompat(context)

        override fun isAccessible(): Boolean {
            return storageVolume.stateCompat == android.os.Environment.MEDIA_MOUNTED ||
                storageVolume.stateCompat == android.os.Environment.MEDIA_MOUNTED_READ_ONLY
        }
    }

    private object FallbackInternalVolume : Volume.Internal {
        private val root = File("/storage/emulated/0")

        override val mediaStoreName = "external_primary"
        override val components = Components.parseUnix(root.absolutePath)

        override fun resolveName(context: Context) = root.absolutePath

        override fun isAccessible(): Boolean =
            try {
                root.exists() && root.isDirectory && root.canRead()
            } catch (_: RuntimeException) {
                false
            }
    }
}
""",
)

source_config_test = "app/src/test/java/org/oxycblt/auxio/music/MusicSourceConfigurationTest.kt"
replace_once(
    source_config_test,
    "import org.junit.Assert.assertFalse\n"
    "import org.junit.Assert.assertTrue\n",
    "import org.junit.Assert.assertFalse\n"
    "import org.junit.Assert.assertNull\n"
    "import org.junit.Assert.assertTrue\n",
)
replace_once(
    source_config_test,
    "        assertTrue(settings.consumePendingInitialScan())\n"
    "        assertFalse(settings.consumePendingInitialScan())",
    "        assertEquals(before + 1L, settings.consumePendingInitialScan())\n"
    "        assertNull(settings.consumePendingInitialScan())",
)

print("PR206 final fixes applied")

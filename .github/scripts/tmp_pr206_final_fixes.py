from pathlib import Path
import re


def read(path: str) -> str:
    return Path(path).read_text(encoding="utf-8")


def write(path: str, text: str) -> None:
    Path(path).write_text(text, encoding="utf-8", newline="\n")


def replace_once(path: str, old: str, new: str) -> None:
    text = read(path)
    count = text.count(old)
    if count == 0 and new in text:
        print(f"already applied: {path}")
        return
    if count != 1:
        raise SystemExit(f"{path}: expected one exact match, found {count}: {old[:80]!r}")
    write(path, text.replace(old, new, 1))
    print(f"updated: {path}")


def replace_regex_once(path: str, pattern: str, replacement: str, *, flags: int = 0) -> None:
    text = read(path)
    matches = list(re.finditer(pattern, text, flags))
    if len(matches) != 1:
        raise SystemExit(f"{path}: expected one regex match, found {len(matches)}: {pattern!r}")
    write(path, re.sub(pattern, replacement, text, count=1, flags=flags))
    print(f"updated: {path}")


def insert_before_final_brace(path: str, marker: str, block: str) -> None:
    text = read(path)
    if marker in text:
        print(f"already applied: {path}")
        return
    head, separator, tail = text.rpartition("\n}")
    if not separator or tail.strip():
        raise SystemExit(f"{path}: cannot identify final class brace")
    write(path, head + "\n" + block.rstrip() + "\n}\n")
    print(f"updated: {path}")


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
replace_once(
    indexing_holder,
    "    private val musicSettings: MusicSettings,\n"
    "    private val rootGate: RootStateHolder,\n",
    "    private val musicSettings: MusicSettings,\n",
)
replace_once(
    indexing_holder,
    "        private val musicSettings: MusicSettings,\n"
    "        private val rootGate: RootStateHolder,\n",
    "        private val musicSettings: MusicSettings,\n",
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
replace_once(
    indexing_holder,
    "                LocationMode.DIRECT_FS ->\n"
    "                    DirectFS(\n"
    "                        musicSettings.safQuery.source,\n"
    "                        rootGate.takeIf {\n"
    "                            musicSettings.rootAccessPolicy == RootAccessPolicy.ON_DEMAND\n"
    "                        },\n"
    "                    )",
    "                LocationMode.DIRECT_FS -> DirectFS(musicSettings.safQuery.source)",
)
replace_once(
    indexing_holder,
    "    override fun onMusicLocationsChanged() {\n"
    "        super.onMusicLocationsChanged()\n"
    "        val generation = musicSettings.sourceConfigurationGeneration\n"
    "        val shouldHandle =\n"
    "            synchronized(this) {\n"
    "                if (generation == lastHandledSourceConfigurationGeneration) {\n"
    "                    false\n"
    "                } else {\n"
    "                    lastHandledSourceConfigurationGeneration = generation\n"
    "                    true\n"
    "                }\n"
    "            }\n"
    "        if (!shouldHandle) {\n"
    "            L.d(\"Ignoring duplicate source callback [generation=$generation]\")\n"
    "            return\n"
    "        }\n"
    "        if (musicSettings.shouldBeObserving) startTracking() else stopTracking()\n"
    "        val initialScan = musicSettings.consumePendingInitialScan()\n"
    "        indexScope.launch {\n"
    "            musicRepository.invalidateSource()\n"
    "            musicRepository.requestIndex(withCache = !initialScan)\n"
    "        }\n"
    "    }",
    "    override fun onMusicLocationsChanged() {\n"
    "        super.onMusicLocationsChanged()\n"
    "        val (generation, initialScan, shouldHandle) =\n"
    "            synchronized(this) {\n"
    "                val pendingGeneration = musicSettings.consumePendingInitialScan()\n"
    "                val currentGeneration =\n"
    "                    pendingGeneration ?: musicSettings.sourceConfigurationGeneration\n"
    "                if (currentGeneration == lastHandledSourceConfigurationGeneration) {\n"
    "                    Triple(currentGeneration, pendingGeneration != null, false)\n"
    "                } else {\n"
    "                    lastHandledSourceConfigurationGeneration = currentGeneration\n"
    "                    Triple(currentGeneration, pendingGeneration != null, true)\n"
    "                }\n"
    "            }\n"
    "        if (!shouldHandle) {\n"
    "            L.d(\"Ignoring duplicate source callback [generation=$generation]\")\n"
    "            return\n"
    "        }\n"
    "        if (musicSettings.shouldBeObserving) startTracking() else stopTracking()\n"
    "        indexScope.launch {\n"
    "            musicRepository.invalidateSource()\n"
    "            musicRepository.requestIndex(withCache = !initialScan)\n"
    "        }\n"
    "    }",
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
replace_once(
    music_repository,
    "            LocationMode.DIRECT_FS ->\n"
    "                DirectFS(\n"
    "                    musicSettings.safQuery.source,\n"
    "                    rootGate.takeIf { musicSettings.rootAccessPolicy == RootAccessPolicy.ON_DEMAND },\n"
    "                )",
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
replace_once(
    direct_fs,
    "        private const val ROOT_SNAPSHOT_TIMEOUT_MS = 15_000L\n",
    "",
)

volume_manager = "musikr/src/main/java/org/oxycblt/musikr/fs/path/VolumeManager.kt"
replace_once(volume_manager, "import android.os.storage.StorageVolume\n", "import android.os.storage.StorageVolume\nimport java.io.File\n")
replace_once(
    volume_manager,
    "    override fun getInternalVolume(): Volume.Internal =\n"
    "        InternalVolumeImpl(storageManager.primaryStorageVolume)",
    "    override fun getInternalVolume(): Volume.Internal =\n"
    "        try {\n"
    "            InternalVolumeImpl(storageManager.primaryStorageVolume)\n"
    "        } catch (_: RuntimeException) {\n"
    "            FallbackInternalVolume\n"
    "        }",
)
insert_before_final_brace(
    volume_manager,
    "private object FallbackInternalVolume",
    """    private object FallbackInternalVolume : Volume.Internal {
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
    }""",
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

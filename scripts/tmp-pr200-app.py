#!/usr/bin/env python3
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]


def write(path: str, content: str, executable: bool = False) -> None:
    target = ROOT / path
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(content.rstrip() + "\n", encoding="utf-8")
    if executable:
        target.chmod(0o755)


def replace_once(path: str, old: str, new: str) -> None:
    target = ROOT / path
    text = target.read_text(encoding="utf-8")
    if text.count(old) != 1:
        raise SystemExit(f"expected one match in {path}: {old[:80]!r}; found {text.count(old)}")
    target.write_text(text.replace(old, new), encoding="utf-8")


def regex_once(path: str, pattern: str, replacement: str, flags: int = re.S) -> None:
    target = ROOT / path
    text = target.read_text(encoding="utf-8")
    updated, count = re.subn(pattern, replacement, text, count=1, flags=flags)
    if count != 1:
        raise SystemExit(f"expected one regex match in {path}: {pattern[:100]!r}; found {count}")
    target.write_text(updated, encoding="utf-8")


write(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/storage/SourceAuthorityValidator.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * SourceAuthorityValidator.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root.storage

import java.io.File
import java.io.FileInputStream
import java.util.ArrayDeque

/** End-to-end app-UID validation for a DirectFS source or Magisk-prepared alias. */
object SourceAuthorityValidator {
    private val audioExtensions =
        setOf("mp3", "flac", "m4a", "mp4", "wav", "ogg", "opus", "aac", "3gp", "amr", "wma")
    private const val MAX_DEPTH = 4
    private const val MAX_VISITED = 512

    fun classifyDirect(path: String, preparedAlias: Boolean): SourceAuthority? {
        val root = File(path)
        val canonicalRoot = runCatching { root.canonicalFile }.getOrNull() ?: return null
        val first = runCatching { root.listFiles() }.getOrNull() ?: return null
        if (!root.exists() || !root.isDirectory || !root.canRead()) return null

        val queue = ArrayDeque<Pair<File, Int>>()
        first.forEach { queue.add(it to 1) }
        var visited = 0
        var representative: File? = null
        while (queue.isNotEmpty() && visited < MAX_VISITED && representative == null) {
            val (candidate, depth) = queue.removeFirst()
            visited++
            val canonical = runCatching { candidate.canonicalFile }.getOrNull() ?: continue
            if (!isWithin(canonical, canonicalRoot)) continue
            if (candidate.isFile && candidate.extension.lowercase() in audioExtensions) {
                representative = candidate
            } else if (candidate.isDirectory && depth < MAX_DEPTH) {
                runCatching { candidate.listFiles() }
                    .getOrNull()
                    ?.forEach { queue.add(it to depth + 1) }
            }
        }

        if (representative != null) {
            val opened =
                runCatching {
                        FileInputStream(representative).use { stream ->
                            stream.read(ByteArray(1))
                            true
                        }
                    }
                    .getOrDefault(false)
            if (!opened) return null
        }
        return if (preparedAlias) SourceAuthority.PREPARED_ALIAS else SourceAuthority.APP_READABLE
    }

    private fun isWithin(candidate: File, root: File): Boolean {
        var cursor: File? = candidate
        while (cursor != null) {
            if (cursor == root) return true
            cursor = cursor.parentFile
        }
        return false
    }
}
''',
)

write(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/storage/PreparedVolumeIndexStore.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * PreparedVolumeIndexStore.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.root.RootStorageCommandPolicy

/** Source authority granted to a path after end-to-end validation. */
enum class SourceAuthority {
    APP_READABLE,
    PREPARED_ALIAS,
    ROOT_SNAPSHOT_ONLY,
    UNAVAILABLE,
}

data class SourceResolution(
    val requestedPath: String,
    val resolvedPath: String?,
    val authority: SourceAuthority,
    val detail: String,
)

data class PreparedVolumeRecord(
    val generationSeconds: Long,
    val volumeId: String,
    val rawPath: String,
    val appPath: String,
    val aliasPath: String,
    val selectedPath: String?,
    val state: String,
    val samplePath: String?,
)

/** Parser for the fixed Magisk helper TSV. */
object PreparedVolumeManifestCodec {
    private val volumeId = Regex("^usbdisk\\d+$", RegexOption.IGNORE_CASE)
    private val states = setOf("app_candidate", "alias_candidate", "raw_only", "unavailable")

    fun parse(text: String): List<PreparedVolumeRecord>? {
        val out = mutableListOf<PreparedVolumeRecord>()
        for (line in text.lineSequence()) {
            if (line.isBlank() || line.startsWith("#")) continue
            val parts = line.split('\t', limit = 9)
            if (parts.size != 9 || parts[0] != "1") return null
            val generation = parts[1].toLongOrNull() ?: return null
            val id = parts[2]
            val raw = parts[3]
            val app = parts[4]
            val alias = parts[5]
            val selected = parts[6].takeUnless { it == "-" }
            val state = parts[7]
            val sample = parts[8].takeUnless { it == "-" }
            if (generation < 0 || !volumeId.matches(id) || state !in states) return null
            if (!validPath(raw) || !validPath(app) || !validPath(alias)) return null
            if (selected != null && !validPath(selected)) return null
            if (sample != null && (selected == null || !isWithin(sample, selected))) return null
            out +=
                PreparedVolumeRecord(
                    generationSeconds = generation,
                    volumeId = id,
                    rawPath = raw,
                    appPath = app,
                    aliasPath = alias,
                    selectedPath = selected,
                    state = state,
                    samplePath = sample,
                )
        }
        return out.sortedWith(compareByDescending<PreparedVolumeRecord> { it.generationSeconds }.thenBy { it.volumeId })
    }

    private fun validPath(path: String): Boolean =
        path != "-" && RootStorageCommandPolicy.isAllowedStorageRoot(path)

    private fun isWithin(candidate: String, root: String): Boolean =
        candidate == root || candidate.startsWith(root.trimEnd('/') + "/")
}

/**
 * App-private cache and resolver for the Magisk-prepared TS18 volume manifest.
 *
 * The cache is loaded without `su`. A refresh is user-started or otherwise asynchronous and is
 * accepted only after strict parsing and an atomic app-private write.
 */
@Singleton
class PreparedVolumeIndexStore
@Inject
constructor(
    @ApplicationContext context: Context,
    private val rootStateHolder: RootStateHolder,
) {
    private val cacheDir = File(context.filesDir, "ts18-root-storage")
    private val cacheFile = File(cacheDir, "volumes.tsv")

    @Volatile private var records: List<PreparedVolumeRecord> = readCachedRecords()

    fun cachedRecords(): List<PreparedVolumeRecord> = records

    fun cachedCandidatePaths(): List<String> = candidatePaths(records)

    /** Explicit/user-started refresh. This may perform the bounded Magisk consent probe. */
    @Synchronized
    fun refreshFromRootSync(): List<PreparedVolumeRecord> {
        if (!rootStateHolder.isUserEnabled()) return records
        val text = rootStateHolder.readPreparedVolumeManifestSync() ?: return records
        val parsed = PreparedVolumeManifestCodec.parse(text) ?: return records
        if (!writeAtomically(text)) return records
        records = parsed
        return records
    }

    fun resolveSourceSync(requestedPath: String): SourceResolution {
        val clean = requestedPath.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
        val current = if (rootStateHolder.isUserEnabled()) refreshFromRootSync() else records
        val match = current.firstOrNull { belongsToRecord(clean, it) }
        val suffix = match?.let { suffixFor(clean, it) }.orEmpty()
        val candidates = linkedSetOf<String>()
        if (!clean.startsWith("/mnt/media_rw/")) candidates += clean
        if (match != null) {
            candidates += appendSuffix(match.appPath, suffix)
            match.selectedPath?.let { candidates += appendSuffix(it, suffix) }
            candidates += appendSuffix(match.aliasPath, suffix)
        }

        for (candidate in candidates) {
            if (candidate.startsWith("/mnt/media_rw/")) continue
            val prepared = candidate.startsWith("/storage/auxio-root/")
            val authority = SourceAuthorityValidator.classifyDirect(candidate, prepared) ?: continue
            return SourceResolution(clean, candidate, authority, "app_uid_open_ok")
        }

        val rawBacking = match?.let { appendSuffix(it.rawPath, suffix) } ?: clean.takeIf {
            it.startsWith("/mnt/media_rw/usbdisk")
        }
        if (rawBacking != null && rootStateHolder.isUserEnabled()) {
            val snapshot = rootStateHolder.snapshotTreeSync(rawBacking, maxDepth = 4, timeoutMs = 5_000L)
            if (snapshot != null) {
                return SourceResolution(
                    clean,
                    null,
                    SourceAuthority.ROOT_SNAPSHOT_ONLY,
                    "root_snapshot_without_app_uid_media_access",
                )
            }
        }
        return SourceResolution(clean, null, SourceAuthority.UNAVAILABLE, "no_valid_app_readable_path")
    }

    private fun readCachedRecords(): List<PreparedVolumeRecord> =
        runCatching { PreparedVolumeManifestCodec.parse(cacheFile.readText()) }
            .getOrNull()
            .orEmpty()

    private fun writeAtomically(text: String): Boolean =
        runCatching {
                cacheDir.mkdirs()
                val temp = File(cacheDir, "volumes.tsv.tmp")
                temp.writeText(text)
                if (!temp.renameTo(cacheFile)) {
                    temp.copyTo(cacheFile, overwrite = true)
                    temp.delete()
                }
                true
            }
            .getOrDefault(false)

    private fun candidatePaths(values: List<PreparedVolumeRecord>): List<String> {
        val out = linkedSetOf<String>()
        values.forEach { record ->
            out += record.appPath
            record.selectedPath?.let(out::add)
            out += record.aliasPath
        }
        return out.toList()
    }

    private fun belongsToRecord(path: String, record: PreparedVolumeRecord): Boolean =
        listOfNotNull(record.rawPath, record.appPath, record.aliasPath, record.selectedPath).any {
            path == it || path.startsWith(it.trimEnd('/') + "/")
        }

    private fun suffixFor(path: String, record: PreparedVolumeRecord): String {
        val root =
            listOfNotNull(record.rawPath, record.appPath, record.aliasPath, record.selectedPath)
                .filter { path == it || path.startsWith(it.trimEnd('/') + "/") }
                .maxByOrNull(String::length)
                ?: return ""
        return path.removePrefix(root).trimStart('/')
    }

    private fun appendSuffix(root: String, suffix: String): String =
        if (suffix.isBlank()) root else root.trimEnd('/') + "/" + suffix
}
''',
)

# Topway-compatible fresh installs default to DirectFS, while persisted choices remain untouched.
location_mode_path = "app/src/main/java/org/oxycblt/auxio/music/locations/LocationMode.kt"
replace_once(
    location_mode_path,
    "    companion object {\n        fun fromInt(int: Int): LocationMode? {\n",
    "    companion object {\n"
    "        fun defaultForFlavor(topwayCompat: Boolean): LocationMode =\n"
    "            if (topwayCompat) DIRECT_FS else SAF\n\n"
    "        fun fromInt(int: Int): LocationMode? {\n",
)
music_settings_path = "app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt"
replace_once(
    music_settings_path,
    "            val mode =\n                sharedPreferences.getInt(\n                    getString(R.string.set_key_locations_mode),\n                    IntegerTable.LOCATION_MODE_SAF,\n                )\n            return LocationMode.fromInt(mode) ?: LocationMode.SAF\n",
    "            val fallback =\n"
    "                LocationMode.defaultForFlavor(org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_FLAVOR)\n"
    "            val mode =\n"
    "                sharedPreferences.getInt(\n"
    "                    getString(R.string.set_key_locations_mode),\n"
    "                    fallback.intCode,\n"
    "                )\n"
    "            return LocationMode.fromInt(mode) ?: fallback\n",
)

# Source picker: include prepared aliases, resolve raw paths to validated app-facing paths, and reject
# root-snapshot-only sources rather than persisting an unusable raw path.
locations_path = "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt"
replace_once(
    locations_path,
    "import org.oxycblt.auxio.headunit.root.RootStateHolder\n",
    "import org.oxycblt.auxio.headunit.root.RootStateHolder\n"
    "import org.oxycblt.auxio.headunit.root.storage.PreparedVolumeIndexStore\n"
    "import org.oxycblt.auxio.headunit.root.storage.SourceAuthority\n",
)
replace_once(
    locations_path,
    "    @Inject lateinit var rootGate: RootStateHolder\n",
    "    @Inject lateinit var rootGate: RootStateHolder\n"
    "    @Inject lateinit var preparedVolumeIndexStore: PreparedVolumeIndexStore\n",
)
replace_once(
    locations_path,
    "                    TopwaySourcePolicy.discoverMusicSourceCandidates(\n                        savedPaths = includeLocationAdapter.locations.map { it.uri.toString() },\n                        mediaStoreParents = discoverMediaStoreAudioParents(),\n                        storageRoots = discoverStorageRoots(),\n",
    "                    val preparedRoots =\n"
    "                        if (BuildConfig.TOPWAY_COMPAT_FLAVOR && locationMode == LocationMode.DIRECT_FS) {\n"
    "                            preparedVolumeIndexStore.refreshFromRootSync()\n"
    "                            preparedVolumeIndexStore.cachedCandidatePaths()\n"
    "                        } else {\n"
    "                            emptyList()\n"
    "                        }\n"
    "                    TopwaySourcePolicy.discoverMusicSourceCandidates(\n"
    "                        savedPaths = includeLocationAdapter.locations.map { it.uri.toString() },\n"
    "                        mediaStoreParents = discoverMediaStoreAudioParents(),\n"
    "                        storageRoots = discoverStorageRoots() + preparedRoots,\n",
)
regex_once(
    locations_path,
    r"    private fun validateAndAcceptPath\(\n        path: String,\n        disableThirdParty: Boolean,\n        callback: \(Location.Unopened\) -> Unit,\n    \) \{.*?\n    \}\n\n    private enum class ManualPathValidation.*?\n    private fun isRootBackedRawDirectPath\(.*?\n    \}\n",
    r'''    private fun validateAndAcceptPath(
        path: String,
        disableThirdParty: Boolean,
        callback: (Location.Unopened) -> Unit,
    ) {
        lifecycleScope.launch {
            val currentContext = context
            if (currentContext == null) {
                clearPendingLocationCallback(callback)
                return@launch
            }
            if (!hasStoragePermission && locationMode != LocationMode.SAF) {
                currentContext.showToast(R.string.set_path_permission_missing)
                clearPendingLocationCallback(callback)
                return@launch
            }
            val resolution =
                withContext(Dispatchers.IO) { preparedVolumeIndexStore.resolveSourceSync(path) }
            val resolvedPath = resolution.resolvedPath
            if (
                resolvedPath == null ||
                    (resolution.authority != SourceAuthority.APP_READABLE &&
                        resolution.authority != SourceAuthority.PREPARED_ALIAS)
            ) {
                L.w("Rejecting music source $path: ${resolution.authority} ${resolution.detail}")
                currentContext.showToast(
                    if (resolution.authority == SourceAuthority.ROOT_SNAPSHOT_ONLY) {
                        R.string.set_path_root_snapshot_only
                    } else {
                        R.string.set_path_unreadable
                    }
                )
                clearPendingLocationCallback(callback)
                return@launch
            }
            val uri = Uri.fromFile(File(resolvedPath))
            val location = Location.Unopened.from(currentContext, uri)
            if (shouldRejectThirdPartyLocation(uri, location, disableThirdParty)) {
                L.w("Rejecting music source $resolvedPath: third-party volume disabled")
                currentContext.showToast(R.string.err_bad_location)
                clearPendingLocationCallback(callback)
                return@launch
            }
            if (location.open(currentContext) == null) {
                L.w("Rejecting music source $resolvedPath: Location.open returned null")
                currentContext.showToast(R.string.set_path_open_failed)
                clearPendingLocationCallback(callback)
                return@launch
            }
            L.i(
                "Accepted TS18 source requested=$path resolved=$resolvedPath authority=${resolution.authority}"
            )
            callback(location)
            clearPendingLocationCallback(callback)
        }
    }
''',
)

# Root toggle updates the central consent generation immediately.
music_pref_path = "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt"
replace_once(
    music_pref_path,
    "        if (preference.key == getString(R.string.set_key_root_fs_status)) {\n",
    "        if (preference.key == getString(R.string.set_key_use_root_fs)) {\n"
    "            preference.onPreferenceChangeListener =\n"
    "                Preference.OnPreferenceChangeListener { _, newValue ->\n"
    "                    rootStateHolder.setUserEnabled(newValue as? Boolean == true)\n"
    "                    true\n"
    "                }\n"
    "        }\n"
    "        if (preference.key == getString(R.string.set_key_root_fs_status)) {\n",
)

# Boot remains playback-first; Magisk late-start preparation is explicitly parallel and external.
boot_path = "app/src/main/java/org/oxycblt/auxio/BootReceiver.kt"
replace_once(
    boot_path,
    "        // Do not start Magisk/su work in the cold boot receiver. Root-assisted DirectFS probes on\n        // demand only after playback/session restoration reaches a configured inaccessible source.\n        journal.log(DiagnosticJournal.CAT_BOOT, \"Root probe deferred\", \"on_demand_direct_fs\")\n",
    "        // Cached playback/session restoration is never blocked by an interactive su process.\n"
    "        // Optional root storage preparation runs independently in the Magisk late-start service;\n"
    "        // the app consumes its cached manifest and refreshes it only from an explicit source flow.\n"
    "        journal.log(DiagnosticJournal.CAT_BOOT, \"Root storage preparation\", \"parallel_magisk_late_start\")\n",
)

# Remove protected-package mutations from product root APIs/UI.
resolver_path = "app/src/main/java/org/oxycblt/auxio/headunit/root/dofun/Ts18DofunIntegrationResolver.kt"
regex_once(
    resolver_path,
    r"\nenum class Ts18RootMutation\(val command: String\) \{.*?\n\}\n",
    "\n",
)
regex_once(
    resolver_path,
    r"\n    suspend fun testStockSelectionDisabledUser0\(\): Boolean =.*?\n    suspend fun restoreStockSelectionDisabledUser0\(\): Boolean =.*?\n        \}\n",
    "\n",
)

diag_path = "app/src/main/java/org/oxycblt/auxio/settings/categories/DiagnosticsRecoveryPreferenceFragment.kt"
replace_once(diag_path, "import androidx.appcompat.app.AlertDialog\n", "")
regex_once(
    diag_path,
    r"\n        if \(preference.key == getString\(R.string.set_key_diagnostics_test_stock_disable\)\) \{.*?\n        \}\n\n        if \(preference.key == getString\(R.string.set_key_diagnostics_restore_stock\)\) \{.*?\n        \}\n",
    "\n",
)
regex_once(
    diag_path,
    r"\n    private fun showDisableStockConfirmation\(\) \{.*?\n    \}\n\n    private fun restoreStock\(\) \{.*?\n    \}\n",
    "\n",
)
replace_once(
    diag_path,
    "        val isRootAvailable = rootStateHolder.stateSnapshot() == RootStateHolder.State.Available\n        findPreference<Preference>(getString(R.string.set_key_diagnostics_test_stock_disable))\n            ?.isVisible = isRootAvailable\n        findPreference<Preference>(getString(R.string.set_key_diagnostics_restore_stock))\n            ?.isVisible = isRootAvailable\n",
    "",
)
write(
    "app/src/main/res/xml/preferences_diagnostics.xml",
    r'''<?xml version="1.0" encoding="utf-8"?>
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android" xmlns:app="http://schemas.android.com/apk/res-auto" app:title="@string/set_diagnostics">
    <PreferenceCategory app:title="@string/set_category_status">
        <Preference app:key="@string/set_head_unit_compat_status" app:summary="@string/set_head_unit_status_desc" app:title="@string/set_head_unit_status" />
    </PreferenceCategory>
    <PreferenceCategory app:title="@string/set_category_diagnostics">
        <Preference app:key="@string/set_key_diagnostics_run_check" app:summary="@string/set_run_dofun_check_desc" app:title="@string/set_run_dofun_check" />
        <Preference android:enabled="false" app:key="@string/set_key_diagnostics_export_report" app:summary="@string/set_export_dofun_report_desc" app:title="@string/set_export_dofun_report" />
    </PreferenceCategory>
</PreferenceScreen>
''',
)

# Settings wording: storage capability, not generic root/package mutation authority.
strings_path = ROOT / "app/src/main/res/values/strings.xml"
strings = strings_path.read_text(encoding="utf-8")
for name, value in {
    "set_use_root_fs": "Root storage fast path",
    "set_use_root_fs_desc": "Allow bounded root storage discovery and prepared USB aliases on Topway builds. Ordinary /storage access is always tried first.",
    "set_root_fs_status_desc": "Check bounded storage-root availability. This does not authorise package mutations or platform privileges.",
}.items():
    strings, count = re.subn(
        rf'<string name="{name}">.*?</string>',
        f'<string name="{name}">{value}</string>',
        strings,
        count=1,
        flags=re.S,
    )
    if count != 1:
        raise SystemExit(f"missing string {name}")
if 'name="set_path_root_snapshot_only"' not in strings:
    strings = strings.replace(
        "</resources>",
        '    <string name="set_path_root_snapshot_only">Root can see this volume, but Auxio cannot open its media yet. Use the matching /storage path or install/repair the prepared-alias helper.</string>\n</resources>',
    )
strings_path.write_text(strings, encoding="utf-8")

# Revised repository authority policy.
agents_path = "AGENTS.md"
regex_once(
    agents_path,
    r"## Auxio-TS Topway/TS18 Policy \(Updated\).*\Z",
    r'''## Auxio-TS Topway/TS18 root storage policy

- Auxio-TS is a Topway/TS18-focused variant app.
- DirectFS is the primary source-selection path for fresh Topway-compatible installs; SAF and MediaStore remain explicit alternatives.
- Root is a first-class **storage** capability on Topway variants, centrally gated by `RootStateHolder` and explicit user consent.
- Do not block `BOOT_COMPLETED`, cache restore, MediaSession readiness or first audio on interactive `su`. Pre-authorised Magisk late-start storage preparation may run independently and publish a bounded manifest.
- `/mnt/media_rw/usbdiskN` is an approved internal backing/discovery path. Persist and play only through an app-readable `/storage/...` path or an app-UID-validated prepared alias.
- A root directory snapshot is discovery evidence only; it does not prove TagLib, artwork or playback file access.
- Root storage operations must be fixed/typed, read-only, one snapshot per changed volume, bounded to 2s probes and at most 20s storage operations, and safely degraded.
- Root storage consent must not authorise protected-package disable/enable, system writes, platform identity, MCU/CAN or vendor-service mutations.
- Product runtime diagnostics remain bounded and user-started; protected-package mutation experiments belong in external Tier 3 tools.
- Playback Stability: all shuffle modes preserve the current track. Autoplay must not be interrupted by background root/index refreshes.
- Album-Art Modes remain `off`, `as-is`, and `optimised`.
''',
)

copilot = ROOT / ".github/copilot-instructions.md"
if copilot.exists():
    text = copilot.read_text(encoding="utf-8")
    marker = "## Auxio-TS Topway/TS18 Policy (Updated)"
    if marker in text:
        text = text[: text.index(marker)] + (ROOT / agents_path).read_text(encoding="utf-8").split("## Auxio-TS Topway/TS18 root storage policy", 1)[1]
        copilot.write_text(text, encoding="utf-8")

# Optional Magisk late-start helper. It prepares candidates only; the app performs final app-UID open validation.
write(
    "tools/ts18-root-storage-fastpath/magisk-module/module.prop",
    r'''id=auxio_ts_root_storage
name=Auxio-TS TS18 Root Storage Fast Path
version=1.0.0
versionCode=10000
author=Auxio-TS
summary=Bounded late-start TS18 USB discovery and prepared aliases for DirectFS
''',
)
write(
    "tools/ts18-root-storage-fastpath/magisk-module/customize.sh",
    r'''#!/system/bin/sh
ui_print "- Installing Auxio-TS TS18 root storage helper"
ui_print "- No /system write, package disable, app launch or library scan is performed"
set_perm_recursive "$MODPATH" 0 0 0755 0644
set_perm "$MODPATH/service.d/55-auxio-root-storage-prepare.sh" 0 0 0755
''',
    executable=True,
)
write(
    "tools/ts18-root-storage-fastpath/magisk-module/service.d/55-auxio-root-storage-prepare.sh",
    r'''#!/system/bin/sh
# Bounded Magisk late-start preparation for the exact TS18 storage layout.
STATE_DIR=/data/adb/auxio-ts-root
MANIFEST="$STATE_DIR/volumes.tsv"
TEMP="$STATE_DIR/volumes.tsv.tmp.$$"
ALIAS_ROOT=/storage/auxio-root
MAX_WAIT_SECONDS=20
MAX_SAMPLE_DEPTH=6

log_msg() { log -t AuxioRootStorage "$*" 2>/dev/null || true; }
valid_component() {
  case "$1" in
    usbdisk[0-9]|usbdisk[0-9][0-9]) return 0 ;;
    *) return 1 ;;
  esac
}
clean_stale_aliases() {
  [ -d "$ALIAS_ROOT" ] || return 0
  for alias in "$ALIAS_ROOT"/usbdisk*; do
    [ -d "$alias" ] || continue
    name=${alias##*/}
    [ -d "/mnt/media_rw/$name" ] && continue
    umount "$alias" 2>/dev/null || true
    rmdir "$alias" 2>/dev/null || true
  done
}

mkdir -p "$STATE_DIR" || exit 1
chmod 0700 "$STATE_DIR" 2>/dev/null || true
start=$(date +%s 2>/dev/null || echo 0)
waited=0
while [ "$waited" -lt "$MAX_WAIT_SECONDS" ]; do
  found=0
  for raw in /mnt/media_rw/usbdisk*; do [ -d "$raw" ] && found=1 && break; done
  [ "$found" -eq 1 ] && break
  sleep 1
  waited=$((waited + 1))
done

clean_stale_aliases
mkdir -p "$ALIAS_ROOT" 2>/dev/null || true
: > "$TEMP" || exit 1

for raw in /mnt/media_rw/usbdisk*; do
  [ -d "$raw" ] || continue
  name=${raw##*/}
  valid_component "$name" || continue
  app="/storage/$name"
  alias="$ALIAS_ROOT/$name"
  selected=-
  state=raw_only

  if [ -d "$app" ] && [ -r "$app" ]; then
    selected="$app"
    state=app_candidate
  else
    mkdir -p "$alias" 2>/dev/null || true
    umount "$alias" 2>/dev/null || true
    if mount --bind "$raw" "$alias" 2>/dev/null; then
      mount -o remount,bind,ro "$alias" 2>/dev/null || true
      selected="$alias"
      state=alias_candidate
    fi
  fi

  sample=-
  if [ "$selected" != - ] && [ -d "$selected" ]; then
    sample=$(find "$selected" -xdev -maxdepth "$MAX_SAMPLE_DEPTH" -type f \( -iname '*.mp3' -o -iname '*.flac' -o -iname '*.m4a' -o -iname '*.wav' -o -iname '*.ogg' -o -iname '*.opus' -o -iname '*.aac' \) -print 2>/dev/null | head -n 1)
    [ -n "$sample" ] || sample=-
  fi
  generated=$(date +%s 2>/dev/null || echo "$start")
  printf '1\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n' \
    "$generated" "$name" "$raw" "$app" "$alias" "$selected" "$state" "$sample" >> "$TEMP"
done

chmod 0600 "$TEMP" 2>/dev/null || true
mv -f "$TEMP" "$MANIFEST" || exit 1
log_msg "prepared manifest=$MANIFEST waited=${waited}s"
exit 0
''',
    executable=True,
)
write(
    "tools/ts18-root-storage-fastpath/README.md",
    r'''# TS18 root storage helper

This optional Magisk module performs bounded late-start discovery of `/mnt/media_rw/usbdiskN`, prefers the normal `/storage/usbdiskN` alias, and creates a read-only `/storage/auxio-root/usbdiskN` bind candidate only when the normal alias is absent.

It does not launch Auxio, invoke interactive `su`, scan the Auxio library, clear caches, disable packages, write `/system` or `/vendor`, or claim platform signing/UID 1000.

The helper writes `/data/adb/auxio-ts-root/volumes.tsv` atomically. Auxio accepts a prepared alias only after its own app UID can list the directory and open a representative media file.

## Rollback

Disable or remove the module in Magisk and reboot/ACC-cycle. The late-start service then stops running. Existing aliases disappear with the storage mount namespace/reboot; the script also removes stale aliases on later runs.

**Requires TS18 validation:** global mount namespace visibility, SELinux/DAC behavior, app-UID media reads, reboot and ACC sleep/wake.
''',
)
write(
    "scripts/package-ts18-root-storage-helper.sh",
    r'''#!/usr/bin/env bash
set -u
root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source_dir="$root_dir/tools/ts18-root-storage-fastpath/magisk-module"
output="${1:-$root_dir/Auxio-TS-ts18-root-storage-helper.zip}"

for required in module.prop customize.sh service.d/55-auxio-root-storage-prepare.sh; do
  if [ ! -f "$source_dir/$required" ]; then
    echo "Missing helper module file: $required" >&2
    exit 2
  fi
done
if ! command -v zip >/dev/null 2>&1; then
  echo "zip is required" >&2
  exit 2
fi
rm -f "$output"
(
  cd "$source_dir" || exit 2
  zip -qr "$output" module.prop customize.sh service.d
) || exit 1
unzip -l "$output" | grep -Fq 'service.d/55-auxio-root-storage-prepare.sh' || exit 1
echo "$output"
''',
    executable=True,
)
write(
    "tools/ts18-root-storage-fastpath/tier3/stock-music-selection-test.sh",
    r'''#!/system/bin/sh
# External Tier 3 mutation test. Not called or packaged by the Auxio runtime APK.
case "${1:-}" in
  --disable-after-baseline|--restore) ;;
  *)
    echo "Usage: $0 --disable-after-baseline | --restore" >&2
    exit 2
    ;;
esac

pkg=com.tw.music
if ! pm path "$pkg" >/dev/null 2>&1; then
  echo "STOP: $pkg is not installed" >&2
  exit 3
fi
if [ "$1" = --restore ]; then
  pm enable --user 0 "$pkg" || exit 1
  echo "Restored $pkg for user 0"
  exit 0
fi

out="/data/local/tmp/auxio-stock-selection-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$out" || exit 1
pm path "$pkg" > "$out/pm-path.txt" 2>&1
dumpsys package "$pkg" > "$out/package-before.txt" 2>&1
cmd package resolve-activity --user 0 --brief -a android.intent.action.MAIN -c android.intent.category.LAUNCHER "$pkg" > "$out/resolve-before.txt" 2>&1
pm disable-user --user 0 "$pkg" || exit 1
printf 'Stock music disabled for a bounded manual validation window.\nRun immediately to rollback:\n  %s --restore\nEvidence: %s\n' "$0" "$out"
''',
    executable=True,
)

# Tests for defaults, manifest parsing and app-UID validation.
write(
    "app/src/test/java/org/oxycblt/auxio/music/locations/LocationModeDefaultTest.kt",
    r'''package org.oxycblt.auxio.music.locations

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationModeDefaultTest {
    @Test
    fun topwayFreshInstallUsesDirectFs() {
        assertEquals(LocationMode.DIRECT_FS, LocationMode.defaultForFlavor(true))
    }

    @Test
    fun standardFreshInstallKeepsSaf() {
        assertEquals(LocationMode.SAF, LocationMode.defaultForFlavor(false))
    }
}
''',
)
write(
    "app/src/test/java/org/oxycblt/auxio/headunit/root/storage/PreparedVolumeManifestCodecTest.kt",
    r'''package org.oxycblt.auxio.headunit.root.storage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PreparedVolumeManifestCodecTest {
    @Test
    fun parsesPreparedAliasRecord() {
        val parsed =
            PreparedVolumeManifestCodec.parse(
                "1\t123\tusbdisk0\t/mnt/media_rw/usbdisk0\t/storage/usbdisk0\t/storage/auxio-root/usbdisk0\t/storage/auxio-root/usbdisk0\talias_candidate\t/storage/auxio-root/usbdisk0/Music/a.flac\n"
            )
        requireNotNull(parsed)
        assertEquals("usbdisk0", parsed.single().volumeId)
        assertEquals("/storage/auxio-root/usbdisk0", parsed.single().selectedPath)
    }

    @Test
    fun rejectsUnsafeOrEscapedPaths() {
        assertNull(
            PreparedVolumeManifestCodec.parse(
                "1\t123\tusbdisk0\t/data/local/tmp\t/storage/usbdisk0\t/storage/auxio-root/usbdisk0\t-\traw_only\t-\n"
            )
        )
        assertNull(
            PreparedVolumeManifestCodec.parse(
                "1\t123\tusbdisk0\t/mnt/media_rw/usbdisk0\t/storage/usbdisk0\t/storage/auxio-root/usbdisk0\t/storage/usbdisk0\tapp_candidate\t/data/escape.mp3\n"
            )
        )
    }
}
''',
)
write(
    "app/src/test/java/org/oxycblt/auxio/headunit/root/storage/SourceAuthorityValidatorTest.kt",
    r'''package org.oxycblt.auxio.headunit.root.storage

import java.nio.file.Files
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SourceAuthorityValidatorTest {
    @Test
    fun opensRepresentativeAudioAsAppUid() {
        val root = Files.createTempDirectory("auxio-source-authority").toFile()
        try {
            val music = root.resolve("Music").apply { mkdirs() }
            music.resolve("track.flac").writeBytes(byteArrayOf(1, 2, 3))
            assertEquals(
                SourceAuthority.APP_READABLE,
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = false),
            )
            assertEquals(
                SourceAuthority.PREPARED_ALIAS,
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = true),
            )
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun rejectsMissingDirectory() {
        assertNull(
            SourceAuthorityValidator.classifyDirect(
                "/definitely/missing/auxio-source",
                preparedAlias = false,
            )
        )
    }
}
''',
)

# Static guard and integration into the canonical head-unit check.
write(
    "scripts/check-ts18-root-storage-fastpath.sh",
    r'''#!/usr/bin/env bash
set -u
fail() { echo "TS18 root storage guard: $*" >&2; exit 1; }

root_gate=musikr/src/main/java/org/oxycblt/musikr/fs/RootGate.kt
root_holder=app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt
direct_fs=musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt
helper=tools/ts18-root-storage-fastpath/magisk-module/service.d/55-auxio-root-storage-prepare.sh

for file in "$root_gate" "$root_holder" "$direct_fs" "$helper"; do
  [ -f "$file" ] || fail "missing $file"
done
grep -Fq 'snapshotTreeSync' "$root_gate" || fail 'typed snapshot API missing'
if grep -Fq 'runRootCommandSync' "$root_gate"; then fail 'free-form root command escaped into Musikr'; fi
grep -Fq 'BuildConfig.TOPWAY_COMPAT_FLAVOR' "$root_holder" || fail 'Topway variant gate missing'
if grep -Eq 'pm (disable-user|enable)|Ts18RootMutation' "$root_holder" app/src/main/java/org/oxycblt/auxio/headunit/root/dofun/Ts18DofunIntegrationResolver.kt; then
  fail 'protected-package mutation remains in runtime root code'
fi
if grep -Fq 'buildRootListCommand' "$direct_fs"; then fail 'per-directory root listing remains in DirectFS'; fi
grep -Fq 'root_snapshot_without_app_uid_media_access' app/src/main/java/org/oxycblt/auxio/headunit/root/storage/PreparedVolumeIndexStore.kt || fail 'snapshot-only classification missing'
grep -Fq 'LocationMode.defaultForFlavor' app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt || fail 'DirectFS fresh default missing'
grep -Fq '/data/adb/auxio-ts-root/volumes.tsv' "$root_holder" || fail 'prepared manifest contract missing'
if grep -Eq 'pm (disable-user|enable)|/system/|/vendor/' "$helper"; then fail 'helper contains forbidden mutation/write'; fi
bash -n "$helper" || fail 'helper shell syntax failed'
bash -n scripts/package-ts18-root-storage-helper.sh || fail 'packager shell syntax failed'
bash -n tools/ts18-root-storage-fastpath/tier3/stock-music-selection-test.sh || fail 'Tier 3 script syntax failed'
echo "TS18 root storage fast-path checks passed"
''',
    executable=True,
)
headunit_path = ROOT / "scripts/check-headunit-compat-safety.sh"
headunit = headunit_path.read_text(encoding="utf-8")
needle = 'echo "headunit compat safety checks passed"\n'
if headunit.count(needle) != 1:
    raise SystemExit("headunit final marker missing")
headunit = headunit.replace(
    needle,
    'bash scripts/check-ts18-root-storage-fastpath.sh\n\necho "headunit compat safety checks passed"\n',
)
headunit_path.write_text(headunit, encoding="utf-8")

print("PR200 app/tooling patches applied")

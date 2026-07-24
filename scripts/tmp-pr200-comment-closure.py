#!/usr/bin/env python3
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]


def write(path: str, content: str) -> None:
    target = ROOT / path
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(content.rstrip() + "\n", encoding="utf-8")


def replace_once(path: str, old: str, new: str) -> None:
    target = ROOT / path
    text = target.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{path}: expected one match, found {count}: {old[:100]!r}")
    target.write_text(text.replace(old, new), encoding="utf-8")


def regex_once(path: str, pattern: str, replacement: str, flags: int = re.S) -> None:
    target = ROOT / path
    text = target.read_text(encoding="utf-8")
    updated, count = re.subn(pattern, replacement, text, count=1, flags=flags)
    if count != 1:
        raise SystemExit(f"{path}: expected one regex match, found {count}: {pattern[:100]!r}")
    target.write_text(updated, encoding="utf-8")


write(
    "app/src/test/java/org/oxycblt/auxio/headunit/root/dofun/Ts18DofunIntegrationResolverTest.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * Ts18DofunIntegrationResolverTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.oxycblt.auxio.headunit.root.dofun

import org.junit.Assert.assertEquals
import org.junit.Test

class Ts18DofunIntegrationResolverTest {
    @Test
    fun `root integration probes match the explicit read only allowlist`() {
        val approved =
            mapOf(
                Ts18RootProbe.Id to "id",
                Ts18RootProbe.PackageSummary to
                    "pm list packages -f -U | grep -E 'com\\.tw\\.music|com\\.tw\\.media|com\\.dofun\\.variety'",
                Ts18RootProbe.ResolveMusicComponents to
                    "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN com.tw.media",
                Ts18RootProbe.ResolveTopwayAlias to
                    "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN -n com.tw.media/com.tw.music.MusicActivity",
                Ts18RootProbe.OverlayRuntime to
                    "appops get com.tw.media SYSTEM_ALERT_WINDOW 2>&1; dumpsys activity services com.tw.media 2>&1 | head -n 160; dumpsys window windows 2>&1 | grep -E 'com.tw.media|CarFloatingControls' | head -n 80",
                Ts18RootProbe.EqualizerComponents to
                    "dumpsys package com.tw.eq 2>&1 | grep -E 'EQChoiceActivity|DSPActivity|EQActivity|enabledComponents|disabledComponents' | head -n 160",
                Ts18RootProbe.VisualizerEffects to
                    "dumpsys media.audio_flinger 2>&1 | grep -i -E 'visualizer|session|com.tw.media' | head -n 200",
                Ts18RootProbe.PackageDumpMedia to "dumpsys package com.tw.media",
                Ts18RootProbe.PackageDumpMusic to "dumpsys package com.tw.music",
                Ts18RootProbe.AppWidgetSummary to "dumpsys appwidget",
                Ts18RootProbe.MediaSessionSummary to "dumpsys media_session",
                Ts18RootProbe.ActivityBroadcastSummary to "dumpsys activity broadcasts",
                Ts18RootProbe.DofunDataHintsReadOnly to
                    "content query --uri content://com.dofun.variety.ExportedProvider/hotseat_app_music",
            )

        assertEquals(approved.keys, Ts18RootProbe.entries.toSet())
        Ts18RootProbe.entries.forEach { probe -> assertEquals(approved.getValue(probe), probe.command) }
    }
}
''',
)

write(
    "scripts/check-ts18-root-storage-fastpath.sh",
    r'''#!/usr/bin/env bash
set -u

fail() {
  echo "TS18 root storage guard: $*" >&2
  exit 1
}

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)" ||
  fail 'cannot resolve script directory'
cd "$script_dir/.." || fail 'cannot change to repository root'

root_gate=musikr/src/main/java/org/oxycblt/musikr/fs/RootGate.kt
root_holder=app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt
direct_fs=musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt
resolver=app/src/main/java/org/oxycblt/auxio/headunit/root/dofun/Ts18DofunIntegrationResolver.kt
index_store=app/src/main/java/org/oxycblt/auxio/headunit/root/storage/PreparedVolumeIndexStore.kt
locations=app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt
helper=tools/ts18-root-storage-fastpath/magisk-module/service.sh
module_prop=tools/ts18-root-storage-fastpath/magisk-module/module.prop

for file in \
  "$root_gate" "$root_holder" "$direct_fs" "$resolver" "$index_store" \
  "$locations" "$helper" "$module_prop"; do
  [ -f "$file" ] || fail "missing $file"
done

grep -Fq 'snapshotTreeSync' "$root_gate" || fail 'typed snapshot API missing'
if grep -Fq 'runRootCommandSync' "$root_gate"; then
  fail 'free-form root command escaped into Musikr'
fi
grep -Fq 'BuildConfig.TOPWAY_COMPAT_FLAVOR' "$root_holder" ||
  fail 'Topway variant gate missing'
grep -Fq '/data/adb/modules/auxio_ts_root_storage/service.sh' "$root_holder" ||
  fail 'fixed prepared-helper command missing'

runtime_roots=(app/src/main/java musikr/src/main/java)
runtime_files=()
for root in "${runtime_roots[@]}"; do
  [ -d "$root" ] || fail "missing runtime source root $root"
done
while IFS= read -r -d '' file; do
  runtime_files+=("$file")
done < <(find "${runtime_roots[@]}" -type f \( -name '*.kt' -o -name '*.java' \) -print0)
[ "${#runtime_files[@]}" -gt 0 ] || fail 'no runtime source files found'

grep -EIl \
  'Ts18RootMutation|pm[[:space:]]+(disable-user|enable|uninstall|clear)([[:space:]]|$)|cmd[[:space:]]+package[[:space:]]+(uninstall|clear|set-enabled-setting)([[:space:]]|$)|content[[:space:]]+(insert|update|delete)([[:space:]]|$)|settings[[:space:]]+put([[:space:]]|$)|setprop[[:space:]]' \
  "${runtime_files[@]}" >/dev/null
mutation_status=$?
if [ "$mutation_status" -eq 0 ]; then
  fail 'protected mutation command remains in production runtime source'
elif [ "$mutation_status" -ne 1 ]; then
  fail 'production runtime mutation scan failed'
fi

if grep -Fq 'buildRootListCommand' "$direct_fs"; then
  fail 'per-directory root listing remains in DirectFS'
fi
grep -Fq 'root_snapshot_without_app_uid_media_access' "$index_store" ||
  fail 'snapshot-only classification missing'
grep -Fq 'RootStorageAccelerationPolicy.choose' "$index_store" ||
  fail 'cost-aware storage ordering missing'
grep -Fq 'isAllowedCanonicalStorageRoot(clean)' "$index_store" ||
  fail 'canonical storage containment gate missing'
grep -Fq 'TopwaySourcePolicy.isAllowedSourceCandidate(path)' "$locations" ||
  fail 'Topway DirectFS path boundary missing'
grep -Fq 'LocationMode.defaultForFlavor' \
  app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt ||
  fail 'DirectFS fresh default missing'
grep -Fq '/data/adb/auxio-ts-root/volumes.tsv' "$root_holder" ||
  fail 'prepared manifest contract missing'
grep -Fq '^description=' "$module_prop" || fail 'Magisk module description metadata missing'

if grep -Eq 'pm (disable-user|enable)|/system/(app|priv-app)|/vendor/(app|priv-app)' "$helper"; then
  fail 'helper contains forbidden package mutation or protected APK write'
fi
if find tools/ts18-root-storage-fastpath/magisk-module -path '*/service.d/*' -type f | grep -q .; then
  fail 'Magisk module must use module-root service.sh, not nested service.d'
fi
grep -Fq 'MAX_SAMPLED_VOLUMES=2' "$helper" ||
  fail 'bounded representative-sample count missing'
grep -Fq 'SAMPLE_TIMEOUT_SECONDS=1' "$helper" ||
  fail 'bounded representative-sample timeout missing'
if grep -Fq 'MAX_VOLUMES=' "$helper"; then
  fail 'helper must not silently omit detected volumes'
fi
grep -Fq 'volumes=$processed sampled=$sampled' "$helper" ||
  fail 'helper volume/sample accounting missing'

shell_count=0
while IFS= read -r -d '' shell_file; do
  bash -n "$shell_file" || fail "shell syntax failed: $shell_file"
  shell_count=$((shell_count + 1))
done < <(find scripts tools/ts18-root-storage-fastpath -type f -name '*.sh' -print0)
[ "$shell_count" -gt 0 ] || fail 'no shell scripts found for syntax validation'

echo "TS18 root storage fast-path checks passed (shell_files=$shell_count)"
''',
)

write(
    "scripts/package-ts18-root-storage-helper.sh",
    r'''#!/usr/bin/env bash
set -u
root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source_dir="$root_dir/tools/ts18-root-storage-fastpath/magisk-module"
output="${1:-$root_dir/Auxio-TS-ts18-root-storage-helper.zip}"

output_dir="$(dirname -- "$output")"
output_name="$(basename -- "$output")"
if ! output_dir="$(cd -- "$output_dir" && pwd)"; then
  echo "Output directory does not exist: $(dirname -- "$output")" >&2
  exit 2
fi
output="$output_dir/$output_name"

for required in module.prop customize.sh service.sh; do
  if [ ! -f "$source_dir/$required" ]; then
    echo "Missing helper module file: $required" >&2
    exit 2
  fi
done
if ! command -v zip >/dev/null 2>&1 || ! command -v unzip >/dev/null 2>&1; then
  echo "zip and unzip are required" >&2
  exit 2
fi
if ! rm -f -- "$output"; then
  echo "Unable to remove existing output ZIP: $output" >&2
  exit 1
fi
(
  cd "$source_dir" || exit 2
  zip -qr "$output" module.prop customize.sh service.sh
) || exit 1
entries="$(unzip -Z1 "$output")" || exit 1
printf '%s\n' "$entries" | grep -Fxq 'service.sh' || exit 1
if printf '%s\n' "$entries" | grep -Fq 'service.d/'; then
  echo "Nested service.d is not a valid module late-start entrypoint" >&2
  exit 1
fi
echo "$output"
''',
)

write(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/RootStorageCommandPolicy.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * RootStorageCommandPolicy.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root

import java.io.File

/** Builds the only recursive root command accepted by the storage root gate. */
object RootStorageCommandPolicy {
    private val usb = Regex("^/storage/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val rawUsb = Regex("^/mnt/media_rw/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val prepared = Regex("^/storage/auxio-root/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val uuid = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")

    fun isAllowedStorageRoot(value: String): Boolean =
        normaliseStoragePath(value)?.let(::matchesAllowedNamespace) == true

    /** Fail closed when an allowed-looking path canonically escapes the approved storage roots. */
    fun isAllowedCanonicalStorageRoot(value: String): Boolean {
        val normalised = normaliseStoragePath(value) ?: return false
        if (!matchesAllowedNamespace(normalised)) return false
        val canonical =
            runCatching { File(normalised).canonicalPath.replace('\\', '/').trimEnd('/') }
                .getOrNull()
                ?.ifEmpty { "/" }
                ?: return false
        return matchesAllowedNamespace(canonical)
    }

    fun buildSnapshotCommand(rootPath: String, maxDepth: Int): String {
        val normalised = normaliseStoragePath(rootPath)
        require(normalised != null && matchesAllowedNamespace(normalised)) {
            "unsafe root storage path"
        }
        require(maxDepth in 1..32) { "invalid snapshot depth" }
        val quotedRoot = shellQuote(normalised)
        val emitScript =
            "root=\$1; shift; for p do " +
                "rel=\${p#\"\$root\"/}; [ -n \"\$rel\" ] || continue; " +
                "case \"\$rel\" in *[[:cntrl:]]*) continue;; esac; " +
                "t=f; [ -d \"\$p\" ] && t=d; [ -L \"\$p\" ] && t=l; " +
                "m=\$(stat -c %Y \"\$p\" 2>/dev/null || echo 0); " +
                "s=\$(stat -c %s \"\$p\" 2>/dev/null || echo 0); " +
                "printf '%s\\t%s\\t%s\\t%s\\n' \"\$t\" \"\$m\" \"\$s\" \"\$rel\"; " +
                "done"
        return "root=$quotedRoot; [ -d \"\$root\" ] || exit 4; " +
            "find \"\$root\" -xdev -mindepth 1 -maxdepth $maxDepth " +
            "-exec sh -c ${shellQuote(emitScript)} sh \"\$root\" {} + 2>/dev/null"
    }

    private fun normaliseStoragePath(value: String): String? {
        val path = value.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
        if (
            path.any { it.isISOControl() } ||
                path.contains("/../") ||
                path.endsWith("/..") ||
                path.contains("/./") ||
                path.endsWith("/.")
        ) {
            return null
        }
        return path
    }

    private fun matchesAllowedNamespace(path: String): Boolean =
        path == "/sdcard" ||
            path.startsWith("/sdcard/") ||
            path == "/storage/emulated/0" ||
            path.startsWith("/storage/emulated/0/") ||
            usb.matches(path) ||
            rawUsb.matches(path) ||
            prepared.matches(path) ||
            uuid.matches(path)

    internal fun shellQuote(value: String): String = "'${value.replace("'", "'\"'\"'")}'"
}
''',
)

write(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/storage/RootStorageAccelerationPolicy.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * RootStorageAccelerationPolicy.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root.storage

import org.oxycblt.auxio.headunit.root.RootStorageCommandPolicy

/** Resolution order selected from already available, bounded storage authority. */
enum class RootStorageResolutionOrder {
    CACHED_ROOT_METADATA_FIRST,
    REFRESHED_ROOT_METADATA_FIRST,
    DIRECT_FIRST,
}

/** Select the lowest expected-cost safe source-resolution order. */
object RootStorageAccelerationPolicy {
    private val rawUsb = Regex("^/mnt/media_rw/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val prepared = Regex("^/storage/auxio-root/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val appUsb = Regex("^/storage/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val uuid = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")

    fun choose(
        requestedPath: String,
        rootEnabled: Boolean,
        rootAvailable: Boolean,
        hasCachedRecord: Boolean,
    ): RootStorageResolutionOrder {
        if (!rootEnabled || !RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(requestedPath)) {
            return RootStorageResolutionOrder.DIRECT_FIRST
        }
        if (hasCachedRecord) return RootStorageResolutionOrder.CACHED_ROOT_METADATA_FIRST
        if (rootAvailable && requiresRootPreparation(requestedPath)) {
            return RootStorageResolutionOrder.REFRESHED_ROOT_METADATA_FIRST
        }
        return RootStorageResolutionOrder.DIRECT_FIRST
    }

    /** Raw backing and prepared-alias paths cannot be usefully resolved without preparation. */
    fun requiresRootPreparation(path: String): Boolean {
        if (!RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(path)) return false
        val clean = path.replace('\\', '/').trimEnd('/')
        return rawUsb.matches(clean) || prepared.matches(clean)
    }

    fun isRemovablePath(path: String): Boolean {
        if (!RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(path)) return false
        val clean = path.replace('\\', '/').trimEnd('/')
        return appUsb.matches(clean) || rawUsb.matches(clean) || prepared.matches(clean) || uuid.matches(clean)
    }
}
''',
)

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

/** End-to-end app-process validation for a DirectFS source or Magisk-prepared alias. */
object SourceAuthorityValidator {
    private val audioExtensions =
        setOf("mp3", "flac", "m4a", "mp4", "wav", "ogg", "opus", "aac", "3gp", "amr", "wma")
    private const val MAX_DEPTH = 4
    private const val MAX_VISITED = 512

    /**
     * Validate a source in the current Android app process.
     *
     * [representativePath] is an optional prepared-manifest hint. When it is contained by [path],
     * has an audio extension and opens successfully, validation is O(1). Any stale, escaped or
     * unreadable hint is ignored and falls back to the bounded directory walk.
     */
    fun classifyDirect(
        path: String,
        preparedAlias: Boolean,
        representativePath: String? = null,
    ): SourceAuthority? {
        val root = File(path)
        val canonicalRoot = runCatching { root.canonicalFile }.getOrNull() ?: return null
        if (!root.exists() || !root.isDirectory || !root.canRead()) return null

        val hintedFile = representativePath?.let(::File)
        if (
            hintedFile != null &&
                hintedFile.extension.lowercase() in audioExtensions &&
                isContainedReadableFile(hintedFile, canonicalRoot) &&
                opensInAppProcess(hintedFile)
        ) {
            return authority(preparedAlias)
        }

        val first = runCatching { root.listFiles() }.getOrNull() ?: return null
        val queue = ArrayDeque<Pair<File, Int>>()
        var enqueued = 0
        fun enqueueBounded(files: Array<File>, depth: Int) {
            for (file in files) {
                if (enqueued >= MAX_VISITED) break
                queue.addLast(file to depth)
                enqueued++
            }
        }
        enqueueBounded(first, 1)

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
                    ?.let { enqueueBounded(it, depth + 1) }
            }
        }

        val mediaFile = representative ?: return null
        if (!opensInAppProcess(mediaFile)) return null
        return authority(preparedAlias)
    }

    private fun isContainedReadableFile(candidate: File, canonicalRoot: File): Boolean {
        val canonical = runCatching { candidate.canonicalFile }.getOrNull() ?: return false
        return candidate.isFile && isWithin(canonical, canonicalRoot)
    }

    private fun opensInAppProcess(file: File): Boolean =
        runCatching {
                FileInputStream(file).use { stream ->
                    stream.read()
                    true
                }
            }
            .getOrDefault(false)

    private fun authority(preparedAlias: Boolean): SourceAuthority =
        if (preparedAlias) SourceAuthority.PREPARED_ALIAS else SourceAuthority.APP_READABLE

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
    "musikr/src/main/java/org/oxycblt/musikr/fs/RootTreeSnapshotCodec.kt",
    r'''/*
 * Copyright (c) 2026 Auxio Project
 * RootTreeSnapshotCodec.kt is part of Auxio.
 */
package org.oxycblt.musikr.fs

/** Strict parser for the fixed TSV emitted by the TS18 root storage snapshot command. */
object RootTreeSnapshotCodec {
    private const val DEFAULT_MAX_ENTRIES = 100_000

    fun parse(
        rootPath: String,
        text: String,
        maxEntries: Int = DEFAULT_MAX_ENTRIES,
    ): RootTreeSnapshot? {
        if (rootPath.isBlank() || maxEntries <= 0) return null
        val entries = ArrayList<RootTreeEntry>()
        for (line in text.lineSequence()) {
            if (line.isBlank()) continue
            if (entries.size >= maxEntries) return null
            val parts = line.split('\t', limit = 4)
            if (parts.size != 4) return null
            val type = parts[0]
            if (type != "d" && type != "f" && type != "l") return null
            val relative = validateRelative(parts[3]) ?: return null
            val modifiedSeconds = parts[1].toLongOrNull() ?: return null
            val size = parts[2].toLongOrNull() ?: return null
            if (modifiedSeconds < 0L || modifiedSeconds > Long.MAX_VALUE / 1000L || size < 0L) {
                return null
            }
            entries +=
                RootTreeEntry(
                    relativePath = relative,
                    isDirectory = type == "d",
                    isSymlink = type == "l",
                    modifiedMs = modifiedSeconds * 1000L,
                    size = size,
                )
        }
        return RootTreeSnapshot(rootPath = rootPath, entries = entries)
    }

    private fun validateRelative(value: String): String? {
        if (
            value.isEmpty() ||
                value.startsWith('/') ||
                value.endsWith('/') ||
                value.any { it.isISOControl() }
        ) {
            return null
        }
        val segments = value.split('/')
        if (segments.any { it.isEmpty() || it == "." || it == ".." }) return null
        return value
    }
}
''',
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
state_file=/data/local/tmp/auxio-stock-selection-state
if ! pm path "$pkg" >/dev/null 2>&1; then
  echo "STOP: $pkg is not installed" >&2
  exit 3
fi

if [ "$1" = --restore ]; then
  if [ ! -f "$state_file" ] || [ "$(cat "$state_file" 2>/dev/null)" != enabled ]; then
    echo "STOP: no enabled-before-test rollback marker; refusing to change $pkg" >&2
    exit 4
  fi
  pm enable --user 0 "$pkg" || exit 1
  rm -f "$state_file" || exit 1
  echo "Restored $pkg to its recorded enabled baseline for user 0"
  exit 0
fi

if pm list packages -d --user 0 "$pkg" 2>/dev/null | grep -Fxq "package:$pkg"; then
  echo "STOP: $pkg was already disabled for user 0; no mutation performed" >&2
  exit 4
fi
if [ -e "$state_file" ]; then
  echo "STOP: unresolved prior test marker exists at $state_file; restore or inspect first" >&2
  exit 4
fi

out="/data/local/tmp/auxio-stock-selection-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$out" || exit 1
pm path "$pkg" > "$out/pm-path.txt" 2>&1 || exit 1
dumpsys package "$pkg" > "$out/package-before.txt" 2>&1 || exit 1
cmd package resolve-activity --user 0 --brief -a android.intent.action.MAIN \
  -c android.intent.category.LAUNCHER "$pkg" > "$out/resolve-before.txt" 2>&1 || exit 1
printf '%s\n' enabled > "$state_file" || exit 1
if ! pm disable-user --user 0 "$pkg"; then
  rm -f "$state_file" || true
  exit 1
fi
printf 'Stock music disabled for a bounded manual validation window.\nRun immediately to rollback:\n  %s --restore\nEvidence: %s\nRollback marker: %s\n' \
  "$0" "$out" "$state_file"
''',
)

write(
    "tools/ts18-root-storage-fastpath/magisk-module/module.prop",
    r'''id=auxio_ts_root_storage
name=Auxio-TS TS18 Root Storage Fast Path
version=1.0.0
versionCode=10000
author=Auxio-TS
description=Bounded late-start TS18 USB discovery and prepared aliases for DirectFS
''',
)

write(
    "tools/ts18-root-storage-fastpath/README.md",
    r'''# TS18 root storage helper

**Evidence confidence:** repository behavior and the bounded helper contract are implemented and CI-validated; alias visibility, SELinux/DAC access and timing remain **Requires TS18 validation**.

**Porting decision:** this helper is scoped to the observed Topway TS18 `/mnt/media_rw/usbdiskN` and `/storage/usbdiskN` layout. Do not treat related 8581 units as equivalent without exact-device validation.

This optional Magisk module uses module-root `service.sh`, Magisk's supported `late_start service` entrypoint, to perform bounded discovery of `/mnt/media_rw/usbdiskN`. It prefers the normal `/storage/usbdiskN` alias and creates a read-only `/storage/auxio-root/usbdiskN` bind candidate only when the normal alias is absent.

The service runs once during Magisk late start. Auxio may invoke the same fixed script with `--once` from an explicit root-enabled source-recovery flow so USB inserted after boot can be prepared without a persistent polling daemon. Concurrent/repeated requests are locked and coalesced, and app-side refresh requests are also serialized and debounced.

It does not launch Auxio, invoke interactive `su` itself, scan the Auxio library, clear caches, disable packages, write `/system` or `/vendor`, or claim platform signing/UID 1000.

The helper writes `/data/adb/auxio-ts-root/volumes.tsv` atomically. Late-start preparation publishes volume and alias candidates without representative hints; an explicit `--once` refresh may add bounded representative-media hints. The manifest never grants Android authority. Auxio accepts a normal or prepared alias only when representative audio exists and opens successfully in the app process. A raw root snapshot without app-process access remains discovery-only and is never persisted for playback.

## Packaging

```sh
bash scripts/package-ts18-root-storage-helper.sh /tmp/Auxio-TS-ts18-root-storage-helper.zip
```

Install the resulting ZIP in Magisk and reboot/ACC-cycle. Enable **Root storage fast path** in the Topway-compatible Auxio build, then use the source picker or root-status row to grant the app's bounded root request.

## Rollback

Disable or remove the module in Magisk and reboot/ACC-cycle. The late-start service then stops running. Existing aliases disappear with the storage mount namespace/reboot; the script also removes stale aliases on later runs.

**Requires TS18 validation:** global mount-namespace visibility, SELinux/DAC behavior, app-process media reads, `/storage/usbdiskN` timing, reboot and ACC sleep/wake.
''',
)

write(
    "musikr/src/test/java/org/oxycblt/musikr/fs/direct/DirectFsRootPolicyTest.kt",
    r'''/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.musikr.fs.direct

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.isAllowedRoot

class DirectFsRootPolicyTest {
    @Test
    fun rejectsProtectedRootsAndDescendants() {
        listOf(
                "/",
                "/system",
                "/system/app",
                "/vendor",
                "/vendor/etc",
                "/data",
                "/data/media",
            )
            .forEach { path -> assertFalse(path, isAllowedRoot(File(path))) }
    }

    @Test
    fun allowsAppFacingPreparedAndRawBackingRoots() {
        assertTrue(isAllowedRoot(File("/storage/usbdisk0")))
        assertTrue(isAllowedRoot(File("/storage/auxio-root/usbdisk0")))
        assertTrue(isAllowedRoot(File("/mnt/media_rw/usbdisk0")))
    }
}
''',
)

write(
    "musikr/src/test/java/org/oxycblt/musikr/fs/RootTreeSnapshotCodecTest.kt",
    r'''/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.musikr.fs

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RootTreeSnapshotCodecTest {
    @Test
    fun parsesBoundedTypedEntriesIncludingWhitespaceNames() {
        val snapshot =
            RootTreeSnapshotCodec.parse(
                "/mnt/media_rw/usbdisk0",
                "d\t10\t0\tMusic\nf\t11\t4\tMusic/track.flac\nl\t12\t1\tMusic/link\nf\t13\t1\t   \n",
            )
        requireNotNull(snapshot)
        assertEquals(4, snapshot.entries.size)
        assertTrue(snapshot.entries[0].isDirectory)
        assertEquals(11_000L, snapshot.entries[1].modifiedMs)
        assertTrue(snapshot.entries[2].isSymlink)
        assertEquals("   ", snapshot.entries[3].relativePath)
    }

    @Test
    fun acceptsMaximumConvertibleTimestamp() {
        val maxSeconds = Long.MAX_VALUE / 1000L
        val snapshot =
            RootTreeSnapshotCodec.parse("/storage/usbdisk0", "f\t$maxSeconds\t1\ttrack.flac\n")
        requireNotNull(snapshot)
        assertEquals(maxSeconds * 1000L, snapshot.entries.single().modifiedMs)
    }

    @Test
    fun rejectsMalformedTraversalControlCharactersAndOverflow() {
        assertNull(RootTreeSnapshotCodec.parse("/storage/usbdisk0", "x\t1\t1\tbad.mp3\n"))
        assertNull(RootTreeSnapshotCodec.parse("/storage/usbdisk0", "f\t1\t1\t../escape.mp3\n"))
        assertNull(RootTreeSnapshotCodec.parse("/storage/usbdisk0", "f\t1\t1\tbad\u001b.mp3\n"))
        assertNull(
            RootTreeSnapshotCodec.parse(
                "/storage/usbdisk0",
                "f\t${Long.MAX_VALUE / 1000L + 1L}\t1\toverflow.mp3\n",
            )
        )
        assertNull(
            RootTreeSnapshotCodec.parse(
                "/storage/usbdisk0",
                "f\t1\t1\ta.mp3\nf\t1\t1\tb.mp3\n",
                maxEntries = 1,
            )
        )
    }
}
''',
)

write(
    "app/src/test/java/org/oxycblt/auxio/headunit/root/storage/SourceAuthorityValidatorTest.kt",
    r'''/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.headunit.root.storage

import java.nio.file.Files
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SourceAuthorityValidatorTest {
    @Test
    fun classifiesReadableRepresentativeAudioInCurrentProcess() {
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
    fun preparedRepresentativeBypassesBoundedWalkWithoutBypassingProcessOpen() {
        val root = Files.createTempDirectory("auxio-source-hint").toFile()
        try {
            var directory = root
            repeat(6) { depth -> directory = directory.resolve("d$depth").apply { mkdirs() } }
            val media = directory.resolve("deep.flac").apply { writeBytes(byteArrayOf(7)) }
            assertNull(
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = false)
            )
            assertEquals(
                SourceAuthority.APP_READABLE,
                SourceAuthorityValidator.classifyDirect(
                    path = root.absolutePath,
                    preparedAlias = false,
                    representativePath = media.absolutePath,
                ),
            )
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun escapedRepresentativeHintIsRejected() {
        val root = Files.createTempDirectory("auxio-source-contained").toFile()
        val outside = Files.createTempFile("auxio-source-outside", ".flac").toFile()
        try {
            outside.writeBytes(byteArrayOf(1))
            assertNull(
                SourceAuthorityValidator.classifyDirect(
                    path = root.absolutePath,
                    preparedAlias = false,
                    representativePath = outside.absolutePath,
                )
            )
        } finally {
            root.deleteRecursively()
            outside.delete()
        }
    }

    @Test
    fun rejectsDirectoryWithoutRepresentativeAudio() {
        val root = Files.createTempDirectory("auxio-source-empty").toFile()
        try {
            root.resolve("notes.txt").writeText("not audio")
            assertNull(
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = false)
            )
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun rejectsTestOwnedMissingDirectory() {
        val parent = Files.createTempDirectory("auxio-source-missing").toFile()
        try {
            val missing = parent.resolve("not-created")
            assertNull(
                SourceAuthorityValidator.classifyDirect(missing.absolutePath, preparedAlias = false)
            )
        } finally {
            parent.deleteRecursively()
        }
    }
}
''',
)

write(
    "app/src/test/java/org/oxycblt/auxio/headunit/root/storage/RootStorageAccelerationPolicyTest.kt",
    r'''/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.headunit.root.storage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RootStorageAccelerationPolicyTest {
    @Test
    fun cachedMetadataLeadsWithoutStartingRoot() {
        assertEquals(
            RootStorageResolutionOrder.CACHED_ROOT_METADATA_FIRST,
            RootStorageAccelerationPolicy.choose(
                requestedPath = "/storage/usbdisk0/Music",
                rootEnabled = true,
                rootAvailable = false,
                hasCachedRecord = true,
            ),
        )
    }

    @Test
    fun grantedRootLeadsOnlyForRawOrPreparedPaths() {
        listOf("/mnt/media_rw/usbdisk0/Music", "/storage/auxio-root/usbdisk0/Music")
            .forEach { path ->
                assertEquals(
                    RootStorageResolutionOrder.REFRESHED_ROOT_METADATA_FIRST,
                    RootStorageAccelerationPolicy.choose(path, true, true, false),
                )
                assertTrue(RootStorageAccelerationPolicy.requiresRootPreparation(path))
            }
        assertEquals(
            RootStorageResolutionOrder.DIRECT_FIRST,
            RootStorageAccelerationPolicy.choose("/storage/usbdisk0/Music", true, true, false),
        )
    }

    @Test
    fun traversalAndProtectedPathsNeverGainRootAcceleration() {
        listOf(
                "/storage/usbdisk0/../data",
                "/mnt/media_rw/usbdisk0/../../data",
                "/data/local/tmp",
            )
            .forEach { path ->
                assertFalse(RootStorageAccelerationPolicy.isRemovablePath(path))
                assertFalse(RootStorageAccelerationPolicy.requiresRootPreparation(path))
                assertEquals(
                    RootStorageResolutionOrder.DIRECT_FIRST,
                    RootStorageAccelerationPolicy.choose(path, true, true, true),
                )
            }
    }

    @Test
    fun disabledRootAlwaysUsesDirectAuthority() {
        assertEquals(
            RootStorageResolutionOrder.DIRECT_FIRST,
            RootStorageAccelerationPolicy.choose(
                requestedPath = "/mnt/media_rw/usbdisk0",
                rootEnabled = false,
                rootAvailable = true,
                hasCachedRecord = true,
            ),
        )
    }

    @Test
    fun removableClassificationCoversTs18AndUuidVolumes() {
        assertTrue(RootStorageAccelerationPolicy.isRemovablePath("/storage/usbdisk1"))
        assertTrue(RootStorageAccelerationPolicy.isRemovablePath("/mnt/media_rw/usbdisk0"))
        assertTrue(RootStorageAccelerationPolicy.isRemovablePath("/storage/auxio-root/usbdisk2"))
        assertTrue(RootStorageAccelerationPolicy.isRemovablePath("/storage/12AB-34CD/Music"))
        assertFalse(RootStorageAccelerationPolicy.isRemovablePath("/storage/emulated/0/Music"))
    }
}
''',
)

# PreparedVolumeIndexStore: canonical containment and genuinely atomic cache replacement.
path = "app/src/main/java/org/oxycblt/auxio/headunit/root/storage/PreparedVolumeIndexStore.kt"
replace_once(path, "import android.content.Context\nimport android.os.SystemClock\n", "import android.content.Context\nimport android.os.Build\nimport android.os.SystemClock\n")
replace_once(path, "import java.io.File\n", "import java.io.File\nimport java.nio.file.Files\nimport java.nio.file.StandardCopyOption\n")
replace_once(
    path,
    '        val clean = requestedPath.replace(\'\\\\\', \'/\').trimEnd(\'/\').ifEmpty { "/" }\n        val rootEnabled = rootStateHolder.isUserEnabled()\n',
    '        val clean = requestedPath.replace(\'\\\\\', \'/\').trimEnd(\'/\').ifEmpty { "/" }\n        if (!RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(clean)) {\n            return SourceResolution(clean, null, SourceAuthority.UNAVAILABLE, "unsafe_storage_path")\n        }\n        val rootEnabled = rootStateHolder.isUserEnabled()\n',
)
replace_once(
    path,
    '''    private fun resolveDirect(clean: String): SourceResolution? {
        if (clean.startsWith("/mnt/media_rw/")) return null
        val prepared = clean.startsWith("/storage/auxio-root/")
''',
    '''    private fun resolveDirect(clean: String): SourceResolution? {
        if (
            clean.startsWith("/mnt/media_rw/") ||
                !RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(clean)
        ) {
            return null
        }
        val prepared = clean.startsWith("/storage/auxio-root/")
''',
)
replace_once(
    path,
    '''        for (candidate in candidatePaths(record, suffix)) {
            val prepared = candidate.startsWith("/storage/auxio-root/")
''',
    '''        for (candidate in candidatePaths(record, suffix)) {
            if (!RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(candidate)) continue
            val prepared = candidate.startsWith("/storage/auxio-root/")
''',
)
regex_once(
    path,
    r'''    private fun writeAtomically\(text: String\): Boolean =\n        runCatching \{.*?\n            \.getOrDefault\(false\)''',
    r'''    private fun writeAtomically(text: String): Boolean {
        if (!cacheDir.isDirectory && !cacheDir.mkdirs()) return false
        val temp = File(cacheDir, "volumes.tsv.tmp")
        return try {
            temp.writeText(text)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                temp.delete()
                false
            } else {
                Files.move(
                    temp.toPath(),
                    cacheFile.toPath(),
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING,
                )
                true
            }
        } catch (_: Exception) {
            temp.delete()
            false
        }
    }''',
)

# LocationsDialog: preserve exclusion/filter directory semantics and apply root authority only to
# playable DirectFS include sources.
path = "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt"
replace_once(
    path,
    "import org.oxycblt.auxio.headunit.root.storage.PreparedVolumeIndexStore\n",
    "import org.oxycblt.auxio.headunit.root.storage.PreparedVolumeIndexStore\nimport org.oxycblt.auxio.headunit.root.storage.RootStorageAccelerationPolicy\n",
)
replace_once(
    path,
    "    private var pendingLocationCallback: ((Location.Unopened) -> Unit)? = null\n",
    "    private var pendingLocationCallback: ((Location.Unopened) -> Unit)? = null\n    private var pendingRequiresPlayableSource = true\n",
)
replace_once(path, "            pendingLocationCallback = { location -> addIncludeLocation(location) }\n            showCandidatePathPicker", "            pendingRequiresPlayableSource = true\n            pendingLocationCallback = { location -> addIncludeLocation(location) }\n            showCandidatePathPicker")
replace_once(path, "        binding.locationsIncludeAdd.setOnClickListener {\n            pendingLocationCallback", "        binding.locationsIncludeAdd.setOnClickListener {\n            pendingRequiresPlayableSource = true\n            pendingLocationCallback")
replace_once(path, "        binding.locationsExcludeAdd.setOnClickListener {\n            pendingLocationCallback", "        binding.locationsExcludeAdd.setOnClickListener {\n            pendingRequiresPlayableSource = false\n            pendingLocationCallback")
replace_once(path, "        binding.locationsFilterAdd.setOnClickListener {\n            pendingLocationCallback", "        binding.locationsFilterAdd.setOnClickListener {\n            pendingRequiresPlayableSource = false\n            pendingLocationCallback")
replace_once(
    path,
    '''    private fun clearPendingLocationCallback(callback: (Location.Unopened) -> Unit) {
        if (pendingLocationCallback === callback) {
            pendingLocationCallback = null
        }
    }
''',
    '''    private fun clearPendingLocationCallback(callback: (Location.Unopened) -> Unit) {
        if (pendingLocationCallback === callback) {
            pendingLocationCallback = null
            pendingRequiresPlayableSource = true
        }
    }
''',
)
replace_once(
    path,
    "        val callback = pendingLocationCallback ?: return\n        val generation = ++candidateDiscoveryGeneration\n",
    "        val callback = pendingLocationCallback ?: return\n        val requiresPlayableSource = pendingRequiresPlayableSource\n        val generation = ++candidateDiscoveryGeneration\n",
)
path_file = ROOT / path
text = path_file.read_text(encoding="utf-8")
text = text.replace("showManualPathEntry(disableThirdParty, callback)", "showManualPathEntry(disableThirdParty, requiresPlayableSource, callback)")
text = text.replace("validateAndAcceptPath(accessibleCandidates[which], disableThirdParty, callback)", "validateAndAcceptPath(accessibleCandidates[which], disableThirdParty, requiresPlayableSource, callback)")
path_file.write_text(text, encoding="utf-8")
regex_once(
    path,
    r'''    private fun validateAndAcceptPath\(.*?\n    private fun shouldRejectThirdPartyLocation\(''',
    r'''    private fun validateAndAcceptPath(
        path: String,
        disableThirdParty: Boolean,
        requiresPlayableSource: Boolean,
        callback: (Location.Unopened) -> Unit,
    ) {
        lifecycleScope.launch {
            val validation =
                withContext(Dispatchers.IO) {
                    validateManualPath(path, requiresPlayableSource)
                }
            val currentContext = context
            if (currentContext == null) {
                clearPendingLocationCallback(callback)
                return@launch
            }
            if (validation != ManualPathValidation.OK) {
                L.w("Rejecting source path $path: $validation")
                currentContext.showToast(validation.toastRes)
                clearPendingLocationCallback(callback)
                return@launch
            }

            var resolvedPath = path
            var authorityDetail = "directory_validation"
            val directTopwayPlayable =
                requiresPlayableSource &&
                    BuildConfig.TOPWAY_COMPAT_FLAVOR &&
                    locationMode == LocationMode.DIRECT_FS
            if (directTopwayPlayable) {
                if (!TopwaySourcePolicy.isAllowedSourceCandidate(path)) {
                    currentContext.showToast(R.string.set_path_unsafe)
                    clearPendingLocationCallback(callback)
                    return@launch
                }
                val resolution =
                    withContext(Dispatchers.IO) { preparedVolumeIndexStore.resolveSourceSync(path) }
                val resolved = resolution.resolvedPath
                if (
                    resolved == null ||
                        (resolution.authority != SourceAuthority.APP_READABLE &&
                            resolution.authority != SourceAuthority.PREPARED_ALIAS)
                ) {
                    L.w("Rejecting playable source $path: ${resolution.authority} ${resolution.detail}")
                    currentContext.showToast(sourceResolutionFailureToast(path, resolution))
                    clearPendingLocationCallback(callback)
                    return@launch
                }
                resolvedPath = resolved
                authorityDetail = "${resolution.authority}:${resolution.detail}"
            }

            val uri = Uri.fromFile(File(resolvedPath))
            val location = Location.Unopened.from(currentContext, uri)
            if (shouldRejectThirdPartyLocation(uri, location, disableThirdParty)) {
                L.w("Rejecting source $resolvedPath: third-party volume disabled")
                currentContext.showToast(R.string.err_bad_location)
                clearPendingLocationCallback(callback)
                return@launch
            }
            if (location.open(currentContext) == null) {
                L.w("Rejecting source $resolvedPath: Location.open returned null")
                currentContext.showToast(R.string.set_path_open_failed)
                clearPendingLocationCallback(callback)
                return@launch
            }
            L.i(
                "Accepted source requested=$path resolved=$resolvedPath mode=$locationMode " +
                    "playable=$requiresPlayableSource authority=$authorityDetail"
            )
            callback(location)
            clearPendingLocationCallback(callback)
        }
    }

    private enum class ManualPathValidation(val toastRes: Int) {
        OK(R.string.lbl_ok),
        UNSAFE(R.string.set_path_unsafe),
        MISSING(R.string.set_path_missing),
        NOT_DIRECTORY(R.string.set_path_not_directory),
        UNREADABLE(R.string.set_path_unreadable),
        PERMISSION_MISSING(R.string.set_path_permission_missing),
    }

    private fun validateManualPath(
        path: String,
        requiresPlayableSource: Boolean,
    ): ManualPathValidation {
        val directTopwayPlayable =
            requiresPlayableSource &&
                BuildConfig.TOPWAY_COMPAT_FLAVOR &&
                locationMode == LocationMode.DIRECT_FS
        if (directTopwayPlayable && !TopwaySourcePolicy.isAllowedSourceCandidate(path)) {
            return ManualPathValidation.UNSAFE
        }
        if (!hasStoragePermission && locationMode != LocationMode.SAF) {
            return ManualPathValidation.PERMISSION_MISSING
        }
        return try {
            val file = File(path)
            val rootRecoveryEligible =
                directTopwayPlayable &&
                    rootGate.isUserEnabled() &&
                    RootStorageAccelerationPolicy.isRemovablePath(path)
            when {
                !file.exists() && !rootRecoveryEligible -> ManualPathValidation.MISSING
                file.exists() && !file.isDirectory -> ManualPathValidation.NOT_DIRECTORY
                file.exists() && !file.canRead() && !rootRecoveryEligible ->
                    ManualPathValidation.UNREADABLE
                else -> ManualPathValidation.OK
            }
        } catch (e: SecurityException) {
            L.w(e, "Security exception while validating manual path $path")
            ManualPathValidation.PERMISSION_MISSING
        } catch (e: RuntimeException) {
            L.w(e, "Runtime exception while validating manual path $path")
            ManualPathValidation.UNREADABLE
        }
    }

    private fun sourceResolutionFailureToast(path: String, resolution: org.oxycblt.auxio.headunit.root.storage.SourceResolution): Int {
        if (resolution.detail == "unsafe_storage_path") return R.string.set_path_unsafe
        if (resolution.authority == SourceAuthority.ROOT_SNAPSHOT_ONLY) {
            return R.string.set_path_root_snapshot_only
        }
        return try {
            val file = File(path)
            when {
                !file.exists() -> R.string.set_path_missing
                !file.isDirectory -> R.string.set_path_not_directory
                !file.canRead() -> R.string.set_path_unreadable
                else -> R.string.set_path_no_supported_audio
            }
        } catch (_: RuntimeException) {
            R.string.set_path_unreadable
        }
    }

    private fun shouldRejectThirdPartyLocation(''',
)
replace_once(
    path,
    '''    private fun showManualPathEntry(
        disableThirdParty: Boolean,
        callback: (Location.Unopened) -> Unit,
''',
    '''    private fun showManualPathEntry(
        disableThirdParty: Boolean,
        requiresPlayableSource: Boolean,
        callback: (Location.Unopened) -> Unit,
''',
)
replace_once(
    path,
    "                validateAndAcceptPath(pathText, disableThirdParty, callback)\n",
    "                validateAndAcceptPath(pathText, disableThirdParty, requiresPlayableSource, callback)\n",
)

# Clamp root snapshot discovery to the caller's remaining picker budget.
path = "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicy.kt"
replace_once(
    path,
    '''            val snapshot =
                rootGate.snapshotTreeSync(
                    root.absolutePath,
                    MAX_SCAN_DEPTH,
                    ROOT_DISCOVERY_SNAPSHOT_TIMEOUT_MS,
                ) ?: return
''',
    '''            val remainingElapsedMs = deadlineElapsedMs - monotonicNowMs()
            if (remainingElapsedMs <= 0L) return
            val snapshot =
                rootGate.snapshotTreeSync(
                    root.absolutePath,
                    MAX_SCAN_DEPTH,
                    minOf(ROOT_DISCOVERY_SNAPSHOT_TIMEOUT_MS, remainingElapsedMs),
                ) ?: return
''',
)

# Root status immediately reflects the explicit enable/probe result.
path = "app/src/main/java/org/oxycblt/auxio/settings/RootDiagnosticsHelper.kt"
replace_once(path, "    private fun rootStatusSummary(context: Context, state: RootStateHolder.State): String =\n", "    fun rootStatusSummary(context: Context, state: RootStateHolder.State): String =\n")
path = "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt"
replace_once(
    path,
    '''                        viewLifecycleOwner.lifecycleScope.launch {
                            withContext(Dispatchers.IO) { rootStateHolder.probeSync() }
                        }
''',
    '''                        viewLifecycleOwner.lifecycleScope.launch {
                            val probed = withContext(Dispatchers.IO) { rootStateHolder.probeSync() }
                            findPreference<Preference>(getString(R.string.set_key_root_fs_status))
                                ?.summary =
                                RootDiagnosticsHelper.rootStatusSummary(requireContext(), probed)
                        }
''',
)

# Helper timing headroom: 1s mount wait + at most two 1s searches (plus bounded kill grace).
path = "tools/ts18-root-storage-fastpath/magisk-module/service.sh"
replace_once(path, "ON_DEMAND_WAIT_SECONDS=2\nMAX_SAMPLED_VOLUMES=2\nMAX_SAMPLE_DEPTH=6\nSAMPLE_TIMEOUT_SECONDS=2\n", "ON_DEMAND_WAIT_SECONDS=1\nMAX_SAMPLED_VOLUMES=2\nMAX_SAMPLE_DEPTH=6\nSAMPLE_TIMEOUT_SECONDS=1\n")

# UI wording and distinct no-audio result.
path = "app/src/main/res/values/strings.xml"
replace_once(
    path,
    '<string name="set_use_root_fs_desc">Allow bounded root storage discovery and prepared USB aliases on Topway builds. Ordinary /storage access is always tried first.</string>',
    '<string name="set_use_root_fs_desc">Allow bounded root storage discovery and prepared USB aliases on Topway builds. Ordinary /storage access is still attempted without root when feasible.</string>',
)
replace_once(
    path,
    '    <string name="set_path_root_snapshot_only">Root can see this volume, but Auxio cannot open its media yet. Use the matching /storage path or install/repair the prepared-alias helper.</string>\n',
    '    <string name="set_path_root_snapshot_only">Root can see this volume, but Auxio cannot open its media yet. Use the matching /storage path or install/repair the prepared-alias helper.</string>\n    <string name="set_path_no_supported_audio">No supported audio file could be opened within the bounded source check.</string>\n',
)

# Documentation: distinguish boot manifest, explicit source flow and physical evidence.
path = "docs/ts18/ROOT_STORAGE_FASTPATH.md"
replace_once(
    path,
    "## Evidence and authority\n\n",
    "## Evidence and authority\n\n**Evidence confidence:** high for repository implementation and CI-enforced boundaries; medium for the captured TS18 mount layout; physical alias visibility, access and performance remain **Requires TS18 validation**.\n\n",
)
regex_once(
    path,
    r'''```text\nMagisk module-root service\.sh \(late_start service\).*?```\n\nThe Magisk helper runs independently during late start\..*?\n\nEvery detected valid `usbdiskN` receives a manifest row\..*?\n''',
    r'''```text
Magisk module-root service.sh (late_start service)
    -> wait boundedly for /mnt/media_rw/usbdiskN
    -> map raw volume to /storage/usbdiskN when usable
    -> otherwise create and verify a read-only prepared alias
    -> atomically publish a compact volume/alias manifest (no representative hint at boot)

Auxio immediate startup (never blocked by root)
    -> restore database, queue, MediaSession and first audio

Explicit source/recovery flow
    -> instantiate the app-private prepared-volume index and load its cache without su
    -> resolve sources using authority- and cost-aware ordering
    -> optionally run helper --once to publish bounded representative-media hints
    -> perform one bounded volume snapshot only after playable-path resolution fails
    -> persist a source only after representative media opens in the Auxio process
```

The Magisk helper runs independently during late start. Auxio does not start a second boot-time scanner or block its immediate lane waiting for that helper. The cached prepared-volume index participates in the explicit source/recovery flow, not first-audio startup. A later explicit source flow can invoke the same fixed helper with `--once`; helper and app-side refresh requests are locked, serialized and debounced.

Every detected valid `usbdiskN` receives a manifest row. The on-demand acceleration path limits representative-file searches separately to two volumes at one second each, preserving helper-timeout headroom without silently omitting later volumes.
''',
)
replace_once(path, "- [x] Publish the prepared-volume manifest atomically with timestamps, typed state and a representative-media hint.", "- [x] Publish the prepared-volume manifest atomically with timestamps and typed state; add representative-media hints only during bounded on-demand refresh.")

# Tests for command normalization and picker timeout budget.
path = "app/src/test/java/org/oxycblt/auxio/headunit/root/RootStorageCommandPolicyTest.kt"
replace_once(path, '                "/mnt/media_rw/usbdisk0/Music\'s",\n', '                "\\\\mnt\\\\media_rw\\\\usbdisk0\\\\Music\'s",\n')
replace_once(path, '        assertTrue(command.contains("Music\'\"\'\"\'s"))\n', '        assertTrue(command.contains("/mnt/media_rw/usbdisk0/Music\'\"\'\"\'s"))\n')

path = "app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicyDiscoveryTest.kt"
replace_once(path, "        var snapshots = 0\n", "        var snapshots = 0\n        var observedTimeoutMs = Long.MAX_VALUE\n")
replace_once(path, "                    snapshots++\n                    return org.oxycblt.musikr.fs.RootTreeSnapshot(\n", "                    snapshots++\n                    observedTimeoutMs = timeoutMs\n                    return org.oxycblt.musikr.fs.RootTreeSnapshot(\n")
replace_once(path, "        TopwaySourcePolicy.discoverAudioParents(root, out, rootGate = gate)\n\n        assertEquals(1, snapshots)\n", "        val deadline = System.nanoTime() / 1_000_000L + 100L\n        TopwaySourcePolicy.discoverAudioParents(\n            root,\n            out,\n            rootGate = gate,\n            deadlineElapsedMs = deadline,\n        )\n\n        assertEquals(1, snapshots)\n        assertTrue(observedTimeoutMs in 1L..100L)\n")

print("PR #200 comment-closure patch applied")

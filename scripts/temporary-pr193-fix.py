#!/usr/bin/env python3
"""Apply the final reviewed PR #193 source-policy fixes."""

from pathlib import Path
import re


def replace_once(text: str, old: str, new: str, label: str) -> str:
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"STOP: expected exactly one {label} match, found {count}")
    return text.replace(old, new, 1)


def substitute_once(text: str, pattern: str, replacement: str, label: str) -> str:
    updated, count = re.subn(pattern, replacement, text, count=1, flags=re.DOTALL)
    if count != 1:
        raise SystemExit(f"STOP: expected exactly one {label} match, found {count}")
    return updated


def main() -> int:
    source_path = Path(
        "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicy.kt"
    )
    source = source_path.read_text(encoding="utf-8")

    source = replace_once(
        source,
        "        val candidates = saved + authorisedOptionalRoots + discoveredRoots\n",
        "        val candidates = saved + authorisedOptionalRoots + discoveredRoots\n"
        "        val displayOptionalRoots = preferAppFacingRoots(authorisedOptionalRoots)\n",
        "optional-root display ordering",
    )
    source = replace_once(
        source,
        "        listOf(saved, authorisedOptionalRoots, audioParents.toList(), musicFolders, usb, generic)\n",
        "        listOf(saved, displayOptionalRoots, audioParents.toList(), musicFolders, usb, generic)\n",
        "candidate ordering",
    )

    new_discovery = """    internal fun discoverAudioParents(
        root: File,
        out: LinkedHashSet<String>,
        rootGate: RootGate? = null,
        enforceSafeRoot: Boolean = true,
        deadline: Long = System.currentTimeMillis() + MAX_SCAN_ELAPSED_MS,
    ) {
        if (enforceSafeRoot && !isAllowedSourceCandidate(root.absolutePath)) return
        val canonicalRoot =
            if (enforceSafeRoot) {
                runCatching { root.canonicalFile }.getOrNull() ?: return
            } else {
                null
            }
        var visited = 0
        val queue = ArrayDeque<Pair<File, Int>>()
        queue.add(root to 0)
        while (queue.isNotEmpty()) {
            if (out.size >= MAX_CANDIDATES || visited >= MAX_VISITED_FILES) return
            if (System.currentTimeMillis() > deadline) return
            val (dir, depth) = queue.removeFirst()
            if (canonicalRoot != null && !isWithinCanonicalRoot(dir, canonicalRoot)) continue
            val children = listFilesSafe(dir, rootGate) ?: continue
            var containsAudio = false
            for (child in children) {
                visited++
                if (visited >= MAX_VISITED_FILES) break
                if (canonicalRoot != null && !isWithinCanonicalRoot(child.file, canonicalRoot)) {
                    continue
                }
                when {
                    child.isFile && child.file.extension.lowercase() in AUDIO_EXTENSIONS ->
                        containsAudio = true
                    child.isDirectory &&
                        depth < MAX_SCAN_DEPTH &&
                        shouldDescend(child.file, enforceSafeRoot, canonicalRoot) ->
                        queue.add(child.file to depth + 1)
                }
            }
            if (
                containsAudio &&
                    (!enforceSafeRoot ||
                        (isAllowedSourceCandidate(dir.absolutePath) &&
                            canonicalRoot != null &&
                            isWithinCanonicalRoot(dir, canonicalRoot)))
            ) {
                out.add(dir.absolutePath)
            }
        }
    }"""
    source = substitute_once(
        source,
        r"    internal fun discoverAudioParents\(.*?\n    }\n\n(?=    private fun listFilesSafe)",
        new_discovery + "\n\n",
        "configured-root traversal",
    )

    new_descend = """    private fun shouldDescend(
        dir: File,
        enforceSafeRoot: Boolean,
        canonicalRoot: File?,
    ): Boolean {
        val name = dir.name
        if (name == "." || name == ".." || name.startsWith('.')) return false
        if (isNoisyDir(name)) return false
        val path = dir.absolutePath.replace('\\', '/')
        if (
            path.contains("/Android/", ignoreCase = true) ||
                path.endsWith("/Android", ignoreCase = true)
        )
            return false
        if (!enforceSafeRoot) return true
        return canonicalRoot != null &&
            isAllowedSourceCandidate(path) &&
            isWithinCanonicalRoot(dir, canonicalRoot)
    }"""
    source = substitute_once(
        source,
        r"    private fun shouldDescend\(.*?\n    }\n\n(?=    internal fun isNoisyDir)",
        new_descend + "\n\n",
        "descent policy",
    )

    containment_marker = """    private fun isContainedByAny(
        candidatePath: String,
        configuredRoots: Collection<String>,
    ): Boolean {
"""
    containment_helper = """    internal fun isWithinCanonicalRoot(candidate: File, canonicalRoot: File): Boolean {
        val canonicalCandidate = runCatching { candidate.canonicalFile }.getOrNull() ?: return false
        var cursor: File? = canonicalCandidate
        while (cursor != null) {
            if (cursor == canonicalRoot) return true
            cursor = cursor.parentFile
        }
        return false
    }

""" + containment_marker
    source = replace_once(
        source,
        containment_marker,
        containment_helper,
        "canonical containment helper insertion",
    )
    source_path.write_text(source, encoding="utf-8")

    test_path = Path(
        "app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicyDiscoveryTest.kt"
    )
    tests = test_path.read_text(encoding="utf-8")
    marker = """    @Test
    fun discoversAudioParentFoldersUnderInjectedRootForTests() {
"""
    regression = """    @Test
    fun canonicalContainmentRejectsSymlinkEscapes() {
        val tempRoot = Files.createTempDirectory("topway-source-boundary").toFile()
        try {
            val configuredRoot = File(tempRoot, "configured").apply { mkdirs() }
            val outsideRoot = File(tempRoot, "outside").apply { mkdirs() }
            val escape = File(configuredRoot, "escape")
            Files.createSymbolicLink(escape.toPath(), outsideRoot.toPath())

            assertTrue(
                TopwaySourcePolicy.isWithinCanonicalRoot(
                    configuredRoot,
                    configuredRoot.canonicalFile,
                )
            )
            assertFalse(
                TopwaySourcePolicy.isWithinCanonicalRoot(
                    escape,
                    configuredRoot.canonicalFile,
                )
            )
        } finally {
            tempRoot.deleteRecursively()
        }
    }

"""
    tests = replace_once(tests, marker, regression + marker, "regression-test insertion")
    test_path.write_text(tests, encoding="utf-8")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

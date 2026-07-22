from pathlib import Path


def replace_once(path: str, old: str, new: str) -> None:
    target = Path(path)
    text = target.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"Expected one match in {path}; found {count}")
    target.write_text(text.replace(old, new, 1), encoding="utf-8", newline="\n")


path = "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicy.kt"
old = "\n".join(
    [
        "        val saved =",
        "            savedPaths.mapNotNull(::normaliseCandidatePath).filter(::isAllowedSourceCandidate)",
        "        val savedSet = saved.map { it.trimEnd('/') }.toSet()",
        "        val media =",
        "            mediaStoreParents",
        "                .mapNotNull(::normaliseCandidatePath)",
        "                .filter(::isAllowedSourceCandidate)",
        "        val injectedRoots =",
        "            storageRoots.mapNotNull(::normaliseCandidatePath).filter(::isAllowedSourceCandidate)",
        "        val candidates = mutableListOf<String>()",
        "        // Configured roots are always authoritative and are scanned before optional suggestions.",
        "        candidates.addAll(saved)",
        "        candidates.addAll(SAFE_GENERIC_FALLBACKS)",
        "        candidates.addAll(injectedRoots)",
        "",
        "        // Do not walk /storage or /mnt/media_rw during configured-only/background access. The",
        "        // explicit source picker opts in and remains the only caller allowed to discover new USBs.",
        "        val discoveredRoots =",
        "            if (allowUnconfiguredUsb) {",
        "                discoverCandidateRoots()",
        "            } else {",
        "                emptyList()",
        "            }",
        "        if (allowUnconfiguredUsb) {",
        "            candidates.addAll(discoveredRoots)",
        "        } else {",
        "            candidates.addAll(discoveredRoots.filter { it.trimEnd('/') in savedSet })",
        "        }",
        "",
        "        val roots = preferAppFacingRoots(candidates).filter(::isAllowedSourceCandidate)",
        "        val audioParents = linkedSetOf<String>()",
        "        val deadline = System.currentTimeMillis() + MAX_SCAN_ELAPSED_MS",
        "        for (root in roots) {",
        "            if (audioParents.size >= MAX_CANDIDATES) break",
        "            if (System.currentTimeMillis() > deadline) break",
        "            discoverAudioParents(File(root), audioParents, rootGate, deadline = deadline)",
        "        }",
        "        val musicFolders =",
        "            roots.mapNotNull {",
        "                musicChildIfAccessible(it) ?: it.takeIf { p -> p.endsWith(\"/Music\", true) }",
        "            }",
        "        val usb = roots.filter(::isUsbCandidate)",
        "        val generic = roots.filterNot(::isUsbCandidate)",
        "        val ordered = linkedSetOf<String>()",
        "        listOf(saved, media, audioParents.toList(), musicFolders, usb, generic).forEach { group ->",
        "            group.filterTo(ordered, ::isAllowedSourceCandidate)",
        "        }",
    ]
)
new = "\n".join(
    [
        "        val saved =",
        "            savedPaths.mapNotNull(::normaliseCandidatePath).filter(::isAllowedSourceCandidate)",
        "        val media =",
        "            mediaStoreParents",
        "                .mapNotNull(::normaliseCandidatePath)",
        "                .filter(::isAllowedSourceCandidate)",
        "        val injectedRoots =",
        "            storageRoots.mapNotNull(::normaliseCandidatePath).filter(::isAllowedSourceCandidate)",
        "        val fallbackRoots = SAFE_GENERIC_FALLBACKS.filter(::isAllowedSourceCandidate)",
        "        val optionalRoots = media + injectedRoots + fallbackRoots",
        "        val authorisedOptionalRoots =",
        "            if (allowUnconfiguredUsb) {",
        "                optionalRoots",
        "            } else {",
        "                optionalRoots.filter { candidate -> isContainedByAny(candidate, saved) }",
        "            }",
        "        val discoveredRoots =",
        "            if (allowUnconfiguredUsb) discoverCandidateRoots() else emptyList()",
        "        val candidates = saved + authorisedOptionalRoots + discoveredRoots",
        "",
        "        // Background/configured-only access never walks or returns an unconfigured root. The",
        "        // explicit source picker is the sole caller that opts into new removable suggestions.",
        "        val roots = preferAppFacingRoots(candidates).filter(::isAllowedSourceCandidate)",
        "        val audioParents = linkedSetOf<String>()",
        "        val deadline = System.currentTimeMillis() + MAX_SCAN_ELAPSED_MS",
        "        for (root in roots) {",
        "            if (audioParents.size >= MAX_CANDIDATES) break",
        "            if (System.currentTimeMillis() > deadline) break",
        "            discoverAudioParents(File(root), audioParents, rootGate, deadline = deadline)",
        "        }",
        "        val musicFolders =",
        "            roots.mapNotNull {",
        "                musicChildIfAccessible(it) ?: it.takeIf { p -> p.endsWith(\"/Music\", true) }",
        "            }",
        "        val usb = roots.filter(::isUsbCandidate)",
        "        val generic = roots.filterNot(::isUsbCandidate)",
        "        val ordered = linkedSetOf<String>()",
        "        listOf(saved, authorisedOptionalRoots, audioParents.toList(), musicFolders, usb, generic)",
        "            .forEach { group -> group.filterTo(ordered, ::isAllowedSourceCandidate) }",
    ]
)
replace_once(path, old, new)

anchor = "\n".join(
    [
        "    private fun normaliseCandidatePath(value: String): String? {",
        "        val trimmed = value.trim()",
    ]
)
helper = "\n".join(
    [
        "    private fun isContainedByAny(candidatePath: String, configuredRoots: Collection<String>): Boolean {",
        "        val candidate = runCatching { File(candidatePath).canonicalFile }.getOrNull() ?: return false",
        "        return configuredRoots.any { configuredPath ->",
        "            val root = runCatching { File(configuredPath).canonicalFile }.getOrNull() ?: return@any false",
        "            var cursor: File? = candidate",
        "            while (cursor != null) {",
        "                if (cursor == root) return@any true",
        "                cursor = cursor.parentFile",
        "            }",
        "            false",
        "        }",
        "    }",
        "",
        anchor,
    ]
)
replace_once(path, anchor, helper)

raw = "app/src/main/java/org/oxycblt/auxio/headunit/ts18/RawFastResume.kt"
replace_once(
    raw,
    '            path.isBlank() ||\n                path.contains("/../") ||',
    '            path.isBlank() ||\n'
    '                path == "." ||\n'
    '                path.startsWith("./") ||\n'
    '                path.startsWith("../") ||\n'
    '                path.contains("/../") ||',
)

tests = "app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicyDiscoveryTest.kt"
replace_once(
    tests,
    '                storageRoots = listOf("/storage/emulated/0", "/storage/usbdisk1"),\n'
    "            )",
    '                storageRoots = listOf("/storage/emulated/0", "/storage/usbdisk1"),\n'
    "                allowUnconfiguredUsb = true,\n"
    "            )",
)
replace_once(
    tests,
    '                storageRoots = listOf("/mnt/media_rw/usbdisk1", "/storage/usbdisk1")\n'
    "            )",
    '                storageRoots = listOf("/mnt/media_rw/usbdisk1", "/storage/usbdisk1"),\n'
    "                allowUnconfiguredUsb = true,\n"
    "            )",
)
test_anchor = "    @Test\n    fun allowsUuidStyleRemovableStorageRootsButRejectsStorageAliases() {"
test_new = """    @Test
    fun configuredOnlyModeExcludesUnconfiguredMediaInjectedAndFallbackRoots() {
        val candidates =
            TopwaySourcePolicy.discoverMusicSourceCandidates(
                savedPaths = listOf("/storage/emulated/0/Music"),
                mediaStoreParents = listOf("/storage/emulated/0/Other", "/storage/usbdisk1/Music"),
                storageRoots = listOf("/storage/emulated/0", "/storage/usbdisk2"),
                allowUnconfiguredUsb = false,
            )

        assertTrue(
            candidates.all {
                it == "/storage/emulated/0/Music" ||
                    it.startsWith("/storage/emulated/0/Music/")
            }
        )
        assertFalse(candidates.any { it.startsWith("/storage/usbdisk") })
        assertFalse(candidates.contains("/storage/emulated/0/Other"))
    }

    @Test
    fun allowsUuidStyleRemovableStorageRootsButRejectsStorageAliases() {"""
replace_once(tests, test_anchor, test_new)

resume_tests = "app/src/test/java/org/oxycblt/auxio/headunit/ts18/Ts18RawFastResumePolicyTest.kt"
old_resume = """        assertFalse(
            RawFastResumeValidator.isInsideConfiguredRoots(
                "/storage/emulated/0/Music/../Private/secret.mp3",
                listOf(root),
            )
        )"""
new_resume = old_resume + """
        assertFalse(
            RawFastResumeValidator.isInsideConfiguredRoots(
                "../storage/emulated/0/Music/track.mp3",
                listOf(root),
            )
        )
        assertFalse(
            RawFastResumeValidator.isInsideConfiguredRoots(
                "./storage/emulated/0/Music/track.mp3",
                listOf(root),
            )
        )"""
replace_once(resume_tests, old_resume, new_resume)

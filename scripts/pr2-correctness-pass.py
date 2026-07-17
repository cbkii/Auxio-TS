#!/usr/bin/env python3
from pathlib import Path


def replace(path: str, old: str, new: str, count: int = 1) -> None:
    p = Path(path)
    text = p.read_text()
    if old not in text:
        raise SystemExit(f"missing patch anchor in {path}: {old[:180]!r}")
    p.write_text(text.replace(old, new, count))


store = 'musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt'
replace(
    store,
    '''                val fingerprintChanged =
                    previous?.fingerprint != snapshot.fingerprint ||
                        previous.fingerprintStrength != snapshot.fingerprintStrength.name''',
    '''                val fingerprintChanged =
                    previous == null ||
                        previous.fingerprint != snapshot.fingerprint ||
                        previous.fingerprintStrength != snapshot.fingerprintStrength.name''',
)
replace(
    store,
    '''                val mustScan =
                    force ||
                        previous?.lastCommittedGeneration == null ||
                        previous.incomplete ||
                        previous.configurationRevision != configurationRevision ||''',
    '''                val mustScan =
                    force ||
                        previous == null ||
                        previous.lastCommittedGeneration == null ||
                        previous.incomplete ||
                        previous.configurationRevision != configurationRevision ||''',
)

# A newly extracted Lean row must remain marked Lean. Using the generic cache-hit path here used to
# classify any row without a prior URI state as Full, which prevented the later Full enrichment pass.
replace(
    store,
    '''    override suspend fun markSeen(file: File, cachedFile: CachedFile?) {
        val plan = currentPlan ?: return
        val sourceKey = SourceIdentity.forFile(file)
        if (sourceKey !in plan.scanSourceKeys) return
        val cached = cachedFile?.audio
        val tags = cached?.tags
        val fileName = file.path.name ?: file.uri.lastPathSegment ?: file.uri.toString()
        val profile =
            dao.uriState(sourceKey, file.uri.toString())?.metadataProfile
                ?: if (cachedFile != null) MetadataProfile.FULL.name else plan.metadataProfile.name
        dao.upsertSeen(
            ScanSeenData(''',
    '''    override suspend fun markSeen(file: File, cachedFile: CachedFile?) {
        val plan = currentPlan ?: return
        val sourceKey = SourceIdentity.forFile(file)
        if (sourceKey !in plan.scanSourceKeys) return
        val profile =
            dao.uriState(sourceKey, file.uri.toString())?.metadataProfile
                ?: if (cachedFile != null) MetadataProfile.FULL.name else plan.metadataProfile.name
        upsertSeen(plan, sourceKey, file, cachedFile, profile)
    }

    private suspend fun upsertSeen(
        plan: IncrementalScanPlan,
        sourceKey: String,
        file: File,
        cachedFile: CachedFile?,
        profile: String,
    ) {
        val cached = cachedFile?.audio
        val tags = cached?.tags
        val fileName = file.path.name ?: file.uri.lastPathSegment ?: file.uri.toString()
        dao.upsertSeen(
            ScanSeenData(''',
)
replace(
    store,
    '''        markSeen(cachedFile.file, cachedFile)
        return true''',
    '''        upsertSeen(
            plan = plan,
            sourceKey = sourceKey,
            file = cachedFile.file,
            cachedFile = cachedFile,
            profile = plan.metadataProfile.name,
        )
        return true''',
)

# A temporarily unavailable source remains durable in Room but is omitted from the active rich graph.
# The previous code streamed unavailable rows back into the graph, contradicting projection
# availability and allowing playback selectors to offer missing files.
replace(
    'musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt',
    'config.scanPlan?.let { it.reuseSourceKeys + it.unavailableSourceKeys }.orEmpty(),',
    'config.scanPlan?.reuseSourceKeys.orEmpty(),',
)
replace(
    'app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt',
    '''            if (plan != null && !plan.hasWork && synchronized(this) { library != null }) {''',
    '''            if (
                plan != null &&
                    !plan.hasWork &&
                    plan.unavailableSourceKeys.isEmpty() &&
                    synchronized(this) { library != null }
            ) {''',
)

# Deterministic root sampling independent of the device locale.
direct = 'musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt'
replace(direct, 'import java.security.MessageDigest\n', 'import java.security.MessageDigest\nimport java.util.Locale\n')
replace(direct, '.sortedBy { it.name.lowercase() }', '.sortedBy { it.name.lowercase(Locale.ROOT) }')

# Diagnostic output is never part of the production PR.
for generated in ('pr183-compile.log', 'pr183-comment.md'):
    Path(generated).unlink(missing_ok=True)

Path('scripts/pr2-correctness-pass.py').unlink()
Path('.github/workflows/pr2-correctness-pass.yml').unlink()

#!/usr/bin/env python3
from pathlib import Path

path = Path('app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt')
text = path.read_text()
text = text.replace(
    '    override suspend fun index(worker: IndexingWorker, withCache: Boolean) =\n        index(worker, withCache, metadataProfile = null)\n',
    '    override suspend fun index(worker: IndexingWorker, withCache: Boolean) =\n        indexWithProfile(worker, withCache, metadataProfile = null)\n',
)
text = text.replace(
    '    ) = index(worker, withCache, metadataProfile as MetadataProfile?)\n\n    private suspend fun index(\n',
    '    ) = indexWithProfile(worker, withCache, metadataProfile)\n\n    private suspend fun indexWithProfile(\n',
)
path.write_text(text)
Path('scripts/pr2-fix-signatures.py').unlink()
Path('.github/workflows/pr2-fix-signatures.yml').unlink()

#!/usr/bin/env python3
"""Apply the final scoped PR #203 source-loading hardening."""

from pathlib import Path
import re


def replace_exact(path: str, old: str, new: str) -> None:
    file = Path(path)
    text = file.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"STOP: expected one match in {path}, found {count}")
    file.write_text(text.replace(old, new), encoding="utf-8", newline="\n")
    print(f"Updated {path}")


def replace_regex(path: str, pattern: str, replacement: str) -> None:
    file = Path(path)
    text = file.read_text(encoding="utf-8")
    updated, count = re.subn(pattern, replacement, text, count=1, flags=re.DOTALL)
    if count != 1:
        raise SystemExit(f"STOP: expected one regex match in {path}, found {count}")
    file.write_text(updated, encoding="utf-8", newline="\n")
    print(f"Updated {path}")


def main() -> int:
    indexing = "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt"
    replace_exact(
        indexing,
        '''    private fun requestVisibleRecoveryScan(sourceAuthority: Boolean) {
        if (!sourceAuthority || musicSettings.lastScanFailed) return
        val needsImmediateScan =
            musicSettings.revision == null || musicSettings.libraryState != LibraryState.USABLE
        if (needsImmediateScan) {
            L.i(
                "Trusted visible startup is repairing the library " +
                    "[state=${musicSettings.libraryState} revision=${musicSettings.revision}]"
            )
            requestIndex(true)
            return
        }

        startupRecoveryJob?.cancel()
''',
        '''    private fun requestVisibleRecoveryScan(sourceAuthority: Boolean) {
        if (!sourceAuthority) return
        val needsImmediateScan =
            musicSettings.revision == null || musicSettings.libraryState != LibraryState.USABLE
        if (needsImmediateScan) {
            L.i(
                "Trusted visible startup is repairing the library " +
                    "[state=${musicSettings.libraryState} revision=${musicSettings.revision}]"
            )
            requestIndex(true)
            return
        }
        // A previous failure must not permanently strand an empty or unusable library,
        // but it should suppress delayed retry loops once a usable generation exists.
        if (musicSettings.lastScanFailed) return

        startupRecoveryJob?.cancel()
''',
    )

    locations = "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt"
    replace_exact(
        locations,
        '''                    // Update permission section
                    locationsPermsDesc.setText(R.string.set_grant_storage)
                    locationsPermsSubtitle.setText(R.string.lng_grant_storage_required)
                }
            }
''',
        '''                    // DirectFS checks actual app-process readability per selected
                    // path. Permission is required for internal shared storage, but an
                    // independently readable USB/prepared alias remains selectable.
                    locationsPermsDesc.setText(R.string.set_grant_storage_anyway)
                    locationsPermsSubtitle.setText(R.string.lng_grant_storage_anyway)
                }
            }
''',
    )
    replace_exact(
        locations,
        '''            // Only disable views in System Database and Direct FS mode when permission not granted
            // File Picker mode doesn't require storage permission
            val isEnabled = locationMode == LocationMode.SAF || hasStoragePermission
''',
        '''            // MediaStore requires framework storage permission. SAF uses URI grants,
            // while DirectFS validates the selected path in the app process.
            val isEnabled =
                LocationPermissionPolicy.isSourceUiEnabled(locationMode, hasStoragePermission)
''',
    )
    replace_exact(
        locations,
        '''        if (!hasStoragePermission && mode != LocationMode.SAF) {
            return ManualPathValidation.PERMISSION_MISSING
        }
''',
        '''        if (
            !hasStoragePermission &&
                LocationPermissionPolicy.requiresStoragePermission(mode, path)
        ) {
            return ManualPathValidation.PERMISSION_MISSING
        }
''',
    )
    replace_regex(
        locations,
        r'''    private fun updatePermissionCardColors\(binding: DialogMusicLocationsBinding\) \{.*?\n    \}\n\n    private fun updatePermissionCardVisibility''',
        '''    private fun updatePermissionCardColors(binding: DialogMusicLocationsBinding) {
        val context = requireContext()
        val useErrorColors = locationMode == LocationMode.MEDIA_STORE && !hasStoragePermission
        with(binding.locationsPermsCard) {
            if (useErrorColors) {
                setCardBackgroundColor(context.getAttrColorCompat(MR.attr.colorErrorContainer))
                binding.locationsPermsDesc.setTextColor(
                    context.getAttrColorCompat(MR.attr.colorOnErrorContainer)
                )
                binding.locationsPermsSubtitle.setTextColor(
                    context.getAttrColorCompat(MR.attr.colorOnErrorContainer)
                )
                binding.locationsPermsOpen.imageTintList =
                    context.getAttrColorCompat(MR.attr.colorOnErrorContainer)
            } else {
                setCardBackgroundColor(
                    context.getAttrColorCompat(MR.attr.colorSecondaryContainer)
                )
                binding.locationsPermsDesc.setTextColor(
                    context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
                )
                binding.locationsPermsSubtitle.setTextColor(
                    context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
                )
                binding.locationsPermsOpen.imageTintList =
                    context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
            }
        }
    }

    private fun updatePermissionCardVisibility''',
    )

    repository = "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt"
    replace_exact(
        repository,
        '''    @Volatile private var indexingWorker: IndexingWorker? = null
    private val deviceLibraryGeneration = AtomicLong(0L)
''',
        '''    @Volatile private var indexingWorker: IndexingWorker? = null
    private val pendingIndexRequests = RepositoryIndexRequestQueue()
    private val deviceLibraryGeneration = AtomicLong(0L)
''',
    )
    replace_exact(
        repository,
        '''    @Synchronized
    override fun registerWorker(worker: IndexingWorker) {
        if (indexingWorker != null) {
            L.w("Worker is already registered")
            return
        }
        L.d("Registering worker $worker")
        indexingWorker = worker
    }
''',
        '''    override fun registerWorker(worker: IndexingWorker) {
        val pending =
            synchronized(this) {
                if (indexingWorker != null) {
                    L.w("Worker is already registered")
                    return
                }
                L.d("Registering worker $worker")
                indexingWorker = worker
                pendingIndexRequests.drain()
            }
        pending?.also {
            L.i("Dispatching scan request queued before worker attachment [request=$it]")
            it.dispatch(worker)
        }
    }
''',
    )
    replace_exact(
        repository,
        '''    override fun requestIndex(withCache: Boolean) {
        indexingWorker?.requestIndex(withCache)
    }

    override fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {
        indexingWorker?.requestIndex(withCache, metadataProfile)
    }
''',
        '''    override fun requestIndex(withCache: Boolean) {
        dispatchOrQueue(RepositoryIndexRequest(withCache, metadataProfile = null))
    }

    override fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {
        dispatchOrQueue(RepositoryIndexRequest(withCache, metadataProfile))
    }

    private fun dispatchOrQueue(request: RepositoryIndexRequest) {
        val worker =
            synchronized(this) {
                indexingWorker
                    ?: run {
                        pendingIndexRequests.offer(request)
                        null
                    }
            }
        if (worker != null) {
            request.dispatch(worker)
        } else {
            L.i("Queued scan request until worker attachment [request=$request]")
        }
    }
''',
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

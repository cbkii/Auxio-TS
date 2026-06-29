# Storage, SAF, MediaStore, manual paths, and overlays

## Storage Access Framework

**Observed in external docs:** `ACTION_OPEN_DOCUMENT_TREE` lets a user pick a directory subtree from DocumentsProviders. The app receives access to the chosen directory and child files.

**Observed in external docs:** Traversing a large directory tree can reduce performance, and Android 11+ restricts access to certain directories through the picker.

**Observed in external docs:** `EXTRA_LOCAL_ONLY` requests local-file data only when supported by providers.

**Inferred for Auxio-TS:** PR #120's manual/direct path selector is compatible with TS18 evidence where DocumentsUI/SAF can be unreliable. Do not regress to mandatory SAF picker selection.

**Requires repo validation:** Ensure `LocationsDialog` failure modes distinguish picker unavailable, manual path inaccessible, unsupported third-party filter, and source temporarily removed.

## MediaStore filtering

**Observed in external docs:** Android's shared-storage docs warn about performance costs when enumerating large trees. MediaStore query restriction is therefore a reasonable performance direction, but the exact SQL selection is repo code.

**Inferred for Auxio-TS:** Filtering at the MediaStore query/SQL layer is preferable to scanning all rows and discarding later. Preserve PR #120's path-keyword filter behaviour unless tests show a false-negative issue.

## Manual path/source failures

**Inferred for Auxio-TS:** A missing USB disk or inaccessible direct path should be a failed scan/source state, not a successful empty library. Preserve cached library where possible.

**Requires TS18 validation:** `/storage/usbdiskN` and `/mnt/media_rw/usbdiskN` behaviour depends on the unit, port, mount timing, and Android/vendor storage services.

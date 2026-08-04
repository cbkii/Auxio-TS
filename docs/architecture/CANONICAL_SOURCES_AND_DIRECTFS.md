# Canonical music sources and DirectFS traversal

This document is the authority for how Auxio-TS identifies a configured music source and how
DirectFS enumerates it. It covers the two defects repaired together: duplicate source roots that
survived every boundary, and a DirectFS traversal whose completion could not be verified from
outside its workers.

## Duplicate-root evidence

**Confidence: Observed.** A physical TS18 diagnostic campaign ended with a scan that never
produced a library:

```text
locationMode=DIRECT_FS
generation=6
checkpoint=RUNNING
libraryState=NEVER
lastScanFailed=false
configuredSourceCount=2
source[0]=/storage/emulated/0/Music
source[1]=/storage/emulated/0/Music
```

Both entries had the same URI, the same canonical path and the same `SourceIdentity` key. The
duplicate could be produced from the picker because the candidate list offers already-configured
paths, and nothing between the picker and the backend compared sources by what would actually be
scanned:

| Boundary | Old behaviour |
| --- | --- |
| Picker candidates | Already-saved paths were offered again |
| `LocationAdapter.add/addAll` | Accepted anything not object-equal |
| `MusicSettingsImpl.stringify` | Serialised the list exactly as provided |
| `MusicSettingsImpl.configuredSourceCount` | Counted raw persisted entries |
| `ConfiguredSourceAwareFS` | Passed the raw list to the backend |
| `DirectFS` | Traversed and fingerprinted each duplicate root |
| Source-key filtering | Retained every duplicate sharing the selected key |

## Canonical identity rules

`musikr/src/main/java/org/oxycblt/musikr/fs/CanonicalSourcePolicy.kt` is the single definition.
`app/src/main/java/org/oxycblt/auxio/music/locations/MusicSourceCanonicalizer.kt` is the app-facing
facade; persistence, the picker, `ConfiguredSourcePolicy` and backend construction all use it
instead of object equality.

`normalizePath` produces the app-facing canonical path:

- separators are unified and collapsed, and trailing separators are trimmed;
- `/sdcard/...` normalises to `/storage/emulated/0/...`;
- `/mnt/media_rw/<usbdiskN|XXXX-XXXX>/...` normalises to `/storage/<volume>/...`, because that
  privileged backing mount is discovery evidence, not a playable app-facing root;
- any other `/mnt/media_rw/...` path is rejected;
- duplicated persisted prefixes such as `/storage/emulated/0/storage/emulated/0/Music` are repaired;
- `.` and `..` segments are rejected outright rather than resolved;
- `/`, `/system`, `/vendor`, `/data`, `/proc`, `/sys`, `/dev` and their descendants are rejected;
- volume tokens are case-folded (UUID volumes upper-case, `usbdiskN` lower-case) while the rest of
  the path stays case-sensitive, matching how Android storage actually behaves;
- anything that does not end up under `/storage/` is rejected.

Identity is then `path:<normalised path>` for file-backed sources and `uri:<trimmed uri>` for
provider-backed ones. `scopeOf` reports `WHOLE_VOLUME` for a volume root such as
`/storage/emulated/0` or `/storage/usbdisk0`, and `EXPLICIT` for anything the user picked inside a
volume.

`SourceIdentity.forLocation` is unchanged. It is a database primary key with volume granularity and
must keep that granularity; canonical deduplication uses the narrower
`SourceIdentity.canonicalKeyForLocation` instead.

## Where duplicates are collapsed

- the picker refuses a duplicate and reports it, and no longer offers a candidate that is already
  configured;
- `LocationAdapter.add` returns `false` for an equivalent source, and `addAll` collapses a restored
  list;
- `MusicSettingsImpl` collapses before comparing, before persisting and before counting;
- `ConfiguredSourceAwareFS` collapses defensively before constructing a backend;
- `DirectFS` collapses its configured roots and fingerprints each distinct canonical root once;
- `ConfiguredSourcePolicy.configurationRevision` keys on canonical identity, so `/sdcard/Music` and
  `/storage/emulated/0/Music` never look like a configuration change.

First-selected ordering is preserved everywhere.

## Migration behaviour

`MusicSettingsImpl.repairPersistedSourceDuplicates` is a read-repair invoked from `safQuery`,
`configuredSourceSpecs` and the configured-source count. It rewrites the persisted list only when
collapsing actually shortens it, and it deliberately does **not** touch the source-configuration
generation: dropping an exact canonical duplicate cannot change the effective scan scope, so it must
not queue another full rescan or invalidate the cached library that Auxio-TS starts from. The repair
is idempotent — a second read finds nothing to change.

## Ancestor and descendant overlap

Exact duplicates are collapsed automatically. Overlap is handled without silently discarding a
deliberate choice:

- a whole-volume candidate that is only a fallback suggestion is suppressed in the picker when a
  narrower explicit source already exists on that volume;
- an explicitly selected overlapping root is kept, and the picker warns that the wider root already
  covers the narrower one;
- `CanonicalSourcePolicy.traversalOrder` puts narrow explicit roots before whole-volume roots and
  deeper roots before shallower ones;
- the traversal's shared canonical visited set then suppresses the overlapping subtree, so
  `/storage/emulated/0` plus `/storage/emulated/0/Music` enumerates each directory exactly once and
  reports the suppression in `duplicateDirectoriesSuppressed`.

A narrower source is never folded into a wider one, because the two scopes do not scan the same
thing: a whole-volume root keeps the stronger noise exclusions described below, so collapsing an
explicit `Music` folder into a whole-volume ancestor could silently shrink the effective scope.

## DirectFS completion model

### Old model

- a `LinkedBlockingQueue` of pending directories;
- three directory workers polling that queue;
- `AtomicInteger` pending and discovered counters;
- termination inferred by each worker when seeding was complete and pending reached zero.

A thread captured on the physical TS18 was parked in that poll loop while no extraction or
evaluation progress existed. Nothing outside the workers could observe or assert completion.

There was a second, structural defect in the same area. `FS.explore()` wrapped its producer in
`coroutineScope { … }`, which waits for its children, so `explore()` only returned after the whole
tree had been enumerated. With Auxio-TS's bounded pipeline channel the producer blocked on
back-pressure before the consumer had been started, and the scan could never finish. `explore()`
now returns immediately through `startOwning`, which keeps structured concurrency and the
channel-ownership contract while letting the bounded channel apply real back-pressure.

### New model

`DirectFsTraversal` is one coordinator:

- one explicit `ArrayDeque` work queue per configured source, owned by the coordinator;
- one directory enumerator at a time, which is what slow head-unit storage wants: throughput comes
  from the downstream classification and extraction stages that consume the file channel;
- completion is structural — a source is finished when its queue is empty, and the traversal is
  finished when the last source has been drained. No polling, and no counter that cannot be
  verified from outside;
- cancellation checkpoints between directories and every bounded group of entries;
- a canonical visited set shared by every source, which stops alias loops, bind-mount duplication
  and overlapping-root duplication;
- symbolic links that resolve outside the selected canonical root are never followed;
- blocking filesystem work stays on `Dispatchers.IO`, and the file channel is closed, closed with
  the causal exception, or cancelled by exactly one owner.

Each configured source produces exactly one outcome: `COMPLETED`, `COMPLETED_EMPTY`,
`TEMPORARILY_UNAVAILABLE`, `PERMISSION_REQUIRED`, `TRUNCATED`, `CANCELLED` or `FAILED`. `DirectFS`
maps those onto the shared `TYPE|detail` source-failure protocol, so a truncated or unavailable
source is reported instead of silently producing a short library.

### Metrics

`DirectFsTraversalMetrics` is deterministic and comparable across runs: directories visited, files
emitted, duplicate directories suppressed, peak queued directories, active enumerators, elapsed
time, per-source results and a bounded list of slow operations (path, operation, elapsed time,
source key, queued and active counts). Slow operations are recorded, never traced per file.

## Directory exclusion policy

The policy knows the scope of the source:

- **Explicit** — a folder the user selected. Ordinary child directories are scanned even when they
  have generic names such as `Download` or `Movies`, because inside `/storage/emulated/0/My Audio`
  those are content, not platform trees. Symbolic-link escapes, protected paths, genuinely
  unreadable platform-restricted children and hidden directories are still skipped.
- **Whole volume** — an accidental or automatically suggested volume root. The stronger name
  exclusions (`Android`, `Download`, `DCIM`, `Pictures`, `Movies`, known head-unit noise
  directories) and a tighter directory budget apply.

**Known limitation.** Hidden directories are skipped in DirectFS regardless of the "with hidden"
preference, which is only plumbed into the SAF backend. This is unchanged by this work and is
recorded here so it is not mistaken for a regression.

## App-UID and root authority boundary

Unchanged and enforced by `DirectFsRootPolicy` and `CanonicalSourcePolicy`:

- ordinary app-UID access is tried and required for both scanning and playback;
- root may only be used for bounded discovery or to prepare a safe app-facing alias;
- root-originated metadata never becomes the sole authority for a file the app cannot open;
- the scan never enumerates arbitrary protected storage as root, and no root shell command is run
  per file or directory;
- `/data`, `/system`, `/vendor`, `/proc`, `/sys`, `/dev` and related roots stay prohibited;
- a privileged `/mnt/media_rw/...` backing path is accepted only as an alias that is rewritten onto
  the app-facing `/storage/<volume>/...` path.

"Root-assisted DirectFS" therefore means discovery or alias preparation, not unrestricted root
playback.

## Tests

- `musikr/src/test/java/org/oxycblt/musikr/fs/CanonicalSourcePolicyTest.kt` — aliases, backing-volume
  mapping, case behaviour, duplicated prefixes, rejections, scopes, ancestry, collapse order and
  traversal order.
- `musikr/src/test/java/org/oxycblt/musikr/fs/direct/DirectFsTraversalTest.kt` — executable
  traversals over real temporary trees, every one with a bounded timeout: small explicit folder,
  empty folder, nested folders, exact duplicate roots, trailing-slash aliases, whole volume plus a
  nested source, explicit-versus-whole-volume exclusions, symlink escape, alias directories
  resolving onto one canonical path, unreadable child, unavailable root, a root disappearing during
  traversal, cancellation, downstream back-pressure, directory and file safety limits, depth
  truncation, one completion per source, an exception in one enumeration, protected canonical
  children, and no coordinator state left behind.
- `musikr/src/test/java/org/oxycblt/musikr/fs/direct/DirectFsRootPolicyTest.kt` — root allow-list and
  per-scope descent policy.
- `app/src/test/java/org/oxycblt/auxio/music/MusicSourceCanonicalizationTest.kt` — duplicates are
  never persisted, aliases are one source, re-selecting is not a configuration change, persisted
  duplicates are migrated once without a new generation, distinct sources keep selection order,
  source-key selection cannot re-expand, the configuration revision is stable across aliases, the
  picker refuses duplicates, and overlap is recognised without silent removal.
- `musikr/src/androidTest/java/org/oxycblt/musikr/fs/direct/DirectFSInstrumentedTest.kt` —
  deterministic on-device completion and metrics under rendezvous back-pressure.

## Remaining physical TS18 requirements

**Confidence: Requires TS18 validation.** CI cannot prove head-unit behaviour. On the physical unit,
compare the old three-worker traversal with the new conservative coordinator over a large internal
`Music` folder and a removable USB volume, and record directories visited, files emitted, duplicate
directories suppressed, peak queued tasks and completion time. Confirm that a previously duplicated
configuration migrates to a single source without a new generation and without a rescan, and that
`checkpoint` reaches `COMMITTED` with a non-empty library.

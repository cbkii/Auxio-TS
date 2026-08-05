# Source-scan attempt leases

Auxio-TS persists a source **configuration generation** separately from each scan **attempt**. A
generation identifies one canonical source configuration. An attempt identifies one bounded owner
trying to publish that generation.

This model prevents a `RUNNING` checkpoint from surviving process death without an identifiable
owner and prevents stale work from publishing or terminalising a newer attempt.

## Durable record

`SourceConfigurationCheckpoint` stores:

- the configuration generation and checkpoint state;
- the current attempt ID and process/lifecycle owner token;
- claimed, last-heartbeat and terminal timestamps;
- bounded phase, count and current-item progress;
- one terminal outcome, reason and bounded failure detail;
- unresolved canonical source keys; and
- the immediately preceding attempt as recovery evidence.

SharedPreferences remains the Android 10-compatible durable store. State-changing claims,
handoffs and completions use one process-wide lock and synchronous preference commit. Progress
heartbeats use asynchronous persistence and never change ownership.

Successful source publication follows one explicit lock order: the source-checkpoint lock before
the repository monitor. The in-memory library is published while the exact generation/attempt
commit still excludes replacement claims; repository code must not invoke a source-checkpoint
operation while holding its monitor.

## Legal transitions

| From | Event | To |
| --- | --- | --- |
| no checkpoint / committed generation | source configuration changes | `PENDING` (new generation) |
| `PENDING` | exact generation claim | `RUNNING` (new attempt ID) |
| stale `RUNNING` | process or service lifecycle recovery | `INTERRUPTED` |
| `RUNNING` | explicit same-process owner handoff | `RUNNING` (same attempt, new lifecycle owner) |
| `RUNNING` | complete readable full/empty publication | `COMMITTED` |
| `RUNNING` | complete readable partial publication | `PARTIALLY_COMMITTED` |
| `RUNNING` | temporary/provider/fatal retryable failure | `FAILED_RETRYABLE` |
| `RUNNING` | non-retryable fatal failure | `FAILED_FINAL` |
| `RUNNING` | user cancellation | `CANCELLED` |
| `RUNNING` | no-progress or overall deadline | `TIMED_OUT` |
| `RUNNING` | service stop, process interruption or replacement | `INTERRUPTED` |
| retryable terminal state | explicit retry/recovery claim | `RUNNING` (new attempt ID) |

No terminal attempt returns to `RUNNING`. A successful publication must match generation, attempt
ID and owner. Completion is rejected when any field is stale or the attempt already has a terminal
outcome.

## Lifecycle and replacement ownership

The indexing holder creates a process token and a lifecycle token. Repository indexing begins only
after the holder atomically claims the pending generation and passes both tokens through
`IndexRequest`.

On process recreation, the previous process token cannot still be live, so its `RUNNING` attempt is
recorded as `PROCESS_INTERRUPTED` before recovery allocates another attempt. Service recreation in
the same process uses the lifecycle token: orderly teardown records `SERVICE_STOPPED`, while an
explicit handoff may transfer the same attempt between lifecycle owners without inventing a new
owner.

A newer configuration first records the old attempt as `SUPERSEDED`, then cancels its coroutine and
waits for structured `finally` cleanup. Only that cleanup may start the replacement. Independent
job/session gates reject an old `finally`, progress callback or terminal callback after newer work
starts.

Metadata enrichment has no source-attempt authority even when it carries a committed generation for
cache identity. Generated-playlist refresh does not use the indexing request path. Neither can claim,
reopen or commit an initial source checkpoint.

## Stage-aware watchdog

The watchdog uses monotonic elapsed time. Meaningful phase, item or count progress resets the
no-progress baseline; repeated identical callbacks do not. The overall 30-minute cap remains the
last-resort bound.

| Condition | Deadline |
| --- | ---: |
| warning without progress | 60 seconds |
| preparing or narrow-folder discovery before first file | 3 minutes |
| unknown-scope discovery before first file | 4 minutes |
| whole-volume/mixed discovery before first file | 5 minutes |
| discovery after first file, extraction or evaluation | 5 minutes |
| final publication stage | 4 minutes |
| total elapsed safety cap | 30 minutes |

These values are conservative policy bounds, not claims about universal device or storage speed.
Narrow explicit folders get a shorter zero-file bound; whole-volume fallback gets more time. Any
continuing meaningful progress keeps a scan alive until the independent overall cap. DirectFS queue
and active-enumerator counts are included when a backend exposes them; absence is represented as
unknown rather than zero.

One watchdog termination produces one `TIMED_OUT` result with phase/count/no-progress detail. The
attempt remains retryable, unresolved sources remain recorded, and the prior committed library is
not replaced.

## Terminal consistency

| Result | Checkpoint | Library compatibility state | `lastScanFailed` | Publication |
| --- | --- | --- | --- | --- |
| non-empty success | `COMMITTED` | `USABLE` | `false` | new revision/library |
| authoritative empty | `COMMITTED` | `EMPTY` | `false` | new revision/empty library |
| readable partial | `PARTIALLY_COMMITTED` | `USABLE` or `EMPTY` | `true` | readable partial + unresolved keys |
| temporary unavailable | `FAILED_RETRYABLE` | previous value | `true` | previous library retained |
| fatal retryable failure | `FAILED_RETRYABLE` | previous value | `true` | previous library retained |
| user cancellation | `CANCELLED` | previous value | `false` | previous library retained |
| service/process interruption | `INTERRUPTED` | previous value | `false` | previous library retained |
| watchdog timeout | `TIMED_OUT` | previous value | `true` | previous library retained |

Exported diagnostics include generation, attempt/owner identity, timestamps, phase/counts,
no-progress duration, replacement status, terminal outcome and bounded unresolved-source evidence.
The UI keeps one source-status card and presents terminal retry actions without promoting raw IDs.

**Confidence: Requires TS18 validation.** Physical evidence is still required for process kill,
service recreation, USB removal/remount, slow whole-volume DirectFS traversal and ACC sleep/wake
behaviour. **Porting decision: pending device evidence; retain the lifecycle design unchanged until
that campaign is complete.**

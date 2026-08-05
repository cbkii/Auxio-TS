/*
 * Copyright (c) 2026 Auxio Project
 * SourceConfigurationCheckpoint.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.music

import java.util.UUID

/** Identifies one process and one service/indexer lifecycle inside that process. */
data class SourceScanAttemptOwner(val processId: String, val lifecycleId: String) {
    init {
        require(processId.isNotBlank())
        require(lifecycleId.isNotBlank())
    }
}

/** One bounded durable progress snapshot for process-death and watchdog diagnostics. */
data class SourceScanAttemptProgress(
    val phase: String,
    val explored: Int = 0,
    val loaded: Int = 0,
    val evaluated: Int = 0,
    val currentItem: String? = null,
    val directFsDirectoriesVisited: Int? = null,
    val directFsEntriesInspected: Int? = null,
    val directFsFilesEmitted: Int? = null,
    val queuedDirectFsWork: Int? = null,
    val activeDirectFsEnumerators: Int? = null,
)

/** Exactly one terminal result may be assigned to an attempt. */
enum class SourceScanAttemptOutcome {
    SUCCESS,
    AUTHORITATIVE_EMPTY,
    PARTIAL_SUCCESS,
    TRUNCATED,
    TEMPORARILY_UNAVAILABLE,
    PERMISSION_REQUIRED,
    CANCELLED,
    SERVICE_STOPPED,
    PROCESS_INTERRUPTED,
    SUPERSEDED,
    TIMED_OUT,
    FAILED_RETRYABLE,
    FAILED_FINAL,
}

/** Durable evidence for one source-scan attempt. */
data class SourceScanAttemptRecord(
    val generation: Long,
    val attemptId: String,
    val owner: SourceScanAttemptOwner,
    val claimedAtMs: Long,
    val heartbeatAtMs: Long,
    val progress: SourceScanAttemptProgress? = null,
    val terminalAtMs: Long? = null,
    val terminalOutcome: SourceScanAttemptOutcome? = null,
    val reason: String? = null,
    val failureClass: String? = null,
    val failureMessage: String? = null,
) {
    val isTerminal: Boolean
        get() = terminalOutcome != null
}

/** Why a caller is allowed to allocate a replacement attempt for the same generation. */
enum class SourceScanClaimReason {
    CONFIGURATION_CHANGE,
    STARTUP_RECOVERY,
    USER_RETRY,
}

/** Terminal values persisted atomically with checkpoint/library compatibility state. */
data class SourceScanAttemptCompletion(
    val outcome: SourceScanAttemptOutcome,
    val unresolvedSourceKeys: Set<String> = emptySet(),
    val reason: String,
    val failureClass: String? = null,
    val failureMessage: String? = null,
    val publishedRevision: UUID? = null,
    val publishedLibraryState: LibraryState? = null,
    val lastScanFailed: Boolean,
)

data class SourceConfigurationCheckpoint(
    val generation: Long,
    val state: State,
    val unresolvedSourceKeys: Set<String> = emptySet(),
    val attempt: SourceScanAttemptRecord? = null,
    val previousAttempt: SourceScanAttemptRecord? = null,
    val reason: String? = null,
) {
    val attemptId: String?
        get() = attempt?.attemptId

    val lastAttemptAtMs: Long?
        get() = attempt?.claimedAtMs ?: previousAttempt?.claimedAtMs

    val lastHeartbeatAtMs: Long?
        get() = attempt?.heartbeatAtMs ?: previousAttempt?.heartbeatAtMs

    val terminalAtMs: Long?
        get() = attempt?.terminalAtMs ?: previousAttempt?.terminalAtMs

    val terminalOutcome: SourceScanAttemptOutcome?
        get() = attempt?.terminalOutcome ?: previousAttempt?.terminalOutcome

    val lastOutcome: String?
        get() = reason ?: terminalOutcome?.name

    /** Bounded export form that distinguishes attempts without dumping unbounded source data. */
    fun diagnosticSummary(nowMs: Long = System.currentTimeMillis()): String {
        val record = attempt ?: previousAttempt
        val progress = record?.progress
        val unresolved =
            unresolvedSourceKeys
                .asSequence()
                .take(MAX_DIAGNOSTIC_SOURCE_KEYS)
                .map { it.take(MAX_DIAGNOSTIC_SOURCE_KEY_LENGTH) }
                .toList()
        val omitted = (unresolvedSourceKeys.size - unresolved.size).coerceAtLeast(0)
        val noProgressMs =
            record
                ?.heartbeatAtMs
                ?.takeIf { state == State.RUNNING && nowMs > it }
                ?.let { nowMs - it } ?: 0L
        return buildString {
            append("generation=").append(generation)
            append(";state=").append(state)
            append(";reason=").append(reason?.take(MAX_DIAGNOSTIC_REASON_LENGTH))
            append(";attemptId=").append(record?.attemptId)
            append(";attemptGeneration=").append(record?.generation)
            append(";attemptSlot=").append(if (attempt != null) "current" else "previous")
            append(";ownerProcess=").append(record?.owner?.processId)
            append(";ownerLifecycle=").append(record?.owner?.lifecycleId)
            append(";claimedAtMs=").append(record?.claimedAtMs)
            append(";heartbeatAtMs=").append(record?.heartbeatAtMs)
            append(";phase=").append(progress?.phase)
            append(";explored=").append(progress?.explored ?: 0)
            append(";loaded=").append(progress?.loaded ?: 0)
            append(";evaluated=").append(progress?.evaluated ?: 0)
            append(";currentItem=")
                .append(progress?.currentItem?.take(MAX_DIAGNOSTIC_REASON_LENGTH))
            append(";directFsDirectories=").append(progress?.directFsDirectoriesVisited)
            append(";directFsEntries=").append(progress?.directFsEntriesInspected)
            append(";directFsFiles=").append(progress?.directFsFilesEmitted)
            append(";directFsQueued=").append(progress?.queuedDirectFsWork)
            append(";directFsActive=").append(progress?.activeDirectFsEnumerators)
            append(";noProgressMs=").append(noProgressMs)
            append(";terminalAtMs=").append(record?.terminalAtMs)
            append(";terminalOutcome=").append(record?.terminalOutcome)
            append(";failureClass=")
                .append(record?.failureClass?.take(MAX_DIAGNOSTIC_REASON_LENGTH))
            append(";failureMessage=")
                .append(record?.failureMessage?.take(MAX_DIAGNOSTIC_REASON_LENGTH))
            append(";unresolved=").append(unresolved)
            if (omitted > 0) append(";unresolvedOmitted=").append(omitted)
        }
    }

    fun canClaim(reason: SourceScanClaimReason): Boolean =
        when (state) {
            State.PENDING -> true
            State.INTERRUPTED ->
                reason == SourceScanClaimReason.STARTUP_RECOVERY ||
                    reason == SourceScanClaimReason.USER_RETRY
            State.PARTIALLY_COMMITTED,
            State.FAILED_RETRYABLE,
            State.CANCELLED,
            State.TIMED_OUT -> reason == SourceScanClaimReason.USER_RETRY
            State.RUNNING,
            State.COMMITTED,
            State.FAILED_FINAL -> false
        }

    enum class State {
        PENDING,
        RUNNING,
        PARTIALLY_COMMITTED,
        COMMITTED,
        INTERRUPTED,
        FAILED_RETRYABLE,
        FAILED_FINAL,
        CANCELLED,
        TIMED_OUT,
    }

    private companion object {
        const val MAX_DIAGNOSTIC_SOURCE_KEYS = 8
        const val MAX_DIAGNOSTIC_SOURCE_KEY_LENGTH = 160
        const val MAX_DIAGNOSTIC_REASON_LENGTH = 240
    }
}

/** One random identifier shared by every settings/indexer instance in this OS process. */
internal object SourceScanProcessIdentity {
    val processId: String = UUID.randomUUID().toString()
}

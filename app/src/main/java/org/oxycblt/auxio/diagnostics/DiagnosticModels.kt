/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticModels.kt is part of Auxio.
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

package org.oxycblt.auxio.diagnostics

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** Evidence classification for a diagnostic finding or event. */
@Parcelize
enum class EvidenceClassification : Parcelable {
    OBSERVED_BY_AUXIO,
    USER_CONFIRMED,
    INFERRED_FROM_PUBLIC_ANDROID_STATE,
    NOT_APPLICABLE,
    NOT_VISIBLE_TO_THIS_APP,
    PERMISSION_DENIED,
    API_UNAVAILABLE,
    QUERY_FAILED,
    NO_EVENT_OBSERVED,
    HYPOTHESIS,
    REQUIRES_EXTERNAL_TS18_VALIDATION,
    UNAVAILABLE_FROM_NORMAL_APP_CONTEXT;

    override fun toString(): String =
        when (this) {
            OBSERVED_BY_AUXIO -> "Observed by Auxio"
            USER_CONFIRMED -> "User confirmed"
            INFERRED_FROM_PUBLIC_ANDROID_STATE -> "Inferred from public Android state"
            NOT_APPLICABLE -> "Not applicable"
            NOT_VISIBLE_TO_THIS_APP -> "Not visible to this app"
            PERMISSION_DENIED -> "Permission denied"
            API_UNAVAILABLE -> "API unavailable"
            QUERY_FAILED -> "Query failed"
            NO_EVENT_OBSERVED -> "No event observed"
            HYPOTHESIS -> "Hypothesis"
            REQUIRES_EXTERNAL_TS18_VALIDATION -> "Requires external TS18 validation"
            UNAVAILABLE_FROM_NORMAL_APP_CONTEXT -> "Unavailable from normal app context"
        }
}

/** A single entry in the automated diagnostic report. */
@Parcelize
data class DiagnosticEntry(
    val name: String,
    val value: String,
    val evidence: EvidenceClassification,
    val primaryMethod: String? = null,
    val fallbackMethod: String? = null,
    val detail: String? = null,
    val confidence: Float = 1.0f,
    val error: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
) : Parcelable

/** A single event in the diagnostics journal. */
@Parcelize
data class DiagnosticEvent(
    val wallTime: Long = System.currentTimeMillis(),
    val monotonicTime: Long = android.os.SystemClock.elapsedRealtime(),
    val sessionId: String? = null,
    val category: String,
    val event: String,
    val detail: String? = null,
    val result: String? = null,
    val evidence: EvidenceClassification = EvidenceClassification.OBSERVED_BY_AUXIO,
) : Parcelable

/** Status categories for diagnostic checks. */
enum class DiagnosticStatus {
    HEALTHY,
    WARNING,
    CRITICAL,
    UNKNOWN,
    NOT_APPLICABLE,
}

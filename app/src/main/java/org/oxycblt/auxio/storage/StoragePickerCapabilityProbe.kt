/*
 * Copyright (c) 2026 Auxio Project
 * StoragePickerCapabilityProbe.kt is part of Auxio.
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

package org.oxycblt.auxio.storage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import org.oxycblt.auxio.music.TopwaySourcePolicy
import timber.log.Timber as L

/**
 * Diagnostic helper to probe storage picker capabilities on the device.
 */
object StoragePickerCapabilityProbe {

    data class CapabilityReport(
        val actionOpenDocumentTreeResolves: Boolean,
        val actionOpenDocumentResolves: Boolean,
        val actionGetContentResolves: Boolean,
        val resolvingActivities: List<String>,
        val ts18CandidatesAccessible: Map<String, Boolean>,
    )

    fun probe(context: Context): CapabilityReport {
        val pm = context.packageManager

        val treeIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        val treeResolves = pm.queryIntentActivities(treeIntent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()

        val openIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
        }
        val openResolves = pm.queryIntentActivities(openIntent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()

        val getContentIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
        }
        val getContentResolves = pm.queryIntentActivities(getContentIntent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()

        val activities = pm.queryIntentActivities(treeIntent, PackageManager.MATCH_DEFAULT_ONLY)
            .map { it.activityInfo.name }

        val ts18Candidates = TopwaySourcePolicy.CANDIDATE_ROOTS.associateWith {
            TopwaySourcePolicy.isAccessibleCandidate(it)
        }

        val report =
            CapabilityReport(
                actionOpenDocumentTreeResolves = treeResolves,
                actionOpenDocumentResolves = openResolves,
                actionGetContentResolves = getContentResolves,
                resolvingActivities = activities,
                ts18CandidatesAccessible = ts18Candidates,
            )

        L.i("Storage Picker Capability Report: $report")
        return report
    }
}

/*
 * Copyright (c) 2026 Auxio Project
 * DofunIntegrationClassifier.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.root.dofun

/** Installed-package topology is evidence about availability, never proof of DoFun selection. */
data class DofunPackageTopology(
    val releaseAuxioPresent: Boolean,
    val debugAuxioPresent: Boolean,
    val stockMusicPresent: Boolean,
    val dofunPresent: Boolean,
)

/** Fixed DoFun music target proven by an observed launcher-owned selection surface. */
enum class DofunSelectedMusicTarget {
    COM_TW_MEDIA,
    COM_TW_MUSIC,
    OTHER,
    UNKNOWN,
}

data class DofunSelectionEvidence(
    val target: DofunSelectedMusicTarget,
    val evidence: String?,
    val source: String,
)

/** Pure, fail-closed classification helpers used by runtime diagnostics and JVM tests. */
object DofunIntegrationClassifier {
    private const val AUXIO_RELEASE_PACKAGE = "com.tw.media"
    private const val AUXIO_DEBUG_PACKAGE = "com.tw.media.debug"
    private const val STOCK_MUSIC_PACKAGE = "com.tw.music"
    private const val DOFUN_PACKAGE = "com.dofun.variety"

    fun topology(installedPackages: Collection<String>): DofunPackageTopology =
        DofunPackageTopology(
            releaseAuxioPresent = AUXIO_RELEASE_PACKAGE in installedPackages,
            debugAuxioPresent = AUXIO_DEBUG_PACKAGE in installedPackages,
            stockMusicPresent = STOCK_MUSIC_PACKAGE in installedPackages,
            dofunPresent = DOFUN_PACKAGE in installedPackages,
        )

    /**
     * Parse only explicit package/component values from DoFun's exported music-selection provider.
     * Package presence elsewhere on the device must never be promoted into launcher-selection
     * proof.
     */
    fun selectedMusicTarget(providerOutput: String?): DofunSelectedMusicTarget {
        val output = providerOutput?.trim().orEmpty()
        if (output.isBlank()) return DofunSelectedMusicTarget.UNKNOWN
        val lower = output.lowercase()
        if (
            "no result" in lower ||
                "0 rows" in lower ||
                "permission denial" in lower ||
                "securityexception" in lower ||
                "unknown uri" in lower
        ) {
            return DofunSelectedMusicTarget.UNKNOWN
        }

        val explicitValueRegex =
            Regex(
                "(?:component|package|pkg|value|hotseat_app_music)\\s*=\\s*([^,}\\s]+)",
                RegexOption.IGNORE_CASE,
            )
        val explicitValues = explicitValueRegex.findAll(output).map { it.groupValues[1] }.toList()
        val componentValues =
            Regex("[A-Za-z0-9_]+(?:\\.[A-Za-z0-9_]+)+/[A-Za-z0-9_.$]+")
                .findAll(output)
                .map { it.value }
                .toList()
        val candidatePackages =
            (explicitValues + componentValues)
                .map(::candidatePackage)
                .filter { it.isNotBlank() }
                .distinct()
        if (candidatePackages.size != 1) return DofunSelectedMusicTarget.UNKNOWN

        return when (candidatePackages.single()) {
            AUXIO_RELEASE_PACKAGE -> DofunSelectedMusicTarget.COM_TW_MEDIA
            STOCK_MUSIC_PACKAGE -> DofunSelectedMusicTarget.COM_TW_MUSIC
            else -> DofunSelectedMusicTarget.OTHER
        }
    }

    /**
     * Only evidence obtained through the exported provider under Auxio's real app UID may establish
     * launcher selection. A root-shell read is retained for observation but never upgrades
     * [DofunSelectedMusicTarget.UNKNOWN] into a claimed app-authority result.
     */
    fun authoritativeSelection(
        appProviderOutput: String?,
        rootProviderOutput: String?,
    ): DofunSelectionEvidence {
        val appTarget = selectedMusicTarget(appProviderOutput)
        return when {
            appTarget != DofunSelectedMusicTarget.UNKNOWN ->
                DofunSelectionEvidence(appTarget, appProviderOutput, SELECTION_SOURCE_APP_UID)
            appProviderOutput != null ->
                DofunSelectionEvidence(
                    DofunSelectedMusicTarget.UNKNOWN,
                    appProviderOutput,
                    SELECTION_SOURCE_APP_UID,
                )
            rootProviderOutput != null ->
                DofunSelectionEvidence(
                    DofunSelectedMusicTarget.UNKNOWN,
                    rootProviderOutput,
                    SELECTION_SOURCE_ROOT_OBSERVATION,
                )
            else ->
                DofunSelectionEvidence(
                    DofunSelectedMusicTarget.UNKNOWN,
                    null,
                    SELECTION_SOURCE_NONE,
                )
        }
    }

    fun recommendation(
        topology: DofunPackageTopology,
        selectedTarget: DofunSelectedMusicTarget,
    ): String =
        when {
            selectedTarget == DofunSelectedMusicTarget.COM_TW_MUSIC ->
                "DoFun selection evidence points to stock com.tw.music. Preserve stock and use the guarded reversible selection experiment only after a saved baseline."
            selectedTarget == DofunSelectedMusicTarget.COM_TW_MEDIA &&
                !topology.releaseAuxioPresent ->
                "DoFun selection evidence points to com.tw.media but the release package is not currently installed. Treat the selection as potentially stale until the exact release build is installed and re-read."
            selectedTarget == DofunSelectedMusicTarget.COM_TW_MEDIA ->
                "DoFun selection evidence points to com.tw.media. Validate notification, MediaSession and one-command ingress paths."
            selectedTarget == DofunSelectedMusicTarget.OTHER ->
                "DoFun selection evidence points to another target. Capture the exact provider value before changing launcher state."
            topology.debugAuxioPresent && !topology.releaseAuxioPresent ->
                "Install the exact com.tw.media release build before fixed-identity validation; the debug suffix is not a DoFun fixed match."
            !topology.releaseAuxioPresent ->
                "Install the signed com.tw.media release APK. Package topology does not establish launcher selection."
            else ->
                "DoFun selection was not found in the inspected scope. Verify the playback channel/session first, then collect current launcher selection evidence."
        }

    private fun candidatePackage(value: String): String = value.substringBefore('/').trim()

    private const val SELECTION_SOURCE_APP_UID = "APP_UID_EXPORTED_PROVIDER"
    private const val SELECTION_SOURCE_ROOT_OBSERVATION = "ROOT_OBSERVATION_ONLY"
    private const val SELECTION_SOURCE_NONE = "NONE"
}

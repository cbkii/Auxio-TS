/*
 * Copyright (c) 2026 Auxio-TS Project
 */

package org.oxycblt.auxio.headunit.ts18

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Ts18RawFastResumePolicyTest {
    @Test
    fun usbdisk0AndUsbdisk1PathsAreAllowed() {
        assertTrue(RawFastResumeValidator.isAllowedDirectPath("/storage/usbdisk0/Music/a.mp3"))
        assertTrue(RawFastResumeValidator.isAllowedDirectPath("/storage/usbdisk1/Music/a.flac"))
    }

    @Test
    fun unsafePathsAreRejected() {
        assertFalse(RawFastResumeValidator.isAllowedDirectPath("/mnt/media_rw/usbdisk0/Music/a.mp3"))
        assertFalse(RawFastResumeValidator.isAllowedDirectPath("/data/local/tmp/a.mp3"))
        assertFalse(RawFastResumeValidator.isAllowedDirectPath("/storage/usbdisk0/../secret/a.mp3"))
    }

    @Test
    fun audioExtensionPolicyIsConservative() {
        assertTrue(RawFastResumeValidator.hasAudioExtension("/storage/usbdisk0/Music/a.mp3"))
        assertTrue(RawFastResumeValidator.hasAudioExtension("/storage/usbdisk1/Music/a.FLAC"))
        assertFalse(RawFastResumeValidator.hasAudioExtension("/storage/usbdisk1/Music/a.txt"))
    }

    @Test
    fun sourcePolicySummarisesMixedVolumeState() {
        val states =
            listOf(
                Ts18SourceRepairStatePolicy.SourceState(
                    "/storage/usbdisk0",
                    Ts18SourceRepairStatePolicy.Kind.ALL_SOURCES_READY,
                    "ready",
                    Ts18SourceRepairStatePolicy.Action.NONE,
                ),
                Ts18SourceRepairStatePolicy.SourceState(
                    "/storage/usbdisk1",
                    Ts18SourceRepairStatePolicy.Kind.MOUNT_MISSING,
                    "missing",
                    Ts18SourceRepairStatePolicy.Action.REINSERT_USB,
                ),
            )
        assertTrue(
            Ts18SourceRepairStatePolicy.summarise(states) ==
                Ts18SourceRepairStatePolicy.Kind.MIXED_MULTIPLE_VOLUME_STATE
        )
    }
}

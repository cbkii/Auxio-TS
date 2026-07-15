package org.oxycblt.auxio.music

import org.junit.Assert.assertEquals
import org.junit.Test

class StartupReadinessControllerTest {
    @Test
    fun `library status changes do not regress capabilities`() {
        val controller = StartupReadinessController()
        controller.publishCapability(StartupReadinessState.FullLibraryReady)
        controller.publishLibraryStatus(StartupLibraryStatus.SourceUnavailable)
        controller.publishLibraryStatus(StartupLibraryStatus.Usable)

        assertEquals(StartupReadinessState.FullLibraryReady, controller.capability)
        assertEquals(StartupLibraryStatus.Usable, controller.state.libraryStatus)
    }

    @Test
    fun `capability milestones are not duplicated`() {
        val controller = StartupReadinessController()
        var notifications = 0
        controller.addListener { notifications++ }

        controller.publishCapability(StartupReadinessState.FastBrowseReady)
        controller.publishCapability(StartupReadinessState.QueueReady)
        controller.publishCapability(StartupReadinessState.FastBrowseReady)

        assertEquals(2, notifications)
        assertEquals(StartupReadinessState.FastBrowseReady, controller.capability)
    }
}

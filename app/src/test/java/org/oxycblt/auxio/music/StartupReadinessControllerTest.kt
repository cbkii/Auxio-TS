package org.oxycblt.auxio.music

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StartupReadinessControllerTest {
    @Test
    fun `initial state is process visible only`() {
        val controller = StartupReadinessController()

        assertEquals(StartupReadinessState.ProcessVisible, controller.capability)
        assertEquals(setOf(StartupCapability.PROCESS_VISIBLE), controller.state.achieved)
        assertEquals(StartupLibraryStatus.Unknown, controller.state.libraryStatus)
    }

    @Test
    fun `out of order publication records capability without implying prerequisites`() {
        val controller = StartupReadinessController()
        controller.publishCapability(StartupReadinessState.SearchReady)

        assertEquals(StartupReadinessState.ProcessVisible, controller.capability)
        assertTrue(StartupCapability.SEARCH_READY in controller.state.achieved)
        assertTrue(StartupCapability.QUEUE_READY !in controller.state.achieved)
    }

    @Test
    fun `contiguous capability advances after missing prerequisites arrive`() {
        val controller = StartupReadinessController()
        controller.publishCapability(StartupReadinessState.SearchReady)
        controller.publishCapability(StartupReadinessState.PlaybackServiceReady)
        controller.publishCapability(StartupReadinessState.QueueReady)
        controller.publishCapability(StartupReadinessState.FastBrowseReady)

        assertEquals(StartupReadinessState.SearchReady, controller.capability)
    }

    @Test
    fun `library status changes do not regress capabilities`() {
        val controller = StartupReadinessController()
        controller.publishCapability(StartupReadinessState.PlaybackServiceReady)
        controller.publishCapability(StartupReadinessState.QueueReady)
        controller.publishCapability(StartupReadinessState.FastBrowseReady)
        controller.publishCapability(StartupReadinessState.SearchReady)
        controller.publishCapability(StartupReadinessState.FullLibraryReady)
        controller.publishLibraryStatus(StartupLibraryStatus.SourceUnavailable)
        controller.publishLibraryStatus(StartupLibraryStatus.Usable)

        assertEquals(StartupReadinessState.FullLibraryReady, controller.capability)
        assertEquals(StartupLibraryStatus.Usable, controller.state.libraryStatus)
    }

    @Test
    fun `duplicate capability milestones are idempotent`() {
        val controller = StartupReadinessController()
        var notifications = 0
        controller.addListener { notifications++ }

        controller.publishCapability(StartupReadinessState.PlaybackServiceReady)
        controller.publishCapability(StartupReadinessState.PlaybackServiceReady)

        assertEquals(2, notifications)
        assertEquals(StartupReadinessState.PlaybackServiceReady, controller.capability)
    }
}

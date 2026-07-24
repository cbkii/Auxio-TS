package org.oxycblt.auxio.music.locations

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationModeDefaultTest {
    @Test
    fun topwayFreshInstallUsesDirectFs() {
        assertEquals(LocationMode.DIRECT_FS, LocationMode.defaultForFlavor(true))
    }

    @Test
    fun standardFreshInstallKeepsSaf() {
        assertEquals(LocationMode.SAF, LocationMode.defaultForFlavor(false))
    }
}

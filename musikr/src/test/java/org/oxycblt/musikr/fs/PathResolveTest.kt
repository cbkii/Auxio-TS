/*
 * Copyright (c) 2026 Auxio Project
 * PathResolveTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.fs

import android.content.Context
import android.net.Uri
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class PathResolveTest {
    private val unusedContext = mockk<Context>(relaxed = true)

    @Test
    fun thirdPartyFileRootDisplaysAsLocalPath() {
        val path = Path(Volume.ThirdParty(Uri.parse("file:///storage/usbdisk0")), Components.root())

        assertEquals("/storage/usbdisk0", path.resolve(unusedContext))
    }

    @Test
    fun thirdPartyFileChildDisplaysAsLocalPath() {
        val path =
            Path(
                Volume.ThirdParty(Uri.parse("file:///storage/usbdisk0")),
                Components.parseUnix("Music"),
            )

        assertEquals(
            "/storage/usbdisk0/Music",
            path.resolve(unusedContext),
        )
    }
}

/*
 * Copyright (c) 2026 Auxio Project
 * Ts18DofunIntegrationResolverTest.kt is part of Auxio.
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

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class Ts18DofunIntegrationResolverTest {

    @Test
    fun `resolver checks are defined and tested`() = runBlocking {
        val probes = Ts18RootProbe.entries
        assertTrue(probes.any { it.name == "Id" && it.command == "id" })

        val mutations = Ts18RootMutation.entries
        assertTrue(mutations.any { it.name == "DisableStockMusicForUser0" })
        assertTrue(mutations.any { it.name == "EnableStockMusicForUser0" })

        val listPackages = probes.first { it.name == "PackageSummary" }
        assertTrue(listPackages.command.contains("pm list packages"))
        assertTrue(listPackages.command.contains("com\\.tw\\.media"))
    }
}

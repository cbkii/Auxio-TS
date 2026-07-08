package org.oxycblt.auxio.headunit.root.dofun

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class Ts18DofunIntegrationResolverTest {

    @Test
    fun `resolver checks are defined and tested`() = runBlocking {
        val probes = Ts18RootProbe.entries
        assertTrue(probes.any { it.name == "Id" && it.command == "id" })
        assertTrue(probes.any { it.name == "DisableStockMusic" })
        assertTrue(probes.any { it.name == "EnableStockMusic" })

        val listPackages = probes.first { it.name == "PackageSummary" }
        assertTrue(listPackages.command.contains("pm list packages"))
        assertTrue(listPackages.command.contains("com\\\\.tw\\\\.media"))
    }
}

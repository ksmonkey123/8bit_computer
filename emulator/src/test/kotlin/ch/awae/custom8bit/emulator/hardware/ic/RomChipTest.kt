package ch.awae.custom8bit.emulator.hardware.ic

import ch.awae.custom8bit.emulator.hardware.wiring.*
import org.junit.jupiter.api.Test
import kotlin.random.*
import kotlin.test.*

class RomChipTest {

    @Test
    fun testRandomAccess() {
        val data = Random.Default.nextBytes(8192)

        val address = MockBus()
        val dataBus = MockBus()
        val rom = RomChip(data, address, dataBus, DataSignal.constant(true), DataSignal.constant(true))

        repeat(100) {
            val adr = Random.nextInt(8192)

            address.state = adr.toUInt()
            rom.tick()
            assertEquals(data[adr].toUInt(), dataBus.driverState)
        }
    }

}
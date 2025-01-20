package ch.awae.custom8bit.hardware.hardware.wiring

import kotlin.test.*

class ConstantBusTest {

    @Test
    fun testConstantBus() {
        val bus = DataBus.constant(0x12345678u)

        assertEquals(0x12345678u, bus.state)
        assertEquals(0u, bus.contention)
    }

}
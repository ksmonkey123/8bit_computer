package ch.awae.custom8bit.emulator.hardware.wiring

import kotlin.test.*

class BusCombinerTest {

    @Test
    fun test2Bytes() {
        val bus0 = MockBus()
        val bus1 = MockBus()
        val combiner = CombinedByteBus(bus0, bus1)

        bus0.state = 0xffffff01u
        bus1.state = 0xffffff02u

        bus0.contention = 0xffffff10u
        bus1.contention = 0xffffff20u

        assertEquals(0x00000201u, combiner.state)
        assertEquals(0x00002010u, combiner.contention)
    }

    @Test
    fun test3Bytes() {
        val bus0 = MockBus()
        val bus1 = MockBus()
        val bus2 = MockBus()
        val combiner = CombinedByteBus(bus0, bus1, bus2)

        bus0.state = 0xffffff01u
        bus1.state = 0xffffff02u
        bus2.state = 0xffffff04u

        bus0.contention = 0xffffff10u
        bus1.contention = 0xffffff20u
        bus2.contention = 0xffffff40u

        assertEquals(0x00040201u, combiner.state)
        assertEquals(0x00402010u, combiner.contention)
    }

    @Test
    fun test4Bytes() {
        val bus0 = MockBus()
        val bus1 = MockBus()
        val bus2 = MockBus()
        val bus3 = MockBus()
        val combiner = CombinedByteBus(bus0, bus1, bus2, bus3)

        bus0.state = 0xffffff01u
        bus1.state = 0xffffff02u
        bus2.state = 0xffffff04u
        bus3.state = 0xffffff08u

        bus0.contention = 0xffffff10u
        bus1.contention = 0xffffff20u
        bus2.contention = 0xffffff40u
        bus3.contention = 0xffffff80u

        assertEquals(0x08040201u, combiner.state)
        assertEquals(0x80402010u, combiner.contention)
    }

}

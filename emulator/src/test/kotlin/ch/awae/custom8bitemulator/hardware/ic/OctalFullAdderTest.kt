package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.hardware.wiring.*
import kotlin.test.*

class OctalFullAdderTest {

    @Test
    fun testAddition() {
        val busA = MockBus()
        val busB = MockBus()
        val carryIn = MockSignal()
        val outBus = MockBus()
        val carryOut = MockSignal()
        val adder = OctalFullAdder(busA, busB, carryIn, outBus, carryOut)

        adder.tick()
        assertEquals(0u, outBus.driverState)
        assertEquals(false, carryOut.driverState)

        for (a in 0..255) {
            for (b in 0..255) {
                // test a + b
                carryIn.state = false
                busA.state = a.toUInt()
                busB.state = b.toUInt()
                adder.tick()
                assertEquals((a + b).toUInt() and 0xffu, outBus.driverState)
                assertEquals((a + b) > 255, carryOut.driverState)
                // test a + b + 1
                carryIn.state = true
                adder.tick()
                assertEquals((a + b + 1).toUInt() and 0xffu, outBus.driverState)
                assertEquals((a + b) > 254, carryOut.driverState)
            }
        }
    }

}

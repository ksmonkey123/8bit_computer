package ch.awae.custom8bitemulator.hardware

import ch.awae.custom8bitemulator.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class BusDerivedSignalTest {

    @ParameterizedTest
    @ValueSource(
        ints = [
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16,
            17, 18, 19, 20, 21, 22, 23, 24,
            25, 26, 27, 28, 29, 30, 31]
    )
    fun testValueExtraction_DirectInit(bit: Int) {
        testValueExtraction(bit, true)
    }

    @ParameterizedTest
    @ValueSource(
        ints = [
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16,
            17, 18, 19, 20, 21, 22, 23, 24,
            25, 26, 27, 28, 29, 30, 31]
    )
    fun testValueExtraction_FluentInit(bit: Int) {
        testValueExtraction(bit, false)
    }

    private fun testValueExtraction(bit: Int, directInit: Boolean) {
        val mask = 1u shl bit

        val bus = WritableBus(true)
        val driver = bus.connectDriver()
        val driverB = bus.connectDriver()
        val sim = Simulation(bus)

        val signal = if (directInit) BusDerivedSignal(bus, bit) else bus.bitSignal(bit)


        assertTrue(signal.state, "initially, see pull-up")
        assertFalse(signal.contention, "initially see no contention on pull-up")

        // drive high
        driver.set(0xffffffffu)
        sim.tick()
        assertTrue(signal.state, "drive high")
        assertFalse(signal.contention, "drive high no contention")

        // drive low
        driver.set(0u)
        sim.tick()
        assertFalse(signal.state, "drive low")
        assertFalse(signal.contention, "drive low no contention")

        // drive only signal high
        driver.set(mask)
        sim.tick()
        assertTrue(signal.state, "drive single signal high")
        assertFalse(signal.contention, "dirve single signal high no contention")

        // drive all others high
        driver.set(mask.inv())
        sim.tick()
        assertFalse(signal.state, "drive all other signals")
        assertFalse(signal.contention, "drive others no contention")

        // contention on other wires
        driver.set(0xffffffffu)
        driverB.set(mask)
        sim.tick()
        assertTrue(signal.state, "contention on others, self high")
        assertFalse(signal.contention, "contention on others only")

        // contention only on this signal
        driver.set(0xffffffffu)
        driverB.set(mask.inv())
        sim.tick()
        assertFalse(signal.state, "contention on this")
        assertTrue(signal.contention, "contention on this")
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 32])
    fun testInvalidInit_Fluent(bit: Int) {
        assertThrows<IllegalArgumentException> {
            val bus = WritableBus(false)
            bus.bitSignal(bit)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 32])
    fun testInvalidInit_Direct(bit: Int) {
        assertThrows<IllegalArgumentException> {
            val bus = WritableBus(false)
            BusDerivedSignal(bus, bit)
        }
    }


}
package ch.awae.custom8bit.hardware.hardware.wiring

import ch.awae.custom8bit.hardware.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class StandardWritableBusTest {

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testIdleStates(high: Boolean) {
        val expectedIdleState = if (high) 0xffffffffu else 0x00000000u

        val bus = StandardWritableBus(high)
        val sim = Simulation(bus)
        val driver = bus.connectDriver()

        assertEquals(expectedIdleState, bus.state)
        assertEquals(0x00000000u, bus.contention)

        // idle tick should not change anything
        sim.tick()
        assertEquals(expectedIdleState, bus.state)
        assertEquals(0x00000000u, bus.contention)

        // load bus
        driver.set(0x12345678u)
        sim.tick()
        assertEquals(0x12345678u, bus.state)
        assertEquals(0x00000000u, bus.contention)

        // release bus. expect return to idle state
        driver.release()
        sim.tick()
        assertEquals(expectedIdleState, bus.state)
        assertEquals(0x00000000u, bus.contention)
    }

    @Test
    fun testDriverUpdatingValue() {
        val bus = StandardWritableBus(false)
        val sim = Simulation(bus)
        val driver = bus.connectDriver()

        driver.set(0x12345678u)
        // bus value changes on tick only
        assertEquals(0u, bus.state)
        assertEquals(0u, bus.contention)
        sim.tick()
        assertEquals(0x12345678u, bus.state)
        assertEquals(0u, bus.contention)
        // driver value can change freely multiple times before a tick
        driver.set(0x87654321u)
        assertEquals(0x12345678u, bus.state)
        assertEquals(0u, bus.contention)
        driver.set(0x13572468u)
        assertEquals(0x12345678u, bus.state)
        assertEquals(0u, bus.contention)
        // last value is used on tick
        sim.tick()
        assertEquals(0x13572468u, bus.state)
        assertEquals(0u, bus.contention)
    }

    @Test
    fun testMultipleSequentialDrivers() {
        val bus = StandardWritableBus(false)
        val sim = Simulation(bus)
        val driverA = bus.connectDriver()
        val driverB = bus.connectDriver()

        driverA.set(0x12345678u)
        driverB.release()
        sim.tick()

        assertEquals(0x12345678u, bus.state)
        assertEquals(0u, bus.contention)

        driverA.release()
        driverB.set(0x87654321u)
        sim.tick()

        assertEquals(0x87654321u, bus.state)
        assertEquals(0u, bus.contention)
    }

    @Test
    fun testContention() {
        val bus = StandardWritableBus(false)
        val sim = Simulation(bus)
        val driverA = bus.connectDriver()
        val driverB = bus.connectDriver()

        driverA.set(0b0001_0010_0011_0100_0101_0110_0111_1000u)
        driverB.set(0b1000_0111_0110_0101_0100_0011_0010_0001u)
        sim.tick()
        assertEquals(0b0000_0010_0010_0100_0100_0010_0010_0000u, bus.state)
        assertEquals(0b1001_0101_0101_0001_0001_0101_0101_1001u, bus.contention)
        // releasing driver resolves contention
        driverB.release()
        sim.tick()
        assertEquals(0b0001_0010_0011_0100_0101_0110_0111_1000u, bus.state)
        assertEquals(0u, bus.contention)
        // multiple matching drivers are OK
        driverB.set(0b0001_0010_0011_0100_0101_0110_0111_1000u)
        sim.tick()
        assertEquals(0b0001_0010_0011_0100_0101_0110_0111_1000u, bus.state)
        assertEquals(0u, bus.contention)
    }

}

package ch.awae.custom8bitemulator.hardware.wiring

import ch.awae.custom8bitemulator.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class WritableSignalTest {

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testIdleStates(expectedIdleState: Boolean) {

        val bus = WritableSignal(expectedIdleState)
        val sim = Simulation(bus)
        val driver = bus.connectDriver()

        assertEquals(expectedIdleState, bus.state)
        assertEquals(false, bus.contention)

        // idle tick should not change anything
        sim.tick()
        assertEquals(expectedIdleState, bus.state)
        assertEquals(false, bus.contention)

        // load bus
        driver.set(true)
        sim.tick()
        assertEquals(true, bus.state)
        assertEquals(false, bus.contention)

        // release bus. expect return to idle state
        driver.release()
        sim.tick()
        assertEquals(expectedIdleState, bus.state)
        assertEquals(false, bus.contention)
    }

    @Test
    fun testDriverUpdatingValue() {
        val signal = WritableSignal(false)
        val sim = Simulation(signal)
        val driver = signal.connectDriver()

        driver.set(true)
        // bus value changes on tick only
        assertEquals(false, signal.state)
        assertEquals(false, signal.contention)
        sim.tick()
        assertEquals(true, signal.state)
        assertEquals(false, signal.contention)
        // driver value can change freely multiple times before a tick
        driver.set(false)
        assertEquals(true, signal.state)
        assertEquals(false, signal.contention)
        driver.set(true)
        assertEquals(true, signal.state)
        assertEquals(false, signal.contention)
        // last value is used on tick
        sim.tick()
        assertEquals(true, signal.state)
        assertEquals(false, signal.contention)
        // drive low
        driver.set(false)
        sim.tick()
        assertEquals(false, signal.state)
        assertEquals(false, signal.contention)

    }

    @Test
    fun testMultipleSequentialDrivers() {
        val signal = WritableSignal(false)
        val sim = Simulation(signal)
        val driverA = signal.connectDriver()
        val driverB = signal.connectDriver()

        driverA.set(false)
        driverB.release()
        sim.tick()

        assertEquals(false, signal.state)
        assertEquals(false, signal.contention)

        driverA.release()
        driverB.set(true)
        sim.tick()

        assertEquals(true, signal.state)
        assertEquals(false, signal.contention)
    }

    @Test
    fun testContention() {
        val bus = WritableSignal(false)
        val sim = Simulation(bus)
        val driverA = bus.connectDriver()
        val driverB = bus.connectDriver()

        driverA.set(true)
        driverB.set(false)
        sim.tick()
        assertEquals(false, bus.state)
        assertEquals(true, bus.contention)
        // releasing driver resolves contention
        driverB.release()
        sim.tick()
        assertEquals(true, bus.state)
        assertEquals(false, bus.contention)
        // multiple matching drivers are OK
        driverB.set(true)
        sim.tick()
        assertEquals(true, bus.state)
        assertEquals(false, bus.contention)
    }

}
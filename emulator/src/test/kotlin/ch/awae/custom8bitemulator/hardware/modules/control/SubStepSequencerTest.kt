package ch.awae.custom8bitemulator.hardware.modules.control

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

private const val TICKS = 100

class SubStepSequencerTest {

    @Test
    fun testCycle() {
        val clock = StandardWritableSignal(false, "clock")
        val reset = StandardWritableSignal(false, "reset")
        val output = StandardWritableBus(false, "output")
        val sequencer = SubStepSequencer(clock, reset, output, "sequencer")
        val sim = Simulation(clock, reset, output, sequencer)

        val clockDriver = clock.connectDriver()
        val resetDriver = reset.connectDriver()

        resetDriver.set(true)
        sim.tick(TICKS)
        assertEquals(0u, output.state)
        resetDriver.set(false)
        sim.tick(TICKS)
        assertEquals(0u, output.state)

        fun testCycle(expected: UInt) {
            clockDriver.set(true)
            sim.tick(TICKS)
            assertEquals(expected, output.state)
            clockDriver.set(false)
            sim.tick(TICKS)
            assertEquals(expected, output.state)
        }

        repeat(10) {
            testCycle(1u)
            testCycle(2u)
            testCycle(3u)
            testCycle(0u)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3])
    fun testResetAtAnyPoint(state: Int) {
        val clock = StandardWritableSignal(false, "clock")
        val reset = StandardWritableSignal(false, "reset")
        val output = StandardWritableBus(false, "output")
        val sequencer = SubStepSequencer(clock, reset, output, "sequencer")
        val sim = Simulation(clock, reset, output, sequencer)

        val clockDriver = clock.connectDriver()
        val resetDriver = reset.connectDriver()

        resetDriver.set(true)
        sim.tick(TICKS)
        assertEquals(0u, output.state)
        resetDriver.set(false)
        sim.tick(TICKS)
        assertEquals(0u, output.state)

        repeat(state) { i ->
            clockDriver.set(true)
            sim.tick(TICKS)
            assertEquals((i + 1).toUInt(), output.state)
            clockDriver.set(false)
            sim.tick(TICKS)
            assertEquals((i + 1).toUInt(), output.state)
        }

        // we are now in the state we expect
        assertEquals(state.toUInt(), output.state)
        resetDriver.set(true)
        sim.tick(TICKS)
        assertEquals(0u, output.state)
    }
}
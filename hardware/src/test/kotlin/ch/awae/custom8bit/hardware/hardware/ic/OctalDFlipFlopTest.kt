package ch.awae.custom8bit.hardware.hardware.ic

import ch.awae.custom8bit.hardware.hardware.wiring.*
import kotlin.test.*

class OctalDFlipFlopTest {

    @Test
    fun testStoringData() {
        val inputBus = MockBus()
        val clock = MockSignal()
        val outputBus = MockBus()
        val buffer = OctalDFlipFlop(inputBus, clock, null, outputBus)

        val initialOut = outputBus.driverState
        inputBus.state = 0x69u
        clock.state = false
        buffer.tick()
        assertEquals(initialOut, outputBus.driverState)
        clock.state = true
        buffer.tick()
        assertEquals(0x69u, outputBus.driverState)

        inputBus.state = 0x96u
        clock.state = false
        buffer.tick()
        assertEquals(0x69u, outputBus.driverState)

        clock.state = true
        buffer.tick()
        assertEquals(0x96u, outputBus.driverState)
    }

    @Test
    fun testClearing() {
        val inputBus = MockBus()
        val clock = MockSignal()
        val reset = MockSignal()
        val outputBus = MockBus()
        val buffer = OctalDFlipFlop(inputBus, clock, reset, outputBus)

        reset.state = true
        buffer.tick()
        assertEquals(0u, outputBus.state)
    }

    @Test
    fun testClockIrrelevantWhileResetActive() {
        val inputBus = MockBus()
        val clock = MockSignal()
        val reset = MockSignal()
        val outputBus = MockBus()
        val buffer = OctalDFlipFlop(inputBus, clock, reset, outputBus)

        reset.state = true
        clock.state = false
        inputBus.state = 0x69u

        buffer.tick()
        assertEquals(0u, outputBus.driverState)

        repeat(2) {
            clock.state = false
            buffer.tick()
            assertEquals(0u, outputBus.driverState)

            clock.state = true
            buffer.tick()
            assertEquals(0u, outputBus.driverState)
        }

        reset.state = false
        buffer.tick()
        assertEquals(0u, outputBus.driverState)

        clock.state = false
        buffer.tick()
        assertEquals(0u, outputBus.driverState)

        clock.state = true
        buffer.tick()
        assertEquals(0x69u, outputBus.driverState)
    }

    @Test
    fun onlyFirstByteOfBusIsUsed() {
        val inputBus = MockBus()
        val clock = MockSignal()
        val outputBus = MockBus()
        val buffer = OctalDFlipFlop(inputBus, clock, null, outputBus)

        inputBus.state = 0x96969669u
        clock.state = false
        buffer.tick()
        clock.state = true
        buffer.tick()
        assertEquals(0x69u, outputBus.driverState)

        inputBus.state = 0x69696996u
        clock.state = false
        buffer.tick()
        assertEquals(0x69u, outputBus.driverState)
        clock.state = true
        buffer.tick()
        assertEquals(0x96u, outputBus.driverState)
    }

    @Test
    fun initialRandomStateOnlyTouchesLowestByte() {
        repeat(1000) {
            val inputBus = MockBus()
            val clock = MockSignal()
            val outputBus = MockBus()

            OctalDFlipFlop(inputBus, clock, null, outputBus)

            assertEquals(0u, outputBus.state and 0xffffff00u)
        }
    }

}

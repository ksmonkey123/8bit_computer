package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.*
import kotlin.test.*

class OctalDFlipFlop74273Test {

    @Test
    fun testStoringData() {
        val inputBus = WritableBus(true)
        val clock = WritableSignal(false)
        val outputBus = WritableBus(true)
        val buffer = OctalDFlipFlop74273(inputBus, clock, null, outputBus)
        val sim = Simulation(inputBus, outputBus, clock, buffer)
        val clockDriver = clock.connectDriver()
        val inputDriver = inputBus.connectDriver()

        inputDriver.set(0x69u)
        clockDriver.set(false)
        sim.tick()
        clockDriver.set(true)
        sim.tick()
        clockDriver.set(false)
        sim.tick()
        assertEquals(0x69u, outputBus.state)

        inputDriver.set(0x96u)
        sim.tick()
        clockDriver.set(true)
        sim.tick()
        clockDriver.set(false)
        assertEquals(0x69u, outputBus.state)
        sim.tick()
        assertEquals(0x96u, outputBus.state)
    }

    @Test
    fun testClearing() {
        val inputBus = WritableBus(true)
        val clock = WritableSignal(false)
        val reset = WritableSignal(false)
        val outputBus = WritableBus(true)
        val buffer = OctalDFlipFlop74273(inputBus, clock, reset, outputBus)
        val sim = Simulation(inputBus, outputBus, clock, reset, buffer)

        val resetDriver = reset.connectDriver()

        sim.tick()
        val out = outputBus.state

        resetDriver.set(true)
        sim.tick()
        assertEquals(out, outputBus.state)
        sim.tick()
        assertEquals(0u, outputBus.state)
    }

    @Test
    fun testClockIrrelevantWhileResetActive() {
        val inputBus = WritableBus(true)
        val clock = WritableSignal(true)
        val reset = WritableSignal(false)
        val outputBus = WritableBus(true)
        val buffer = OctalDFlipFlop74273(inputBus, clock, reset, outputBus)
        val sim = Simulation(inputBus, outputBus, clock, reset, buffer)
        val resetDriver = reset.connectDriver()
        val inputDriver = inputBus.connectDriver()
        val clockDriver = clock.connectDriver()

        inputDriver.set(0x69u)
        clockDriver.set(false)

        sim.tick(20)
        resetDriver.set(true)
        sim.tick(2)
        assertEquals(0u, outputBus.state)
        clockDriver.set(true)
        sim.tick()
        assertEquals(0u, outputBus.state)
        clockDriver.set(false)
        sim.tick()
        assertEquals(0u, outputBus.state)
        clockDriver.set(true)
        sim.tick()
        assertEquals(0u, outputBus.state)
        resetDriver.set(false)
        sim.tick()
        assertEquals(0u, outputBus.state)
        clockDriver.set(false)
        sim.tick()
        assertEquals(0u, outputBus.state)
        clockDriver.set(true)
        sim.tick()
        assertEquals(0u, outputBus.state)
        sim.tick()
        assertEquals(0x69u, outputBus.state)
    }

    @Test
    fun onlyFirstByteOfBusIsUsed() {
        val inputBus = WritableBus(true)
        val clock = WritableSignal(false)
        val outputBus = WritableBus(true)
        val buffer = OctalDFlipFlop74273(inputBus, clock, null, outputBus)
        val sim = Simulation(inputBus, outputBus, clock, buffer)
        val clockDriver = clock.connectDriver()
        val inputDriver = inputBus.connectDriver()

        inputDriver.set(0x96969669u)
        clockDriver.set(false)
        sim.tick()
        clockDriver.set(true)
        sim.tick()
        clockDriver.set(false)
        sim.tick()
        assertEquals(0x69u, outputBus.state)

        inputDriver.set(0x69696996u)
        sim.tick()
        clockDriver.set(true)
        sim.tick()
        clockDriver.set(false)
        assertEquals(0x69u, outputBus.state)
        sim.tick()
        assertEquals(0x96u, outputBus.state)
    }

    @Test
    fun initialRandomStateOnlyTouchesLowestByte() {
        repeat(1000) {
            val inputBus = WritableBus(true)
            val clock = WritableSignal(false)
            val outputBus = WritableBus(true)
            val buffer = OctalDFlipFlop74273(inputBus, clock, null, outputBus)
            val sim = Simulation(inputBus, outputBus, clock, buffer)
            sim.tick()

            assertEquals(0u, outputBus.state and 0xffffff00u)
        }
    }

}
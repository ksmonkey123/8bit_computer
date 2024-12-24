package ch.awae.custom8bitemulator.hardware.modules.control

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class SequencerTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 3])
    fun testSequenceLength(f: Int) {
        val fBus = StandardWritableBus(false)
        val stepSignal = StandardWritableSignal(false)
        val resetSignal = StandardWritableSignal(true)
        val qBus = StandardWritableBus(false)

        val sequencer = Sequencer(fBus, stepSignal, resetSignal, qBus, "testSequencer")

        val sim = Simulation(fBus, stepSignal, resetSignal, qBus, sequencer)

        val fDriver = fBus.connectDriver()
        val stepDriver = stepSignal.connectDriver()
        val resetDriver = resetSignal.connectDriver()

        fDriver.set(0u)

        sim.tick(100)
        assertEquals(0u, qBus.state)

        resetDriver.set(false)

        repeat(10) {

            sim.tick(50)
            // apply f value during cycle
            fDriver.set(f.toUInt())
            sim.tick(50)
            assertEquals(0u, qBus.state)

            if (f > 0) {
                stepDriver.set(true)
                sim.tick(100)
                assertEquals(1u, qBus.state)

                stepDriver.set(false)
                sim.tick(100)
                assertEquals(1u, qBus.state)
            }

            if (f > 1) {
                stepDriver.set(true)
                sim.tick(100)
                assertEquals(2u, qBus.state)

                stepDriver.set(false)
                sim.tick(100)
                assertEquals(2u, qBus.state)
            }

            stepDriver.set(true)
            sim.tick(100)
            assertEquals(3u, qBus.state)

            stepDriver.set(false)
            sim.tick(100)
            assertEquals(3u, qBus.state)

            stepDriver.set(true)
            sim.tick(100)
            assertEquals(0u, qBus.state)

            stepDriver.set(false)
            sim.tick(100)
            assertEquals(0u, qBus.state)
        }
    }

    @Test
    fun testTransitionBetweenCycleLengths() {
        val fBus = StandardWritableBus(false)
        val stepSignal = StandardWritableSignal(false)
        val resetSignal = StandardWritableSignal(true)
        val qBus = StandardWritableBus(false)

        val sequencer = Sequencer(fBus, stepSignal, resetSignal, qBus, "testSequencer")

        val sim = Simulation(fBus, stepSignal, resetSignal, qBus, sequencer)

        val fDriver = fBus.connectDriver()
        val stepDriver = stepSignal.connectDriver()
        val resetDriver = resetSignal.connectDriver()

        fDriver.set(0u)

        sim.tick(100)
        assertEquals(0u, qBus.state)

        resetDriver.set(false)
        sim.tick(100)
        assertEquals(0u, qBus.state)

        fun testCycle(f: Int, initial: Boolean = false) {
            if (!initial) {
                stepDriver.set(true)
                sim.tick(100)
                assertEquals(0u, qBus.state)
            }
            stepDriver.set(false)
            fDriver.set(f.toUInt())
            sim.tick(100)
            assertEquals(0u, qBus.state)

            if (f > 0) {
                stepDriver.set(true)
                sim.tick(100)
                assertEquals(1u, qBus.state)

                stepDriver.set(false)
                sim.tick(100)
                assertEquals(1u, qBus.state)
            }

            if (f > 1) {
                stepDriver.set(true)
                sim.tick(100)
                assertEquals(2u, qBus.state)

                stepDriver.set(false)
                sim.tick(100)
                assertEquals(2u, qBus.state)
            }

            stepDriver.set(true)
            sim.tick(100)
            assertEquals(3u, qBus.state)

            stepDriver.set(false)
            sim.tick(100)
            assertEquals(3u, qBus.state)
        }

        testCycle(0, initial = true)

        repeat(1000) {
            testCycle(listOf(0, 1, 3).random())
        }

    }

}
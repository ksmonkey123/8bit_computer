package ch.awae.custom8bitemulator.hardware.modules.control

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

private val CYCLE_0 = listOf(1, 2, 3, 12, 13, 14, 15, 0)
private val CYCLE_1 = listOf(1, 2, 3, 4, 5, 6, 7, 12, 13, 14, 15, 0)
private val CYCLE_2 = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)

private const val TICKS = 10

class SequencerUnitTest {


    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3])
    fun testSequenceLength(cycle: Int) {

        val loop = chooseLoop(cycle)

        val fBus = StandardWritableBus(false, "fBus")
        val stepSignal = StandardWritableSignal(false, "stepSignal")
        val resetSignal = StandardWritableSignal(true, "resetSignal")
        val qBus = StandardWritableBus(false, "qBus")

        val sequencer = SequencerUnit(fBus, stepSignal, resetSignal, qBus, "testSequencer")

        val sim = Simulation(fBus, stepSignal, resetSignal, qBus, sequencer)

        val fDriver = fBus.connectDriver()
        val stepDriver = stepSignal.connectDriver()
        val resetDriver = resetSignal.connectDriver()

        fDriver.set(cycle.toUInt())
        stepDriver.set(false)

        sim.tick(TICKS)
        assertEquals(0u, qBus.state)

        resetDriver.set(false)
        sim.tick(TICKS)


        repeat(100) {
            loop.forEach { i ->
                stepDriver.set(true)
                sim.tick(TICKS)
                assertEquals(i.toUInt(), qBus.state)
                stepDriver.set(false)
                sim.tick(TICKS)
                assertEquals(i.toUInt(), qBus.state)
            }
        }
    }

    private fun chooseLoop(cycle: Int): List<Int> {
        val loop = when (cycle) {
            1 -> CYCLE_1
            3 -> CYCLE_2
            else -> CYCLE_0
        }
        return loop
    }

    @Test
    fun testRandomSequences() {

        val fBus = StandardWritableBus(false, "fBus")
        val stepSignal = StandardWritableSignal(false, "stepSignal")
        val resetSignal = StandardWritableSignal(true, "resetSignal")
        val qBus = StandardWritableBus(false, "qBus")

        val sequencer = SequencerUnit(fBus, stepSignal, resetSignal, qBus, "testSequencer")

        val sim = Simulation(fBus, stepSignal, resetSignal, qBus, sequencer)

        val fDriver = fBus.connectDriver()
        val stepDriver = stepSignal.connectDriver()
        val resetDriver = resetSignal.connectDriver()

        fDriver.set(0u)
        stepDriver.set(false)

        sim.tick(TICKS)
        assertEquals(0u, qBus.state)

        resetDriver.set(false)
        sim.tick(TICKS)

        repeat(1000) {
            val cycle = listOf(0, 1, 3).random()
            val loop = chooseLoop(cycle)

            loop.forEach { i ->
                stepDriver.set(true)
                if (i == 1) {
                    fDriver.set(cycle.toUInt())
                }
                sim.tick(TICKS)
                assertEquals(i.toUInt(), qBus.state)
                stepDriver.set(false)
                sim.tick(TICKS)
                assertEquals(i.toUInt(), qBus.state)
            }
        }

    }
}
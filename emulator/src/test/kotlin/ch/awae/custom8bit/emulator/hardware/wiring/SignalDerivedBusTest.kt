package ch.awae.custom8bit.emulator.hardware.wiring

import org.junit.jupiter.api.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class SignalDerivedBusTest {

    @ParameterizedTest
    @MethodSource("positions")
    fun testPosition(position: Int) {
        val signal = MockSignal()

        val bus = DataBus.ofSignals(position to signal)
        assertEquals(0u, bus.state)

        signal.state = true
        assertEquals(1u shl position, bus.state)
    }

    @ParameterizedTest
    @MethodSource("dualPositions")
    fun testMergeOf2Signals(xPos: Int, yPos: Int) {
        val signalX = MockSignal()
        val signalY = MockSignal()

        val bus = DataBus.ofSignals(xPos to signalX, yPos to signalY)
        assertEquals(0u, bus.state)

        signalX.state = true
        assertEquals(1u shl xPos, bus.state)

        signalX.state = false
        signalY.state = true
        assertEquals(1u shl yPos, bus.state)

        signalX.state = true
        assertEquals((1u shl xPos) or (1u shl yPos), bus.state)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 32])
    fun testBadPosition(position: Int) {
        val signal = MockSignal()

        assertThrows<IllegalArgumentException> {
            DataBus.ofSignals(position to signal)
        }
    }

    companion object {
        @JvmStatic
        fun positions() = (0..31).map { Arguments.of(it) }

        @JvmStatic
        fun dualPositions() = (0..31).flatMap { x ->
            (0..31).filter { it != x }.map { y ->
                Arguments.of(x, y)
            }
        }
    }

}
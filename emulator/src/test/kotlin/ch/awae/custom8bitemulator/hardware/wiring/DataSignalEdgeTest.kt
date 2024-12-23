package ch.awae.custom8bitemulator.hardware.wiring

import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class DataSignalEdgeTest {

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun testRaising(initHigh: Boolean) {
        val signal = MockSignal(state = initHigh)
        val edge = signal.edge(true)

        assertFalse(edge.triggered())
        assertFalse(edge.triggered())
        signal.state = false
        assertFalse(edge.triggered())
        assertFalse(edge.triggered())

        signal.state = true
        // only one trigger on raising
        assertTrue(edge.triggered())
        assertFalse(edge.triggered())

        // no trigger on falling
        signal.state = false
        assertFalse(edge.triggered())
        assertFalse(edge.triggered())

        // no trigger when not probed
        signal.state = true
        signal.state = false
        assertFalse(edge.triggered())
    }


    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun testFalling(initHigh: Boolean) {
        val signal = MockSignal(state = initHigh)
        val edge = signal.edge(false)

        assertFalse(edge.triggered())
        assertFalse(edge.triggered())
        signal.state = true
        assertFalse(edge.triggered())
        assertFalse(edge.triggered())

        signal.state = false
        // only one trigger on falling
        assertTrue(edge.triggered())
        assertFalse(edge.triggered())

        // no trigger on raising
        signal.state = true
        assertFalse(edge.triggered())
        assertFalse(edge.triggered())

        // no trigger when not probed
        signal.state = false
        signal.state = true
        assertFalse(edge.triggered())
    }

}
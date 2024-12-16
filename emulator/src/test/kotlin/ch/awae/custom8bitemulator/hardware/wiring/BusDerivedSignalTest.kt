package ch.awae.custom8bitemulator.hardware.wiring

import org.junit.jupiter.api.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class BusDerivedSignalTest {

    @ParameterizedTest
    @MethodSource("allValidBits")
    fun testValueExtraction(bit: Int) {
        val bus = MockBus()
        val mask = 1u shl bit
        val signal = bus.bitSignal(bit)

        bus.state = 0u
        bus.contention = 0u
        assertEquals(false, signal.state)
        assertEquals(false, signal.contention)

        // set signal high
        bus.state = mask
        bus.contention = 0u
        assertEquals(true, signal.state)
        assertEquals(false, signal.contention)

        // set contention high
        bus.state = 0u
        bus.contention = mask
        assertEquals(false, signal.state)
        assertEquals(true, signal.contention)

        // set all others high
        bus.state = mask.inv()
        bus.contention = 0u
        assertEquals(false, signal.state)
        assertEquals(false, signal.contention)


        // contention on all others
        bus.state = mask
        bus.contention = mask.inv()
        assertEquals(true, signal.state)
        assertEquals(false, signal.contention)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 32])
    fun testInvalidInit(bit: Int) {
        val bus = MockBus()
        assertThrows<IllegalArgumentException> {
            bus.bitSignal(bit)
        }
    }

    companion object {

        @JvmStatic
        fun allValidBits() = (0..31).map { Arguments.of(it) }

    }

}
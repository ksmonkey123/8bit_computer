package ch.awae.custom8bitemulator.hardware.wiring

import org.junit.jupiter.api.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class ByteBusTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3])
    fun testByteBusExtractsCorrectByte(byte: Int) {
        val bus = MockBus()

        val byteBus = bus.byte(byte)

        bus.state = 0x04030201u
        bus.contention = 0x40302010u

        assertEquals((byte + 1).toUInt(), byteBus.state)
        assertEquals(((byte + 1) * 16).toUInt(), byteBus.contention)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 4])
    fun testByteBusInitFailure(byte: Int) {
        val bus = MockBus()
        assertThrows<IllegalArgumentException> {
            bus.byte(byte)
        }
    }
}

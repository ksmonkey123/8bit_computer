package ch.awae.custom8bit.hardware.hardware.ic

import ch.awae.custom8bit.hardware.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class BinaryToSelectionDecoderTest {

    @ParameterizedTest
    @MethodSource("data")
    fun `correct output selected if no enable signal given`(input: Int, output: Long) {
        val inputBus = MockBus()
        val outputBus = MockBus()

        val decoder = BinaryToSelectionDecoder(inputBus, outputBus, 0xffu)

        inputBus.state = input.toUInt()
        decoder.tick()
        assertEquals(output.toUInt(), outputBus.driverState)
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `correct output selected if enabled, none if disabled`(input: Int, output: Long) {
        val inputBus = MockBus()
        val outputBus = MockBus()
        val enabled = MockSignal(true)

        val decoder = BinaryToSelectionDecoder(inputBus, outputBus, 0xffu, enabled)

        inputBus.state = input.toUInt()
        decoder.tick()
        assertEquals(output.toUInt(), outputBus.driverState)
        enabled.state = false
        decoder.tick()
        assertEquals(0u, outputBus.driverState)
    }

    companion object {
        @JvmStatic
        fun data() = listOf(
            Arguments.of(0x00, 0x0000_0001),
            Arguments.of(0x01, 0x0000_0002),
            Arguments.of(0x02, 0x0000_0004),
            Arguments.of(0x03, 0x0000_0008),
            Arguments.of(0x04, 0x0000_0010),
            Arguments.of(0x05, 0x0000_0020),
            Arguments.of(0x06, 0x0000_0040),
            Arguments.of(0x07, 0x0000_0080),
            Arguments.of(0x08, 0x0000_0100),
            Arguments.of(0x09, 0x0000_0200),
            Arguments.of(0x0a, 0x0000_0400),
            Arguments.of(0x0b, 0x0000_0800),
            Arguments.of(0x0c, 0x0000_1000),
            Arguments.of(0x0d, 0x0000_2000),
            Arguments.of(0x0e, 0x0000_4000),
            Arguments.of(0x0f, 0x0000_8000),
            Arguments.of(0x10, 0x0001_0000),
            Arguments.of(0x11, 0x0002_0000),
            Arguments.of(0x12, 0x0004_0000),
            Arguments.of(0x13, 0x0008_0000),
            Arguments.of(0x14, 0x0010_0000),
            Arguments.of(0x15, 0x0020_0000),
            Arguments.of(0x16, 0x0040_0000),
            Arguments.of(0x17, 0x0080_0000),
            Arguments.of(0x18, 0x0100_0000),
            Arguments.of(0x19, 0x0200_0000),
            Arguments.of(0x1a, 0x0400_0000),
            Arguments.of(0x1b, 0x0800_0000),
            Arguments.of(0x1c, 0x1000_0000),
            Arguments.of(0x1d, 0x2000_0000),
            Arguments.of(0x1e, 0x4000_0000),
            Arguments.of(0x1f, 0x8000_0000),
            Arguments.of(0x20, 0x0000_0001),
        )
    }

}
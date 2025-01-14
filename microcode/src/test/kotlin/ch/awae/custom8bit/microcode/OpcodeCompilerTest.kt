package ch.awae.custom8bit.microcode

import ch.awae.custom8bit.microcode.Action.*
import ch.awae.custom8bit.microcode.FlagCondition.*
import kotlin.test.*

class OpcodeCompilerTest {

    @Test
    fun `test GOTO compiled correctly`() {
        val instruction = Op(0xc8, ADDRESS_FROM_LITERAL, BRANCH)

        val result = OpcodeCompiler.compile(instruction)

        assertEquals(16, result.size)
        assertEquals(0x00c8 to 0x03, result[0])
        assertEquals(0x01c8 to 0x03, result[1])
        assertEquals(0x02c8 to 0x03, result[2])
        assertEquals(0x03c8 to 0x03, result[3])
        assertEquals(0x04c8 to 0x03, result[4])
        assertEquals(0x05c8 to 0x03, result[5])
        assertEquals(0x06c8 to 0x03, result[6])
        assertEquals(0x07c8 to 0x03, result[7])
        assertEquals(0x10c8 to 0x40, result[8])
        assertEquals(0x11c8 to 0x40, result[9])
        assertEquals(0x12c8 to 0x40, result[10])
        assertEquals(0x13c8 to 0x40, result[11])
        assertEquals(0x14c8 to 0x40, result[12])
        assertEquals(0x15c8 to 0x40, result[13])
        assertEquals(0x16c8 to 0x40, result[14])
        assertEquals(0x17c8 to 0x40, result[15])

    }

    @Test
    fun `test BRGZ compiled correctly`() {
        val instruction = Op(0xc5, IF_NEGATIVE_CLEAR, IF_ZERO_CLEAR, ADDRESS_FROM_LITERAL, BRANCH)

        val result = OpcodeCompiler.compile(instruction)

        assertEquals(16, result.size)
        assertEquals(0x00c5 to 0x03, result[0])
        assertEquals(0x01c5 to 0x03, result[1])
        assertEquals(0x02c5 to 0x03, result[2])
        assertEquals(0x03c5 to 0x03, result[3])
        assertEquals(0x04c5 to 0x03, result[4])
        assertEquals(0x05c5 to 0x03, result[5])
        assertEquals(0x06c5 to 0x03, result[6])
        assertEquals(0x07c5 to 0x03, result[7])
        assertEquals(0x10c5 to 0x40, result[8])
        assertEquals(0x11c5 to 0x00, result[9])
        assertEquals(0x12c5 to 0x00, result[10])
        assertEquals(0x13c5 to 0x00, result[11])
        assertEquals(0x14c5 to 0x40, result[12])
        assertEquals(0x15c5 to 0x00, result[13])
        assertEquals(0x16c5 to 0x00, result[14])
        assertEquals(0x17c5 to 0x00, result[15])
    }

}
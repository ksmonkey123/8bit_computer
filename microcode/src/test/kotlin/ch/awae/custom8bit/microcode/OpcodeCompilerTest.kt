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
        assertEquals(0x0c8 to 0x03, result[0])
        assertEquals(0x1c8 to 0x03, result[1])
        assertEquals(0x2c8 to 0x03, result[2])
        assertEquals(0x3c8 to 0x03, result[3])
        assertEquals(0x4c8 to 0x03, result[4])
        assertEquals(0x5c8 to 0x03, result[5])
        assertEquals(0x6c8 to 0x03, result[6])
        assertEquals(0x7c8 to 0x03, result[7])
        assertEquals(0x8c8 to 0x40, result[8])
        assertEquals(0x9c8 to 0x40, result[9])
        assertEquals(0xac8 to 0x40, result[10])
        assertEquals(0xbc8 to 0x40, result[11])
        assertEquals(0xcc8 to 0x40, result[12])
        assertEquals(0xdc8 to 0x40, result[13])
        assertEquals(0xec8 to 0x40, result[14])
        assertEquals(0xfc8 to 0x40, result[15])

    }

    @Test
    fun `test BRGZ compiled correctly`() {
        val instruction = Op(0xc5, IF_NEGATIVE_CLEAR, IF_ZERO_CLEAR, ADDRESS_FROM_LITERAL, BRANCH)

        val result = OpcodeCompiler.compile(instruction)

        assertEquals(16, result.size)
        assertEquals(0x0c5 to 0x03, result[0])
        assertEquals(0x1c5 to 0x03, result[1])
        assertEquals(0x2c5 to 0x03, result[2])
        assertEquals(0x3c5 to 0x03, result[3])
        assertEquals(0x4c5 to 0x03, result[4])
        assertEquals(0x5c5 to 0x03, result[5])
        assertEquals(0x6c5 to 0x03, result[6])
        assertEquals(0x7c5 to 0x03, result[7])
        assertEquals(0x8c5 to 0x40, result[8])
        assertEquals(0x9c5 to 0x00, result[9])
        assertEquals(0xac5 to 0x00, result[10])
        assertEquals(0xbc5 to 0x00, result[11])
        assertEquals(0xcc5 to 0x40, result[12])
        assertEquals(0xdc5 to 0x00, result[13])
        assertEquals(0xec5 to 0x00, result[14])
        assertEquals(0xfc5 to 0x00, result[15])
    }

}
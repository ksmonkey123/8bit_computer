package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.microcode.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SingleCommandTests {

    private val microcode = Microcode(Compiler.compileInstructionSet(INSTRUCTION_SET))

    private fun execute(
        program: IntArray,
        inputState: ProcessorState = ProcessorState()
    ): ProcessorState {
        val code = program.map { it.toByte() }.toByteArray()
        val pu = ProcessingUnit(microcode, StandardMemoryBus(RomChip8k(0, code)))
        return pu.executeNextCommand(inputState)
    }

    private fun execute(inputState: ProcessorState, vararg programBytes: Int) = execute(programBytes, inputState)

    @Test
    fun `AND B`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerB = 0b00110011),
            0x00
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00110011, output.registerB)
    }

    @Test
    fun `AND C`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 0b00110011),
            0x01
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00110011, output.registerC)
    }

    @Test
    fun `AND D`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerD = 0b00110011),
            0x02
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00110011, output.registerD)
    }

    @Test
    fun `AND 51`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x03, 0x33
        )

        assertEquals(0b00100001, output.registerA)
    }

    @Test
    fun `AND $4`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x04, 0x00, 0x04, 0xff, 0x33
        )

        assertEquals(0b00100001, output.registerA)
    }

    @Test
    fun `AND (CD)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 4, registerD = 0),
            0x05, 0xff, 0xff, 0xff, 0x33
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00000100, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }

    @Test
    fun `IOR B`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerB = 0b00110011),
            0x06
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00110011, output.registerB)
    }

    @Test
    fun `IOR C`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 0b00110011),
            0x07
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00110011, output.registerC)
    }

    @Test
    fun `IOR D`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerD = 0b00110011),
            0x08
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00110011, output.registerD)
    }

    @Test
    fun `IOR 51`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x09, 0x33
        )

        assertEquals(0b01111011, output.registerA)
    }

    @Test
    fun `IOR $4`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x0a, 0x00, 0x04, 0xff, 0x33
        )

        assertEquals(0b01111011, output.registerA)
    }

    @Test
    fun `IOR (CD)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 4, registerD = 0),
            0x0b, 0xff, 0xff, 0xff, 0x33
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00000100, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }

    @Test
    fun `NOT A`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x0c
        )

        assertEquals(0x96, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x00, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `NOT B`() {
        val output = execute(
            ProcessorState(registerB = 0b01101001),
            0x0d
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x96, output.registerB)
        assertEquals(0x00, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `NOT C`() {
        val output = execute(
            ProcessorState(registerC = 0b01101001),
            0x0e
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x96, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `NOT D`() {
        val output = execute(
            ProcessorState(registerD = 0b01101001),
            0x0f
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x00, output.registerC)
        assertEquals(0x96, output.registerD)
    }

    @Test
    fun `XOR B`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerB = 0b00110011),
            0x10
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00110011, output.registerB)
    }

    @Test
    fun `XOR C`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 0b00110011),
            0x11
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00110011, output.registerC)
    }

    @Test
    fun `XOR D`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerD = 0b00110011),
            0x12
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00110011, output.registerD)
    }

    @Test
    fun `XOR 51`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x13, 0x33
        )

        assertEquals(0b01011010, output.registerA)
    }

    @Test
    fun `XOR $4`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x14, 0x00, 0x04, 0xff, 0x33
        )

        assertEquals(0b01011010, output.registerA)
    }

    @Test
    fun `XOR (CD)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 4, registerD = 0),
            0x15, 0xff, 0xff, 0xff, 0x33
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00000100, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }

    @Test
    fun `SHL carry set, top bit clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x16
        )

        assertEquals(0b11010010, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SHL carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x16
        )

        assertEquals(0b00101100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RLC carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x17
        )

        assertEquals(0b11010011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RLC carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x17
        )

        assertEquals(0b00101100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RL carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x18
        )

        assertEquals(0b11010010, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RL carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x18
        )

        assertEquals(0b00101101, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `USHR carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x19
        )

        assertEquals(0b00110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `USHR carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x19
        )

        assertEquals(0b01001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ASHR carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x1a
        )

        assertEquals(0b00110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ASHR carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x1a
        )

        assertEquals(0b11001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RRC carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x1b
        )

        assertEquals(0b10110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RRC carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x1b
        )

        assertEquals(0b01001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RRC carry clear, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = false)),
            0x1b
        )

        assertEquals(0b00110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RRC carry set, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = true)),
            0x1b
        )

        assertEquals(0b11001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RR carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x1c
        )

        assertEquals(0b10110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RR carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x1c
        )

        assertEquals(0b01001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RR carry clear, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = false)),
            0x1c
        )

        assertEquals(0b10110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RR carry set, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = true)),
            0x1c
        )

        assertEquals(0b01001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SWAP B`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerB = 0b00110011),
            0x1d
        )

        assertEquals(0b00110011, output.registerA)
        assertEquals(0b01101001, output.registerB)
    }

    @Test
    fun `SWAP C`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 0b00110011),
            0x1e
        )

        assertEquals(0b00110011, output.registerA)
        assertEquals(0b01101001, output.registerC)
    }

    @Test
    fun `SWAP D`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerD = 0b00110011),
            0x1f
        )

        assertEquals(0b00110011, output.registerA)
        assertEquals(0b01101001, output.registerD)
    }

    @Test
    fun `ADD B`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 12, flags = Flags(carry = true)),
            0x20
        )

        assertEquals(112, output.registerA)
        assertEquals(12, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ADDC B, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = false)),
            0x21
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC B, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = true)),
            0x21
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADD C`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 12, flags = Flags(carry = true)),
            0x22
        )

        assertEquals(112, output.registerA)
        assertEquals(12, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ADDC C, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = false)),
            0x23
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC C, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = true)),
            0x23
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADD D`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 12, flags = Flags(carry = true)),
            0x24
        )

        assertEquals(112, output.registerA)
        assertEquals(12, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ADDC D, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = false)),
            0x25
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC D, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = true)),
            0x25
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADD i`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x26, 12
        )

        assertEquals(112, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ADDC i, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x27, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC i, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x27, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADD (L)`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x28, 0x00, 0x04, 0xff, 12
        )

        assertEquals(112, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ADDC (L), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x29, 0x00, 0x04, 0xff, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC (L), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x29, 0x00, 0x04, 0xff, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADD (CD)`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = true)),
            0x2a, 0x00, 0x04, 0xff, 12
        )

        assertEquals(112, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ADDC (CD), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = false)),
            0x2b, 0xff, 0xff, 0xff, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC (CD), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = true)),
            0x2b, 0xff, 0xff, 0xff, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SUB B`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 12, flags = Flags(carry = false)),
            0x2c
        )

        assertEquals(88, output.registerA)
        assertEquals(12, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SUBC B, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = true)),
            0x2d
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC B, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = false)),
            0x2d
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUB C`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 12, flags = Flags(carry = false)),
            0x2e
        )

        assertEquals(88, output.registerA)
        assertEquals(12, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SUBC C, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = true)),
            0x2f
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC C, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = false)),
            0x2f
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUB D`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 12, flags = Flags(carry = false)),
            0x30
        )

        assertEquals(88, output.registerA)
        assertEquals(12, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SUBC D, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = true)),
            0x31
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC D, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = false)),
            0x31
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUB i`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x32, 12
        )

        assertEquals(88, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SUBC i, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x33, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC i, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x33, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUB (L)`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x34, 0x00, 0x04, 0xff, 12
        )

        assertEquals(88, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SUBC (L), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x35, 0x00, 0x04, 0xff, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC (L), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x35, 0x00, 0x04, 0xff, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUB (CD)`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = false)),
            0x36, 0x00, 0x04, 0xff, 12
        )

        assertEquals(88, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SUBC (CD), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = true)),
            0x37, 0xff, 0xff, 0xff, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC (CD), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = false)),
            0x37, 0xff, 0xff, 0xff, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ISUB B`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 12, flags = Flags(carry = false)),
            0x38
        )

        assertEquals(168, output.registerA)
        assertEquals(12, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ISUBC B, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = true)),
            0x39
        )

        assertEquals(100, output.registerA)
        assertEquals(200, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ISUBC B, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = false)),
            0x39
        )

        assertEquals(99, output.registerA)
        assertEquals(200, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ISUB i`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x3a, 12
        )

        assertEquals(168, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ISUBC i, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x3b, 200
        )

        assertEquals(100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ISUBC i, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x3b, 200
        )

        assertEquals(99, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ISUB (L)`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x3c, 0x00, 0x04, 0xff, 12
        )

        assertEquals(168, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ISUBC (L), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x3d, 0x00, 0x04, 0xff, 200
        )

        assertEquals(100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ISUBC (L), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x3d, 0x00, 0x04, 0xff, 200
        )

        assertEquals(99, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ISUB (CD)`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = false)),
            0x3e, 0x00, 0x04, 0xff, 12
        )

        assertEquals(168, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `ISUBC (CD), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = true)),
            0x3f, 0xff, 0xff, 0xff, 200
        )

        assertEquals(100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ISUBC (CD), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = false)),
            0x3f, 0xff, 0xff, 0xff, 200
        )

        assertEquals(99, output.registerA)
        assertTrue(output.flags.carry)
    }

}

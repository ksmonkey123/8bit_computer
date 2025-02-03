package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.microcode.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogicAndRollCommandsTest {

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
            0x10
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00110011, output.registerB)
    }

    @Test
    fun `AND C`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 0b00110011),
            0x11
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00110011, output.registerC)
    }

    @Test
    fun `AND D`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerD = 0b00110011),
            0x12
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00110011, output.registerD)
    }

    @Test
    fun `AND 51`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x13, 0x33
        )

        assertEquals(0b00100001, output.registerA)
    }

    @Test
    fun `AND $4`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x14, 0x00, 0x04, 0xff, 0x33
        )

        assertEquals(0b00100001, output.registerA)
    }

    @Test
    fun `AND (CD + l)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 3, registerD = 0),
            0x15, 0x01, 0xff, 0xff, 0x33
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00000011, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }

    @Test
    fun `AND (SP + l)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, stackPointer = 0x0003, registerC = 0, registerD = 0),
            0x16, 0x01, 0xff, 0xff, 0x33
        )

        assertEquals(0b00100001, output.registerA)
        assertEquals(0b00000000, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }

    @Test
    fun `IOR B`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerB = 0b00110011),
            0x18
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00110011, output.registerB)
    }

    @Test
    fun `IOR C`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 0b00110011),
            0x19
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00110011, output.registerC)
    }

    @Test
    fun `IOR D`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerD = 0b00110011),
            0x1a
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00110011, output.registerD)
    }

    @Test
    fun `IOR 51`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x1b, 0x33
        )

        assertEquals(0b01111011, output.registerA)
    }

    @Test
    fun `IOR $4`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x1c, 0x00, 0x04, 0xff, 0x33
        )

        assertEquals(0b01111011, output.registerA)
    }

    @Test
    fun `IOR (CD + l)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 4, registerD = 0),
            0x1d, 0x00, 0xff, 0xff, 0x33
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00000100, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }

    @Test
    fun `IOR (SP + l)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 4, registerD = 0, stackPointer = 0x0003),
            0x1e, 0x01, 0xff, 0xff, 0x33
        )

        assertEquals(0b01111011, output.registerA)
        assertEquals(0b00000100, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }

    @Test
    fun `NOT A`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x3c
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
            0x3d
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
            0x3e
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
            0x3f
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
            0x20
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00110011, output.registerB)
    }

    @Test
    fun `XOR C`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 0b00110011),
            0x21
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00110011, output.registerC)
    }

    @Test
    fun `XOR D`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerD = 0b00110011),
            0x22
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00110011, output.registerD)
    }

    @Test
    fun `XOR 51`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x23, 0x33
        )

        assertEquals(0b01011010, output.registerA)
    }

    @Test
    fun `XOR $4`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001),
            0x24, 0x00, 0x04, 0xff, 0x33
        )

        assertEquals(0b01011010, output.registerA)
    }

    @Test
    fun `XOR (CD + l)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 4, registerD = 0),
            0x25, 0x00, 0xff, 0xff, 0x33
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00000100, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }
    @Test
    fun `XOR (SP + l)`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, stackPointer = 0x0003, registerC = 4, registerD = 0),
            0x26, 0x01, 0xff, 0xff, 0x33
        )

        assertEquals(0b01011010, output.registerA)
        assertEquals(0b00000100, output.registerC)
        assertEquals(0b00000000, output.registerD)
    }

    @Test
    fun `RLC carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x40
        )

        assertEquals(0b11010011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RLC carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x40
        )

        assertEquals(0b00101100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RL carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x41
        )

        assertEquals(0b11010010, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RL carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x41
        )

        assertEquals(0b00101101, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RRA carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x44
        )

        assertEquals(0b00110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RRA carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x44
        )

        assertEquals(0b11001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RRC carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x42
        )

        assertEquals(0b10110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RRC carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x42
        )

        assertEquals(0b01001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RRC carry clear, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = false)),
            0x42
        )

        assertEquals(0b00110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RRC carry set, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = true)),
            0x42
        )

        assertEquals(0b11001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RR carry set, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = true)),
            0x43
        )

        assertEquals(0b10110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RR carry clear, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = false)),
            0x43
        )

        assertEquals(0b01001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `RR carry clear, top clear`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, flags = Flags(carry = false)),
            0x43
        )

        assertEquals(0b10110100, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `RR carry set, top bit set`() {
        val output = execute(
            ProcessorState(registerA = 0b10010110, flags = Flags(carry = true)),
            0x43
        )

        assertEquals(0b01001011, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `swp B`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerB = 0b00110011),
            0x45
        )

        assertEquals(0b00110011, output.registerA)
        assertEquals(0b01101001, output.registerB)
    }

    @Test
    fun `swp C`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerC = 0b00110011),
            0x46
        )

        assertEquals(0b00110011, output.registerA)
        assertEquals(0b01101001, output.registerC)
    }

    @Test
    fun `swp D`() {
        val output = execute(
            ProcessorState(registerA = 0b01101001, registerD = 0b00110011),
            0x47
        )

        assertEquals(0b00110011, output.registerA)
        assertEquals(0b01101001, output.registerD)
    }

}

package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.microcode.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArithmeticCommandsTest {

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
    fun `ADDC B, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = false)),
            0x20
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC B, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = true)),
            0x20
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC C, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = false)),
            0x21
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC C, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = true)),
            0x21
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC D, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = false)),
            0x22
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC D, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = true)),
            0x22
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC i, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x23, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC i, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x23, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC (L), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x24, 0x00, 0x04, 0xff, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC (L), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x24, 0x00, 0x04, 0xff, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC (CD), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = false)),
            0x25, 0xff, 0xff, 0xff, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADDC (CD), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = true)),
            0x25, 0xff, 0xff, 0xff, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SUBC B, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = true)),
            0x28
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC B, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = false)),
            0x28
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC C, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = true)),
            0x29
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC C, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = false)),
            0x29
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC D, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = true)),
            0x2a
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC D, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = false)),
            0x2a
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC i, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x2b, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC i, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x2b, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC (L), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x2c, 0x00, 0x04, 0xff, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC (L), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x2c, 0x00, 0x04, 0xff, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC (CD), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = true)),
            0x2d, 0xff, 0xff, 0xff, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SUBC (CD), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 4, registerD = 0, flags = Flags(carry = false)),
            0x2d, 0xff, 0xff, 0xff, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `DECC A carry set`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = true)),
            0x30
        )

        assertEquals(12, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC A carry clear`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = false)),
            0x30
        )

        assertEquals(11, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC B carry set`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = true)),
            0x31
        )

        assertEquals(12, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC B carry clear`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = false)),
            0x31
        )

        assertEquals(11, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC C carry set`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = true)),
            0x32
        )

        assertEquals(12, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC C carry clear`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = false)),
            0x32
        )

        assertEquals(11, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC D carry set`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = true)),
            0x33
        )

        assertEquals(12, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC D carry clear`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = false)),
            0x33
        )

        assertEquals(11, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `INCC A carry clear`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = false)),
            0x38
        )

        assertEquals(12, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC A carry set`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = true)),
            0x38
        )

        assertEquals(13, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC B carry clear`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = false)),
            0x39
        )

        assertEquals(12, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC B carry set`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = true)),
            0x39
        )

        assertEquals(13, output.registerB)
        assertFalse(output.flags.carry)
    }
    
    @Test
    fun `INCC C carry clear`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = false)),
            0x3a
        )

        assertEquals(12, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC C carry set`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = true)),
            0x3a
        )

        assertEquals(13, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC D carry clear`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = false)),
            0x3b
        )

        assertEquals(12, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC D carry set`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = true)),
            0x3b
        )

        assertEquals(13, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC A, carry set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x40
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC A, carry clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x40
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC B, carry set`() {
        val output = execute(
            ProcessorState(registerB = 100, flags = Flags(carry = true)),
            0x41
        )

        assertEquals(156, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC B, carry clear`() {
        val output = execute(
            ProcessorState(registerB = 100, flags = Flags(carry = false)),
            0x41
        )

        assertEquals(155, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC C, carry set`() {
        val output = execute(
            ProcessorState(registerC = 100, flags = Flags(carry = true)),
            0x42
        )

        assertEquals(156, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC C, carry clear`() {
        val output = execute(
            ProcessorState(registerC = 100, flags = Flags(carry = false)),
            0x42
        )

        assertEquals(155, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC D, carry set`() {
        val output = execute(
            ProcessorState(registerD = 100, flags = Flags(carry = true)),
            0x43
        )

        assertEquals(156, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC D, carry clear`() {
        val output = execute(
            ProcessorState(registerD = 100, flags = Flags(carry = false)),
            0x43
        )

        assertEquals(155, output.registerD)
        assertFalse(output.flags.carry)
    }
    
}

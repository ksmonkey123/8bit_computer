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

    @Test
    fun `DEC A no underflow`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = true)),
            0x40
        )

        assertEquals(11, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC A with underflow`() {
        val output = execute(
            ProcessorState(registerA = 0, flags = Flags(carry = true)),
            0x40
        )

        assertEquals(255, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `DEC B no underflow`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = true)),
            0x41
        )

        assertEquals(11, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC B with underflow`() {
        val output = execute(
            ProcessorState(registerB = 0, flags = Flags(carry = true)),
            0x41
        )

        assertEquals(255, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `DEC C no underflow`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = true)),
            0x42
        )

        assertEquals(11, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC C with underflow`() {
        val output = execute(
            ProcessorState(registerC = 0, flags = Flags(carry = true)),
            0x42
        )

        assertEquals(255, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `DEC D no underflow`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = true)),
            0x43
        )

        assertEquals(11, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC D with underflow`() {
        val output = execute(
            ProcessorState(registerD = 0, flags = Flags(carry = true)),
            0x43
        )

        assertEquals(255, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `DECC A carry set`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = true)),
            0x44
        )

        assertEquals(12, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC A carry clear`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = false)),
            0x44
        )

        assertEquals(11, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC B carry set`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = true)),
            0x45
        )

        assertEquals(12, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC B carry clear`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = false)),
            0x45
        )

        assertEquals(11, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC C carry set`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = true)),
            0x46
        )

        assertEquals(12, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC C carry clear`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = false)),
            0x46
        )

        assertEquals(11, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC D carry set`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = true)),
            0x47
        )

        assertEquals(12, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DECC D carry clear`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = false)),
            0x47
        )

        assertEquals(11, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `INC A no overflow`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = false)),
            0x48
        )

        assertEquals(13, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC A with overflow`() {
        val output = execute(
            ProcessorState(registerA = 255, flags = Flags(carry = true)),
            0x48
        )

        assertEquals(0, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `INC B no overflow`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = false)),
            0x49
        )

        assertEquals(13, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC B with overflow`() {
        val output = execute(
            ProcessorState(registerB = 255, flags = Flags(carry = true)),
            0x49
        )

        assertEquals(0, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `INC C no overflow`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = false)),
            0x4a
        )

        assertEquals(13, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC C with overflow`() {
        val output = execute(
            ProcessorState(registerC = 255, flags = Flags(carry = true)),
            0x4a
        )

        assertEquals(0, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `INC D no overflow`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = false)),
            0x4b
        )

        assertEquals(13, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC D with overflow`() {
        val output = execute(
            ProcessorState(registerD = 255, flags = Flags(carry = true)),
            0x4b
        )

        assertEquals(0, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `INCC A carry clear`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = false)),
            0x4c
        )

        assertEquals(12, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC A carry set`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = true)),
            0x4c
        )

        assertEquals(13, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC B carry clear`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = false)),
            0x4d
        )

        assertEquals(12, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC B carry set`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = true)),
            0x4d
        )

        assertEquals(13, output.registerB)
        assertFalse(output.flags.carry)
    }
    
    @Test
    fun `INCC C carry clear`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = false)),
            0x4e
        )

        assertEquals(12, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC C carry set`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = true)),
            0x4e
        )

        assertEquals(13, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC D carry clear`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = false)),
            0x4f
        )

        assertEquals(12, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INCC D carry set`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = true)),
            0x4f
        )

        assertEquals(13, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMP A, carry set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x50
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMP A, carry clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x50
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMP A with overflow`() {
        val output = execute(
            ProcessorState(registerA = 0, flags = Flags(carry = false)),
            0x50
        )

        assertEquals(0, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `COMP B, carry set`() {
        val output = execute(
            ProcessorState(registerB = 100, flags = Flags(carry = true)),
            0x51
        )

        assertEquals(156, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMP B, carry clear`() {
        val output = execute(
            ProcessorState(registerB = 100, flags = Flags(carry = false)),
            0x51
        )

        assertEquals(156, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMP C, carry set`() {
        val output = execute(
            ProcessorState(registerC = 100, flags = Flags(carry = true)),
            0x52
        )

        assertEquals(156, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMP C, carry clear`() {
        val output = execute(
            ProcessorState(registerC = 100, flags = Flags(carry = false)),
            0x52
        )

        assertEquals(156, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMP D, carry set`() {
        val output = execute(
            ProcessorState(registerD = 100, flags = Flags(carry = true)),
            0x53
        )

        assertEquals(156, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMP D, carry clear`() {
        val output = execute(
            ProcessorState(registerD = 100, flags = Flags(carry = false)),
            0x53
        )

        assertEquals(156, output.registerD)
        assertFalse(output.flags.carry)
    }
    
    @Test
    fun `COMPC A, carry set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x54
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC A, carry clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x54
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC B, carry set`() {
        val output = execute(
            ProcessorState(registerB = 100, flags = Flags(carry = true)),
            0x55
        )

        assertEquals(156, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC B, carry clear`() {
        val output = execute(
            ProcessorState(registerB = 100, flags = Flags(carry = false)),
            0x55
        )

        assertEquals(155, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC C, carry set`() {
        val output = execute(
            ProcessorState(registerC = 100, flags = Flags(carry = true)),
            0x56
        )

        assertEquals(156, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC C, carry clear`() {
        val output = execute(
            ProcessorState(registerC = 100, flags = Flags(carry = false)),
            0x56
        )

        assertEquals(155, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC D, carry set`() {
        val output = execute(
            ProcessorState(registerD = 100, flags = Flags(carry = true)),
            0x57
        )

        assertEquals(156, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `COMPC D, carry clear`() {
        val output = execute(
            ProcessorState(registerD = 100, flags = Flags(carry = false)),
            0x57
        )

        assertEquals(155, output.registerD)
        assertFalse(output.flags.carry)
    }
    
}

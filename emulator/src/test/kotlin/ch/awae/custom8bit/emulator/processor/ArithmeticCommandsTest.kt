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
    fun `ADC B, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = false)),
            0x00
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC B, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = true)),
            0x00
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC C, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = false)),
            0x01
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC C, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = true)),
            0x01
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC D, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = false)),
            0x02
        )

        assertEquals(44, output.registerA)
        assertEquals(200, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC D, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = true)),
            0x02
        )

        assertEquals(45, output.registerA)
        assertEquals(200, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC i, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x03, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC i, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x03, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC (L), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x04, 0x00, 0x04, 0xff, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC (L), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x04, 0x00, 0x04, 0xff, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC (CD + l), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 3, registerD = 0, flags = Flags(carry = false)),
            0x05, 0x01, 0xff, 0xff, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC (CD + l), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 3, registerD = 0, flags = Flags(carry = true)),
            0x05, 0x01, 0xff, 0xff, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }
    @Test
    fun `ADC (SP + l), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, stackPointer = 0x0003, flags = Flags(carry = false)),
            0x06, 0x01, 0xff, 0xff, 200
        )

        assertEquals(44, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `ADC (SP + l), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, stackPointer = 0x0003, flags = Flags(carry = true)),
            0x06, 0x01, 0xff, 0xff, 200
        )

        assertEquals(45, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `SBC B, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = true)),
            0x08
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC B, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = false)),
            0x08
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC C, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = true)),
            0x09
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC C, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 200, flags = Flags(carry = false)),
            0x09
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC D, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = true)),
            0x0a
        )

        assertEquals(156, output.registerA)
        assertEquals(200, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC D, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 200, flags = Flags(carry = false)),
            0x0a
        )

        assertEquals(155, output.registerA)
        assertEquals(200, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC i, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x0b, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC i, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x0b, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC (L), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x0c, 0x00, 0x04, 0xff, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC (L), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x0c, 0x00, 0x04, 0xff, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC (CD + l), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 3, registerD = 0, flags = Flags(carry = true)),
            0x0d, 0x01, 0xff, 0xff, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC (CD + l), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 3, registerD = 0, flags = Flags(carry = false)),
            0x0d, 0x01, 0xff, 0xff, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC (SP + l), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, stackPointer = 0x0003, flags = Flags(carry = true)),
            0x0e, 0x01, 0xff, 0xff, 200
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `SBC (SP + l), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, stackPointer = 0x0003, flags = Flags(carry = false)),
            0x0e, 0x01, 0xff, 0xff, 200
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `DEC A carry set`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = true)),
            0x30
        )

        assertEquals(12, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC A carry clear`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = false)),
            0x30
        )

        assertEquals(11, output.registerA)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC B carry set`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = true)),
            0x31
        )

        assertEquals(12, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC B carry clear`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = false)),
            0x31
        )

        assertEquals(11, output.registerB)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC C carry set`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = true)),
            0x32
        )

        assertEquals(12, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC C carry clear`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = false)),
            0x32
        )

        assertEquals(11, output.registerC)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC D carry set`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = true)),
            0x33
        )

        assertEquals(12, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `DEC D carry clear`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = false)),
            0x33
        )

        assertEquals(11, output.registerD)
        assertTrue(output.flags.carry)
    }

    @Test
    fun `INC A carry clear`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = false)),
            0x34
        )

        assertEquals(12, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC A carry set`() {
        val output = execute(
            ProcessorState(registerA = 12, flags = Flags(carry = true)),
            0x34
        )

        assertEquals(13, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC B carry clear`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = false)),
            0x35
        )

        assertEquals(12, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC B carry set`() {
        val output = execute(
            ProcessorState(registerB = 12, flags = Flags(carry = true)),
            0x35
        )

        assertEquals(13, output.registerB)
        assertFalse(output.flags.carry)
    }
    
    @Test
    fun `INC C carry clear`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = false)),
            0x36
        )

        assertEquals(12, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC C carry set`() {
        val output = execute(
            ProcessorState(registerC = 12, flags = Flags(carry = true)),
            0x36
        )

        assertEquals(13, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC D carry clear`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = false)),
            0x37
        )

        assertEquals(12, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `INC D carry set`() {
        val output = execute(
            ProcessorState(registerD = 12, flags = Flags(carry = true)),
            0x37
        )

        assertEquals(13, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `NEG A, carry set`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = true)),
            0x38
        )

        assertEquals(156, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `NEG A, carry clear`() {
        val output = execute(
            ProcessorState(registerA = 100, flags = Flags(carry = false)),
            0x38
        )

        assertEquals(155, output.registerA)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `NEG B, carry set`() {
        val output = execute(
            ProcessorState(registerB = 100, flags = Flags(carry = true)),
            0x39
        )

        assertEquals(156, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `NEG B, carry clear`() {
        val output = execute(
            ProcessorState(registerB = 100, flags = Flags(carry = false)),
            0x39
        )

        assertEquals(155, output.registerB)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `NEG C, carry set`() {
        val output = execute(
            ProcessorState(registerC = 100, flags = Flags(carry = true)),
            0x3a
        )

        assertEquals(156, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `NEG C, carry clear`() {
        val output = execute(
            ProcessorState(registerC = 100, flags = Flags(carry = false)),
            0x3a
        )

        assertEquals(155, output.registerC)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `NEG D, carry set`() {
        val output = execute(
            ProcessorState(registerD = 100, flags = Flags(carry = true)),
            0x3b
        )

        assertEquals(156, output.registerD)
        assertFalse(output.flags.carry)
    }

    @Test
    fun `NEG D, carry clear`() {
        val output = execute(
            ProcessorState(registerD = 100, flags = Flags(carry = false)),
            0x3b
        )

        assertEquals(155, output.registerD)
        assertFalse(output.flags.carry)
    }


    @Test
    fun `CMP B, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = true)),
            0x28
        )

        assertEquals(100, output.registerA)
        assertEquals(200, output.registerB)
        assertFalse(output.flags.carry)
        assertTrue(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP B, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerB = 200, flags = Flags(carry = false)),
            0x28
        )

        assertEquals(100, output.registerA)
        assertEquals(200, output.registerB)
        assertFalse(output.flags.carry)
        assertTrue(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP C, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 100, flags = Flags(carry = true)),
            0x29
        )

        assertEquals(100, output.registerA)
        assertEquals(100, output.registerC)
        assertTrue(output.flags.carry)
        assertFalse(output.flags.negative)
        assertTrue(output.flags.zero)
    }

    @Test
    fun `CMP C, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 100, flags = Flags(carry = false)),
            0x29
        )

        assertEquals(100, output.registerA)
        assertEquals(100, output.registerC)
        assertTrue(output.flags.carry)
        assertFalse(output.flags.negative)
        assertTrue(output.flags.zero)
    }

    @Test
    fun `CMP D, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 90, flags = Flags(carry = true)),
            0x2a
        )

        assertEquals(100, output.registerA)
        assertEquals(90, output.registerD)
        assertTrue(output.flags.carry)
        assertFalse(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP D, flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerD = 90, flags = Flags(carry = false)),
            0x2a
        )

        assertEquals(100, output.registerA)
        assertEquals(90, output.registerD)
        assertTrue(output.flags.carry)
        assertFalse(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP i, flag clear`() {
        val output = execute(
            ProcessorState(registerA = 255, flags = Flags(carry = true)),
            0x2b, 254
        )

        assertEquals(255, output.registerA)
        assertTrue(output.flags.carry)
        assertFalse(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP i, flag set`() {
        val output = execute(
            ProcessorState(registerA = 255, flags = Flags(carry = false)),
            0x2b, 254
        )

        assertEquals(255, output.registerA)
        assertTrue(output.flags.carry)
        assertFalse(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP (L), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 255, flags = Flags(carry = true)),
            0x2c, 0x00, 0x04, 0xff, 1
        )

        assertEquals(255, output.registerA)
        assertTrue(output.flags.carry)
        assertTrue(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP (L), flag set`() {
        val output = execute(
            ProcessorState(registerA = 255, flags = Flags(carry = false)),
            0x2c, 0x00, 0x04, 0xff, 1
        )

        assertEquals(255, output.registerA)
        assertTrue(output.flags.carry)
        assertTrue(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP (CD + l), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 3, registerD = 0, flags = Flags(carry = true)),
            0x2d, 0x01, 0xff, 0xff, 200
        )

        assertEquals(100, output.registerA)
        assertFalse(output.flags.carry)
        assertTrue(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP (CD + l), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, registerC = 3, registerD = 0, flags = Flags(carry = false)),
            0x2d, 0x01, 0xff, 0xff, 200
        )

        assertEquals(100, output.registerA)
        assertFalse(output.flags.carry)
        assertTrue(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP (SP + l), flag clear`() {
        val output = execute(
            ProcessorState(registerA = 100, stackPointer = 0x0003, flags = Flags(carry = true)),
            0x2e, 0x01, 0xff, 0xff, 200
        )

        assertEquals(100, output.registerA)
        assertFalse(output.flags.carry)
        assertTrue(output.flags.negative)
        assertFalse(output.flags.zero)
    }

    @Test
    fun `CMP (SP + l), flag set`() {
        val output = execute(
            ProcessorState(registerA = 100, stackPointer = 0x0003, flags = Flags(carry = false)),
            0x2e, 0x01, 0xff, 0xff, 200
        )

        assertEquals(100, output.registerA)
        assertFalse(output.flags.carry)
        assertTrue(output.flags.negative)
        assertFalse(output.flags.zero)
    }
    
}

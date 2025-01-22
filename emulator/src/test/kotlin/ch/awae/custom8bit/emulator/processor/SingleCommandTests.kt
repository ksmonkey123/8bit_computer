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


}
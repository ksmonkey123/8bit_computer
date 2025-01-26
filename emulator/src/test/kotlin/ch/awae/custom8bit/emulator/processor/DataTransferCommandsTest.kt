package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.microcode.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataTransferCommandsTest {

    private val microcode = Microcode(Compiler.compileInstructionSet(INSTRUCTION_SET))

    private val ram = RamChip8k(57344)

    private fun execute(
        program: IntArray,
        inputState: ProcessorState = ProcessorState()
    ): ProcessorState {
        val code = program.map { it.toByte() }.toByteArray()
        val pu = ProcessingUnit(
            microcode,
            StandardMemoryBus(RomChip8k(0, code), ram)
        )
        return pu.executeNextCommand(inputState)
    }

    private fun execute(inputState: ProcessorState, vararg programBytes: Int) = execute(programBytes, inputState)

    @ParameterizedTest
    @ValueSource(ints = [0x60, 0x65, 0x6a, 0x6f])
    fun `mov x x, no change expected`(command: Int) {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            command
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov A B`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x61
        )

        assertEquals(24, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov A C`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x62
        )

        assertEquals(36, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov A D`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x63
        )

        assertEquals(48, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov B A`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x64
        )

        assertEquals(12, output.registerA)
        assertEquals(12, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov B C`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x66
        )

        assertEquals(12, output.registerA)
        assertEquals(36, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov B D`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x67
        )

        assertEquals(12, output.registerA)
        assertEquals(48, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov C A`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x68
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(12, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov C B`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x69
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(24, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov C D`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x6b
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(48, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov D A`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x6c
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(12, output.registerD)
    }

    @Test
    fun `mov D B`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x6d
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(24, output.registerD)
    }

    @Test
    fun `mov D C`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x6e
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(36, output.registerD)
    }

    @Test
    fun `LOAD A (L)`() {
        val output = execute(
            ProcessorState(),
            0x70, 0x00, 0x04, 0xff, 0x69
        )

        assertEquals(0x69, output.registerA)
    }

    @Test
    fun `LOAD B (L)`() {
        val output = execute(
            ProcessorState(),
            0x71, 0x00, 0x04, 0xff, 0x69
        )

        assertEquals(0x69, output.registerB)
    }

    @Test
    fun `LOAD C (L)`() {
        val output = execute(
            ProcessorState(),
            0x72, 0x00, 0x04, 0xff, 0x69
        )

        assertEquals(0x69, output.registerC)
    }

    @Test
    fun `LOAD D (L)`() {
        val output = execute(
            ProcessorState(),
            0x73, 0x00, 0x04, 0xff, 0x69
        )

        assertEquals(0x69, output.registerD)
    }

    @Test
    fun `LOAD A (CD)`() {
        val output = execute(
            ProcessorState(registerC = 0x04, registerD = 0x00),
            0x74, 0xff, 0xff, 0xff, 0x69
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x04, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `LOAD B (CD)`() {
        val output = execute(
            ProcessorState(registerC = 0x04, registerD = 0x00),
            0x75, 0xff, 0xff, 0xff, 0x69
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x69, output.registerB)
        assertEquals(0x04, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `LOAD A i`() {
        val output = execute(
            ProcessorState(),
            0x7c, 0x69
        )

        assertEquals(0x69, output.registerA)
    }

    @Test
    fun `LOAD B i`() {
        val output = execute(
            ProcessorState(),
            0x7d, 0x69
        )

        assertEquals(0x69, output.registerB)
    }

    @Test
    fun `LOAD C i`() {
        val output = execute(
            ProcessorState(),
            0x7e, 0x69
        )

        assertEquals(0x69, output.registerC)
    }

    @Test
    fun `LOAD D i`() {
        val output = execute(
            ProcessorState(),
            0x7f, 0x69
        )

        assertEquals(0x69, output.registerD)
    }

    @Test
    fun `STORE A (L)`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69),
            0x80, 0xff, 0xab
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `STORE B (L)`() {
        ram.clear()
        execute(
            ProcessorState(registerB = 0x69),
            0x81, 0xff, 0xab
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `STORE C (L)`() {
        ram.clear()
        execute(
            ProcessorState(registerC = 0x69),
            0x82, 0xff, 0xab
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `STORE D (L)`() {
        ram.clear()
        execute(
            ProcessorState(registerD = 0x69),
            0x83, 0xff, 0xab
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `STORE A (CD)`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69, registerC = 0xab, registerD = 0xff),
            0x84
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `STORE B (CD)`() {
        ram.clear()
        execute(
            ProcessorState(registerB = 0x69, registerC = 0xab, registerD = 0xff),
            0x85
        )

        assertEquals(0x69, ram.read(0xffab))
    }


}

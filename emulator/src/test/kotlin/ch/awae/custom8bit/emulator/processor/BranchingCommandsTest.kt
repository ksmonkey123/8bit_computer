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
class BranchingCommandsTest {

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
    @CsvFileSource(resources = ["/branching_commands.csv"], numLinesToSkip = 1)
    fun `branch test`(command: Int, carry: Boolean, sign: Int, shouldBranch: Boolean) {
        val output = execute(
            ProcessorState(flags = Flags(carry = carry, zero = sign == 0, negative = sign < 0)),
            command, 0xab, 0xcd
        )

        if (shouldBranch) {
            assertEquals(0xabcd, output.programCounter)
        } else {
            assertEquals(0x0003, output.programCounter)
        }
    }

    @Test
    fun `jsr i`() {
        ram.clear()
        val output = execute(
            ProcessorState(stackPointer = 0xffbb),
            0xba, 0x33, 0x44
        )

        assertEquals(0x3344, output.programCounter)
        assertEquals(0xffb9, output.stackPointer)
        assertEquals(0x03, ram.read(0xffb9))
        assertEquals(0x00, ram.read(0xffba))
    }

    @Test
    fun `ret from subroutine`() {
        ram.clear()
        ram.write(0xffb9, 0x69)
        ram.write(0xffba, 0x96)

        val output = execute(
            ProcessorState(stackPointer = 0xffb9),
            0xbb
        )

        assertEquals(0xffbb, output.stackPointer)
        assertEquals(0x9669, output.programCounter)
    }

}

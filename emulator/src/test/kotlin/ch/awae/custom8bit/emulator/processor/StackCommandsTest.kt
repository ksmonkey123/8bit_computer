package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.microcode.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StackCommandsTest {

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

    @Test
    fun spa() {
        val output = execute(
            ProcessorState(stackPointer = 0xfeda),
            0x8e, 0x10
        )

        assertEquals(0xfeca, output.stackPointer)
    }

    @Test
    fun spf() {
        val output = execute(
            ProcessorState(stackPointer = 0xfeca),
            0x8f, 0x10
        )

        assertEquals(0xfeda, output.stackPointer)
    }

}

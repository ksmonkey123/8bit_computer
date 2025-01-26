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



}

package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.microcode.*
import kotlin.test.*

class ProcessingUnitTest {

    private fun execute(
        program: IntArray,
        iterations: Int,
        inputState: ProcessorState = ProcessorState()
    ): ProcessorState {
        val code = program.map { it.toByte() }.toByteArray()

        val pu = ProcessingUnit(
            Microcode(Compiler.compileInstructionSet(INSTRUCTION_SET)),
            StandardMemoryBus(RomChip8k(0, code)),
        )

        var state = inputState
        for (i in 0 until iterations) {
            state = pu.executeNextCommand(state)
        }
        return state
    }

    @Test
    fun test() {
        val program = intArrayOf(
            // LLOAD A, 0x0f
            0x98, 0x0f,
            // LLOAD B, 0x01
            0x99, 0x01,
            // ADD C
            0x7a,
        )

        val result = execute(program, 3)

        assertEquals(15, result.registerA)
        assertEquals(1, result.registerB)
        assertEquals(16, result.registerC)
    }

    @Test
    fun testMemoryLoad() {
        val program = intArrayOf(
            // LOAD A, $0004
            0x90, 0x00, 0x04,
            // NOP
            0xff,
            // referenced value $0004
            0x69
        )
        val result = execute(program, 1)
        assertEquals(0x69, result.registerA)
        assertEquals(0x00, result.literal2)
        assertEquals(0x04, result.literal1)
        assertEquals(0x0003, result.programCounter)
    }
}
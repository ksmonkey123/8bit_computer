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
    fun `load A, load B, A = A + B`() {
        val program = intArrayOf(
            // LOAD A 15
            0x6c, 0x0f,
            // LOAD B 1
            0x6d, 0x01,
            // ADD B
            0x20,
        )

        val result = execute(program, 3)

        assertEquals(16, result.registerA)
        assertEquals(1, result.registerB)
    }

    @Test
    fun testMemoryLoad() {
        val program = intArrayOf(
            // LOAD A, $0004
            0x60, 0x00, 0x04,
            // HALT
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

    @Test
    fun `swap A B`() {
        val program = intArrayOf(
            // LOAD A, 105
            0x6c, 0x69,
            // LOAD B, 150
            0x6d, 0x96,
            // SWAP B
            0x1d
        )

        val result = execute(program, 3)
        assertEquals(150, result.registerA)
        assertEquals(105, result.registerB)
    }
}
package ch.awae.custom8bit.emulator.programs

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.emulator.processor.*
import ch.awae.custom8bit.microcode.*
import kotlin.test.*

class FibonacciTest {

    private val microcode = Microcode(Compiler.compileInstructionSet(INSTRUCTION_SET))
    private val out = SerialOutputCapture(0x2000)
    private val ram = RamChip8k(57344)

    fun executeProgram(
        program: IntArray,
        inputState: ProcessorState = ProcessorState(),
    ): ProcessorState {
        val code = program.map { it.toByte() }.toByteArray()
        val pu = ProcessingUnit(
            microcode,
            StandardMemoryBus(RomChip8k(0, code), ram, out)
        )

        var state = inputState

        while (!state.halted) {
            state = pu.executeNextCommand(state)
        }

        println("finished program. ${pu.statistics}")
        return state
    }

    @Test
    fun testFibonacci() {
        val programm = intArrayOf(
            // LOAD A 1
            0x7c, 0x01,
            // LOAD B 1
            0x7d, 0x01,
            // LOAD D 10
            0x7f, 0x0a,
            // start of loop, address is 0x0006
            // carry clear
            0xfc,
            // ADD B
            0x20,
            // SWAP B
            0x1d,
            // carry clear
            0xfc,
            // DEC D
            0x33,
            // continue loop if D > 0
            // BNZ 0x0006
            0xb3, 0x00, 0x06,
            // we are done, the result lies in B. move to A
            // MOV A B
            0x61,
            // HALT
            0xff,
        )

        val output = executeProgram(programm)

        assertEquals(144, output.registerA)
    }

    @Test
    fun testFibonacci_16bit_load() {
        val programm = intArrayOf(
            // LOAD AB 0x0101
            0x7a, 0x01, 0x01,
            // LOAD D 10
            0x7f, 0x0a,
            // start of loop, address is 0x0005
            // cclr
            0xfc,
            // ADD B
            0x20,
            // SWAP B
            0x1d,
            // cclr
            0xfc,
            // DEC D
            0x33,
            // continue loop if D > 0
            // BNZ 0x0005
            0xb3, 0x00, 0x05,
            // we are done, the result lies in B. move to A
            // MOV A B
            0x61,
            // HALT
            0xff,
        )

        val output = executeProgram(programm)

        assertEquals(144, output.registerA)
    }

    @Test
    fun `fibonacci with output`() {
        out.clear()

        val programm = intArrayOf(
            // LOAD AB 0x0101
            0x7a, 0x01, 0x01,
            // STORE A 0x2000 (send 1)
            0x80, 0x20, 0x00,
            // STORE B 0x2000 (send 1)
            0x81, 0x20, 0x00,
            // LOAD D 11
            0x7f, 0x0b,
            // start of loop, address is 0x000b
            // cclr
            0xfc,
            // ADD B
            0x20,
            // STORE A 0x2000 (send next number)
            0x80, 0x20, 0x00,
            // SWAP B
            0x1d,
            // continue loop if (--D) > 0
            // cclr
            0xfc,
            // dec d
            0x33,
            // bnz 0x000b
            0xb3, 0x00, 0x0b,
            // HALT
            0xff,
        )

        executeProgram(programm)

        assertEquals(
            listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233),
            out.list
        )

    }

}
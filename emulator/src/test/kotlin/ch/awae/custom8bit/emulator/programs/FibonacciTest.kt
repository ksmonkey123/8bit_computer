package ch.awae.custom8bit.emulator.programs

import ch.awae.custom8bit.assembler.*
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
        return executeProgram(program.map { it.toByte() }.toByteArray(), inputState)
    }

    fun executeProgram(
        program: ByteArray,
        inputState: ProcessorState = ProcessorState(),
    ): ProcessorState {
        val pu = ProcessingUnit(
            microcode,
            StandardMemoryBus(RomChip8k(0, program), ram, out)
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
            // mov A #1
            0x7c, 0x01,
            // mov B #1
            0x7d, 0x01,
            // mov D #10
            0x7f, 0x0a,
            // start of loop, address is 0x0006
            // cfc
            0xfc,
            // adc B
            0x00,
            // swp B
            0x48,
            // carry clear
            0xfc,
            // dec D
            0x33,
            // continue loop if D > 0
            // bnz 0x0006
            0xb3, 0x00, 0x06,
            // we are done, the result lies in B. move to A
            // mov A B
            0x61,
            // hlt
            0xff,
        )

        val output = executeProgram(programm)

        assertEquals(144, output.registerA)
    }

    @Test
    fun testFibonacci_16bit_load() {
        val programm = intArrayOf(
            // mov AB #0x0101
            0x9a, 0x01, 0x01,
            // mov D #10
            0x7f, 0x0a,
            // start of loop, address is 0x0005
            // cfc
            0xfc,
            // adc B
            0x00,
            // swp B
            0x48,
            // carry clear
            0xfc,
            // dec D
            0x33,
            // continue loop if D > 0
            // bnz 0x0005
            0xb3, 0x00, 0x05,
            // we are done, the result lies in B. move to A
            // mov A B
            0x61,
            // hlt
            0xff,
        )

        val output = executeProgram(programm)

        assertEquals(144, output.registerA)
    }

    @Test
    fun `fibonacci with output`() {
        out.clear()

        val programm = intArrayOf(
            // mov AB #0x0101
            0x9a, 0x01, 0x01,
            // mov *0x2000 A (send 1)
            0x80, 0x20, 0x00,
            // mov *0x2000 A (send 1)
            0x81, 0x20, 0x00,
            // LOAD D 11
            0x7f, 0x0b,
            // start of loop, address is 0x000b
            // cfc
            0xfc,
            // adc B
            0x00,
            // mov *0x2000 A (send next number)
            0x80, 0x20, 0x00,
            // SWAP B
            0x48,
            // continue loop if (--D) > 0
            // cfc
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


    @Test
    fun `fibonacci with subroutine`() {
        out.clear()

        val source = """
            .vars
            	0x2000: output[1]
            
            .code 0x0000
            	mov AB #0x0101
            	mov *output A
            	mov *output B
            	mov D #11
            loop:
            	jsr next
            	; new next fibonacci number is in A
            	mov *output A
            	; decrement D and repeat if not done
            	cfc
            	dec D
            	bnz loop
            	hlt
            
            ; calculate next number and publish it
            next:
            	swp B
            	cfc
            	adc B
            	ret
        """.trimIndent()

        val program = Assembler().assemble(source)

        executeProgram(program)

        assertEquals(
            listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233),
            out.list
        )

    }

    @Test
    fun `fibonacci from source`() {
        out.clear()

        val source = """
            .vars
            	0x2000: output[1]
            
            .code 0x0000
            	mov AB #0x0101
            	mov *output A
            	mov *output B
            	mov D #11
            loop:
                cfc
                adc B
            	mov *output A
                swp B
            	cfc
            	dec D
            	bnz loop
            	hlt
        """.trimIndent()

        val program = Assembler().assemble(source)

        executeProgram(program)

        assertEquals(
            listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233),
            out.list
        )

    }

}
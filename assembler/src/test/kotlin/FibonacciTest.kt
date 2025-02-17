import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.emulator.processor.*
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


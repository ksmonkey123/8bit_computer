package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.microcode.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagementCommandsTest {

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
    fun `nop`() {
        val output = execute(
            ProcessorState(
                registerA = 12,
                registerB = 24,
                registerC = 36,
                registerD = 48,
                flags = Flags(carry = true, zero = true, negative = false)
            ),
            0xfe
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
        assertEquals(true, output.flags.carry)
        assertEquals(true, output.flags.zero)
        assertEquals(false, output.flags.negative)
    }

    @Test
    fun `halt`() {
        val output = execute(ProcessorState(), 0xff)

        assertTrue(output.halted)
    }

}

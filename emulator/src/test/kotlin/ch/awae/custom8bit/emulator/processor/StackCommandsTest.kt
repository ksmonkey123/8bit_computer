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
    fun `PUSH A`() {
        ram.clear()
        val output = execute(
            ProcessorState(registerA = 0x69, stackPointer = 0xfedc),
            0x90
        )

        assertEquals(0xfedb, output.stackPointer)
        assertEquals(0x69, ram.read(0xfedb))
    }

    @Test
    fun `PUSH B`() {
        ram.clear()
        val output = execute(
            ProcessorState(registerB = 0x69, stackPointer = 0xfedc),
            0x91
        )

        assertEquals(0xfedb, output.stackPointer)
        assertEquals(0x69, ram.read(0xfedb))
    }

    @Test
    fun `PUSH C`() {
        ram.clear()
        val output = execute(
            ProcessorState(registerC = 0x69, stackPointer = 0xfedc),
            0x92
        )

        assertEquals(0xfedb, output.stackPointer)
        assertEquals(0x69, ram.read(0xfedb))
    }

    @Test
    fun `PUSH D`() {
        ram.clear()
        val output = execute(
            ProcessorState(registerD = 0x69, stackPointer = 0xfedc),
            0x93
        )

        assertEquals(0xfedb, output.stackPointer)
        assertEquals(0x69, ram.read(0xfedb))
    }

    @Test
    fun `POP A`() {
        ram.clear()
        ram.write(0xfedb, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfedb),
            0x94
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0xfedc, output.stackPointer)
    }

    @Test
    fun `POP B`() {
        ram.clear()
        ram.write(0xfedb, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfedb),
            0x95
        )

        assertEquals(0x69, output.registerB)
        assertEquals(0xfedc, output.stackPointer)
    }

    @Test
    fun `POP C`() {
        ram.clear()
        ram.write(0xfedb, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfedb),
            0x96
        )

        assertEquals(0x69, output.registerC)
        assertEquals(0xfedc, output.stackPointer)
    }

    @Test
    fun `POP D`() {
        ram.clear()
        ram.write(0xfedb, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfedb),
            0x97
        )

        assertEquals(0x69, output.registerD)
        assertEquals(0xfedc, output.stackPointer)
    }

    @Test
    fun `PEEK A`() {
        ram.clear()
        ram.write(0xfedb, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfedb),
            0x98
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0xfedb, output.stackPointer)
    }

    @Test
    fun `PEEK B`() {
        ram.clear()
        ram.write(0xfedb, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfedb),
            0x99
        )

        assertEquals(0x69, output.registerB)
        assertEquals(0xfedb, output.stackPointer)
    }

    @Test
    fun `PEEK C`() {
        ram.clear()
        ram.write(0xfedb, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfedb),
            0x9a
        )

        assertEquals(0x69, output.registerC)
        assertEquals(0xfedb, output.stackPointer)
    }

    @Test
    fun `PEEK D`() {
        ram.clear()
        ram.write(0xfedb, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfedb),
            0x9b
        )

        assertEquals(0x69, output.registerD)
        assertEquals(0xfedb, output.stackPointer)
    }

    @Test
    fun `push ab`() {
        ram.clear()
        val output = execute(
            ProcessorState(registerA = 0x69, registerB = 0x96, stackPointer = 0xfedc),
            0x9c
        )

        assertEquals(0xfeda, output.stackPointer)
        assertEquals(0x69, ram.read(0xfeda))
        assertEquals(0x96, ram.read(0xfedb))
    }

    @Test
    fun `push cd`() {
        ram.clear()
        val output = execute(
            ProcessorState(registerC = 0x69, registerD = 0x96, stackPointer = 0xfedc),
            0x9d
        )

        assertEquals(0xfeda, output.stackPointer)
        assertEquals(0x69, ram.read(0xfeda))
        assertEquals(0x96, ram.read(0xfedb))
    }

    @Test
    fun `pop ab`() {
        ram.clear()
        ram.write(0xfeda, 0x69)
        ram.write(0xfedb, 0x96)
        val output = execute(
            ProcessorState(stackPointer = 0xfeda),
            0x9e
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
        assertEquals(0xfedc, output.stackPointer)
    }

    @Test
    fun `pop cd`() {
        ram.clear()
        ram.write(0xfeda, 0x69)
        ram.write(0xfedb, 0x96)
        val output = execute(
            ProcessorState(stackPointer = 0xfeda),
            0x9f
        )

        assertEquals(0x69, output.registerC)
        assertEquals(0x96, output.registerD)
        assertEquals(0xfedc, output.stackPointer)
    }


    @Test
    fun `peek ab`() {
        ram.clear()
        ram.write(0xfeda, 0x69)
        ram.write(0xfedb, 0x96)
        val output = execute(
            ProcessorState(stackPointer = 0xfeda),
            0x8c
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
        assertEquals(0xfeda, output.stackPointer)
    }

    @Test
    fun `peek cd`() {
        ram.clear()
        ram.write(0xfeda, 0x69)
        ram.write(0xfedb, 0x96)
        val output = execute(
            ProcessorState(stackPointer = 0xfeda),
            0x8d
        )

        assertEquals(0x69, output.registerC)
        assertEquals(0x96, output.registerD)
        assertEquals(0xfeda, output.stackPointer)
    }

    @Test
    fun salloc() {
        val output = execute(
            ProcessorState(stackPointer = 0xfeda),
            0x8a, 0x10
        )

        assertEquals(0xfeca, output.stackPointer)
    }

    @Test
    fun sfree() {
        val output = execute(
            ProcessorState(stackPointer = 0xfeca),
            0x8b, 0x10
        )

        assertEquals(0xfeda, output.stackPointer)
    }

}

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
class DataTransferCommandsTest {

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
    @ValueSource(ints = [0x60, 0x65, 0x6a, 0x6f])
    fun `mov x x, no change expected`(command: Int) {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            command
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov A B`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x61
        )

        assertEquals(24, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov A C`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x62
        )

        assertEquals(36, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov A D`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x63
        )

        assertEquals(48, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov B A`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x64
        )

        assertEquals(12, output.registerA)
        assertEquals(12, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov B C`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x66
        )

        assertEquals(12, output.registerA)
        assertEquals(36, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov B D`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x67
        )

        assertEquals(12, output.registerA)
        assertEquals(48, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov C A`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x68
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(12, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov C B`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x69
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(24, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov C D`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x6b
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(48, output.registerC)
        assertEquals(48, output.registerD)
    }

    @Test
    fun `mov D A`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x6c
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(12, output.registerD)
    }

    @Test
    fun `mov D B`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x6d
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(24, output.registerD)
    }

    @Test
    fun `mov D C`() {
        val output = execute(
            ProcessorState(registerA = 12, registerB = 24, registerC = 36, registerD = 48),
            0x6e
        )

        assertEquals(12, output.registerA)
        assertEquals(24, output.registerB)
        assertEquals(36, output.registerC)
        assertEquals(36, output.registerD)
    }

    @Test
    fun `mov A (L)`() {
        val output = execute(
            ProcessorState(),
            0x70, 0x04, 0x00, 0xff, 0x69
        )

        assertEquals(0x69, output.registerA)
    }

    @Test
    fun `mov B (L)`() {
        val output = execute(
            ProcessorState(),
            0x71, 0x04, 0x00, 0xff, 0x69
        )

        assertEquals(0x69, output.registerB)
    }

    @Test
    fun `mov C (L)`() {
        val output = execute(
            ProcessorState(),
            0x72, 0x04, 0x00, 0xff, 0x69
        )

        assertEquals(0x69, output.registerC)
    }

    @Test
    fun `mov D (L)`() {
        val output = execute(
            ProcessorState(),
            0x73, 0x04, 0x00, 0xff, 0x69
        )

        assertEquals(0x69, output.registerD)
    }

    @Test
    fun `mov A (CD + l)`() {
        val output = execute(
            ProcessorState(registerC = 0x03, registerD = 0x00),
            0x74, 0x01, 0xff, 0xff, 0x69
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x03, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `mov B (CD + l)`() {
        val output = execute(
            ProcessorState(registerC = 0x03, registerD = 0x00),
            0x75, 0x01, 0xff, 0xff, 0x69
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x69, output.registerB)
        assertEquals(0x03, output.registerC)
        assertEquals(0x00, output.registerD)
    }
    @Test
    fun `mov C (CD + l)`() {
        val output = execute(
            ProcessorState(registerC = 0x04, registerD = 0x00),
            0x76, 0x00, 0xff, 0xff, 0x69
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x69, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `mov D (CD + l)`() {
        val output = execute(
            ProcessorState(registerC = 0x03, registerD = 0x00),
            0x77, 0x01, 0xff, 0xff, 0x69
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x03, output.registerC)
        assertEquals(0x69, output.registerD)
    }

    @Test
    fun `mov AB (L)`() {
        val output = execute(
            ProcessorState(),
            0x94, 0x04, 0x00, 0xff, 0x69, 0x96
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
    }

    @Test
    fun `mov CD (L)`() {
        val output = execute(
            ProcessorState(),
            0x95, 0x04, 0x00, 0xff, 0x69, 0x96
        )

        assertEquals(0x69, output.registerC)
        assertEquals(0x96, output.registerD)
    }

    @Test
    fun `mov AB (CD + l)`() {
        val output = execute(
            ProcessorState(registerC = 0x03, registerD = 0x00),
            0x96, 0x01, 0x04, 0xff, 0x69, 0x96
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
        assertEquals(0x03, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `mov CD (CD + l)`() {
        val output = execute(
            ProcessorState(registerC = 0x04, registerD = 0x00),
            0x97, 0x00, 0x04, 0xff, 0x69, 0x96
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x69, output.registerC)
        assertEquals(0x96, output.registerD)
    }

    @Test
    fun `mov CD (SP + l)`() {
        val output = execute(
            ProcessorState(stackPointer = 0x0003),
            0x99, 0x01, 0x04, 0xff, 0x69, 0x96
        )

        assertEquals(0x00, output.registerA)
        assertEquals(0x00, output.registerB)
        assertEquals(0x69, output.registerC)
        assertEquals(0x96, output.registerD)
    }

    @Test
    fun `mov AB (SP + l)`() {
        val output = execute(
            ProcessorState(stackPointer = 0x0002),
            0x98, 0x02, 0x04, 0xff, 0x69, 0x96
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
        assertEquals(0x00, output.registerC)
        assertEquals(0x00, output.registerD)
    }

    @Test
    fun `mov AB i`() {
        val output = execute(
            ProcessorState(),
            0x9a, 0x69, 0x96
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
    }

    @Test
    fun `mov CD i`() {
        val output = execute(
            ProcessorState(),
            0x9b, 0x69, 0x96
        )

        assertEquals(0x69, output.registerC)
        assertEquals(0x96, output.registerD)
    }

    @Test
    fun `mov A i`() {
        val output = execute(
            ProcessorState(),
            0x7c, 0x69
        )

        assertEquals(0x69, output.registerA)
    }

    @Test
    fun `mov B i`() {
        val output = execute(
            ProcessorState(),
            0x7d, 0x69
        )

        assertEquals(0x69, output.registerB)
    }

    @Test
    fun `mov C i`() {
        val output = execute(
            ProcessorState(),
            0x7e, 0x69
        )

        assertEquals(0x69, output.registerC)
    }

    @Test
    fun `mov D i`() {
        val output = execute(
            ProcessorState(),
            0x7f, 0x69
        )

        assertEquals(0x69, output.registerD)
    }

    @Test
    fun `mov (L) A`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69),
            0x80, 0xab, 0xff
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `mov (L) B`() {
        ram.clear()
        execute(
            ProcessorState(registerB = 0x69),
            0x81, 0xab, 0xff
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `mov (L) C`() {
        ram.clear()
        execute(
            ProcessorState(registerC = 0x69),
            0x82, 0xab, 0xff
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `mov (L) D`() {
        ram.clear()
        execute(
            ProcessorState(registerD = 0x69),
            0x83, 0xab, 0xff
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `mov (CD + l) A`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69, registerC = 0xaa, registerD = 0xff),
            0x84, 0x01
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `mov (CD + l) B`() {
        ram.clear()
        execute(
            ProcessorState(registerB = 0x69, registerC = 0xaa, registerD = 0xff),
            0x85, 0x01
        )

        assertEquals(0x69, ram.read(0xffab))
    }

    @Test
    fun `mov (CD + l) C`() {
        ram.clear()
        execute(
            ProcessorState(registerC = 0xaa, registerD = 0xff),
            0x86, 0x01
        )

        assertEquals(0xaa, ram.read(0xffab))
    }

    @Test
    fun `mov (CD + l) D`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69, registerC = 0xaa, registerD = 0xff),
            0x87, 0x01
        )

        assertEquals(0xff, ram.read(0xffab))
    }

    @Test
    fun `mov (L) AB`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69, registerB = 0x96),
            0x8c, 0xab, 0xff
        )

        assertEquals(0x69, ram.read(0xffab))
        assertEquals(0x96, ram.read(0xffac))
    }

    @Test
    fun `mov (L) CD`() {
        ram.clear()
        execute(
            ProcessorState(registerC = 0x69, registerD = 0x96),
            0x8d, 0xab, 0xff
        )

        assertEquals(0x69, ram.read(0xffab))
        assertEquals(0x96, ram.read(0xffac))
    }

    @Test
    fun `mov (CD + l) AB`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69, registerB = 0x96, registerC = 0xaa, registerD = 0xff),
            0x9c, 0x01
        )

        assertEquals(0x69, ram.read(0xffab))
        assertEquals(0x96, ram.read(0xffac))
    }

    @Test
    fun `mov (CD + l) CD`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69, registerB = 0x96, registerC = 0xab, registerD = 0xff),
            0x9c, 0x02
        )

        assertEquals(0x69, ram.read(0xffad))
        assertEquals(0x96, ram.read(0xffae))
    }

    @Test
    fun `mov AB CD`() {
        val output = execute(
            ProcessorState(registerA = 0x69, registerB = 0x96, registerC = 0xab, registerD = 0xba),
            0x91
        )

        assertEquals(0xab, output.registerA)
        assertEquals(0xba, output.registerB)
        assertEquals(0xab, output.registerC)
        assertEquals(0xba, output.registerD)
    }

    @Test
    fun `mov CD CD`() {
        val output = execute(
            ProcessorState(registerA = 0x69, registerB = 0x96, registerC = 0xab, registerD = 0xba),
            0x93
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
        assertEquals(0xab, output.registerC)
        assertEquals(0xba, output.registerD)
    }


    @Test
    fun `mov AB AB`() {
        val output = execute(
            ProcessorState(registerA = 0x69, registerB = 0x96, registerC = 0xab, registerD = 0xba),
            0x90
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
        assertEquals(0xab, output.registerC)
        assertEquals(0xba, output.registerD)
    }

    @Test
    fun `mov CD AB`() {
        val output = execute(
            ProcessorState(registerA = 0x69, registerB = 0x96, registerC = 0xab, registerD = 0xba),
            0x92
        )

        assertEquals(0x69, output.registerA)
        assertEquals(0x96, output.registerB)
        assertEquals(0x69, output.registerC)
        assertEquals(0x96, output.registerD)
    }

    @Test
    fun `mov A (SP + l)`() {
        ram.clear()
        ram.write(0xfdac, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfda0),
            0x50, 0x0c
        )

        assertEquals(0x69, output.registerA)
    }

    @Test
    fun `mov B (SP + l)`() {
        ram.clear()
        ram.write(0xfdac, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfda0),
            0x51, 0x0c
        )

        assertEquals(0x69, output.registerB)
    }

    @Test
    fun `mov C (SP + l)`() {
        ram.clear()
        ram.write(0xfdac, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfda0),
            0x52, 0x0c
        )

        assertEquals(0x69, output.registerC)
    }

    @Test
    fun `mov D (SP + l)`() {
        ram.clear()
        ram.write(0xfdac, 0x69)
        val output = execute(
            ProcessorState(stackPointer = 0xfda0),
            0x53, 0x0c
        )

        assertEquals(0x69, output.registerD)
    }

    @Test
    fun `mov (SP + l) A`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69, stackPointer = 0xfda0),
            0x54, 0x0a
        )

        assertEquals(0x69, ram.read(0xfdaa))
    }

    @Test
    fun `mov (SP + l) B`() {
        ram.clear()
        execute(
            ProcessorState(registerB = 0x69, stackPointer = 0xfda0),
            0x55, 0x0a
        )

        assertEquals(0x69, ram.read(0xfdaa))
    }

    @Test
    fun `mov (SP + l) C`() {
        ram.clear()
        execute(
            ProcessorState(registerC = 0x69, stackPointer = 0xfda0),
            0x56, 0x0a
        )

        assertEquals(0x69, ram.read(0xfdaa))
    }

    @Test
    fun `mov (SP + l) D`() {
        ram.clear()
        execute(
            ProcessorState(registerD = 0x69, stackPointer = 0xfda0),
            0x57, 0x0a
        )

        assertEquals(0x69, ram.read(0xfdaa))
    }

    @Test
    fun `mov (SP + l) AB`() {
        ram.clear()
        execute(
            ProcessorState(registerA = 0x69, registerB = 0x96, stackPointer = 0xfda0),
            0x9e, 0x0a
        )

        assertEquals(0x69, ram.read(0xfdaa))
        assertEquals(0x96, ram.read(0xfdab))
    }

    @Test
    fun `mov (SP + l) CD`() {
        ram.clear()
        execute(
            ProcessorState(registerC = 0x69, registerD = 0x96, stackPointer = 0xfda0),
            0x9f, 0x0a
        )

        assertEquals(0x69, ram.read(0xfdaa))
        assertEquals(0x96, ram.read(0xfdab))
    }



}

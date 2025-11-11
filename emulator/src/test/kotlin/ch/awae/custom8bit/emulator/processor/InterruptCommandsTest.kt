package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.microcode.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class InterruptCommandsTest {

    class InterruptInjector(var interrupt: Boolean) : MemoryBusDevice {
        override fun read(address: Int): Int? {
            return null
        }

        override fun write(address: Int, data: Int): Boolean {
            return false
        }

        override fun interruptRequested(): Boolean {
            return interrupt
        }
    }

    private val microcode = Microcode(Compiler.compileInstructionSet(INSTRUCTION_SET))

    private val ram = RamChip8k(57344)
    private val interruptInjector = InterruptInjector(interrupt = false)

    private fun execute(
        program: IntArray,
        inputState: ProcessorState = ProcessorState()
    ): ProcessorState {
        val code = program.map { it.toByte() }.toByteArray()
        val pu = ProcessingUnit(
            microcode,
            StandardMemoryBus(RomChip8k(0, code), ram, interruptInjector)
        )
        return pu.executeNextCommand(inputState)
    }

    private fun execute(inputState: ProcessorState, vararg programBytes: Int) = execute(programBytes, inputState)

    @Test
    fun noint() {
        val output = execute(
            ProcessorState(interruptsEnabled = true),
            0xf2
        )

        assertFalse(output.interruptsEnabled)
    }

    @Test
    fun enint() {
        val output = execute(
            ProcessorState(interruptsEnabled = false),
            0xf3
        )

        assertTrue(output.interruptsEnabled)
    }

    @Test
    fun noint_redundant() {
        val output = execute(
            ProcessorState(interruptsEnabled = false),
            0xf2
        )

        assertFalse(output.interruptsEnabled)
    }

    @Test
    fun enint_redundant() {
        val output = execute(
            ProcessorState(interruptsEnabled = true),
            0xf3
        )

        assertTrue(output.interruptsEnabled)
    }

    @Test
    fun siv() {
        val output = execute(
            ProcessorState(interruptRegister = 0),
            0xf4, 0x34, 0x12
        )

        assertEquals(0x1234, output.interruptRegister)
    }

    @Test
    fun interrupt() {
        interruptInjector.interrupt = true
        val output = execute(
            ProcessorState(
                registerA = 10,
                registerB = 20,
                registerC = 30,
                registerD = 40,
                programCounter = 0x0001,
                interruptRegister = 0x1234,
                interruptsEnabled = true
            ),
            0xff, 0xff
        )

        assertFalse(output.interruptsEnabled)
        assertFalse(output.halted)
        assertEquals(0x1234, output.interruptRegister)
        assertEquals(0x1234, output.programCounter)
        assertEquals(0xfffa, output.stackPointer)
        assertEquals(40, ram.read(0xffff))
        assertEquals(30, ram.read(0xfffe))
        assertEquals(20, ram.read(0xfffd))
        assertEquals(10, ram.read(0xfffc))
        assertEquals(0, ram.read(0xfffb))
        assertEquals(1, ram.read(0xfffa))
    }

    @Test
    fun exint() {
        ram.write(0xfffa, 1)
        ram.write(0xfffb, 0)
        ram.write(0xfffc, 10)
        ram.write(0xfffd, 20)
        ram.write(0xfffe, 30)
        ram.write(0xffff, 40)

        val output = execute(
            ProcessorState(
                registerA = 0,
                registerB = 0,
                registerC = 0,
                registerD = 0,
                programCounter = 0,
                stackPointer = 0xfffa,
                interruptRegister = 0x1234,
                interruptsEnabled = false
            ),
            0xf1, 0xff
        )

        assertTrue(output.interruptsEnabled)
        assertFalse(output.halted)
        assertEquals(0x1234, output.interruptRegister)
        assertEquals(0x0001, output.programCounter)
        assertEquals(0x0000, output.stackPointer)
        assertEquals(40, output.registerD)
        assertEquals(30, output.registerC)
        assertEquals(20, output.registerB)
        assertEquals(10, output.registerA)
    }

}

package ch.awae.custom8bit.emulator.hardware.modules.alu

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.random.*
import kotlin.test.*

class ALUTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/modules/alu/ALU_test.csv"], numLinesToSkip = 1)
    fun testALU_enabled(command: String, c: Int, a: Int, b: Int?, l: Int?, cIn: Boolean?, res: Int, cOut: Boolean?) {
        val inputA = StandardWritableBus(false, "inputA")
        val inputB = StandardWritableBus(false, "inputB")
        val inputL = StandardWritableBus(false, "inputL")
        val carryIn = StandardWritableSignal(false, "carryIn")
        val control = StandardWritableBus(false, "control")
        val output = StandardWritableBus(false, "output")
        val carryOut = StandardWritableSignal(false, "carryOut")

        val dInputA = inputA.connectDriver()
        val dInputB = inputB.connectDriver()
        val dInputL = inputL.connectDriver()
        val dCarryIn = carryIn.connectDriver()
        val dControl = control.connectDriver()

        val alu = ALU(inputA, inputB, inputL, carryIn, DataSignal.constant(true), control, output, carryOut, "ALU")

        val sim = Simulation(inputA, inputB, inputL, carryIn, control, output, carryOut, alu)

        repeat(100) {
            dInputA.set(a.toUInt())
            dInputB.set(b?.toUInt() ?: Random.nextUInt())
            dInputL.set(l?.toUInt() ?: Random.nextUInt())
            dCarryIn.set(cIn ?: Random.nextBoolean())
            dControl.set(c.toUInt())
            sim.tick(10)
            assertEquals(res.toUInt(), output.state)
            if (cOut != null) {
                assertEquals(cOut, carryOut.state)
            }
        }
    }


    @ParameterizedTest
    @CsvFileSource(resources = ["/modules/alu/ALU_test.csv"], numLinesToSkip = 1)
    fun testALU_disabled(command: String, c: Int, a: Int, b: Int?, l: Int?, cIn: Boolean?, res: Int, cOut: Boolean?) {
        val inputA = StandardWritableBus(false, "inputA")
        val inputB = StandardWritableBus(false, "inputB")
        val inputL = StandardWritableBus(false, "inputL")
        val carryIn = StandardWritableSignal(false, "carryIn")
        val control = StandardWritableBus(false, "control")
        val output = StandardWritableBus(false, "output")
        val carryOut = StandardWritableSignal(false, "carryOut")

        val dInputA = inputA.connectDriver()
        val dInputB = inputB.connectDriver()
        val dInputL = inputL.connectDriver()
        val dCarryIn = carryIn.connectDriver()
        val dControl = control.connectDriver()

        val alu = ALU(inputA, inputB, inputL, carryIn, DataSignal.constant(false), control, output, carryOut, "ALU")

        val sim = Simulation(inputA, inputB, inputL, carryIn, control, output, carryOut, alu)

        repeat(100) {
            dInputA.set(a.toUInt())
            dInputB.set(b?.toUInt() ?: Random.nextUInt())
            dInputL.set(l?.toUInt() ?: Random.nextUInt())
            dCarryIn.set(cIn ?: Random.nextBoolean())
            dControl.set(c.toUInt())
            sim.tick(10)
            assertEquals(0u, output.state)
        }
    }

}
package ch.awae.custom8bit.emulator.hardware.modules.alu

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class MathUnitTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/modules/alu/math_unit_test.csv"], numLinesToSkip = 1)
    fun testMathUnit(c: Int, a: Int, b: Int, cIn: Boolean, res: Int, cOut: Boolean) {
        val inputA = StandardWritableBus(false, "inputA")
        val inputB = StandardWritableBus(false, "inputB")
        val carryIn = StandardWritableSignal(false, "carryIn")
        val control = StandardWritableBus(false, "control")
        val output = StandardWritableBus(false, "output")
        val carryOut = StandardWritableSignal(false, "carryOut")

        val dInputA = inputA.connectDriver()
        val dInputB = inputB.connectDriver()
        val dCarryIn = carryIn.connectDriver()
        val dControl = control.connectDriver()

        val mu = MathUnit(inputA, inputB, carryIn, control, output, carryOut, "TestMathUnit")

        val sim = Simulation(inputA, inputB, carryIn, control, output, carryOut, mu)

        dInputA.set(a.toUInt())
        dInputB.set(b.toUInt())
        dCarryIn.set(cIn)
        dControl.set(c.toUInt())
        sim.tick(100)
        assertEquals(res.toUInt(), output.state)
        assertEquals(cOut, carryOut.state)
    }

}
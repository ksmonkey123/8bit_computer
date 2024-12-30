package ch.awae.custom8bitemulator.hardware.modules.alu

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class MathUnitTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/math_unit_test.csv"], numLinesToSkip = 1)
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

    companion object {
        @JvmStatic
        fun data() = listOf(
            Arguments.of(0b000, 0xaa, 0xcc, 0xaa),
            Arguments.of(0b001, 0xaa, 0xcc, 0x88),
            Arguments.of(0b010, 0xaa, 0xcc, 0xee),
            Arguments.of(0b011, 0xaa, 0xcc, 0x66),
            Arguments.of(0b100, 0xaa, 0xcc, 0x55),
            Arguments.of(0b101, 0xaa, 0xcc, 0x77),
            Arguments.of(0b110, 0xaa, 0xcc, 0x11),
            Arguments.of(0b111, 0xaa, 0xcc, 0x99),
        )
    }

}
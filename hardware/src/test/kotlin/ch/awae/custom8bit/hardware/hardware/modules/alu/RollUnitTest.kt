package ch.awae.custom8bit.hardware.hardware.modules.alu

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class RollUnitTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/modules/alu/roll_unit_test.csv"], delimiter = ';', numLinesToSkip = 1)
    fun test(command: Int, input: Int, carryIn: Boolean, expectedOutput: Int, expectedCarryOut: Boolean) {
        val inputA = StandardWritableBus(false)
        val control = StandardWritableBus(false)
        val cIn = StandardWritableSignal(false)
        val output = StandardWritableBus(false)
        val carryOut = StandardWritableSignal(false)
        val rollUnit = RollUnit(inputA, cIn, control, output, carryOut, "testRU")
        val sim = Simulation(inputA, control, cIn, output, carryOut, rollUnit)

        inputA.connectDriver().set(input.toUInt())
        control.connectDriver().set(command.toUInt())
        cIn.connectDriver().set(carryIn)

        sim.tick(100)

        assertEquals((expectedOutput.takeUnless { it < 0 } ?: 0).toUInt(), output.state)
        assertEquals(expectedCarryOut, carryOut.state)
    }

}
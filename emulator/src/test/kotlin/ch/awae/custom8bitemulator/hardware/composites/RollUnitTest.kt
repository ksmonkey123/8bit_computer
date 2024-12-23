package ch.awae.custom8bitemulator.hardware.composites

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class RollUnitTest {

    @ParameterizedTest
    @MethodSource("combinations")
    fun test(command: Int, input: Int, carryIn: Boolean, expectedOutput: Int?, expectedCarryOut: Boolean) {
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

        assertEquals((expectedOutput ?: 0).toUInt(), output.state)
        assertEquals(expectedCarryOut, carryOut.state)
    }

    companion object {
        @JvmStatic
        fun combinations(): List<Arguments?> = (0..3).flatMap { cmd ->
            (0..255).flatMap { input ->
                listOf(false, true).map { cin ->
                    if (cmd == 3) {
                        Arguments.of(3, input, cin, null, input % 2 != 0)
                    } else if (cmd == 1) {
                        if (cin) {
                            Arguments.of(1, input, true, ((input shl 1) + 1) and 0xff, (input and 0x80) != 0)
                        } else {
                            Arguments.of(1, input, false, (input shl 1) and 0xff, (input and 0x80) != 0)
                        }
                    } else if (cmd == 2) {
                        if (cin) {
                            Arguments.of(2, input, true, ((input shr 1) + 128) and 0xff, input % 2 != 0)
                        } else {
                            Arguments.of(2, input, false, (input shr 1) and 0xff, input % 2 != 0)
                        }
                    } else {
                        Arguments.of(0, input, cin, ((input * 16) + (input / 16)) and 0xff, (input and 0x80) != 0)
                    }
                }
            }
        }
    }

}
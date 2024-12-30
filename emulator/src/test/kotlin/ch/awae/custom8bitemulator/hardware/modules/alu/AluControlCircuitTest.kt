package ch.awae.custom8bitemulator.hardware.modules.alu

import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class AluControlCircuitTest {

    @ParameterizedTest
    @MethodSource("data")
    fun testEnabled(input: Int, enable: Boolean) {
        val command = MockBus(input.toUInt())
        val control = MockBus()
        val circuit = AluControlCircuit(command, DataSignal.constant(enable), control)
        circuit.tick()

        if (enable) {
            if (input and 0x08 == 0) {
                // logic unit is active when bit 3 is 0
                assertEquals(0x30u, control.driverState!! and 0x38u)
            } else if (input in listOf(0x18, 0x19, 0x1a)) {
                // roll unit is active for special codes
                assertEquals(0x18u, control.driverState!! and 0x38u)
            } else {
                // math unit is active in all other cases
                assertEquals(0x28u, control.driverState!! and 0x38u)
            }
        } else {
            // if disabled, the output channels are all high
            assertEquals(0x38u, control.driverState!! and 0x38u)
        }

        if (input and 0x10 != 0) {
            // regL used instead of regB when bit 4 is 1
            assertEquals(0x02u, control.driverState!! and 0x03u)
        } else {
            // regB used when bit 4 is 0
            assertEquals(0x01u, control.driverState!! and 0x03u)
        }

        if (input == 0x1b) {
            // a is inverted if command is 11011
            assertEquals(0x04u, control.driverState!! and 0x04u)
        } else {
            assertEquals(0x00u, control.driverState!! and 0x04u)
        }

    }

    companion object {

        @JvmStatic
        fun data() = (0..31).flatMap { listOf(Arguments.of(it, false), Arguments.of(it, true)) }

    }

}
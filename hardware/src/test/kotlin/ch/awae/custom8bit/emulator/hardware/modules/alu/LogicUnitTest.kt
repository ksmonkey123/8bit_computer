package ch.awae.custom8bit.emulator.hardware.modules.alu

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class LogicUnitTest {

    @ParameterizedTest
    @MethodSource("data")
    fun testLogicUnit(c: Int, a: Int, b: Int, res: Int) {
        val inputA = StandardWritableBus(false, "inputA")
        val inputB = StandardWritableBus(false, "inputB")
        val control = StandardWritableBus(false, "control")
        val output = StandardWritableBus(false, "output")

        val dInputA = inputA.connectDriver()
        val dInputB = inputB.connectDriver()
        val dControl = control.connectDriver()

        val lu = LogicUnit(inputA, inputB, control, output, "TestLogicUnit")

        val sim = Simulation(inputA, inputB, control, output, lu)

        dInputA.set(a.toUInt())
        dInputB.set(b.toUInt())
        dControl.set(c.toUInt())
        sim.tick(100)
        assertEquals(res.toUInt(), output.state)
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
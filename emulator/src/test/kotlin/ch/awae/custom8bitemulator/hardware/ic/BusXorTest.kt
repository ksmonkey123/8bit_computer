package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class BusXorTest {

    @ParameterizedTest
    @MethodSource("busValues")
    fun testBusValue(value: Int) {
        val inputValue = value.toUInt()
        val inputBus = MockBus()
        val controlSignal = MockSignal()
        val outputBus = MockBus()
        val gate = BusXor(inputBus, controlSignal, outputBus)

        gate.tick()
        assertEquals(0x00u, outputBus.driverState)

        inputBus.state = inputValue
        gate.tick()
        assertEquals(inputValue, outputBus.driverState)

        controlSignal.state = true
        gate.tick()
        assertEquals(inputValue.inv() and 0xffu, outputBus.driverState)
    }

    companion object {
        @JvmStatic
        fun busValues(): List<Arguments> = (0..255).map { Arguments.of(it) }
    }

}

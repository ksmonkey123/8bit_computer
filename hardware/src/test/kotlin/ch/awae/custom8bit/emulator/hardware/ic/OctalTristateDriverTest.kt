package ch.awae.custom8bit.emulator.hardware.ic

import ch.awae.custom8bit.emulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class OctalTristateDriverTest {

    @ParameterizedTest
    @MethodSource("allBytes")
    fun testTristateDriver(value: Int) {
        val inverseValue = value.toUInt() and 0xffu

        val input = MockBus(state = value.toUInt())
        val enable = MockSignal()
        val output = MockBus()
        val driver = OctalTristateDriver(input, enable, output)

        assertNull(output.driverState)

        // while not enabled, output released
        driver.tick()
        assertNull(output.driverState)

        // enabled -> show output
        enable.state = true
        assertNull(output.driverState)
        driver.tick()
        assertEquals(value.toUInt(), output.driverState)

        // input change is transparent while enabled
        input.state = inverseValue
        assertEquals(value.toUInt(), output.driverState)
        driver.tick()
        assertEquals(inverseValue, output.driverState)

        // disable on enable low
        enable.state = false
        assertEquals(inverseValue, output.driverState)
        driver.tick()
        assertNull(output.driverState)
    }

    @Test
    fun testDriverTakesFirstByteOnly() {
        val input = MockBus(0xffffff00u)
        val enable = MockSignal(true)
        val output = MockBus()
        val driver = OctalTristateDriver(input, enable, output)

        assertNull(output.driverState)
        driver.tick()
        assertEquals(0u, output.driverState)
    }

    companion object {
        @JvmStatic
        fun allBytes() = (0..255).map { Arguments.of(it) }
    }
}

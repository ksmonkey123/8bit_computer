package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*

class OctalTristateDriver74244Test {

    @ParameterizedTest
    @MethodSource("allBytes")
    fun testTristateDriver(value: Int) {
        val input = MockBus()
        val enable = MockSignal()
    }

    companion object {
        @JvmStatic
        fun allBytes() = (0..255).map { Arguments.of(it) }
    }
}
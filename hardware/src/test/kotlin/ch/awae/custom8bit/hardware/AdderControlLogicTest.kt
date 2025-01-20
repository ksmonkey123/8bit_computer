package ch.awae.custom8bit.hardware

import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class AdderControlLogicTest {

    infix fun Boolean.nand(other: Boolean) = !(this && other)

    @ParameterizedTest
    @MethodSource("logicTable")
    fun testIntendedLogic(useCarry: Boolean, carry: Boolean, invert: Boolean, expected: Boolean) {
        val result = if (useCarry) carry else invert
        assertEquals(expected, result)
    }

    @ParameterizedTest
    @MethodSource("logicTable")
    fun testBooleanLogic(useCarry: Boolean, carry: Boolean, invert: Boolean, expected: Boolean) {
        val result = (useCarry and carry) or (!useCarry and invert)
        assertEquals(expected, result)
    }

    @ParameterizedTest
    @MethodSource("logicTable")
    fun testNandLogic(useCarry: Boolean, carry: Boolean, invert: Boolean, expected: Boolean) {
        val result = (useCarry nand carry) nand ((useCarry nand useCarry) nand invert)
        assertEquals(expected, result)
    }

    companion object {
        @JvmStatic
        fun logicTable(): List<Arguments> {

            val t = true
            val f = false

            return listOf(
                Arguments.of(f, f, f, f),
                Arguments.of(f, f, t, t),
                Arguments.of(f, t, f, f),
                Arguments.of(f, t, t, t),
                Arguments.of(t, f, f, f),
                Arguments.of(t, f, t, f),
                Arguments.of(t, t, f, t),
                Arguments.of(t, t, t, t),
            )
        }
    }

}

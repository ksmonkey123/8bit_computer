package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class SignalChoiceTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/signal_choice_test.csv"], numLinesToSkip = 1)
    fun test(x: Int, y: Int, z: Int, q: Int) {
        val sigX = MockSignal()
        val sigY = MockSignal()
        val sigZ = MockSignal()
        val sigQ = MockSignal()

        val ic = SignalChoice(sigX, sigY, sigZ, sigQ)

        sigX.state = x > 0
        sigY.state = y > 0
        sigZ.state = z > 0

        ic.tick()

        assertEquals(q > 0, sigQ.driverState)
    }
}
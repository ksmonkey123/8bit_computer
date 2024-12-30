package ch.awae.custom8bitemulator.hardware.modules.control

import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class StepStepSequencerUpdateCircuitTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/sequencer_update_circuit_test.csv"], numLinesToSkip = 1)
    fun test(q: Int, f: Int, j2_expected: Int, k2_expected: Int, k1_expected: Int) {
        val q1 = MockSignal(q % 2 != 0)
        val q2 = MockSignal(q > 1)
        val f1 = MockSignal(f % 2 != 0)
        val f2 = MockSignal(f > 1)

        val j2 = MockSignal()
        val k1 = MockSignal()
        val k2 = MockSignal()

        val circuit = StepSequencerUpdateCircuit(q1, q2, f1, f2, k1, j2, k2)

        circuit.tick()

        assertEquals(j2_expected > 0, j2.driverState)
        assertEquals(k1_expected > 0, k1.driverState)
        assertEquals(k2_expected > 0, k2.driverState)
    }

}
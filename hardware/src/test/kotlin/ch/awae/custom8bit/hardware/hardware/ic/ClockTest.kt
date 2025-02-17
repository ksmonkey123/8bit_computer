package ch.awae.custom8bit.hardware.hardware.ic

import ch.awae.custom8bit.hardware.hardware.wiring.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.*

class ClockTest {

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 10, 25, 50, 100, 999])
    fun testClock(halfCycle: Int) {
        val signal = MockSignal()
        val clock = Clock(halfCycle, signal)

        repeat(100) {
            repeat(halfCycle) {
                clock.tick()
                assertEquals(false, signal.driverState)
            }
            repeat(halfCycle) {
                clock.tick()
                assertEquals(true, signal.driverState)
            }
        }
    }
}
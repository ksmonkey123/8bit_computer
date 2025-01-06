package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.hardware.wiring.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class JKFlipFlopTest {

    @Test
    fun test() {

        val j = MockSignal()
        val k = MockSignal()
        val clock = MockSignal()
        val reset = MockSignal(true)
        val q = MockSignal()

        val flipflop = JKFlipFlop(j, k.inverted(), clock, reset, q)

        flipflop.tick()
        assertEquals(false, q.driverState)
        reset.state = false
        flipflop.tick()

        fun update(jValue: Boolean, kValue: Boolean, expectedState: Boolean) {
            j.state = jValue
            k.state = kValue
            clock.state = true
            flipflop.tick()
            assertEquals(expectedState, q.driverState)
            clock.state = false
            flipflop.tick()
            assertEquals(expectedState, q.driverState)
        }

        // 0 -> 1
        update(true, false, true)

        // 1 -> 1 (hold)
        update(false, false, true)

        // 1 -> 1 (set)
        update(true, false, true)

        // 1 -> 0 (clear)
        update(false, true, false)

        // 0 -> 0 (hold)
        update(false, false, false)

        // 0 -> 0 (clear)
        update(false, true, false)

        // 0 -> 1 (toggle)
        update(true, true, true)

        // 1 -> 0 (toggle)
        update(true, true, false)
    }

    @Test
    fun `clock should not trigger when raised during reset signal`() {
        val clock = MockSignal()
        val reset = MockSignal()
        val q = MockSignal()

        val jk = JKFlipFlop(
            j = DataSignal.constant(true),
            kInv = DataSignal.constant(false),
            clock = clock,
            reset = reset,
            q = q
        )

        jk.tick()
        reset.state = true
        jk.tick()
        assertEquals(false, q.driverState)
        clock.state = true
        jk.tick()
        assertEquals(false, q.driverState)
        reset.state = false
        jk.tick()
        assertEquals(false, q.driverState)
        clock.state = false
        jk.tick()
        assertEquals(false, q.driverState)
        clock.state = true
        jk.tick()
        assertEquals(true, q.driverState)
    }

}
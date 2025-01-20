package ch.awae.custom8bit.emulator.hardware.modules.registers

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class StackPointerTest {

    val bus = StandardWritableBus(false)
    val push = MockSignal(false)
    val pop = MockSignal(false)
    val reset = MockSignal(true)
    val clock = MockSignal(false)
    val sp = StackPointer(bus, push, pop, clock, reset, "SP")
    val sim = Simulation(sp, bus)

    @BeforeEach
    fun resetChip() {
        sim.tick(10)
        assertEquals(0u, bus.state)
        reset.state = false
        sim.tick(10)
        assertEquals(0u, bus.state)
    }

    @Test
    fun `initial state after reset is 0xffff`() {
        push.state = true
        sim.tick(10)
        assertEquals(0xffffu, bus.state)

        push.state = false
        pop.state = true
        sim.tick(10)
        assertEquals(0u, bus.state)
    }

    @Test
    fun `clock does nothing while idle`() {
        repeat(10) {
            clock.state = true
            sim.tick(10)
            clock.state = false
            sim.tick(10)
        }

        push.state = true
        sim.tick(10)
        assertEquals(0xffffu, bus.state)
    }

    @Test
    fun `push decrements state`() {
        push.state = true

        repeat(100) { i ->
            clock.state = false
            sim.tick(10)
            assertEquals(0xffffu - i.toUInt(), bus.state)
            clock.state = true
            sim.tick(10)
            assertEquals(0xfffeu - i.toUInt(), bus.state)
        }
    }

    @Test
    fun `pop increments state`() {
        push.state = true

        // move stack a bit
        repeat(100) {
            clock.state = false
            sim.tick(10)
            clock.state = true
            sim.tick(10)
        }

        // move stack back with assertions
        push.state = false
        pop.state = true
        repeat(100) { i ->
            clock.state = false
            sim.tick(10)
            assertEquals(0xffffu - (99 - i).toUInt(), bus.state)
            clock.state = true
            sim.tick(10)
            if (i < 99) {
                assertEquals(0xffffu - (98 - i).toUInt(), bus.state)
            } else {
                assertEquals(0u, bus.state)
            }
        }

    }

}
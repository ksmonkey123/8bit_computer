package ch.awae.custom8bit.hardware.hardware.wiring

import org.junit.jupiter.api.Test
import kotlin.test.*

class InvertedSignalTest {

    @Test
    fun test() {
        val signal = MockSignal()

        val inv = signal.inverted()

        assertEquals(true, inv.state)

        signal.state = true

        assertEquals(false, inv.state)

        signal.state = false

        assertEquals(true, inv.state)


    }

}
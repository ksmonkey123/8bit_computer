package ch.awae.custom8bit.emulator.hardware.wiring

import kotlin.random.*

/**
 * Implements a single bit data signal
 *
 * In case of contention, the signal is pulled low
 */
interface WritableSignal : DataSignal {

    fun connectDriver(): Driver

    interface Driver {
        fun set(value: Boolean)
        fun setRandom() {
            set(Random.nextBoolean())
        }

        fun release()
    }
}

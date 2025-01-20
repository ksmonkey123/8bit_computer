package ch.awae.custom8bit.hardware.hardware.wiring

import kotlin.random.*

/**
 * Implements a data bus of up to 32bits.
 *
 * In case of contention, the offending bit is pulled low
 */
interface WritableBus : DataBus {

    fun connectDriver(): Driver

    interface Driver {
        fun set(value: UInt)
        fun setRandom(mask: UInt) {
            set(Random.nextUInt() and mask)
        }

        fun release()
    }
}
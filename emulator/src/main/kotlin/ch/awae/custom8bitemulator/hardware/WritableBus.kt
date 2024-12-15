package ch.awae.custom8bitemulator.hardware

import ch.awae.custom8bitemulator.*

/**
 * Implements a data bus of up to 32bits.
 *
 * In case of contention, the offending bit is pulled low
 *
 * @param pullHigh if `true`, an unloaded bus (no component driving it actively) is pulled high.
 *                 if `false`, an unloaded bus is pulled low.
 */
class WritableBus(private val pullHigh: Boolean) : SimulationElement(ElementType.WIRE), DataBus {
    private val idleState = if (pullHigh) 0xffffffffu else 0x00000000u

    private val drivers = mutableMapOf<Driver, UInt>()

    override var state: UInt = idleState
        private set
    override var contention: UInt = 0x00000000u
        private set

    private var dirty = false

    private fun drive(driver: Driver, value: UInt) {
        drivers[driver] = value
        dirty = true
    }

    private fun release(driver: Driver) {
        drivers.remove(driver)
        dirty = true
    }

    override fun tick(tickID: Long) {
        if (!dirty) {
            return
        }
        dirty = false

        if (drivers.isEmpty()) {
            state = idleState
            contention = 0x00000000u
        } else {
            state = drivers.values.reduce(UInt::and)
            contention = drivers.values.reduce(UInt::or) and state.inv()
        }
    }

    interface Driver {
        fun set(value: UInt)
        fun release()
    }

    fun connectDriver(): Driver {
        return DriverImpl()
    }

    private inner class DriverImpl : Driver {
        override fun set(value: UInt) {
            this@WritableBus.drive(this, value)
        }

        override fun release() {
            this@WritableBus.release(this)
        }
    }

    /**
     * extract a single bit of the data bus as a signal wire.
     *
     * The signal wire updates synchronously with the bus without needing to tick
     */
    override fun bitSignal(bit: Int): DataSignal {
        return BusDerivedSignal(this, bit)
    }

}

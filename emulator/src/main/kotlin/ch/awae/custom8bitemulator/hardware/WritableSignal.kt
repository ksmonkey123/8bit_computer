package ch.awae.custom8bitemulator.hardware

import ch.awae.custom8bitemulator.*

/**
 * Implements a single bit data signal
 *
 * In case of contention, the signal is pulled low
 *
 * @param pullHigh if `true`, an unloaded signal (no component driving it actively) is pulled high.
 *                 if `false`, an unloaded signal is pulled low.
 */
class WritableSignal(private val pullHigh: Boolean) : SimulationElement(ElementType.WIRE), DataSignal {
    private val idleState = pullHigh

    private val drivers = mutableMapOf<Driver, Boolean>()

    override var state: Boolean = idleState
        private set
    override var contention: Boolean = false
        private set

    private var dirty = false

    private fun drive(driver: Driver, value: Boolean) {
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
            contention = false
        } else {
            state = drivers.values.reduce(Boolean::and)
            contention = drivers.values.reduce(Boolean::or) and !state
        }
    }

    interface Driver {
        fun set(value: Boolean)
        fun release()
    }

    fun connectDriver(): Driver {
        return DriverImpl()
    }

    private inner class DriverImpl : Driver {
        override fun set(value: Boolean) {
            this@WritableSignal.drive(this, value)
        }

        override fun release() {
            this@WritableSignal.release(this)
        }
    }
}

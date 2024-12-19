package ch.awae.custom8bitemulator.hardware.wiring

import ch.awae.custom8bitemulator.*

class StandardWritableSignal(pullHigh: Boolean, name: String? = null) : SimulationElement(ElementType.WIRE, name),
    WritableSignal {
    private val idleState = pullHigh

    private val drivers = mutableMapOf<WritableSignal.Driver, Boolean>()

    override var state: Boolean = idleState
        private set
    override var contention: Boolean = false
        private set

    private var dirty = false

    private fun drive(driver: WritableSignal.Driver, value: Boolean) {
        drivers[driver] = value
        dirty = true
    }

    private fun release(driver: WritableSignal.Driver) {
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

    override fun connectDriver(): WritableSignal.Driver {
        return DriverImpl()
    }

    private inner class DriverImpl : WritableSignal.Driver {
        override fun set(value: Boolean) {
            this@StandardWritableSignal.drive(this, value)
        }

        override fun release() {
            this@StandardWritableSignal.release(this)
        }
    }
}

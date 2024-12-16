package ch.awae.custom8bitemulator.hardware.wiring

import ch.awae.custom8bitemulator.*

class StandardWritableBus(pullHigh: Boolean) : SimulationElement(ElementType.WIRE), WritableBus {
    private val idleState = if (pullHigh) 0xffffffffu else 0x00000000u

    private val drivers = mutableMapOf<WritableBus.Driver, UInt>()

    override var state: UInt = idleState
        private set
    override var contention: UInt = 0x00000000u
        private set

    private var dirty = false

    private fun drive(driver: WritableBus.Driver, value: UInt) {
        drivers[driver] = value
        dirty = true
    }

    private fun release(driver: WritableBus.Driver) {
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

    override fun connectDriver(): WritableBus.Driver {
        return DriverImpl()
    }

    private inner class DriverImpl : WritableBus.Driver {
        override fun set(value: UInt) {
            this@StandardWritableBus.drive(this, value)
        }

        override fun release() {
            this@StandardWritableBus.release(this)
        }
    }

}

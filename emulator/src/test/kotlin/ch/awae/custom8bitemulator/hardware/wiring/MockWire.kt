package ch.awae.custom8bitemulator.hardware.wiring

class MockBus(
    override var state: UInt = 0u,
    override var contention: UInt = 0u,
) : WritableBus {
    var driverState: UInt? = null

    override fun connectDriver(): WritableBus.Driver = object : WritableBus.Driver {
        override fun set(value: UInt) {
            driverState = value
        }

        override fun release() {
            driverState = null
        }
    }
}

class MockSignal(
    override var state: Boolean = false,
    override var contention: Boolean = false,
) : WritableSignal {
    var driverState: Boolean? = null

    override fun connectDriver(): WritableSignal.Driver = object : WritableSignal.Driver {
        override fun set(value: Boolean) {
            driverState = value
        }

        override fun release() {
            driverState = null
        }
    }
}

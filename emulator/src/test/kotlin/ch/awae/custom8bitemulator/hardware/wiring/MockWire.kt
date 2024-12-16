package ch.awae.custom8bitemulator.hardware.wiring

class MockBus : DataBus {
    override var state: UInt = 0u
    override var contention: UInt = 0u
}

class MockSignal : DataSignal {
    override var state: Boolean = false
    override var contention: Boolean = false
}
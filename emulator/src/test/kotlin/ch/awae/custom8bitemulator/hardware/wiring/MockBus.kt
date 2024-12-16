package ch.awae.custom8bitemulator.hardware.wiring

class MockBus : DataBus {
    override var state: UInt = 0u
    override var contention: UInt = 0u
}
package ch.awae.custom8bitemulator.hardware

class MockBus : DataBus {
    override var state: UInt = 0u
    override var contention: UInt = 0u
}
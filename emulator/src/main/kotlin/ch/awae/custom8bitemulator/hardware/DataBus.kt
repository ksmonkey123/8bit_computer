package ch.awae.custom8bitemulator.hardware

interface DataBus {
    val state: UInt
    val contention: UInt
    fun bitSignal(bit: Int): DataSignal
}
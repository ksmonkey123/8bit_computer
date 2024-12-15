package ch.awae.custom8bitemulator.hardware

class BusDerivedSignal(private val bus: DataBus, bit: Int) : DataSignal {

    private val mask: UInt

    init {
        if (bit < 0 || bit > 31) {
            throw IllegalArgumentException("bit must be in range 0..31")
        }
        mask = 1u shl bit
    }

    override val state: Boolean
        get() = (bus.state and mask) != 0u

    override val contention: Boolean
        get() = (bus.contention and mask) != 0u

}
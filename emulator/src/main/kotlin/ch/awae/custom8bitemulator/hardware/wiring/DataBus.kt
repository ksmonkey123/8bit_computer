package ch.awae.custom8bitemulator.hardware.wiring

interface DataBus {
    val state: UInt
    val contention: UInt

    fun bitSignal(bit: Int): DataSignal {
        return BusDerivedSignal(this, bit)
    }

    fun byteBus(byte: Int): DataBus {
        return ByteBus(this, byte)
    }

    private class BusDerivedSignal(private val bus: DataBus, bit: Int) : DataSignal {

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

    private class ByteBus(private val source: DataBus, val byte: Int) : DataBus {
        init {
            if (byte < 0 || byte > 3) {
                throw IllegalArgumentException("byte must be in range 0..3")
            }
        }

        override val state: UInt
            get() = (source.state shr (byte * 8)) and 0xffu

        override val contention: UInt
            get() = (source.contention shr (byte * 8)) and 0xffu

    }

    private data class ConstantBus(override val state: UInt) : DataBus {
        override val contention: UInt
            get() = 0u
    }

    companion object {
        fun constant(value: UInt): DataBus = ConstantBus(value)
    }

}

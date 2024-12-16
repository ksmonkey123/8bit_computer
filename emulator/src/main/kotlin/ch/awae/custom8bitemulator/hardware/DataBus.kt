package ch.awae.custom8bitemulator.hardware

interface DataBus {
    val state: UInt
    val contention: UInt

    fun bitSignal(bit: Int): DataSignal {
        return BusDerivedSignal(this, bit)
    }

    fun byteBus(byte: Int): DataBus {
        return ByteBus(this, byte)
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
}

package ch.awae.custom8bitemulator.hardware.wiring

interface DataSignal {
    val state: Boolean
    val contention: Boolean

    fun inverted(): DataSignal = object : DataSignal {
        override val state: Boolean = !this@DataSignal.state
        override val contention: Boolean = false
    }

    private data class ConstantSignal(override val state: Boolean) : DataSignal {
        override val contention: Boolean
            get() = false
    }

    companion object {
        fun constant(value: Boolean): DataSignal = ConstantSignal(value)
    }

}
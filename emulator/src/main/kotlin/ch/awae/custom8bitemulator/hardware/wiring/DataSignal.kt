package ch.awae.custom8bitemulator.hardware.wiring

interface DataSignal {
    val state: Boolean
    val contention: Boolean

    fun inverted(): DataSignal = object : DataSignal {
        override val state: Boolean = !this@DataSignal.state
        override val contention: Boolean = false
    }

}

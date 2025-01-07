package ch.awae.custom8bit.emulator.hardware.wiring

interface DataSignal {
    val state: Boolean
    val contention: Boolean

    fun inverted(): DataSignal = object : DataSignal {
        override val state: Boolean
            get() = !this@DataSignal.state
        override val contention: Boolean = false
    }

    fun edge(raising: Boolean = true): Edge = EdgeImpl(this, raising)

    private data class ConstantSignal(override val state: Boolean) : DataSignal {
        override val contention: Boolean
            get() = false
    }

    interface Edge {
        fun triggered(): Boolean
    }

    private data class EdgeImpl(private val signal: DataSignal, val raising: Boolean) : Edge {
        private var lastState = raising

        override fun triggered(): Boolean {
            val oldValue = lastState
            val newValue = signal.state

            lastState = newValue

            if (newValue != oldValue) {
                // we have an edge
                if (raising == newValue) {
                    // edge is the correct one
                    return true
                }
            }
            return false
        }
    }

    companion object {
        fun constant(value: Boolean): DataSignal = ConstantSignal(value)
    }

}
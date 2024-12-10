package ch.awae.custom8bitemulator.hardware


sealed interface BusState<T : Any> {
    fun merge(other: BusState<T>): BusState<T>

    fun tryValue(): T

    class Conflict<T : Any> : BusState<T> {

        override fun tryValue(): T {
            throw UnsupportedOperationException("bus in conflict")
        }

        override fun merge(other: BusState<T>): BusState<T> {
            return this
        }

        override fun equals(other: Any?): Boolean {
            return other is Floating<*>
        }

        override fun hashCode(): Int {
            return -1
        }
    }

    class Floating<T : Any> : BusState<T> {
        override fun tryValue(): T {
            throw UnsupportedOperationException("bus floating")
        }

        override fun merge(other: BusState<T>): BusState<T> {
            return other
        }

        override fun equals(other: Any?): Boolean {
            return other is Floating<*>
        }

        override fun hashCode(): Int {
            return 0
        }
    }

    class BusValue<T : Any>(val value: T) : BusState<T> {
        override fun tryValue(): T {
            return value
        }

        override fun merge(other: BusState<T>): BusState<T> {
            return if (other is BusValue) {
                Conflict()
            } else {
                other.merge(this)
            }
        }

        override fun equals(other: Any?): Boolean {
            return other is BusValue<*> && value == other.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }
    }

    companion object {
        fun <T : Any> of(value: T?): BusState<T> {
            return if (value == null) {
                Floating()
            } else {
                BusValue(value)
            }
        }

        fun <T : Any> floating(): BusState<T> = Floating()
        fun <T : Any> conflict(): BusState<T> = Conflict()
    }
}

fun interface BusConsumer<T : Any> {
    fun consume(data: BusState<T>)
}

interface DataBus<T : Any> {
    fun connect(consumer: BusConsumer<T>)
    val currentState: BusState<T>
}

class WritableBus<T : Any>(initial: T? = null) : DataBus<T> {

    private val sourceStates: MutableMap<Any, T?> = mutableMapOf()
    private val consumers: MutableSet<BusConsumer<T>> = mutableSetOf()
    private var _currentState: BusState<T> = BusState.of(initial)

    override val currentState: BusState<T>
        get() = _currentState

    fun push(source: Any, value: T?) {
        if (sourceStates[source] == value) {
            // no update, ignore update to prevent cycles
            return
        }

        if (value == null) {
            sourceStates.remove(source)
        } else {
            sourceStates[source] = value
        }

        val previousState = _currentState

        _currentState = sourceStates.values.fold(BusState.floating()) { acc, busValue ->
            acc.merge(BusState.of(busValue))
        }

        if (previousState != _currentState) {
            consumers.forEach { it.consume(_currentState) }
        }
    }

    override fun connect(consumer: BusConsumer<T>) {
        consumers.add(consumer)
        consumer.consume(_currentState)
    }

}

typealias Signal = DataBus<Boolean>
typealias WritableSignal = WritableBus<Boolean>
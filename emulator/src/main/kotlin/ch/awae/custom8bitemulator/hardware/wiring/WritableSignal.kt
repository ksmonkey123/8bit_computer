package ch.awae.custom8bitemulator.hardware.wiring

/**
 * Implements a single bit data signal
 *
 * In case of contention, the signal is pulled low
 *
 * @param pullHigh if `true`, an unloaded signal (no component driving it actively) is pulled high.
 *                 if `false`, an unloaded signal is pulled low.
 */
interface WritableSignal : DataSignal {

    fun connectDriver(): Driver

    interface Driver {
        fun set(value: Boolean)
        fun release()
    }
}

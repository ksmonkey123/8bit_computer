package ch.awae.custom8bitemulator.hardware.wiring

/**
 * Implements a data bus of up to 32bits.
 *
 * In case of contention, the offending bit is pulled low
 *
 * @param pullHigh if `true`, an unloaded bus (no component driving it actively) is pulled high.
 *                 if `false`, an unloaded bus is pulled low.
 */
interface WritableBus : DataBus {

    fun connectDriver(): Driver

    interface Driver {
        fun set(value: UInt)
        fun release()
    }
}
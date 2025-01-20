package ch.awae.custom8bit.hardware.hardware.ic

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

/**
 * binary data to channel selection decoder.
 *
 * decodes the lowest 5 bits of the input bus to a selection signal.
 * The corresponding bit in the output is pulled high.
 */
class BinaryToSelectionDecoder(
    private val input: DataBus,
    output: WritableBus,
    private val mask: UInt,
    private val enable: DataSignal? = null,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val outputDriver = output.connectDriver()

    override fun tick(tickID: Long) {
        if ((enable != null) && !enable.state) {
            outputDriver.set(0u)
        } else {
            outputDriver.set(1u shl (input.state and mask and 0x1fu).toInt())
        }
    }

}
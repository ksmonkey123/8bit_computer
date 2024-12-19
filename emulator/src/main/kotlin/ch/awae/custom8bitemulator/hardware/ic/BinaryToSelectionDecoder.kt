package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*

/**
 * binary data to channel selection decoder.
 *
 * decodes the lowest 5 bits of the input bus to a selection signal.
 * The corresponding bit in the output is pulled high.
 */
class BinaryToSelectionDecoder(
    private val input: DataBus,
    output: WritableBus,
) : SimulationElement(ElementType.COMPONENT) {

    private val outputDriver = output.connectDriver()

    override fun tick(tickID: Long) {
        outputDriver.set(1u shl (input.state and 0x1fu).toInt())
    }

}
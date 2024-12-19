package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*

/**
 * Octal Xor.
 *
 * Simulates every bit of an 8-bit bus xor-ed with the control signal.
 */
class BusXor(
    private val input: DataBus,
    private val control: DataSignal,
    output: WritableBus,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val outputDriver = output.connectDriver()

    override fun tick(tickID: Long) {
        outputDriver.set(input.state xor if (control.state) 0xffu else 0x00u)
    }

}

package ch.awae.custom8bit.hardware.hardware.ic

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

/**
 * Simulates the 74244 octal tristate driver chip
 *
 * the 2 physical enable signals are tied together as there's no need to use this chip as 2 quad drivers.
 *
 * @param input input bus. only lowest byte is used
 * @param enable enable signal. active high
 * @param output output bus. driven to the input value if enable is high. (upper bytes driven low)
 */
class OctalTristateDriver(
    private val input: DataBus,
    private val enable: DataSignal,
    output: WritableBus,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val outputDriver = output.connectDriver()

    override fun tick(tickID: Long) {
        if (enable.state) {
            outputDriver.set(input.state and 0xffu)
        } else {
            outputDriver.release()
        }
    }

}

package ch.awae.custom8bit.emulator.hardware.ic

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*

/**
 * Simulates 2 74244 octal tristate driver chip
 *
 * the 4 physical enable signals are tied together as there's no need to use this chip as 2 quad drivers.
 *
 * @param input input bus. only lowest 2 bytes are used
 * @param enable enable signal. active high
 * @param output output bus. driven to the input value if enable is high. (upper bytes driven low)
 */
class DualOctalTristateDrivers(
    private val input: DataBus,
    private val enable: DataSignal,
    output: WritableBus,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val outputDriver = output.connectDriver()

    override fun tick(tickID: Long) {
        if (enable.state) {
            outputDriver.set(input.state and 0xffffu)
        } else {
            outputDriver.release()
        }
    }

}

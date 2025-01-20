package ch.awae.custom8bit.hardware.hardware.ic

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

class Clock(
    private val ticksPerHalfCycle: Int,
    output: WritableSignal,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private var currentState = 0L

    private val driver = output.connectDriver()

    override fun tick(tickID: Long) {
        driver.set((currentState++ % (2 * ticksPerHalfCycle)) >= ticksPerHalfCycle)
    }

}
package ch.awae.custom8bit.emulator.hardware.ic

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*

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
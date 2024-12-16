package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.*
import kotlin.random.*

/**
 * Simulates the 74273 Octal D Flip Flop IC
 *
 * Data is latched on the rising edge of the clock.
 * While the reset signal is high, output is zeroed and no clock edges are observed.
 */
class OctalDFlipFlop74273(
    private val inputBus: DataBus,
    private val clock: DataSignal,
    private val reset: DataSignal?,
    outputBus: WritableBus,
) : SimulationElement(ElementType.COMPONENT) {

    private val driver = outputBus.connectDriver()

    private var internalState = (Random.nextInt().toUInt() and 0xffu).also { driver.set(it) }

    private var lastClockState: Boolean = true
    private var lastInput: UInt = inputBus.state

    override fun tick(tickID: Long) {
        // update state
        val clockState = clock.state
        if (reset != null && reset.state) {
            internalState = 0u
        } else if (clockState && !lastClockState) {
            // randomly decide on which signal to use
            val randomMask = Random.nextUInt()
            internalState = ((inputBus.state and randomMask) or (lastInput and randomMask.inv())) and 0xffu
        }
        lastClockState = clockState
        lastInput = inputBus.state

        // update output
        driver.set(internalState)
    }

}

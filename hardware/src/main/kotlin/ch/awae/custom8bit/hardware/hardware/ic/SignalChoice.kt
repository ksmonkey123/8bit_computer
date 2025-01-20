package ch.awae.custom8bit.hardware.hardware.ic

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

/**
 * Implements a simple quad NAND circuit for choosing between 2 signals.
 *
 * For 2 input signals X,Y and a control signal Z the output is:
 *
 * Q = !ZX + ZY
 *
 * This is achievable with 4 NAND gates in the circuit
 *
 * Q = !(!(!(ZZ)X)!(Zy))
 */
class SignalChoice(
    private val x: DataSignal,
    private val y: DataSignal,
    private val z: DataSignal,
    q: WritableSignal,
    name: String? = null
) : SimulationElement(ElementType.COMPONENT, name) {

    private val driver = q.connectDriver()

    override fun tick(tickID: Long) {
        driver.set(
            if (z.state) {
                y.state
            } else {
                x.state
            }
        )
    }

}
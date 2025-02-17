package ch.awae.custom8bit.hardware.hardware.modules.alu

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

/**
 * Roll Unit
 *
 * Abstract implementation due to the triviality of it.
 */
class RollUnit(
    private val inputA: DataBus,
    private val carryIn: DataSignal,
    private val control: DataBus,
    output: WritableBus,
    carryOut: WritableSignal,
    name: String? = null,
) : SimulationElement(
    ElementType.COMPONENT, name
) {

    private val outputDriver = output.connectDriver()
    private val carryOutDriver = carryOut.connectDriver().also { it.set(false) }

    override fun tick(tickID: Long) {
        val ctrl = (control.state and 0x03u).toInt()
        val cin = if (carryIn.state) 1u else 0u

        when (ctrl) {
            // 00: swap
            0 -> {
                outputDriver.set(((inputA.state and 0xf0u) shr 4) + ((inputA.state and 0x0fu) shl 4))
                carryOutDriver.set(inputA.state and 0x80u != 0u)
            }
            // 10: shl
            2 -> {
                outputDriver.set(((inputA.state shl 1) and 0xfeu) + cin)
                carryOutDriver.set(inputA.state and 0x80u != 0u)
            }
            // 11: shr
            3 -> {
                outputDriver.set(((inputA.state shr 1) and 0x7fu) + (cin shl 7))
                carryOutDriver.set(inputA.state and 0x01u != 0u)
            }
            // 01: no operation (special case `neg` in Math unit)
            else -> {
                outputDriver.release()
                carryOutDriver.set(inputA.state and 0x80u != 0u)
            }
        }
    }

}
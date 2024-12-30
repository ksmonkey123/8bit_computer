package ch.awae.custom8bitemulator.hardware.modules.control

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.ic.*
import ch.awae.custom8bitemulator.hardware.wiring.*

/**
 * Sub-Step sequencer. Cycles through output values `00`, `01`, `10`, `11`
 *
 * @param step the clock signal
 * @param reset the reset signal. while high, the sequencer is halted and reset to `00`
 * @param q 2-bit output bus indicating the current sub-step
 */
class SubStepSequencer(
    private val step: DataSignal,
    private val reset: DataSignal,
    private val q: WritableBus,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val q1Signal = StandardWritableSignal(false, toString() + "-q1")
    private val q2Signal = StandardWritableSignal(false, toString() + "-q2")

    private val ff1 =
        JKFlipFlop(DataSignal.constant(true), DataSignal.constant(false), step, reset, q1Signal, toString() + "-jk1")
    private val ff2 = JKFlipFlop(q1Signal, q1Signal.inverted(), step, reset, q2Signal, toString() + "-jk2")

    private val combiner = DataBus.ofSignals(0 to q1Signal, 1 to q2Signal)
    private val buffer = OctalTristateDriver(combiner, DataSignal.constant(true), q, toString() + "-qDriver")

    override fun getSubElements(): List<SimulationElement> {
        return listOf(q1Signal, q2Signal, ff1, ff2, buffer)
    }

}
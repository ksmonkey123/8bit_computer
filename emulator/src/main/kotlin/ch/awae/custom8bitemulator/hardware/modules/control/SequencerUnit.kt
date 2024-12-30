package ch.awae.custom8bitemulator.hardware.modules.control

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.ic.*
import ch.awae.custom8bitemulator.hardware.wiring.*

/**
 * Full Sequencer Unit providing both sub-step and step coordination
 *
 * @param f 2-bit data bus indicating literal fetch width. should only be updated while step is `00`
 * @param step the step signal. the sequencer advances on every raising edge
 * @param reset while high, the sequencer is halted and reset to `0000`
 * @param q 4-bit output bus indicating the current step. the high 2 bits indicate the step, the 2 low bits the sub-step
 */
class SequencerUnit(
    private val f: DataBus,
    private val step: DataSignal,
    private val reset: DataSignal,
    private val q: WritableBus,
    name: String? = null
) : SimulationElement(ElementType.COMPONENT, name) {

    private val subStepBus = StandardWritableBus(false, toString() + "-subBus")
    private val subStepSequencer = SubStepSequencer(step, reset, subStepBus, toString() + "-subSequencer")

    private val stepBus = StandardWritableBus(false, toString() + "-stepBus")
    private val stepSequencer = StepSequencer(f, subStepBus.bitSignal(1).inverted(), reset, stepBus, toString() + "-stepSequencer")

    private val combinedBus = DataBus.ofSignals(
        0 to subStepBus.bitSignal(0),
        1 to subStepBus.bitSignal(1),
        2 to stepBus.bitSignal(0),
        3 to stepBus.bitSignal(1),
    )

    private val driver = OctalTristateDriver(combinedBus, DataSignal.constant(true), q, toString() + "-qDriver")

    override fun getSubElements(): List<SimulationElement> {
        return listOf(subStepSequencer, subStepBus, stepBus, stepSequencer, driver)
    }
}

package ch.awae.custom8bit.hardware.hardware.modules.control

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.ic.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

/**
 * Sequencer Unit
 *
 * @param f 2-bit data bus indicating literal fetch width
 * @param step the step signal. the sequencer advances on every raising edge
 * @param reset while high, the sequencer is haltet, when falling it is reset
 * @param q 2-bit output bus indicating the current step
 */
class StepSequencer(
    private val f: DataBus,
    private val step: DataSignal,
    private val reset: DataSignal,
    private val q: WritableBus,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val j2Signal = StandardWritableSignal(false, toString() + "-j2sig")
    private val k1Signal = StandardWritableSignal(false, toString() + "-k1sig")
    private val k2Signal = StandardWritableSignal(false, toString() + "-k2sig")

    private val q1Signal = StandardWritableSignal(false, toString() + "-q1sig")
    private val q2Signal = StandardWritableSignal(false, toString() + "-q2sig")

    private val ff1 = JKFlipFlop(
        DataSignal.constant(true),
        k1Signal,
        step,
        reset,
        q1Signal,
        toString() + "-jk1"
    )
    private val ff2 = JKFlipFlop(
        j2Signal,
        k2Signal,
        step,
        reset,
        q2Signal,
        toString() + "-jk2"
    )

    private val circ = StepSequencerUpdateCircuit(
        q1Signal,
        q2Signal,
        f.bit(0),
        f.bit(1),
        k1Signal,
        j2Signal,
        k2Signal,
        toString() + "-circ"
    )

    private val combiner = DataBus.ofSignals(0 to q1Signal, 1 to q2Signal)
    private val buffer = OctalTristateDriver(combiner, DataSignal.constant(true), q, toString() + "-qDriver")

    override fun getSubElements(): List<SimulationElement> {
        return listOf(buffer, circ, ff1, ff2, j2Signal, k1Signal, k2Signal, q1Signal, q2Signal)
    }

}
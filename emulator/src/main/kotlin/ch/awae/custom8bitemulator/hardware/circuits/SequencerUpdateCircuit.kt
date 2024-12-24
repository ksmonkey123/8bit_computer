package ch.awae.custom8bitemulator.hardware.circuits

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*

/**
 * Logic gate grid for sequencing the step counter based on the literal fetch width
 */
class SequencerUpdateCircuit(
    private val q1: DataSignal,
    private val q2: DataSignal,
    private val f1: DataSignal,
    private val f2: DataSignal,
    kInv1: WritableSignal,
    j2: WritableSignal,
    kInv2: WritableSignal,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    val d_j2 = j2.connectDriver()
    val d_k1 = kInv1.connectDriver()
    val d_k2 = kInv2.connectDriver()

    override fun tick(tickID: Long) {
        d_k1.set(q1.state and !q2.state and !f2.state)
        d_k2.set(!(q1.state and q2.state))
        d_j2.set(!(!q1.state and !q2.state and f1.state))
    }

}
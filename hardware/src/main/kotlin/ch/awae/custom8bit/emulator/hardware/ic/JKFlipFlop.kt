package ch.awae.custom8bit.emulator.hardware.ic

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*
import kotlin.random.*

/**
 * J-notK-Flipflop representing a single unit of the 74109 IC.
 *
 * asynchronous load and clear features are not simulated
 */
class JKFlipFlop(
    private val j: DataSignal,
    private val kInv: DataSignal,
    clock: DataSignal,
    private val reset: DataSignal?,
    q: WritableSignal,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {
    private var internalState = Random.nextBoolean()

    private val qDriver = q.connectDriver().also { it.set(internalState) }
    private val clockEdge = clock.edge()


    override fun tick(tickID: Long) {
        val edge = clockEdge.triggered()
        if (reset?.state == true) {
            internalState = false
        } else {
            if (edge) {
                val inputs = j.state to kInv.state
                internalState = when (inputs) {
                    false to false -> false         // J=0, K=1 -> reset
                    true to true -> true            // J=1, K=0 -> set
                    true to false -> !internalState // J=K=1 -> toggle
                    else -> internalState           // J=K=0 -> hold
                }
            }
        }
        qDriver.set(internalState)
    }

}

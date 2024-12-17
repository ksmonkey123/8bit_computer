package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*
import kotlin.random.*

/**
 * Simulates a chained full-adder setup consisting of the 74283 series chips
 */
class OctalFullAdder(
    private val inA: DataBus,
    private val inB: DataBus,
    private val carryIn: DataSignal,
    sum: WritableBus,
    carryOut: WritableSignal,
) : SimulationElement(ElementType.COMPONENT) {

    private val sumDriver = sum.connectDriver().also { it.set(Random.nextUInt(0x100u)) }
    private val carryOutDriver = carryOut.connectDriver().also { it.set(Random.nextBoolean()) }

    override fun tick(tickID: Long) {
        val a = inA.state and 0xffu
        val b = inB.state and 0xffu

        val sum = a + b + if (carryIn.state) 1u else 0u

        sumDriver.set(sum and 0xffu)
        carryOutDriver.set((sum and 0x100u) != 0u)
    }

}
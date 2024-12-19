package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.wiring.*

/**
 * Simulates 8 parallel 2 input logic gates. (AND, OR, XOR).
 *
 * This can be implemented with ICs 4081, 4071, 4070
 */
class BusLogicGate(
    private val operation: Operation,
    private val inputA: DataBus,
    private val inputB: DataBus,
    output: WritableBus,
) : SimulationElement(ElementType.COMPONENT) {

    private val outputDriver = output.connectDriver()

    override fun tick(tickID: Long) {
        outputDriver.set(
            when (operation) {
                Operation.AND -> inputA.state and inputB.state
                Operation.IOR -> inputA.state or inputB.state
                Operation.XOR -> inputA.state xor inputB.state
                Operation.NAND -> (inputB.state and inputA.state).inv()
            }
        )
    }

    enum class Operation {
        AND, IOR, XOR, NAND
    }


}
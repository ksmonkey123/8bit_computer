package ch.awae.custom8bit.emulator.hardware.modules.alu

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.ic.*
import ch.awae.custom8bit.emulator.hardware.wiring.*

/**
 * commands (all with carry bit!)
 * 00 -> decrement
 * 01 -> increment
 * 10 -> addition
 * 11 -> subtraction
 *
 * bit 0 controls b inverter
 * bit 1 controls b gate
 */
class MathUnit(
    inputA: DataBus,
    inputB: DataBus,
    carryIn: DataSignal,
    control: DataBus,
    output: WritableBus,
    carryOut: WritableSignal,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val bBus = StandardWritableBus(true, toString() + "-b")
    private val invBus = StandardWritableBus(false, toString() + "-inv")

    private val bGate = OctalTristateDriver(inputB, control.bit(1), bBus, toString() + "-bGate")
    private val bInverter = BusXor(bBus, control.bit(0), invBus, toString() + "-bInverter")

    private val adder = OctalFullAdder(inputA, invBus, carryIn, output, carryOut, toString() + "-adder")

    override fun getSubElements(): List<SimulationElement> {
        return listOf(bBus, invBus, bGate, bInverter, adder)
    }
}
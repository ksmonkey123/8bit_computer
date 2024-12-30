package ch.awae.custom8bitemulator.hardware.modules.alu

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.ic.*
import ch.awae.custom8bitemulator.hardware.wiring.*

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

    private val bGate = OctalTristateDriver(inputB, control.bit(2), bBus, toString() + "-bGate")
    private val bInverter = BusXor(bBus, control.bit(1), invBus, toString() + "-bInverter")

    private val cIn = StandardWritableSignal(false, toString() + "-cIn")
    private val carrySelect = SignalChoice(control.bit(1), carryIn, control.bit(0), cIn, toString() + "-carrySelect")

    private val adder = OctalFullAdder(inputA, invBus, cIn, output, carryOut, toString() + "-adder")

    override fun getSubElements(): List<SimulationElement> {
        return listOf(bBus, invBus, bGate, bInverter, cIn, carrySelect, adder)
    }
}
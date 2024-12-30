package ch.awae.custom8bitemulator.hardware.modules.alu

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.ic.*
import ch.awae.custom8bitemulator.hardware.wiring.*

/**
 * Logic unit.
 *
 * Control:
 * 000: A
 * 001: A and B
 * 010: A ior B
 * 011: A xor B
 * 100: !A
 * 101: !(A and B)
 * 110: !(A ior B)
 * 111: !(A xor B)
 */
class LogicUnit(
    inputA: DataBus,
    inputB: DataBus,
    control: DataBus,
    output: WritableBus,
    name: String? = null
) : SimulationElement(ElementType.COMPONENT, name) {

    // selection logic
    private val selectorBus = StandardWritableBus(false, toString() + "-selector")
    private val selectionDecoder =
        BinaryToSelectionDecoder(control, selectorBus, 0x03u, toString() + "-selectionDecoder")
    private val controlIdentity = selectorBus.bit(0)
    private val controlAndGate = selectorBus.bit(1)
    private val controlIorGate = selectorBus.bit(2)
    private val controlXorGate = selectorBus.bit(3)
    private val invertResult = control.bit(2)

    // intermediate bus for each basic operation
    private val andBus = StandardWritableBus(false, toString() + "-andBus")
    private val iorBus = StandardWritableBus(false, toString() + "-iorBus")
    private val xorBus = StandardWritableBus(false, toString() + "-xorBus")

    // basic gates
    private val andGate = BusLogicGate(BusLogicGate.Operation.AND, inputA, inputB, andBus, toString() + "-andGate")
    private val iorGate = BusLogicGate(BusLogicGate.Operation.IOR, inputA, inputB, iorBus, toString() + "-iorGate")
    private val xorGate = BusLogicGate(BusLogicGate.Operation.XOR, inputA, inputB, xorBus, toString() + "-xorGate")

    // combined bus
    private val mergeBus = StandardWritableBus(false, toString() + "-mergeBus")

    // selection gates
    private val andBuffer = OctalTristateDriver(andBus, controlAndGate, mergeBus, toString() + "-andDriver")
    private val iorBuffer = OctalTristateDriver(iorBus, controlIorGate, mergeBus, toString() + "-iorDriver")
    private val xorBuffer = OctalTristateDriver(xorBus, controlXorGate, mergeBus, toString() + "-xorDriver")
    private val identBuffer = OctalTristateDriver(inputA, controlIdentity, mergeBus, toString() + "-identityDriver")

    // output inverter
    private val inverter = BusXor(mergeBus, invertResult, output, toString() + "-inverter")

    override fun getSubElements(): List<SimulationElement> {
        return listOf(
            selectorBus, selectionDecoder,
            andBus, iorBus, xorBus,
            andGate, iorGate, xorGate,
            mergeBus,
            andBuffer, iorBuffer, xorBuffer, identBuffer,
            inverter,
        )
    }
}
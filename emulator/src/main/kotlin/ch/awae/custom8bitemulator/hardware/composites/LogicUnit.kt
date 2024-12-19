package ch.awae.custom8bitemulator.hardware.composites

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
) : SimulationElement(ElementType.COMPONENT) {

    // selection logic
    private val selectorBus = StandardWritableBus(false)
    private val selectionDecoder = BinaryToSelectionDecoder(control, selectorBus, 0x03u)
    private val controlNotGate = selectorBus.bitSignal(0)
    private val controlAndGate = selectorBus.bitSignal(1)
    private val controlIorGate = selectorBus.bitSignal(2)
    private val controlXorGate = selectorBus.bitSignal(3)
    private val invertResult = control.bitSignal(2)

    // intermediate bus for each basic operation
    private val andBus = StandardWritableBus(false)
    private val iorBus = StandardWritableBus(false)
    private val xorBus = StandardWritableBus(false)

    // basic gates
    private val andGate = BusLogicGate(BusLogicGate.Operation.AND, inputA, inputB, andBus)
    private val iorGate = BusLogicGate(BusLogicGate.Operation.IOR, inputA, inputB, iorBus)
    private val xorGate = BusLogicGate(BusLogicGate.Operation.XOR, inputA, inputB, xorBus)

    // combined bus
    private val mergeBus = StandardWritableBus(false)

    // selection gates
    private val andBuffer = OctalTristateDriver(andBus, controlAndGate, mergeBus)
    private val iorBuffer = OctalTristateDriver(iorBus, controlIorGate, mergeBus)
    private val xorBuffer = OctalTristateDriver(xorBus, controlXorGate, mergeBus)
    private val identBuffer = OctalTristateDriver(inputA, controlNotGate, mergeBus)

    // output inverter
    private val inverter = BusXor(mergeBus, invertResult, output)

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
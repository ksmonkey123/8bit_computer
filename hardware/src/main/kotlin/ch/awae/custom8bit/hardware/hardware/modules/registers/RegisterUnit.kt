package ch.awae.custom8bit.hardware.hardware.modules.registers

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.ic.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

/**
 * @param dataBus 8-bit data bus
 * @param selectionBus 4-bit register selection bus.
 * - The 2 high bits select a register to read from.
 * - The 2 low bits select a register to write to.
 *
 * Register addresses are:
 *  - A: 00
 *  - B: 11
 *  - C: 01
 *  - D: 10
 *
 *  @param readEnable when high, the selected read register is published to the data bus
 *  @param writeEnable when high, the selected write register is written to from the data bus
 *  @param addressBus the 16-bit address bus, the registers C and D can be published to. C is used for the low byte
 *  @param addressReadEnable when high, the content of registers C and D is published to the address bus.
 *  must be present if a data bus is provided
 */
class RegisterUnit(
    dataBus: WritableBus,
    selectionBus: DataBus,
    readEnable: DataSignal,
    writeEnable: DataSignal,
    reset: DataSignal,
    addressBus: WritableBus?,
    addressReadEnable: DataSignal?,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val readSelection = StandardWritableBus(false, toString() + "-readSelection")
    private val readSelector =
        BinaryToSelectionDecoder(selectionBus, readSelection, 0b1100u, readEnable, toString() + "-readSelector")

    private val writeSelection = StandardWritableBus(false, toString() + "-writeSelection")
    private val writeSelector =
        BinaryToSelectionDecoder(selectionBus, writeSelection, 0b0011u, writeEnable, toString() + "-writeSelector")

    val registerA = Register(dataBus, readSelection.bit(0), writeSelection.bit(0), reset, toString() + "-registerA")
    val registerB = Register(dataBus, readSelection.bit(12), writeSelection.bit(3), reset, toString() + "-registerB")
    val registerC = Register(dataBus, readSelection.bit(4), writeSelection.bit(1), reset, toString() + "-registerC")
    val registerD = Register(dataBus, readSelection.bit(8), writeSelection.bit(2), reset, toString() + "-registerD")

    private val addressDriver = addressBus?.let { bus ->
        if (addressReadEnable == null) {
            throw IllegalArgumentException("addressReadEnable required if address bus is provided")
        }

        DualOctalTristateDrivers(
            CombinedByteBus(registerC.directBus, registerD.directBus),
            addressReadEnable,
            bus,
            toString() + "-addressDriver"
        )
    }

    override fun getSubElements(): List<SimulationElement> {
        return listOfNotNull(
            readSelector, readSelection, writeSelector, writeSelection, addressDriver,
            registerA, registerB, registerC, registerD
        )
    }

}
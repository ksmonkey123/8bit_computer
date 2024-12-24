package ch.awae.custom8bitemulator.hardware.modules.registers

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.ic.*
import ch.awae.custom8bitemulator.hardware.wiring.*

class Register(
    dataBus: WritableBus,
    read: DataSignal,
    write: DataSignal,
    reset: DataSignal?,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val internalBus = StandardWritableBus(false, toString() + "-internal")
    private val flipflop = OctalDFlipFlop(dataBus, write, reset, internalBus, toString() + "-flipflop")
    private val driver = OctalTristateDriver(internalBus, read, dataBus, toString() + "-outDriver")

    override fun getSubElements(): List<SimulationElement> {
        return listOf(internalBus, flipflop, driver)
    }

    val directBus: DataBus
        get() = internalBus

}
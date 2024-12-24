package ch.awae.custom8bitemulator.hardware.modules

import ch.awae.custom8bitemulator.*
import ch.awae.custom8bitemulator.hardware.ic.*
import ch.awae.custom8bitemulator.hardware.wiring.*

class Register(
    dataBus: WritableBus,
    read: DataSignal,
    write: DataSignal,
    reset: DataSignal?,
) : SimulationElement(ElementType.COMPONENT) {

    private val internalBus = StandardWritableBus(false)
    private val flipflop = OctalDFlipFlop(dataBus, write, reset, internalBus)
    private val driver = OctalTristateDriver(internalBus, read, dataBus)

    override fun getSubElements(): List<SimulationElement> {
        return listOf(internalBus, flipflop, driver)
    }

    val directBus: DataBus
        get() = internalBus

}
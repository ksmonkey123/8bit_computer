package ch.awae.custom8bit.hardware.hardware.ic

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

/**
 * Parallel EEPROM in read only configuration
 */
class RomChip(
    val data: ByteArray,
    val addressBus: DataBus,
    dataBus: WritableBus,
    val readEnable: DataSignal,
    val chipEnable: DataSignal,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    val dataDriver = dataBus.connectDriver()

    override fun tick(tickID: Long) {
        if (chipEnable.state && readEnable.state) {
            dataDriver.set(data[(addressBus.state and 0x1fffu).toInt()].toUInt())
        } else {
            dataDriver.release()
        }
    }

}
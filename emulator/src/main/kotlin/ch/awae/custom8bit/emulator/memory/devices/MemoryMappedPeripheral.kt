package ch.awae.custom8bit.emulator.memory.devices

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.memory.*

class MemoryMappedPeripheral(val peripheralDevice: PeripheralDevice, val offset: Int) : MemoryBusDevice {

    private fun getLocalAddress(globalAddress: Int): Byte? {
        return if (globalAddress >= offset && globalAddress < (offset + 256)) {
            (globalAddress - offset).toByte()
        } else {
            null
        }
    }

    override fun read(address: Int): Int? {
        return getLocalAddress(address)?.let {
            val rawRead = peripheralDevice.read(it)
            if (rawRead != null) {
                rawRead.toInt() and 0x00ff
            } else {
                null
            }
        }
    }

    override fun write(address: Int, data: Int): Boolean {
        return getLocalAddress(address)?.let {
            peripheralDevice.write(it, data.toByte())
            true
        } ?: false
    }

}
package ch.awae.custom8bit.emulator

interface PeripheralDevice {
    fun read(address: Byte): Byte?
    fun write(address: Byte, data: Byte)
}

open class PeripheralDeviceBase : PeripheralDevice {
    override fun read(address: Byte): Byte? {
        return null
    }

    override fun write(address: Byte, data: Byte) {
    }
}
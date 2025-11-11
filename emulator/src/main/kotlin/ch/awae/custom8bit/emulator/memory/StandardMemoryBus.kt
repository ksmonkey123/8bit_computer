package ch.awae.custom8bit.emulator.memory

import ch.awae.custom8bit.emulator.*

data class StandardMemoryBus(
    val devices: List<MemoryBusDevice>,
) : MemoryBus {

    constructor(vararg devices: MemoryBusDevice) : this(devices.asList())

    private val log = createLogger()

    override fun read(address: Int): Int {
        val candidates = devices.mapNotNull { d -> d.read(address and 0xffff)?.let { d to it } }

        return when (candidates.size) {
            0 -> {
                log.warn("no device responded to memory read from ${address.toHex(2)}")
                0x00
            }

            1 -> candidates.first().second and 0xff
            else -> {
                log.error("multiple devices responded to memory read from ${address.toHex(2)}")
                throw IllegalStateException("device collision on memory read from ${address.toHex(2)}")
            }
        }
    }

    override fun write(address: Int, data: Int) {
        val writingDevices = devices.filter { it.write(address and 0xffff, data and 0xff) }
        when (writingDevices.size) {
            0 -> {
                log.warn("no device processed memory write to ${address.toHex(2)} (value: ${data.toHex(1)})")
            }

            1 -> {}
            else -> {
                log.warn("multiple devices processed memory write to ${address.toHex(2)} (value: ${data.toHex(1)})")
            }
        }
    }

    override fun interruptRequested(): Boolean {
        return devices.any { it.interruptRequested() }
    }

}
package ch.awae.custom8bit.emulator.memory.devices

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.memory.*

/**
 * Rom chip holding 8kB of memory
 */
data class RomChip8k(val page: Int, val data: ByteArray) : MemoryBusDevice {

    private val log = createLogger()

    private fun addressInRange(address: Int): Boolean {
        return (page == (address and 0xE000))
    }

    override fun read(address: Int): Int? {
        return if (addressInRange(address)) {
            data[(address - page)].toInt()
        } else {
            null
        }
    }

    override fun write(address: Int, data: Int): Boolean {
        if (addressInRange(address)) {
            log.warn("attempted write to ${address.toHex(2)} targeted ROM chip. This write will be ignored!")
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RomChip8k

        if (page != other.page) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = page.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
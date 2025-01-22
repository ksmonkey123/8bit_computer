package ch.awae.custom8bit.emulator.memory.devices

import ch.awae.custom8bit.emulator.memory.*

/**
 * Ram chip holding 8kB of memory
 */
data class RamChip8k(val page: Int) : MemoryBusDevice {

    private val state = ByteArray(8192)

    private fun addressInRange(address: Int): Boolean {
        return (page == (address and 0xE000))
    }

    override fun read(address: Int): Int? {
        return if (addressInRange(address)) {
            state[(address - page)].toInt()
        } else {
            null
        }
    }

    override fun write(address: Int, data: Int): Boolean {
        if (addressInRange(address)) {
            state[(address - page)] = data.toByte()
            return true
        } else {
            return false
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RamChip8k

        return page == other.page
    }

    override fun hashCode(): Int {
        var result = page.hashCode()
        result *= 31
        return result
    }
}
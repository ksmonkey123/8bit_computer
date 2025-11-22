package ch.awae.custom8bit.emulator.memory.devices

import ch.awae.binfiles.BinaryFile
import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.memory.*
import kotlin.experimental.and

/**
 * Rom chip holding a binary file
 */
class BinFileRomChip(val file: BinaryFile) : MemoryBusDevice {

    private val log = createLogger()

    private fun addressInRange(address: Int): Boolean {
        return address < file.sizeLimit
    }

    override fun read(address: Int): Int? {
        return if (addressInRange(address)) {
            file.getByte(address)?.toInt()?.and(0xff)
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
}
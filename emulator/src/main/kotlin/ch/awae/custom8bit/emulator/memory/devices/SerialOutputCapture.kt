package ch.awae.custom8bit.emulator.memory.devices

import ch.awae.custom8bit.emulator.memory.*

class SerialOutputCapture(private val address: Int) : MemoryBusDevice {

    private var output = mutableListOf<Int>()

    fun clear() {
        output.clear()
    }

    val list: List<Int>
        get() = output

    override fun read(address: Int): Int? {
        // serial output has no read support
        return null
    }

    override fun write(address: Int, data: Int): Boolean {
        if (address == this.address) {
            output.add(data and 0xff)
            return true
        } else {
            return false
        }
    }
}
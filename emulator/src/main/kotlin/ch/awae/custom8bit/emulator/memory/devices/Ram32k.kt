package ch.awae.custom8bit.emulator.memory.devices

import ch.awae.custom8bit.emulator.memory.*
import kotlin.random.*

class Ram32k : MemoryBusDevice {

    private val state = Random.nextBytes(32768)

    override fun read(address: Int): Int? {
        return if (address < 32768) {
            null
        } else {
            state[address - 32768].toInt() and 0xff
        }
    }

    override fun write(address: Int, data: Int): Boolean {
        return if (address < 32768) {
            false
        } else {
            state[address - 32768] = data.toByte()
            true
        }
    }
}
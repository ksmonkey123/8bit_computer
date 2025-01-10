package ch.awae.eeprom_programmer.api

import ch.awae.eeprom_programmer.*
import ch.awae.eeprom_programmer.com.*

class ComPortProgrammer(private val comDevice: ComDevice) : Programmer {

    override fun readByte(address: Int): UByte {
        val result = comDevice.sendCommand("r${address.hex(4)}")
            ?: throw NullPointerException("read command expects a response")

        return result.toInt(16).toUByte()
    }

    override fun readLine(address: Int): ByteArray {
        if (address % 64 != 0) throw java.lang.IllegalArgumentException("address must be start of a page")

        // TODO: implement page read
        return ByteArray(64) { i ->
            readByte(address + i).toByte()
        }
    }

    override fun writeByte(address: Int, data: UByte) {
        comDevice.sendCommand("w${address.hex(4)}:${data.toInt().hex(2)}")
    }

    override fun writePage(address: Int, data: ByteArray) {
        if (address % 64 != 0) throw java.lang.IllegalArgumentException("address must be start of a page")
        if (data.size < 64) throw java.lang.IllegalArgumentException("less than 64 bytes provided")

        // TODO: implement page write
        for (i in 0..63) {
            writeByte(address + i, data[i].toUByte())
        }
    }

    override fun dumpMemory(): ByteArray {
        val dump = ByteArray(8192)

        for (page in 0..127) {
            val startAddress = page * 64
            val pageContent = readLine(startAddress)
            pageContent.copyInto(dump, destinationOffset = startAddress)
        }

        return dump
    }

}
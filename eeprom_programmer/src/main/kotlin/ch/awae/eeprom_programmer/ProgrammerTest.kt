package ch.awae.eeprom_programmer

import ch.awae.eeprom_programmer.api.*
import ch.awae.eeprom_programmer.com.*
import com.fazecast.jSerialComm.*
import org.slf4j.LoggerFactory

fun main() {
    val programmer: Programmer = ComPortProgrammer(JscComDevice(SerialPort.getCommPorts()[0]))

    val logger = LoggerFactory.getLogger("ProgrammerTest")

    val dump = programmer.dumpMemory()

    logger.info("done")
}
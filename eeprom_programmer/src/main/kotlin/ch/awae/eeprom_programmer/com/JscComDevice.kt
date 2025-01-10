package ch.awae.eeprom_programmer.com

import com.fazecast.jSerialComm.*
import org.slf4j.*
import java.io.*

class JscComDevice(comPort: SerialPort) : ComDevice {
    private val logger = LoggerFactory.getLogger(JscComDevice::class.java)

    private val tx: PrintWriter
    private val rx: BufferedReader

    init {
        comPort.setComPortParameters(230400, 8, 1, 0)
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING or SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0)

        if (!comPort.openPort()) {
            throw RuntimeException("Could not open port")
        }
        logger.info("connection opened to ${comPort.systemPortPath}")
        logger.info("waiting for device startup...")

        Thread.sleep(1000)

        tx = PrintWriter(OutputStreamWriter(comPort.outputStream))
        rx = BufferedReader(InputStreamReader(comPort.inputStream))

        syncWithDevice()
        logger.info("connection established")
    }

    private var syncCounter = 0
    private fun syncWithDevice() {
        repeat(10) {
            val string = "SYN_${syncCounter.toString().padStart(2, '0')}"
            syncCounter = (syncCounter + 1) % 100

            logger.debug("> {}", string)
            tx.println(string)
            tx.flush()

            try {
                do {
                    val received = rx.readLine() ?: throw IllegalStateException("stream terminated")
                    logger.debug("< {}", received)
                    // it is possible that we receive bad lines. eat them and only check the last line
                } while (received != string)
                // we have the correct SYN packet -> finish sync
                return
            } catch (e: SerialPortTimeoutException) {
                logger.warn("Read Timeout")
            }
        }
        throw SerialPortTimeoutException("unable to sync with device in time!")
    }

    override fun sendCommand(command: String): String? = synchronized(this) {
        logger.debug("> {}", command)
        tx.println(command)
        tx.flush()

        val response = rx.readLine() ?: throw IllegalStateException("stream terminated")
        logger.debug("< {}", command)


        if (response.startsWith('+')) {
            return response.substring(1).takeUnless { it.isEmpty() }
        } else {
            throw IllegalStateException("bad response: $response")
        }
    }

}
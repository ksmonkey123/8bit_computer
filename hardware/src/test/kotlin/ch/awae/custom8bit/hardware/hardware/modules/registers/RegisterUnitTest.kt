package ch.awae.custom8bit.hardware.hardware.modules.registers

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.wiring.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import kotlin.test.*

class RegisterUnitTest {

    private val dataBus = StandardWritableBus(true, "data")
    private val selectionBus = StandardWritableBus(false, "selection")
    private val readEnable = StandardWritableSignal(false, "read")
    private val writeEnable = StandardWritableSignal(false, "write")
    private val reset = StandardWritableSignal(false, "reset")
    private val addressBus = StandardWritableBus(true, "address")
    private val addressSelect = StandardWritableSignal(false, "addressSelect")
    private val ru =
        RegisterUnit(dataBus, selectionBus, readEnable, writeEnable, reset, addressBus, addressSelect, "registerUnit")
    private val dataDriver = dataBus.connectDriver()
    private val selectionDriver = selectionBus.connectDriver()
    private val readDriver = readEnable.connectDriver()
    private val writeDriver = writeEnable.connectDriver()
    private val resetDriver = reset.connectDriver()
    private val addressDriver = addressSelect.connectDriver()
    private val sim = Simulation(ru, dataBus, selectionBus, readEnable, writeEnable, reset, addressBus, addressSelect)

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3])
    fun `write and recall must work for each register individually`(address: Int) {
        // stabilize startup
        resetDriver.set(true)
        sim.tick(10)
        resetDriver.set(false)
        sim.tick(10)
        assertEquals(0xffffffffu, dataBus.state)
        assertEquals(0x00u, dataBus.contention)

        // prepare data on bus
        dataDriver.set(0x69u)
        selectionDriver.set(address.toUInt())
        sim.tick(10)
        assertEquals(0x69u, dataBus.state)
        assertEquals(0x00u, dataBus.contention)

        // write to register
        writeDriver.set(true)
        sim.tick(10)
        writeDriver.set(false)
        sim.tick(10)
        assertEquals(0x69u, dataBus.state)
        assertEquals(0x00u, dataBus.contention)

        // release bus
        dataDriver.release()
        selectionDriver.release()
        sim.tick(10)
        assertEquals(0xffffffffu, dataBus.state)
        assertEquals(0x00u, dataBus.contention)

        // recall each register. only the selected register must be affected
        readDriver.set(true)
        for (i in 0..3) {
            selectionDriver.set(i.toUInt().shl(2))
            sim.tick(10)

            if (i == address) {
                assertEquals(0x69u, dataBus.state)
            } else {
                assertEquals(0x00u, dataBus.state)
            }
            assertEquals(0x00u, dataBus.contention)
        }
    }

    @Test
    fun `registers C and D can drive the address bus`() {
        // stabilize startup
        resetDriver.set(true)
        sim.tick(10)
        resetDriver.set(false)
        sim.tick(10)
        assertEquals(0xffffffffu, dataBus.state)
        assertEquals(0x00u, dataBus.contention)
        assertEquals(0xffffffffu, addressBus.state)

        // prepare 0x69 to C
        dataDriver.set(0x69u)
        selectionDriver.set(1u)
        sim.tick(10)
        assertEquals(0x69u, dataBus.state)
        assertEquals(0x00u, dataBus.contention)
        assertEquals(0xffffffffu, addressBus.state)
        // write to register
        writeDriver.set(true)
        sim.tick(10)
        writeDriver.set(false)
        sim.tick(10)
        assertEquals(0x69u, dataBus.state)
        assertEquals(0x00u, dataBus.contention)
        assertEquals(0xffffffffu, addressBus.state)

        // prepare 0x96 to D
        dataDriver.set(0x96u)
        selectionDriver.set(2u)
        sim.tick(10)
        assertEquals(0x96u, dataBus.state)
        assertEquals(0x00u, dataBus.contention)
        assertEquals(0xffffffffu, addressBus.state)
        // write to register
        writeDriver.set(true)
        sim.tick(10)
        writeDriver.set(false)
        sim.tick(10)
        assertEquals(0x96u, dataBus.state)
        assertEquals(0x00u, dataBus.contention)
        assertEquals(0xffffffffu, addressBus.state)

        // release bus
        dataDriver.release()
        selectionDriver.release()
        sim.tick(10)
        assertEquals(0xffffffffu, dataBus.state)
        assertEquals(0x00u, dataBus.contention)
        assertEquals(0xffffffffu, addressBus.state)

        // activate address bus
        addressDriver.set(true)
        sim.tick(10)
        assertEquals(0xffffffffu, dataBus.state)
        assertEquals(0x00u, dataBus.contention)
        assertEquals(0x9669u, addressBus.state)
    }

}
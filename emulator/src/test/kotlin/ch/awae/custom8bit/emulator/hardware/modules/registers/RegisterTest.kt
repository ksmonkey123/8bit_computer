package ch.awae.custom8bit.emulator.hardware.modules.registers

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class RegisterTest {

    @Test
    fun testStoreAndRecall() {
        val dataBus = StandardWritableBus(false)
        val read = StandardWritableSignal(false)
        val write = StandardWritableSignal(false)
        val register = Register(dataBus, read, write, null)
        val sim = Simulation(dataBus, read, write, register)

        val dataDriver = dataBus.connectDriver()
        val readDriver = read.connectDriver()
        val writeDriver = write.connectDriver()

        sim.tick()
        val initialInternal = register.directBus.state
        sim.tick(10)
        assertEquals(initialInternal, register.directBus.state)

        // put data on bus
        dataDriver.set(0x69u)
        sim.tick()
        assertEquals(initialInternal, register.directBus.state)

        // latch data
        writeDriver.set(true)
        sim.tick()
        assertEquals(initialInternal, register.directBus.state)
        writeDriver.set(false)
        dataDriver.release()
        sim.tick()
        assertEquals(0x69u, register.directBus.state)
        assertEquals(0x00u, dataBus.state)
        sim.tick(100)
        assertEquals(0x69u, register.directBus.state)
        assertEquals(0x00u, dataBus.state)

        // recall data
        readDriver.set(true)
        sim.tick()
        assertEquals(0x69u, register.directBus.state)
        assertEquals(0x00u, dataBus.state)
        sim.tick()
        assertEquals(0x69u, register.directBus.state)
        assertEquals(0x69u, dataBus.state)
        sim.tick(100)
        assertEquals(0x69u, register.directBus.state)
        assertEquals(0x69u, dataBus.state)
    }

    @Test
    fun testStartWithReset() {

        val dataBus = StandardWritableBus(false)
        val read = StandardWritableSignal(false)
        val write = StandardWritableSignal(false)
        val reset = StandardWritableSignal(true)
        val register = Register(dataBus, read, write, reset)
        val sim = Simulation(dataBus, read, write, reset, register)

        // clear on second tick
        sim.tick(2)
        assertEquals(0u, register.directBus.state)
    }

    @Test
    fun testDataTransferBetweenRegisters() {
        val dataBus = StandardWritableBus(false)
        val reset = StandardWritableSignal(true)
        val readA = StandardWritableSignal(false)
        val readB = StandardWritableSignal(false)
        val writeA = StandardWritableSignal(false)
        val writeB = StandardWritableSignal(false)
        val registerA = Register(dataBus, readA, writeA, reset)
        val registerB = Register(dataBus, readB, writeB, reset)
        val sim = Simulation(dataBus, readA, readB, writeA, writeB, reset, registerA, registerB)

        val dataDriver = dataBus.connectDriver()
        val resetDriver = reset.connectDriver()
        val writeADriver = writeA.connectDriver()
        val writeBDriver = writeB.connectDriver()
        val readADriver = readA.connectDriver()
        val readBDriver = readB.connectDriver()

        // both registers cleared after 2 ticks
        sim.tick(2)
        assertEquals(0u, registerA.directBus.state)
        assertEquals(0u, registerB.directBus.state)

        resetDriver.set(false)
        sim.tick(100)
        assertEquals(0u, registerA.directBus.state)
        assertEquals(0u, registerB.directBus.state)
        assertEquals(0u, dataBus.state)

        // store value in A
        dataDriver.set(0x69u)
        sim.tick()
        writeADriver.set(true)
        sim.tick()
        writeADriver.set(false)
        dataDriver.release()
        sim.tick(100)
        assertEquals(0x69u, registerA.directBus.state)
        assertEquals(0u, registerB.directBus.state)
        assertEquals(0u, dataBus.state)

        // transfer A to B
        readADriver.set(true)
        sim.tick(2)
        writeBDriver.set(true)
        sim.tick()
        writeBDriver.set(false)
        readADriver.set(false)
        sim.tick(100)
        assertEquals(0x69u, registerA.directBus.state)
        assertEquals(0x69u, registerB.directBus.state)
        assertEquals(0u, dataBus.state)

        // store new value in A
        dataDriver.set(0x96u)
        sim.tick()
        writeADriver.set(true)
        sim.tick()
        writeADriver.set(false)
        dataDriver.release()
        sim.tick(100)
        assertEquals(0x96u, registerA.directBus.state)
        assertEquals(0x69u, registerB.directBus.state)
        assertEquals(0u, dataBus.state)

        // transfer B to A
        readBDriver.set(true)
        sim.tick(2)
        writeADriver.set(true)
        sim.tick()
        writeADriver.set(false)
        readBDriver.set(false)
        sim.tick(100)
        assertEquals(0x69u, registerA.directBus.state)
        assertEquals(0x69u, registerB.directBus.state)
        assertEquals(0u, dataBus.state)
    }

    @Test
    fun testRegisterUnaffectedByDataBusIfNotWriting() {
        val dataBus = StandardWritableBus(false)
        val read = StandardWritableSignal(false)
        val write = StandardWritableSignal(false)
        val register = Register(dataBus, read, write, null)
        val sim = Simulation(dataBus, read, write, register)

        val dataDriver = dataBus.connectDriver()

        sim.tick()
        val initialInternal = register.directBus.state

        repeat(1000) {
            dataDriver.setRandom(0xffu)
            sim.tick()
            assertEquals(initialInternal, register.directBus.state)
        }
    }

}

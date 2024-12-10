package ch.awae.custom8bitemulator.hardware

import kotlin.test.*

class RegisterTest {
    val dataBus = WritableBus<UByte>()
    val read = WritableSignal(false)
    val write = WritableSignal(false)
    val register = Register(dataBus, read, write)

    @Test
    fun testPassiveWhenDisabled() {
        for (i in 0..<255) {
            val data = i.toUByte()
            dataBus.push(this, data)
            assertEquals(BusState.of(data), dataBus.currentState)
        }

        dataBus.push(this, null)
        read.push(this, true)
        assertEquals(BusState.of<UByte>(0u), dataBus.currentState)
    }

    @Test
    fun testSaveAndRecall() {
        for (i in 0..255) {
            val data = i.toUByte()
            dataBus.push(this, data)
            write.push(this, true)
            write.push(this, false)
            dataBus.push(this, null)
            read.push(this, true)
            assertEquals(BusState.of(data), dataBus.currentState)
            read.push(this, false)
        }
    }

    @Test
    fun testMoveBetweenRegisters() {
        val readB = WritableSignal(false)
        val writeB = WritableSignal(false)
        val registerB = Register(dataBus, readB, writeB)

        for (i in 0..255) {
            val data = i.toUByte()

            // move literal to A
            dataBus.push(this, data)
            write.push(this, true)
            write.push(this, false)
            dataBus.push(this, null)

            // move A to B
            read.push(this, true)
            writeB.push(this, true)
            writeB.push(this, false)
            read.push(this, false)

            // check content of B
            readB.push(this, true)
            assertEquals(BusState.of(data), dataBus.currentState)
            readB.push(this, false)
        }
    }

    @Test
    fun testSaveAndConsumeInternalBus() {
        var previous: UByte = 0u
        for (i in 0..255) {
            val data = i.toUByte()
            dataBus.push(this, data)
            assertEquals(BusState.of(previous), register.internalBus.currentState)
            write.push(this, true)
            assertEquals(BusState.of(data), register.internalBus.currentState)
            write.push(this, false)
            dataBus.push(this, null)
            read.push(this, true)
            assertEquals(BusState.of(data), dataBus.currentState)
            assertEquals(BusState.of(data), register.internalBus.currentState)
            read.push(this, false)

            previous = data
        }
    }

    @Test
    fun testBusContentionIrrelevantWhenDisabled() {
        // load a value into register
        dataBus.push(1, 42u)
        write.push(1, true)
        write.push(1, false)
        dataBus.push(2, 69u)

        assertEquals(42u, register.internalBus.currentState.tryValue())

        dataBus.push(2, null)
        dataBus.push(1, null)

        read.push(1, true)

        assertEquals(42u, dataBus.currentState.tryValue())
    }

    @Test
    fun testDataBusContentionProblemOnWrite() {
        dataBus.push(1, 42u)
        dataBus.push(2, 69u)

        assertFails {
            write.push(1, true)
        }
    }

    @Test
    fun testReadSignalContention() {
        read.push(1, true)
        assertFails {
            read.push(2, false)
        }
    }

    @Test
    fun testWriteSignalContention() {
        read.push(1, false)
        assertFails {
            read.push(2, true)
        }
    }

    @Test
    fun testWriteDuringRead() {
        // init register
        dataBus.push(1, 42u)
        write.push(1, true)
        write.push(1, false)
        dataBus.push(1, null)
        // test
        read.push(1, true)
        write.push(1, true)
        write.push(1, false)
        // validate
        assertEquals(42u, dataBus.currentState.tryValue())
    }

}
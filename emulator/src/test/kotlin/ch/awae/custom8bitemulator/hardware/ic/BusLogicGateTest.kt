package ch.awae.custom8bitemulator.hardware.ic

import ch.awae.custom8bitemulator.hardware.wiring.*
import kotlin.test.*

class BusLogicGateTest {

    @Test
    fun testAndGate() {
        val inputA = MockBus()
        val inputB = MockBus()
        val output = MockBus()

        val gate = BusLogicGate(BusLogicGate.Operation.AND, inputA, inputB, output)

        for (byte in 0..3) {
            for (a in 0..255) {
                for (b in 0..255) {
                    inputA.state = a.toUInt() shl (8 * byte)
                    inputB.state = b.toUInt() shl (8 * byte)
                    gate.tick()
                    assertEquals((a.toUInt() and b.toUInt()) shl (8 * byte), output.driverState)
                }
            }
        }
    }

    @Test
    fun testXorGate() {
        val inputA = MockBus()
        val inputB = MockBus()
        val output = MockBus()

        val gate = BusLogicGate(BusLogicGate.Operation.XOR, inputA, inputB, output)

        for (byte in 0..3) {
            for (a in 0..255) {
                for (b in 0..255) {
                    inputA.state = a.toUInt() shl (8 * byte)
                    inputB.state = b.toUInt() shl (8 * byte)
                    gate.tick()
                    assertEquals((a.toUInt() xor b.toUInt()) shl (8 * byte), output.driverState)
                }
            }
        }
    }

    @Test
    fun testIorGate() {
        val inputA = MockBus()
        val inputB = MockBus()
        val output = MockBus()

        val gate = BusLogicGate(BusLogicGate.Operation.IOR, inputA, inputB, output)

        for (byte in 0..3) {
            for (a in 0..255) {
                for (b in 0..255) {
                    inputA.state = a.toUInt() shl (8 * byte)
                    inputB.state = b.toUInt() shl (8 * byte)
                    gate.tick()
                    assertEquals((a.toUInt() or b.toUInt()) shl (8 * byte), output.driverState)
                }
            }
        }
    }

}

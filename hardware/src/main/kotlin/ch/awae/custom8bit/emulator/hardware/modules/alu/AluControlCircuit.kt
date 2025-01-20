package ch.awae.custom8bit.emulator.hardware.modules.alu

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.wiring.*

/**
 * bit 0: !regL
 * bit 1: !regB
 * bit 2: invA
 * bit 3: !logic
 * bit 4: !math
 * bit 5: !roll
 */
class AluControlCircuit(
    private val command: DataBus,
    private val enable: DataSignal,
    output: WritableBus,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val driver = output.connectDriver()

    private val commandMappings = mapOf(
        // logic unit A,B
        0b0_0000 to 0b110_001,
        0b0_0001 to 0b110_001,
        0b0_0010 to 0b110_001,
        0b0_0011 to 0b110_001,
        0b0_0100 to 0b110_001,
        0b0_0101 to 0b110_001,
        0b0_0110 to 0b110_001,
        0b0_0111 to 0b110_001,
        // math unit A,B
        0b0_1000 to 0b101_001,
        0b0_1001 to 0b101_001,
        0b0_1010 to 0b101_001,
        0b0_1011 to 0b101_001,
        // nibble swap
        0b0_1100 to 0b011_001,
        // 2s complement
        0b0_1101 to 0b101_101,
        // roll unit
        0b0_1110 to 0b011_001,
        0b0_1111 to 0b011_001,
        // literal logic
        0b1_0000 to 0b110_010,
        0b1_0001 to 0b110_010,
        0b1_0010 to 0b110_010,
        0b1_0011 to 0b110_010,
        0b1_0100 to 0b110_010,
        0b1_0101 to 0b110_010,
        0b1_0110 to 0b110_010,
        0b1_0111 to 0b110_010,
        // literal math
        0b1_1000 to 0b101_010,
        0b1_1001 to 0b101_010,
        0b1_1010 to 0b101_010,
        0b1_1011 to 0b101_010,
        // nibble swap
        0b1_1100 to 0b011_010,
        // 2s complement
        0b1_1101 to 0b101_110,
        // roll unit
        0b1_1110 to 0b011_010,
        0b1_1111 to 0b011_010,
    )

    override fun tick(tickID: Long) {
        val rawOutput = commandMappings[command.state.toInt() and 0x1f]!!.toUInt() and 0x3fu

        if (enable.state) {
            driver.set(rawOutput)
        } else {
            driver.set(rawOutput or 0x38u)
        }
    }

}
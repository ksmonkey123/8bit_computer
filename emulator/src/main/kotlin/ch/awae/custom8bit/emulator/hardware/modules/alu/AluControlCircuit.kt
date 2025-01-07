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
        0b00_000 to 0b110_001,
        0b00_001 to 0b110_001,
        0b00_010 to 0b110_001,
        0b00_011 to 0b110_001,
        0b00_100 to 0b110_001,
        0b00_101 to 0b110_001,
        0b00_110 to 0b110_001,
        0b00_111 to 0b110_001,
        // math unit A,B
        0b01_000 to 0b101_001,
        0b01_001 to 0b101_001,
        0b01_010 to 0b101_001,
        0b01_011 to 0b101_001,
        0b01_100 to 0b101_001,
        0b01_101 to 0b101_001,
        0b01_110 to 0b101_001,
        0b01_111 to 0b101_001,
        // literal logic
        0b10_000 to 0b110_010,
        0b10_001 to 0b110_010,
        0b10_010 to 0b110_010,
        0b10_011 to 0b110_010,
        0b10_100 to 0b110_010,
        0b10_101 to 0b110_010,
        0b10_110 to 0b110_010,
        0b10_111 to 0b110_010,
        // roll unit
        0b11_000 to 0b011_010,
        0b11_001 to 0b011_010,
        0b11_010 to 0b011_010,
        // math unit (complement of A)
        0b11_011 to 0b101_110,
        // literal math
        0b11_100 to 0b101_010,
        0b11_101 to 0b101_010,
        0b11_110 to 0b101_010,
        0b11_111 to 0b101_010,
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
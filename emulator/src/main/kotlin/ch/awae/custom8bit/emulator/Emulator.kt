package ch.awae.custom8bit.emulator

import ch.awae.custom8bit.emulator.memory.*
import ch.awae.custom8bit.emulator.memory.devices.*
import ch.awae.custom8bit.emulator.processor.*

class Emulator(memoryBus: MemoryBus) {

    private val processingUnit = ProcessingUnit(memoryBus)

    private var state = ProcessorState()

    fun runToCompletion() {
        while (!state.halted) {
            state = processingUnit.executeNextCommand(state)
        }
    }

    constructor(
        program: ByteArray,
        vararg peripheral: Pair<Int, PeripheralDevice>
    ) : this(buildMemoryBus(program, peripheral.toMap()))
}

private fun buildMemoryBus(program: ByteArray, peripherals: Map<Int, PeripheralDevice>): MemoryBus {
    return StandardMemoryBus(
        RomChip8k(0, program),
        Ram32k(),
        *peripherals.map { (id, peripheral) ->
            MemoryMappedPeripheral(peripheral, 0x4000 + id * 0x0100)
        }.toTypedArray()
    )
}
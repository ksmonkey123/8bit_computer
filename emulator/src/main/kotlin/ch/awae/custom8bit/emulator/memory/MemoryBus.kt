package ch.awae.custom8bit.emulator.memory


interface MemoryBus {
    fun read(address: Int): Int
    fun write(address: Int, data: Int)
}



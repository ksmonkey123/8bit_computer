package ch.awae.custom8bit.emulator.processor

class Microcode(
    val addressingRom: ByteArray,
    val actionRom: ByteArray
) {

    constructor(raw: Pair<ByteArray, ByteArray>) : this(raw.first, raw.second)

    fun execute(instruction: Int, step: Int, flags: Flags): ExecuteBlock {
        val address = (((step shl 3) + flags.encoded) shl 8) + instruction
        return ExecuteBlock((addressingRom[address].toInt() and 0xff) + ((actionRom[address].toInt() and 0xff) shl 8))
    }

    companion object {
        val INSTANCE = Microcode(ch.awae.custom8bit.microcode.Microcode.microcode)
    }

}
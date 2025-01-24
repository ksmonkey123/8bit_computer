package ch.awae.custom8bit.emulator.processor

data class ControlBlock(
    val skipFetch: Int,
    val carryUpdate: Boolean?,
    val halt: Boolean,
) {
    constructor(raw: Int) : this(
        (raw and 0x03).also {
            if (it == 0) throw java.lang.IllegalArgumentException("bad control byte: $raw (bad fetch size)")
        },
        when (raw and 0x0c) {
            0 -> null
            8 -> false
            12 -> true
            else -> throw IllegalArgumentException("bad control byte: $raw (bad carry block)")
        },
        (raw and 0x80) != 0
    )
}
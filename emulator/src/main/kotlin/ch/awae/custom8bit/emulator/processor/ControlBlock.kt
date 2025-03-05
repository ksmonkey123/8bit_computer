package ch.awae.custom8bit.emulator.processor

data class ControlBlock(
    val skipFetch: Int,
    val halt: Boolean,
) {
    constructor(raw: Int) : this(
        (raw and 0x03).also {
            if (it == 3) throw java.lang.IllegalArgumentException("bad control byte: $raw (bad fetch size)")
        },
        (raw and 0x80) != 0
    )
}
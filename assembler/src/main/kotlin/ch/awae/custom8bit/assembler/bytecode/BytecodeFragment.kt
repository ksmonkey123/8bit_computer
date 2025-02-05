package ch.awae.custom8bit.assembler.bytecode

data class BytecodeFragment(
    val startAt: Int,
    val data: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BytecodeFragment

        return startAt == other.startAt
    }

    override fun hashCode(): Int {
        return startAt
    }

    class Builder(private val startAt: Int) {

        private var data = mutableListOf<Byte>()

        fun append(int: Int): Builder {
            data.add((int and 0xff).toByte())
            return this
        }

        fun append(vararg ints: Int): Builder {
            ints.forEach { data.add((it and 0xff).toByte()) }
            return this
        }

        fun build(): BytecodeFragment {
            return BytecodeFragment(this.startAt, this.data.toByteArray())
        }

    }

}
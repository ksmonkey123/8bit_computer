package ch.awae.custom8bit.assembler.ast

data class Constant(
    val symbol: String,
    val size: Int,
    val data: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Constant

        if (size != other.size) return false
        if (symbol != other.symbol) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + symbol.hashCode()
        return result
    }
}

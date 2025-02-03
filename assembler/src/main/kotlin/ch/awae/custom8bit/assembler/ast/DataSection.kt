package ch.awae.custom8bit.assembler.ast

data class DataSection(
    val constants: List<Constant>
)

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

fun AssemblerParser.DataStatementContext.toConstant(): Constant {
    return when (this) {
        is AssemblerParser.SimpleConstantContext -> this.toConstant()
        is AssemblerParser.ArrayConstantContext -> this.toConstant()
        else -> throw ParsingException(this)
    }
}

fun AssemblerParser.SimpleConstantContext.toConstant(): Constant {
    val field = this.fieldDeclaration().toFieldDeclaration()
    val value = this.value.toInt()
    val decomposedData = ByteArray(field.size) { i ->
        (value ushr (8 * i)).toByte()
    }
    return Constant(field.symbol, field.size, decomposedData)
}

fun AssemblerParser.ArrayConstantContext.toConstant(): Constant {
    val field = this.fieldDeclaration().toFieldDeclaration()
    val data = this.value.toList()
    val dataBytes = ByteArray(field.size) { i ->
        (data[i] and 0xff).toByte()
    }

    return Constant(field.symbol, field.size, dataBytes)
}

fun AssemblerParser.ListOfNumbersContext.toList(): List<Int> {
    val continuation = this.listOfNumbers()?.toList() ?: emptyList()
    return listOf(this.numericExpression().toInt(), *continuation.toTypedArray())
}

fun AssemblerParser.DataSectionContext.toDataSection(): DataSection {
    return DataSection(dataStatement().map { it.toConstant() })
}
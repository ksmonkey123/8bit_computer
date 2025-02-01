package ch.awae.custom8bit.assembler.ast


fun AssemblerParser.NumericLiteralContext.toInt(): Int {
    val text = this.NUMBER().text.filter { it != '_' }
    return parseNumericLiteral(text)
}

fun parseNumericLiteral(text: String): Int {
    return when {
        text.startsWith("0b") -> text.drop(2).toInt(2)
        text.startsWith("0x") -> text.drop(2).toInt(16)
        else -> text.toInt(10)
    }
}

fun AssemblerParser.NumericExpressionContext.toInt(): Int {
    return this.numericLiteral().toInt()
}

fun AssemblerParser.LiteralValueContext.toInt(): Int {
    return this.numericExpression().toInt()
}
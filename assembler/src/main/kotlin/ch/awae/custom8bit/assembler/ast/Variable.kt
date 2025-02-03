package ch.awae.custom8bit.assembler.ast

data class Variable(
    val symbol: String,
    val size: Int,
    val position: Int,
)


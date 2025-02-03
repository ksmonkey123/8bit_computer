package ch.awae.custom8bit.assembler.ast

data class CodeSection(
    val startAt: Int,
    val instructions: List<Instruction>,
)

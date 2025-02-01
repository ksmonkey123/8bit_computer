package ch.awae.custom8bit.assembler.ast

data class CodeSection(
    val startAt: Int,
    val instructions: List<Instruction>,
)

fun AssemblerParser.CodeSectionContext.toCodeSection(): CodeSection = CodeSection(
    startAt = this.startAt.toInt(),
    this.statement().map { it.toInstruction() }
)

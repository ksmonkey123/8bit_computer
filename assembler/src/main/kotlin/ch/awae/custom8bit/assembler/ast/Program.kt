package ch.awae.custom8bit.assembler.ast

data class Program(
    val codeSections: List<CodeSection>,
    val constants: List<Constant>,
    val variables: List<Variable>,
)


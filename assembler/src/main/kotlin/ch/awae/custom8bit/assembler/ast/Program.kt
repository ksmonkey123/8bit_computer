package ch.awae.custom8bit.assembler.ast

data class Program(
    val codeSections: List<CodeSection>,
    val variables: List<Variable>,
)

fun AssemblerParser.ProgramContext.toProgram(): Program = Program(
    codeSections = section().filterIsInstance<AssemblerParser.CodeSectionContext>().map { it.toCodeSection() },
    variables = section()
        .filterIsInstance<AssemblerParser.VariableSectionContext>()
        .map { it.toVarsSection() }
        .reduce { a, b -> a.join(b) }
        .variables
)
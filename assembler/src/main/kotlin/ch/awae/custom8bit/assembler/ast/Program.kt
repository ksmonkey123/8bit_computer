package ch.awae.custom8bit.assembler.ast

data class Program(
    val codeSections: List<CodeSection>,
    val constants: List<Constant>,
    val variables: List<Variable>,
)

fun AssemblerParser.ProgramContext.toProgram(): Program = Program(
    codeSections = section().filterIsInstance<AssemblerParser.CodeSectionContext>()
        .map { it.toCodeSection() },
    constants = section().filterIsInstance<AssemblerParser.DataSectionContext>()
        .flatMap { it.toDataSection().constants },
    variables = section()
        .filterIsInstance<AssemblerParser.VariableSectionContext>()
        .map { it.toVarsSection() }
        .reduce { a, b -> a.join(b) }
        .variables
)
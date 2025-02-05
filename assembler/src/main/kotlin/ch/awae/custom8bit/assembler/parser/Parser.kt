package ch.awae.custom8bit.assembler.parser

import AssemblerLexer
import AssemblerParser
import ch.awae.custom8bit.assembler.*
import ch.awae.custom8bit.assembler.ast.*
import org.antlr.v4.runtime.*
import org.springframework.stereotype.Component

@Component
class Parser {

    private val logger = createLogger()

    fun parseProgram(input: String): Program {
        if (!input.endsWith('\n')) {
            logger.warn("program does not end with a line break")
            return parseProgram(input + '\n')
        }

        logger.info("parsing program (${input.length} chars)")
        val inputCharStream = CharStreams.fromString(input)
        val tokenStream = CommonTokenStream(AssemblerLexer(inputCharStream))
        val parser = AssemblerParser(tokenStream)
        val listener = HasErrorErrorListener()
        parser.addErrorListener(listener)
        val program = parser.program()

        if (listener.hasError) {
            logger.error("error parsing program")
            throw RuntimeException("parser had an error")
        }

        logger.info("generating AST")
        return program.toProgram().also {
            logger.info("found ${it.codeSections.size} code sections")
            it.codeSections.forEach{ c ->
                logger.info("  ${c.startAt.toHex(2)}: ${c.instructions.size} instructions")
            }
            logger.info("found ${it.variables.size} variables")
            it.variables.forEach { v ->
                logger.info("  ${v.position.toHex(2)}: ${v.symbol}")
            }
            logger.info("found ${it.constants.size} constants")
            it.constants.forEach { c ->
                logger.info("  ${c.symbol}: ${c.size} byte(s)")
            }
        }
    }

}

class HasErrorErrorListener : BaseErrorListener() {

    var hasError = false
        private set

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        hasError = true
    }
}

data class FieldDeclaration(val symbol: String, val size: Int)

data class DataSection(
    val constants: List<Constant>
)

data class VarsSection(
    val variables: List<Variable>,
) {

    fun join(other: VarsSection): VarsSection {
        val occupiedByThis = variables.flatMap { v -> v.position until (v.position + v.size) }
        val occupiedByOther = variables.flatMap { v -> v.position until (v.position + v.size) }
        if (occupiedByThis.intersect(occupiedByOther.toSet()).isNotEmpty()) {
            throw IllegalArgumentException("variable sections overlapping: $this, $other")
        }

        return VarsSection(this.variables + other.variables)
    }
}

fun AssemblerParser.ShiftInstructionContext.toInstruction(): Instruction {
    return ShiftInstruction(ShiftOperation.valueOf(this.text))
}

fun AssemblerParser.SwapInstructionContext.toInstruction(): Instruction {
    return SwapInstruction(
        this.register8NotA()?.toRegister()
            ?: this.addressingExpression()?.toAddressingExpression()
            ?: throw ParsingException(this)
    )
}


fun AssemblerParser.CodeSectionContext.toCodeSection(): CodeSection = CodeSection(
    startAt = this.startAt.toInt(),
    this.statement().map { it.toInstruction() }
)


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

fun AssemblerParser.StackManipulationInstructionContext.toInstruction(): Instruction {
    return when (this.op.text) {
        "spa" -> StackAllocationInstruction(this.size.toInt())
        "spf" -> StackFreeInstruction(this.size.toInt())
        else -> throw ParsingException(this)
    }
}

fun AssemblerParser.InstructionContext.toInstruction(): Instruction {
    this.binaryAluInstruction()?.let { return it.toInstruction() }
    this.unaryAluInstruction()?.let { return it.toInstruction() }
    this.moveInstruction()?.let { return it.toInstruction() }
    this.branchInstruction()?.let { return it.toInstruction() }
    this.simpleInstruction()?.let { return it.toInstruction() }
    this.shiftInstruction()?.let { return it.toInstruction() }
    this.swapInstruction()?.let { return it.toInstruction() }
    this.stackManipulationInstruction()?.let { return it.toInstruction() }
    throw ParsingException(this)
}

fun AssemblerParser.StatementContext.toInstruction(): Instruction {
    return when (this) {
        is AssemblerParser.LabelledInstructionContext -> LabelledInstruction(
            this.label().SYMBOL().text,
            this.instruction().toInstruction()
        )

        is AssemblerParser.NormalInstructionContext -> this.instruction().toInstruction()
        else -> throw ParsingException(this)
    }
}


fun AssemblerParser.SimpleInstructionContext.toInstruction(): Instruction {
    return when (this.operation.text) {
        "ret" -> ReturnInstruction
        "nop" -> NopInstruction
        "hlt" -> HaltInstruction
        "cfc" -> CarryUpdateInstruction(false)
        "cfs" -> CarryUpdateInstruction(true)
        else -> throw ParsingException(this)
    }
}

fun AssemblerParser.BinaryAluInstructionContext.toInstruction(): BinaryAluInstruction {
    val op = BinaryAluOperation.valueOf(this.operation.text.uppercase())
    val source = when (val src = this.binaryAluOpSrc()) {
        is AssemblerParser.BinaryAluOpRegisterSourceContext -> src.register8().toRegister()
        is AssemblerParser.BinaryAluOpLiteralSourceContext -> LiteralSource(src.literalValue().toInt())
        is AssemblerParser.BinaryAluOpAddressingSourceContext -> src.addressingExpression().toAddressingExpression()
        else -> throw ParsingException(this)
    }
    return BinaryAluInstruction(op, source)
}

fun AssemblerParser.RegisterContext.toRegister(): Register {
    val options = (Register8.entries union Register16.entries)
    return options.find { it.name == this.text.uppercase() }
        ?: throw IllegalArgumentException("unknown register $this")
}

fun AssemblerParser.Register8Context.toRegister(): Register8 {
    return Register8.valueOf(this.text.uppercase())
}

fun AssemblerParser.Register8NotAContext.toRegister(): Register8 {
    return Register8.valueOf(this.text.uppercase())
}

fun AssemblerParser.Register16Context.toRegister(): Register16 {
    return Register16.valueOf(this.text.uppercase())
}

fun AssemblerParser.AddressingExpressionContext.toAddressingExpression(): AddressingExpression {
    return when (this) {
        is AssemblerParser.LiteralAdrExprContext -> LiteralAddressing(this.numericExpression().toInt())
        is AssemblerParser.SymbolAdrContext -> SymbolicAddressing(this.SYMBOL().text, 0)
        is AssemblerParser.DynamicAdrExprContext -> RegisterCDAddressing(0)
        is AssemblerParser.ComplexAddressingContext -> this.complexAddressingExpression().toAddressingExpression()
        else -> throw ParsingException(this)
    }
}

fun AssemblerParser.ComplexAddressingExpressionContext.toAddressingExpression(): AddressingExpression {
    return when (this) {
        is AssemblerParser.RegisterOffsetAddressingContext -> RegisterCDAddressing(this.numericExpression().toInt())
        is AssemblerParser.StackOffsetAddressingContext -> StackAddressing(this.numericExpression().toInt())
        is AssemblerParser.SymbolOffsetAddressingContext -> SymbolicAddressing(
            this.SYMBOL().text,
            this.numericExpression().toInt()
        )

        else -> throw ParsingException(this)
    }
}

fun AssemblerParser.UnaryAluInstructionContext.toInstruction(): UnaryAluInstruction {
    return UnaryAluInstruction(
        UnaryAluOperation.valueOf(this.operation.text.uppercase()),
        this.register8().toRegister(),
    )
}

fun AssemblerParser.MoveInstructionContext.toInstruction(): Instruction {
    return when (this) {
        is AssemblerParser.MovCopy8Context -> RegisterCopyInstruction(this.from.toRegister(), this.to.toRegister())
        is AssemblerParser.MovCopy16Context -> RegisterCopyInstruction(this.from.toRegister(), this.to.toRegister())
        is AssemblerParser.MovLoadContext -> RegisterLoadInstruction(
            when (val src = this.from) {
                is AssemblerParser.LiteralMoveSourceContext -> LiteralSource(src.literalValue().toInt())
                is AssemblerParser.AddressedMoveSourceContext -> src.addressingExpression().toAddressingExpression()
                else -> throw ParsingException(this)
            },
            this.to.toRegister()
        )

        is AssemblerParser.MovStoreContext -> RegisterStoreInstruction(
            this.from.toRegister(),
            this.to.addressingExpression().toAddressingExpression()
        )

        else -> throw ParsingException(this)
    }
}

fun AssemblerParser.BranchInstructionContext.toInstruction(): BranchInstruction {
    return BranchInstruction(
        BranchOperation.valueOf(this.operation.text.uppercase()),
        this.branchTarget().SYMBOL().text,
    )
}

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


fun AssemblerParser.VariableSectionContext.toVarsSection(): VarsSection {
    val startAt = this.startAt?.toInt()

    val unplaced = mutableListOf<FieldDeclaration>()
    val fixedPlace = mutableListOf<Pair<Int, FieldDeclaration>>()

    for (statement in this.variableStatement()) {
        when (statement) {
            is AssemblerParser.VariableDeclarationContext -> {
                unplaced.add(statement.fieldDeclaration().toFieldDeclaration())
            }

            is AssemblerParser.VariableDeclarationWithFixedPositionContext -> {
                fixedPlace.add(statement.pos.toInt() to statement.fieldDeclaration().toFieldDeclaration())
            }

            else -> throw ParsingException(this)
        }
    }

    val variables = mutableListOf<Variable>()


    val usedPositions = mutableSetOf<Int>()
    fun checkAndReservePositions(start: Int, size: Int): Boolean {
        val newPositions = start.until(start + size).toSet()
        if (usedPositions.intersect(newPositions).isNotEmpty()) {
            return false
        }
        // range is ok, reserve it
        usedPositions.addAll(newPositions)
        return true
    }

    // place fixed variables
    for ((pos, field) in fixedPlace) {
        if (checkAndReservePositions(pos, field.size)) {
            variables.add(Variable(field.symbol, field.size, pos))
        } else {
            throw IllegalArgumentException("fixed variable cannot be placed. overlapping another one!")
        }
    }

    // place dynamic variables wherever possible
    for (fd in unplaced) {
        var pos = startAt
            ?: throw IllegalArgumentException("cannot have unplaced variables in variable section without starting point")
        while (true) {
            if (checkAndReservePositions(pos, fd.size)) {
                // success, we found a place for the variable
                variables.add(Variable(fd.symbol, fd.size, pos))
                break
            } else {
                pos++
            }
        }
    }

    return VarsSection(variables.toList().sortedBy { it.position })
}

fun AssemblerParser.FieldDeclarationContext.toFieldDeclaration(): FieldDeclaration =
    when (this) {
        //is AssemblerParser.SimpleFieldContext -> FieldDeclaration(this.SYMBOL().text, 1)
        is AssemblerParser.FieldWithSizeContext -> FieldDeclaration(this.SYMBOL().text, this.size.toInt())
        else -> throw ParsingException(this)
    }
package ch.awae.custom8bit.assembler.ast

import AssemblerParser
import org.antlr.v4.runtime.*

sealed interface Instruction {
    val size: Int
}

data class BinaryAluInstruction(
    val operation: BinaryAluOperation,
    val source: BinaryAluOpSource,
) : Instruction {
    override val size: Int = when (source) {
        is Register8 -> 1
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is LiteralSource -> 2
        is SymbolicAddressing -> 3
        is LiteralAddressing -> 3
    }
}


enum class BinaryAluOperation {
    AND, IOR, XOR, ADC, SBC
}

sealed interface BinaryAluOpSource

sealed interface Register

enum class Register16 : Register {
    AB, CD
}

enum class Register8 : Register, BinaryAluOpSource {
    A, B, C, D
}

sealed interface MoveSource

data class LiteralSource(val value: Int) : BinaryAluOpSource, MoveSource

sealed interface AddressingExpression : BinaryAluOpSource, MoveSource

data class LiteralAddressing(val value: Int) : AddressingExpression
data class SymbolicAddressing(val symbol: String, val offset: Int = 0) : AddressingExpression
data class RegisterCDAddressing(val offset: Int) : AddressingExpression
data class StackAddressing(val offset: Int) : AddressingExpression

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

data class UnaryAluInstruction(
    val operation: UnaryAluOperation,
    val source: Register8,
) : Instruction {
    override val size = 1
}

enum class ShiftOperation {
    RLC, RL, RRC, RR, RRA
}

data class ShiftInstruction(val operation: ShiftOperation) : Instruction {
    override val size = 1
}

enum class UnaryAluOperation {
    NOT, INC, DEC, COMP
}

data class SwapInstruction(val register: Register8) : Instruction {
    override val size = 1
}

fun AssemblerParser.UnaryAluInstructionContext.toInstruction(): UnaryAluInstruction {
    return UnaryAluInstruction(
        UnaryAluOperation.valueOf(this.operation.text.uppercase()),
        this.register8().toRegister(),
    )
}

data class RegisterCopyInstruction(val from: Register, val to: Register) : Instruction {
    override val size = 1
}

data class RegisterLoadInstruction(val from: MoveSource, val to: Register) : Instruction {
    override val size: Int = when (from) {
        is LiteralAddressing -> 3
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is SymbolicAddressing -> 2
        is LiteralSource -> if (to is Register16) 3 else 2
    }
}

data class RegisterStoreInstruction(val from: Register, val to: AddressingExpression) : Instruction {
    override val size: Int = when (to) {
        is LiteralAddressing -> 3
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is SymbolicAddressing -> 3
    }
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

enum class BranchOperation {
    BCC, BCS, BZ, BNZ, BLZ, BGZ, BLEZ, BGEZ, GOTO, CALL
}

data class BranchInstruction(val operation: BranchOperation, val target: String) : Instruction {
    override val size = 3
}

fun AssemblerParser.BranchInstructionContext.toInstruction(): BranchInstruction {
    return BranchInstruction(
        BranchOperation.valueOf(this.operation.text.uppercase()),
        this.branchTarget().SYMBOL().text,
    )
}

data object ReturnInstruction : Instruction {
    override val size = 1
}

data object HaltInstruction : Instruction {
    override val size = 1
}

data object NopInstruction : Instruction {
    override val size = 1
}

data class CarryUpdateInstruction(val value: Boolean) : Instruction {
    override val size = 1
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

data class StackAllocationInstruction(val amount: Int) : Instruction {
    override val size = 2
}

data class StackFreeInstruction(val amount: Int) : Instruction {
    override val size = 2
}

fun AssemblerParser.StackManipulationInstructionContext.toInstruction(): Instruction {
    return when (this.op.text) {
        "spa" -> StackAllocationInstruction(this.size.toInt())
        "spf" -> StackFreeInstruction(this.size.toInt())
        else -> throw ParsingException(this)
    }
}

fun AssemblerParser.ShiftInstructionContext.toInstruction(): Instruction {
    return ShiftInstruction(ShiftOperation.valueOf(this.text))
}

fun AssemblerParser.SwapInstructionContext.toInstruction(): Instruction {
    return SwapInstruction(this.register8NotA().toRegister())
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

data class LabelledInstruction(val label: String, val instruction: Instruction) : Instruction {
    override val size = instruction.size
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

class ParsingException(rule: ParserRuleContext) : RuntimeException(
    "parse error at ${rule.start.line}:${rule.start.charPositionInLine}: ${rule.text}"
)
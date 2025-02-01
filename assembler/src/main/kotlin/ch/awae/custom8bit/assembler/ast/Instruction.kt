package ch.awae.custom8bit.assembler.ast

sealed interface Instruction {
    val size: Int
}

data class BinaryAluInstruction(
    val operation: BinaryAluOperation,
    val source: BinaryAluOpSource,
) : Instruction {
    override val size: Int = when (source) {
        is Register8 -> 1
        RegisterCDAddressing -> 1
        is LiteralSource -> 2
        is SymbolicAddressing -> 3
        is LiteralAddressing -> 3
    }
}


enum class BinaryAluOperation {
    AND, IOR, XOR, ADD, ADDC, SUB, SUBC, ISUB, ISUBC
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
data class SymbolicAddressing(val symbol: String) : AddressingExpression
data object RegisterCDAddressing : AddressingExpression

fun AssemblerParser.BinaryAluInstructionContext.toInstruction(): BinaryAluInstruction {
    val op = BinaryAluOperation.valueOf(this.operation.text.uppercase())
    val source = when (val src = this.binaryAluOpSrc()) {
        is AssemblerParser.BinaryAluOpRegisterSourceContext -> src.register8().toRegister()
        is AssemblerParser.BinaryAluOpLiteralSourceContext -> LiteralSource(src.literalValue().toInt())
        is AssemblerParser.BinaryAluOpAddressingSourceContext -> src.addressingExpression().toAddressingExpression()
        else -> TODO("unsupported source $this")
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

fun AssemblerParser.AddressingExpressionContext.toAddressingExpression(): AddressingExpression {
    return when (this) {
        is AssemblerParser.LiteralAdrExprContext -> LiteralAddressing(this.numericExpression().toInt())
        is AssemblerParser.SymbolAdrContext -> SymbolicAddressing(this.SYMBOL().text)
        is AssemblerParser.DynamicAdrExprContext -> RegisterCDAddressing
        else -> TODO("unsupported addressing type : $this")
    }
}

data class UnaryAluInstruction(
    val operation: UnaryAluOperation,
    val source: Register8,
) : Instruction {
    override val size = 1
}

enum class UnaryAluOperation {
    NOT, SHL, RCL, RL, USHR, ASHR, RRC, RR, INC, INCC, DEC, DECC, COMP, COMPC, SWAP
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
        RegisterCDAddressing -> 1
        is SymbolicAddressing -> 2
        is LiteralSource -> if (to is Register16) 3 else 2
    }
}

data class RegisterStoreInstruction(val from: Register, val to: AddressingExpression) : Instruction {
    override val size: Int = when (to) {
        is LiteralAddressing -> 3
        RegisterCDAddressing -> 1
        is SymbolicAddressing -> 3
    }
}

fun AssemblerParser.MoveInstructionContext.toInstruction(): Instruction {
    return when (this) {
        is AssemblerParser.MovCopyContext -> RegisterCopyInstruction(this.from.toRegister(), this.to.toRegister())
        is AssemblerParser.MovLoadContext -> RegisterLoadInstruction(
            when (val src = this.from) {
                is AssemblerParser.LiteralMoveSourceContext -> LiteralSource(src.literalValue().toInt())
                is AssemblerParser.AddressedMoveSourceContext -> src.addressingExpression().toAddressingExpression()
                else -> TODO("unsupported source $src")
            },
            this.to.toRegister()
        )

        is AssemblerParser.MovStoreContext -> RegisterStoreInstruction(
            this.from.toRegister(),
            this.to.addressingExpression().toAddressingExpression()
        )

        else -> TODO("unsupported move instruction $this")
    }
}

data class StackPushInstruction(val register: Register) : Instruction {
    override val size = 1
}

data class StackPopInstruction(val register: Register) : Instruction {
    override val size = 1
}

data class StackPeekInstruction(val register: Register) : Instruction {
    override val size = 1
}

fun AssemblerParser.StackInstructionContext.toInstruction(): Instruction {
    return when (this.operation.text) {
        "push" -> StackPushInstruction(this.register().toRegister())
        "pop" -> StackPopInstruction(this.register().toRegister())
        "peek" -> StackPeekInstruction(this.register().toRegister())
        else -> TODO("unsupported stack operation $this")
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

data class DecrementAndBranchInstruction(val register: Register8, val target: String) : Instruction {
    override val size = 3
}

fun AssemblerParser.DecrementAndBranchInstructionContext.toInstruction(): DecrementAndBranchInstruction {
    return DecrementAndBranchInstruction(this.register8().toRegister(), this.branchTarget().SYMBOL().text)
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

fun AssemblerParser.SimpleInstructionContext.toInstruction(): Instruction {
    return when (this.operation.text) {
        "return" -> ReturnInstruction
        "nop" -> NopInstruction
        "halt" -> HaltInstruction
        else -> TODO("unsupported operation $this")
    }
}

fun AssemblerParser.InstructionContext.toInstruction(): Instruction {
    this.binaryAluInstruction()?.let { return it.toInstruction() }
    this.unaryAluInstruction()?.let { return it.toInstruction() }
    this.moveInstruction()?.let { return it.toInstruction() }
    this.stackInstruction()?.let { return it.toInstruction() }
    this.branchInstruction()?.let { return it.toInstruction() }
    this.decrementAndBranchInstruction()?.let { return it.toInstruction() }
    this.simpleInstruction()?.let { return it.toInstruction() }
    TODO("unsupported instruction $this")
}

data class LabelledInstruction(val label: String, val instruction: Instruction) : Instruction {
    override val size = instruction.size
}

fun AssemblerParser.StatementContext.toInstruction(): Instruction {
    return when (this) {
        is AssemblerParser.LabelledInstructionContext -> LabelledInstruction(this.label().SYMBOL().text, this.instruction().toInstruction())
        is AssemblerParser.NormalInstructionContext -> this.instruction().toInstruction()
        else -> TODO("unsupported statement $this")
    }
}

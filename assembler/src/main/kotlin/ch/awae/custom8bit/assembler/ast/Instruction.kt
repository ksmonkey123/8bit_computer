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

enum class Register8 : Register, BinaryAluOpSource, SwapTarget {
    A, B, C, D
}

sealed interface SwapTarget

sealed interface MoveSource

data class LiteralSource(val value: Int) : BinaryAluOpSource, MoveSource

sealed interface AddressingExpression : BinaryAluOpSource, MoveSource, SwapTarget

data class LiteralAddressing(val value: Int) : AddressingExpression
data class SymbolicAddressing(val symbol: String, val offset: Int = 0) : AddressingExpression
data class RegisterCDAddressing(val offset: Int) : AddressingExpression
data class StackAddressing(val offset: Int) : AddressingExpression

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

data class SwapInstruction(val target: SwapTarget) : Instruction {
    override val size = when(target) {
        is Register -> 1
        is LiteralAddressing -> 3
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is SymbolicAddressing -> 3
    }
}

data class RegisterCopyInstruction(val from: Register, val to: Register) : Instruction {
    override val size = 1
}

data class RegisterLoadInstruction(val from: MoveSource, val to: Register) : Instruction {
    override val size: Int = when (from) {
        is LiteralAddressing -> 3
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is SymbolicAddressing -> 3
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

enum class BranchOperation {
    BCC, BCS, BZ, BNZ, BLZ, BGZ, BNP, BNN, GOTO, CALL
}

data class BranchInstruction(val operation: BranchOperation, val target: String) : Instruction {
    override val size = 3
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

data class StackAllocationInstruction(val amount: Int) : Instruction {
    override val size = 2
}

data class StackFreeInstruction(val amount: Int) : Instruction {
    override val size = 2
}

data class LabelledInstruction(val label: String, val instruction: Instruction) : Instruction {
    override val size = instruction.size
}

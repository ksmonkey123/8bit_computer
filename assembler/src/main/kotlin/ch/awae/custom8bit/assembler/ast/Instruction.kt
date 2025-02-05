package ch.awae.custom8bit.assembler.ast

import ch.awae.custom8bit.assembler.bytecode.*
import javax.script.CompiledScript

sealed interface Instruction {
    val size: Int
}

data class BinaryAluInstruction(
    val operation: BinaryAluOperation,
    val source: BinaryAluOpSource,
) : Instruction, CompilingInstruction {
    override val size: Int = when (source) {
        is Register8 -> 1
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is LiteralSource -> 2
        is SymbolicAddressing -> 3
        is LiteralAddressing -> 3
    }

    override fun compile(symbolMap: SymbolMap): IntArray {
        val root = operation.rootOpcode

        return when (source) {
            Register8.A -> throw IllegalArgumentException("binary alu op does not support A as an argument")
            Register8.B -> intArrayOf(root)
            Register8.C -> intArrayOf(root + 1)
            Register8.D -> intArrayOf(root + 2)
            is LiteralSource -> intArrayOf(root + 3, source.value)
            is StaticAddressing -> intArrayOf(root + 4, *source.encode(symbolMap).unwrap())
            is RegisterCDAddressing -> intArrayOf(root + 5, source.offset)
            is StackAddressing -> intArrayOf(root + 6, source.offset)
        }
    }
}

enum class BinaryAluOperation(val rootOpcode: Int) {
    ADC(0x00), SBC(0x08),
    AND(0x10), IOR(0x18),
    XOR(0x20), CMP(0x28),
}

sealed interface CompilingInstruction {
    fun compile(symbolMap: SymbolMap): IntArray
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

sealed interface StaticAddressing {
    fun encode(symbolMap: SymbolMap): Pair<Int, Int>
}

fun resolveSymbol(symbol: String, symbolMap: SymbolMap): Pair<Int, Int> {
    val raw = symbolMap[symbol] ?: throw IllegalArgumentException("unresolvable symbol: $symbol")
    return Pair((raw ushr 8) and 0xff, raw and 0xff)
}

data class LiteralAddressing(val value: Int) : AddressingExpression, StaticAddressing {
    override fun encode(symbolMap: SymbolMap): Pair<Int, Int> {
        return Pair((value ushr 8) and 0xff, value and 0xff)
    }
}

data class SymbolicAddressing(val symbol: String, val offset: Int = 0) : AddressingExpression, StaticAddressing {
    override fun encode(symbolMap: SymbolMap): Pair<Int, Int> {
        val raw = symbolMap[symbol] ?: throw IllegalArgumentException("unresolvable symbol: $symbol")
        return LiteralAddressing(raw + offset).encode(symbolMap)
    }
}

data class RegisterCDAddressing(val offset: Int) : AddressingExpression
data class StackAddressing(val offset: Int) : AddressingExpression

data class UnaryAluInstruction(
    val operation: UnaryAluOperation,
    val source: Register8,
) : Instruction, CompilingInstruction {
    override val size = 1

    override fun compile(symbolMap: SymbolMap): IntArray {
        val offset = when(source) {
            Register8.A -> 0
            Register8.B -> 1
            Register8.C -> 2
            Register8.D -> 3
        }

        return intArrayOf(operation.opcode + offset)
    }

}

enum class ShiftOperation(val opcode: Int) {
    RLC(0x40), RL(0x41), RRC(0x42), RR(0x43), RRA(0x44)
}

data class ShiftInstruction(val operation: ShiftOperation) : Instruction {
    override val size = 1
}

enum class UnaryAluOperation(val opcode: Int) {
    NOT(0x3c), INC(0x34), DEC(0x30), COMP(0x38)
}

data class SwapInstruction(val target: SwapTarget) : Instruction, CompilingInstruction {
    override val size = when (target) {
        is Register -> 1
        is LiteralAddressing -> 3
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is SymbolicAddressing -> 3
    }

    override fun compile(symbolMap: SymbolMap): IntArray {
        return when (target) {
            Register8.A -> throw IllegalArgumentException("swap does not support register A as argument")
            Register8.B -> intArrayOf(0x48)
            Register8.C -> intArrayOf(0x49)
            Register8.D -> intArrayOf(0x4a)
            is StaticAddressing -> intArrayOf(0x4b, *target.encode(symbolMap).unwrap())
            is RegisterCDAddressing -> intArrayOf(0x4c, target.offset)
            is StackAddressing -> intArrayOf(0x4d, target.offset)
        }
    }
}


data class RegisterCopyInstruction(val from: Register, val to: Register) : Instruction {
    override val size = 1

    val opcode: Int
        get() {
            fun registerToOffset(register: Register): Int = when (register) {
                Register8.A -> 0
                Register8.B -> 1
                Register8.C -> 2
                Register8.D -> 3
                Register16.AB -> 0
                Register16.CD -> 1
            }

            return if (from is Register8 && to is Register8) {
                0x60 + (registerToOffset(from)) + (4 * registerToOffset(to))
            } else if (from is Register16 && to is Register16) {
                0x90 + (registerToOffset(from)) + (2 * registerToOffset(to))
            } else {
                throw IllegalStateException("register types are mixed. this should never happen!")
            }
        }
}

fun Pair<Int, Int>.unwrap(): IntArray = intArrayOf(first, second)

data class RegisterLoadInstruction(val from: MoveSource, val to: Register) : Instruction, CompilingInstruction {
    override val size: Int = when (from) {
        is LiteralAddressing -> 3
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is SymbolicAddressing -> 3
        is LiteralSource -> if (to is Register16) 3 else 2
    }

    override fun compile(symbolMap: SymbolMap): IntArray {
        when (to) {
            is Register8 -> {
                val offset = when (to) {
                    Register8.A -> 0
                    Register8.B -> 1
                    Register8.C -> 2
                    Register8.D -> 3
                }

                return when (from) {
                    is StaticAddressing -> intArrayOf(0x70 + offset, *from.encode(symbolMap).unwrap())
                    is RegisterCDAddressing -> intArrayOf(0x74 + offset, from.offset and 0xff)
                    is StackAddressing -> intArrayOf(0x78 + offset, from.offset and 0xff)
                    is LiteralSource -> intArrayOf(0x7c + offset, from.value and 0xff)
                }
            }

            is Register16 -> {
                val offset = if (to == Register16.AB) 0 else 1

                return when (from) {
                    is StaticAddressing -> intArrayOf(0x94 + offset, *from.encode(symbolMap).unwrap())
                    is RegisterCDAddressing -> intArrayOf(0x96 + offset, from.offset and 0xff)
                    is StackAddressing -> intArrayOf(0x98 + offset, from.offset and 0xff)
                    is LiteralSource -> intArrayOf(0x9a + offset, (from.value ushr 8) and 0xff, from.value and 0xff)
                }
            }
        }
    }
}

data class RegisterStoreInstruction(val from: Register, val to: AddressingExpression) : Instruction,
    CompilingInstruction {
    override val size: Int = when (to) {
        is LiteralAddressing -> 3
        is RegisterCDAddressing -> 2
        is StackAddressing -> 2
        is SymbolicAddressing -> 3
    }

    override fun compile(symbolMap: SymbolMap): IntArray {
        when (from) {
            is Register8 -> {
                val offset = when (from) {
                    Register8.A -> 0
                    Register8.B -> 1
                    Register8.C -> 2
                    Register8.D -> 3
                }

                return when (to) {
                    is StaticAddressing -> intArrayOf(0x80 + offset, *to.encode(symbolMap).unwrap())
                    is RegisterCDAddressing -> intArrayOf(0x84 + offset, to.offset and 0xff)
                    is StackAddressing -> intArrayOf(0x88 + offset, to.offset and 0xff)
                }
            }

            is Register16 -> {
                val offset = if (from == Register16.AB) 0 else 1

                return when (to) {
                    is StaticAddressing -> intArrayOf(0x8c + offset, *to.encode(symbolMap).unwrap())
                    is RegisterCDAddressing -> intArrayOf(0x9c + offset, to.offset and 0xff)
                    is StackAddressing -> intArrayOf(0x9e + offset, to.offset and 0xff)
                }
            }
        }
    }
}

enum class BranchOperation(val opcode: Int) {
    BCC(0xb0),
    BCS(0xb1),
    BZ(0xb2),
    BNZ(0xb3),
    BLZ(0xb4),
    BGZ(0xb5),
    BNP(0xb6),
    BNN(0xb7),
    JMP(0xb8),
    JSR(0xb8),
}

data class BranchInstruction(val operation: BranchOperation, val target: String) : Instruction, CompilingInstruction {
    override val size = 3

    override fun compile(symbolMap: SymbolMap): IntArray {
        return intArrayOf(operation.opcode, *resolveSymbol(target, symbolMap).unwrap())
    }
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

package ch.awae.custom8bit.microcode

enum class DataSource(val port: Int) {
    READ_REG_A(1),
    READ_REG_B(2),
    READ_REG_C(3),
    READ_REG_D(4),
    READ_MEMORY(5),
    READ_ALU(6),
    READ_LITERAL_1(8),
    READ_LITERAL_2(9),
    READ_PC_HIGH(10),
    READ_PC_LOW(11),
}

enum class DataTarget(val port: Int) {
    WRITE_REG_A(1),
    WRITE_REG_B(2),
    WRITE_REG_C(3),
    WRITE_REG_D(4),
    WRITE_MEMORY(5),
    WRITE_PC_LOW(6),
    WRITE_PC_HIGH(7),
}

enum class AddressSource(val port: Int) {
    ADR_LITERAL(0),
    ADR_REG_CD(1),
    ADR_INCREMENTER(2),
    ADR_INCREMENTER_DECREMENT(3),
    ADR_STACK_POINTER(4),
}

sealed interface Action {
    val command: Int
}

enum class AluOperation(override val command: Int) : Action {
    AND(1),
    IOR(2),
    XOR(3),
    INVERT(4),
    NAND(5),
    INOR(6),
    XNOR(7),
    DECREMENT(8),
    INCREMENT(9),
    ADDITION(10),
    SUBTRACTION(11),
    NIBBLE_SWAP(12),
    COMPLEMENT(13),
    SHIFT_LEFT(14),
    SHIFT_RIGHT(15),
    LITERAL_AND(17),
    LITERAL_IOR(18),
    LITERAL_XOR(19),
    LITERAL_NAND(21),
    LITERAL_INOR(22),
    LITERAL_XNOR(23),
    LITERAL_ADDITION(26),
    LITERAL_SUBTRACTION(27),
}

enum class AddressTarget(override val command: Int) : Action {
    WRITE_PC(28),
    WRITE_REG_CD(29),
    WRITE_STACK_POINTER(31),
}

sealed interface MicroOperation {
    fun condition(condition: ConditionExpression): Conditional = Conditional(condition, this, null)
    fun operationFor(state: FlagState): MicroOp?
}

data class FlagState(
    val carry: Boolean,
    val zero: Boolean,
    val negative: Boolean,
) {

    fun toSignal(): Int {
        var result = 0
        if (carry) {
            result += 0x0100
        }
        if (zero) {
            result += 0x0200
        }
        if (negative) {
            result += 0x0400
        }
        return result
    }

    companion object {

        val VALID_COMBINATIONS = listOf(
            FlagState(carry = false, zero = false, negative = false),
            FlagState(carry = true, zero = false, negative = false),
            FlagState(carry = false, zero = true, negative = false),
            FlagState(carry = true, zero = true, negative = false),
            FlagState(carry = false, zero = false, negative = true),
            FlagState(carry = true, zero = false, negative = true),
        )

    }
}

data class MicroOp(
    val dataSource: DataSource? = null,
    val dataTarget: DataTarget? = null,
    val addressSource: AddressSource? = null,
    val action: Action? = null,
) : MicroOperation {
    override fun operationFor(state: FlagState) = this
}

sealed interface ConditionExpression {
    fun satisfiedBy(state: FlagState): Boolean

    infix fun and(condition: ConditionExpression): ConditionExpression = ConjunctiveCondition(this, condition)
    infix fun or(condition: ConditionExpression): ConditionExpression = DisjunctiveCondition(this, condition)
}

enum class Condition : ConditionExpression {
    CARRY_SET,
    CARRY_CLEAR,
    ZERO,
    NOT_ZERO,
    POSITIVE,
    NOT_POSITIVE,
    NEGATIVE,
    NOT_NEGATIVE,
    ;

    override fun satisfiedBy(state: FlagState): Boolean {
        return when (this) {
            CARRY_SET -> state.carry
            CARRY_CLEAR -> !state.carry
            ZERO -> state.zero
            NOT_ZERO -> !state.zero
            POSITIVE -> !state.zero && !state.negative
            NOT_POSITIVE -> state.zero || state.negative
            NEGATIVE -> state.negative
            NOT_NEGATIVE -> !state.negative
        }
    }
}

data class ConjunctiveCondition(val x: ConditionExpression, val y: ConditionExpression) : ConditionExpression {
    override fun satisfiedBy(state: FlagState): Boolean {
        return x.satisfiedBy(state) && y.satisfiedBy(state)
    }
}

data class DisjunctiveCondition(val x: ConditionExpression, val y: ConditionExpression) : ConditionExpression {
    override fun satisfiedBy(state: FlagState): Boolean {
        return x.satisfiedBy(state) || y.satisfiedBy(state)
    }
}

data class Conditional(
    val condition: ConditionExpression,
    val satisfied: MicroOperation?,
    val unsatisfied: MicroOperation?
) : MicroOperation {
    fun otherwise(operation: MicroOperation) = copy(unsatisfied = operation)
    override fun operationFor(state: FlagState): MicroOp? {
        return if (condition.satisfiedBy(state)) {
            satisfied?.operationFor(state)
        } else {
            unsatisfied?.operationFor(state)
        }
    }
}

data class Operation(
    val code: Int,
    val mnemonic: String,
    val fetchSize: Int,
    val updateCarry: Boolean? = null,
    val step0: MicroOperation? = null,
    val step1: MicroOperation? = null,
    val step2: MicroOperation? = null,
    val step3: MicroOperation? = null,
)
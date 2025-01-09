package ch.awae.custom8bit.microcode

sealed interface ActionOrCondition

enum class Action : ActionOrCondition {
    READ_ALU, READ_MEMORY, READ_REGISTER, READ_LITERAL, READ_ALU_WITH_LITERAL,
    WRITE_REGISTER, WRITE_MEMORY,
    ADDRESS_FROM_LITERAL, ADDRESS_FROM_REGISTERS,
    BRANCH,
    CARRY_SET, CARRY_CLEAR,
}

sealed interface Condition : ActionOrCondition {
    infix fun or(other: Condition): Condition
}

enum class FlagCondition : Condition {
    IF_CARRY_SET, IF_CARRY_CLEAR,
    IF_ZERO_SET, IF_ZERO_CLEAR,
    IF_NEGATIVE_SET, IF_NEGATIVE_CLEAR,

    ;

    override fun or(other: Condition): Condition {
        return if (other is FlagCondition) {
            ConditionChoice(listOf(this, other))
        } else {
            other.or(this)
        }
    }
}

data class ConditionChoice(val conditions: List<FlagCondition>) : Condition {
    override fun or(other: Condition): Condition {
        return when (other) {
            is ConditionChoice -> ConditionChoice(conditions + other.conditions)
            is FlagCondition -> ConditionChoice(conditions + other)
        }
    }
}

/**
 * @param mask: marks parameter bits used for *unrolling* parameterized commands
 */
data class Op(
    val address: Int,
    val mask: Int = 0,
    val conditions: List<Condition> = emptyList(),
    val actions: List<Action> = emptyList(),
) {

    constructor(address: Int, mask: Int = 0, vararg actionOrCondition: ActionOrCondition) : this(
        address,
        mask,
        conditions = actionOrCondition.filterIsInstance<Condition>(),
        actions = actionOrCondition.filterIsInstance<Action>(),
    )

    constructor(address: Int, vararg actionOrCondition: ActionOrCondition) : this(
        address,
        mask = 0,
        conditions = actionOrCondition.filterIsInstance<Condition>(),
        actions = actionOrCondition.filterIsInstance<Action>(),
    )
}

package ch.awae.custom8bit.microcode

object OpcodeCompiler {

    val executeNoOp = CompiledOperation.Execute(null, null, false, false)

    fun compileOperation(op: Op): List<CompiledOperation> {
        return (0..255).flatMap { candidate ->
            // filter parameterized addresses
            if (candidate and (op.mask.inv()) != op.address) {
                return@flatMap emptyList()
            }

            val fetch = getFetchForOp(op)

            (0..7).mapNotNull { flags ->
                return@mapNotNull CompiledOperation(
                    opcode = candidate or (flags shl 8),
                    fetch = fetch,
                    execute = getExecuteForFlags(op, flags) ?: executeNoOp
                )
            }
        }
    }

    fun mapOperationToControlSignals(op: CompiledOperation): List<Pair<Int, Int>> {
        val fetchValue = when (op.fetch.fetchSize) {
            2 -> 0x03
            1 -> 0x01
            else -> 0x00
        } + when (op.fetch.carryUpdate) {
            true -> 0x0c
            false -> 0x08
            null -> 0x00
        } + op.fetch.aluOperation.shl(4)

        val executeValue = when (op.execute.dataWrite) {
            CompiledOperation.DataWrite.MEMORY -> 0x01
            CompiledOperation.DataWrite.REGISTER -> 0x02
            null -> 0x00
        } + when (op.execute.dataRead) {
            CompiledOperation.DataRead.MEMORY -> 0x10
            CompiledOperation.DataRead.REGISTER -> 0x14
            CompiledOperation.DataRead.LITERAL -> 0x08
            CompiledOperation.DataRead.ALU -> 0x1c
            null -> 0x00
        } + when (op.execute.addressFromRegisters) {
            true -> 0x20
            false -> 0x00
        } + when (op.execute.branch) {
            true -> 0x40
            false -> 0x00
        }

        return listOf(
            // fetch cycle
            op.opcode + 0x000 to fetchValue,
            op.opcode + 0x800 to executeValue,
        )
    }

    fun compile(op: Op): List<Pair<Int, Int>> {
        return compileOperation(op).flatMap(::mapOperationToControlSignals).sortedBy { it.first }
    }

    fun getExecuteForOp(op: Op) = CompiledOperation.Execute(
        dataRead = when {
            op.actions.contains(Action.READ_ALU) -> CompiledOperation.DataRead.ALU
            op.actions.contains(Action.READ_ALU_WITH_LITERAL) -> CompiledOperation.DataRead.ALU
            op.actions.contains(Action.READ_REGISTER) -> CompiledOperation.DataRead.REGISTER
            op.actions.contains(Action.READ_MEMORY) -> CompiledOperation.DataRead.MEMORY
            op.actions.contains(Action.READ_LITERAL) -> CompiledOperation.DataRead.LITERAL
            else -> null
        },
        dataWrite = when {
            op.actions.contains(Action.WRITE_REGISTER) -> CompiledOperation.DataWrite.REGISTER
            op.actions.contains(Action.WRITE_MEMORY) -> CompiledOperation.DataWrite.MEMORY
            else -> null
        },
        addressFromRegisters = op.actions.contains(Action.ADDRESS_FROM_REGISTERS),
        branch = op.actions.contains(Action.BRANCH),
    )

    fun getFetchForOp(op: Op) = CompiledOperation.Fetch(
        fetchSize = when {
            op.actions.contains(Action.READ_LITERAL) -> 1
            op.actions.contains(Action.READ_ALU_WITH_LITERAL) -> 1
            op.actions.contains(Action.ADDRESS_FROM_LITERAL) -> 2
            else -> 0
        },
        carryUpdate = when {
            op.actions.contains(Action.CARRY_SET) -> true
            op.actions.contains(Action.CARRY_CLEAR) -> false
            else -> null
        },
        aluOperation = op.alu?.operation ?: 0
    )

    fun getExecuteForFlags(op: Op, flags: Int): CompiledOperation.Execute? {
        // base case: no conditions -> basic operation
        if (op.conditions.isEmpty()) {
            return getExecuteForOp(op)
        }

        // check conditions
        for (condition in op.conditions) {
            if (condition is FlagCondition && !conditionMet(flags, condition)) {
                // simple condition not met -> abort
                return null
            } else if (condition is ConditionChoice) {
                // we have a choice -> check if any is met
                if (!condition.conditions.any { conditionMet(flags, it) }) {
                    // none of the choice matches -> abort
                    return null
                }
            }
        }

        // conditions are met -> execute normally
        return getExecuteForOp(op)
    }

    /**
     * @flags bitmap:
     *  flags[0] -> zero
     *  flags[1] -> negative
     *  flags[2] -> carry
     */
    fun conditionMet(flags: Int, condition: FlagCondition): Boolean {
        if (flags and 0x01 == 0 && condition == FlagCondition.IF_ZERO_SET)
        // zero flag cleared, must be set
            return false

        if (flags and 0x01 != 0 && condition == FlagCondition.IF_ZERO_CLEAR)
        // zero flag set, must be cleared
            return false

        if (flags and 0x02 == 0 && condition == FlagCondition.IF_NEGATIVE_SET)
        // negative flag cleared, must be set
            return false

        if (flags and 0x02 != 0 && condition == FlagCondition.IF_NEGATIVE_CLEAR)
        // negative flag set, must be cleared
            return false

        if (flags and 0x04 == 0 && condition == FlagCondition.IF_CARRY_SET)
        // carry flag cleared, must be set
            return false

        if (flags and 0x04 != 0 && condition == FlagCondition.IF_CARRY_CLEAR)
        // carry flag set, must be cleared
            return false

        return true
    }

}

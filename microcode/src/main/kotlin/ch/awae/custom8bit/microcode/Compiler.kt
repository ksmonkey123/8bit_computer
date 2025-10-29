package ch.awae.custom8bit.microcode

object Compiler {

    fun compileOperation(operation: Operation): Map<Int, Int> {
        val result = mutableMapOf<Int, Int>()

        for (state in FlagState.VALID_COMBINATIONS) {
            val steps = compileOperationSteps(operation, state)

            steps.forEachIndexed { index, step ->
                result[index.shl(8 + 3)
                        + state.toSignal().shl(8)
                        + operation.code
                ] = step
            }
        }

        return result.toMap()
    }

    private fun compileOperationSteps(operation: Operation, flagState: FlagState): List<Int> {
        val result = mutableListOf<Int>()

        // fetch Instruction
        result.add(compileStep(MicroOp(DataSource.READ_MEMORY, addressSource = AddressSource.ADR_PC), true, flagState))

        val lastIndex = operation.steps.size - 1
        operation.steps.forEachIndexed { index, step ->
            result.add(compileStep(step, index < lastIndex, flagState))
        }

        return result
    }

    private fun compileStep(step: MicroOperation?, hasNextStep: Boolean, state: FlagState): Int {
        val selectedOp = step?.operationFor(state)
            ?: return if (hasNextStep) {
                0x0080
            } else {
                0x0000
            }

        // current step has content
        return (selectedOp.dataSource.port) +
                (selectedOp.dataTarget.port shl 4) +
                (if (hasNextStep && !selectedOp.terminal) 0x0080 else 0) +
                (selectedOp.action.command shl 8) +
                (selectedOp.addressSource.port shl 13)
    }

    fun compileInstructionSet(operations: Set<Operation>): Pair<ByteArray, ByteArray> {
        val map = operations.map { compileOperation(it) }.reduce { a, b -> a + b }

        val lowMap = ByteArray(32768) {
            map[it]?.toByte() ?: 0
        }

        val highMap = ByteArray(32768) {
            // HALT on unknown code
            (map[it]?.ushr(8) ?: SequencerCommand.HALT.command).toByte()
        }

        return Pair(lowMap, highMap)
    }
}
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
        result.add(compileStep(MicroOp(DataSource.READ_MEMORY, null, AddressSource.ADR_PC), true, flagState))
        // optionally fetch L2
        if (operation.fetchSize == 2) {
            result.add(compileStep(MicroOp(DataSource.READ_MEMORY, DataTarget.WRITE_LITERAL_2, AddressSource.ADR_INCREMENTER_INCREMENT), true, flagState))
        }
        // optionally fetch L1
        if (operation.fetchSize >= 1) {
            result.add(compileStep(MicroOp(DataSource.READ_MEMORY, DataTarget.WRITE_LITERAL_1, AddressSource.ADR_INCREMENTER_INCREMENT), true, flagState))
        }
        // update PC
        result.add(compileStep(MicroOp(addressSource = AddressSource.ADR_INCREMENTER_INCREMENT, action = AddressTarget.WRITE_PC), true, flagState))

        result.add(compileStep(operation.step0, operation.step1 != null, flagState))
        result.add(compileStep(operation.step1, operation.step2 != null, flagState))
        result.add(compileStep(operation.step2, operation.step3 != null, flagState))
        result.add(compileStep(operation.step3, false, flagState))

        return result
    }

    private fun compileStep(step: MicroOperation?, hasNextStep: Boolean, state: FlagState): Int {
        val selectedOp = step?.operationFor(state)
            ?: return if (!hasNextStep) {
                0x0000
            } else {
                0x0080
            }

        // current step has content
        return (selectedOp.dataSource?.port ?: 0) +
                (((selectedOp.dataTarget ?: DataTarget.WRITE_ALU_INPUT).port) shl 4) +
                (if (hasNextStep) 0x0080 else 0) +
                ((selectedOp.action?.command ?: 0) shl 8) +
                ((selectedOp.addressSource?.port ?: 0) shl 13)
    }

    fun compileInstructionSet(operations: Set<Operation>): Pair<ByteArray, ByteArray> {
        val map = operations.map { compileOperation(it) }.reduce { a, b -> a + b }

        val lowMap = ByteArray(16384) {
            map[it]?.toByte() ?: 0
        }

        val highMap = ByteArray(16384) {
            map[it]?.ushr(8)?.toByte() ?: 0
        }

        return Pair(lowMap, highMap)
    }
}
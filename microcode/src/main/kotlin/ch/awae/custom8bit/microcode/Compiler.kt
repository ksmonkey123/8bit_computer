package ch.awae.custom8bit.microcode

object Compiler {

    fun compileOperation(operation: Operation): Map<Int, Int> {
        val result = mutableMapOf<Int, Int>()

        result[0x0700 + operation.code] = compileControl(operation)

        for (state in FlagState.VALID_COMBINATIONS) {
            result[0x0000 + state.toSignal() + operation.code] =
                compileStep(operation.step0, operation.step1 != null, state)
            result[0x0800 + state.toSignal() + operation.code] =
                compileStep(operation.step1, operation.step2 != null, state)
            result[0x1000 + state.toSignal() + operation.code] =
                compileStep(operation.step2, operation.step3 != null, state)
            result[0x1800 + state.toSignal() + operation.code] =
                compileStep(operation.step3, false, state)
        }

        return result.toMap()
    }

    private fun compileStep(step: MicroOperation?, hasNextStep: Boolean, state: FlagState): Int {
        val selectedOp = step?.operationFor(state)
            ?: return if (hasNextStep) {
                0x0000
            } else {
                0x0080
            }

        // current step has content
        return (selectedOp.dataSource?.port ?: 0) +
                ((selectedOp.dataTarget?.port ?: 0) shl 4) +
                (if (hasNextStep) 0 else 0x0080) +
                ((selectedOp.action?.command ?: 0) shl 8) +
                ((selectedOp.addressSource?.port ?: 0) shl 13)
    }

    private fun compileControl(operation: Operation): Int {
        return when (operation.fetchSize) {
            0 -> 3
            1 -> 2
            2 -> 1
            else -> throw IllegalArgumentException("invalid fetch size: ${operation.fetchSize}")
        } + when (operation.updateCarry) {
            true -> 0b1100
            false -> 0b1000
            null -> 0b0000
        }
    }

    fun compileInstructionSet(operations: Set<Operation>): Pair<ByteArray, ByteArray> {
        val map = operations.map { compileOperation(it) }.reduce { a, b -> a + b }

        val lowMap = ByteArray(8192) {
            (map[it]?.and(0x0000_00ff) ?: if (it.and(0x0000_ff00) == 0x0000_0700) 0x03 else 0x80).toByte()
        }

        val highMap = ByteArray(8192) {
            map[it]?.and(0x0000_ff00)?.ushr(8)?.toByte() ?: 0
        }

        return Pair(lowMap, highMap)
    }
}
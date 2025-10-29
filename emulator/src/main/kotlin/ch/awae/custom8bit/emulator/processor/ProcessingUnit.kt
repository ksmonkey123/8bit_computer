package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*

import ch.awae.custom8bit.microcode.*

class ProcessingUnit(
    private val microcode: Microcode,
    private val memoryBus: MemoryBus,
) {

    constructor(memoryBus: MemoryBus) : this(Microcode.INSTANCE, memoryBus)

    data class Statistics(val instructionCount: Int, val stepCount: Int)

    var statistics = Statistics(0, 0)
        private set

    fun singleStep(state: ProcessorState): ProcessorState {
        if (state.halted) {
            throw IllegalStateException("halted state")
        }
        return executeStep(state)
    }

    fun executeNextCommand(inputState: ProcessorState): ProcessorState {
        if (inputState.halted) {
            throw IllegalStateException("halted state")
        }
        var state = inputState
        do {
            state = singleStep(state)
            statistics = statistics.copy(stepCount = statistics.stepCount + 1)
        } while (!state.halted && state.stepCounter != 0)
        statistics = statistics.copy(instructionCount = statistics.instructionCount + 1)
        return state
    }

    private fun executeStep(state: ProcessorState): ProcessorState {
        if (state.stepCounter !in 0..15) {
            throw IllegalStateException("invalid stepCounter: ${state.stepCounter}")
        }

        val execute = microcode.execute(state.instructionRegister, state.stepCounter and 0x07, state.flags)
        val address = state.getAddress(execute.addressSource)

        val aluState = (execute.action as? AluOperation)?.let { ALU.calculate(state, it) }

        val data = when (execute.dataSource) {
            null -> 0xff
            DataSource.READ_REG_A -> state.registerA
            DataSource.READ_REG_B -> state.registerB
            DataSource.READ_REG_C -> state.registerC
            DataSource.READ_REG_D -> state.registerD
            DataSource.READ_MEMORY -> memoryBus.read(address)
            DataSource.READ_ALU -> aluState?.data ?: throw IllegalStateException("ALU read without ALU operation")
            DataSource.READ_LITERAL_1 -> state.literal1
            DataSource.READ_LITERAL_2 -> state.literal2
            DataSource.READ_PC_HIGH -> (state.programCounter ushr 8) and 0x00ff
            DataSource.READ_PC_LOW -> state.programCounter and 0x00ff
            DataSource.READ_STACK_POINTER_LOW -> (state.stackPointer ushr 8) and 0x00ff
            DataSource.READ_STACK_POINTER_HIGH -> state.stackPointer and 0x00ff
        }

        val stateAfterDataTarget = when (execute.dataTarget) {
            DataTarget.WRITE_ALU_INPUT -> state.copy(aluInput = data)
            DataTarget.WRITE_REG_A -> state.copy(registerA = data)
            DataTarget.WRITE_REG_B -> state.copy(registerB = data)
            DataTarget.WRITE_REG_C -> state.copy(registerC = data)
            DataTarget.WRITE_REG_D -> state.copy(registerD = data)
            DataTarget.WRITE_MEMORY -> state.also { memoryBus.write(address, data) }
            DataTarget.WRITE_LITERAL_1 -> state.copy(literal1 = data)
            DataTarget.WRITE_LITERAL_2 -> state.copy(literal2 = data)
        }.let {
            if (execute.dataSource == DataSource.READ_ALU) {
                it.copy(flags = it.flags.copy(carry = aluState!!.carry))
            } else {
                it
            }
        }

        val stateAfterAddressSet = stateAfterDataTarget.let { st ->
            when (execute.action) {
                is AluOperation -> st
                AddressTarget.WRITE_PC -> st.copy(programCounter = address)
                AddressTarget.WRITE_STACK_POINTER -> st.copy(stackPointer = address)
                SequencerCommand.HALT -> st.copy(halted = true)
            }
        }

        val stateAfterFlagUpdate = stateAfterAddressSet.let { st ->
            if (execute.dataTarget in listOf(
                    DataTarget.WRITE_REG_A,
                    DataTarget.WRITE_REG_B,
                    DataTarget.WRITE_REG_C,
                    DataTarget.WRITE_REG_D
                )
                || ((execute.dataSource == DataSource.READ_ALU) &&
                        (execute.dataTarget != DataTarget.WRITE_LITERAL_2))
            ) {
                st.copy(flags = st.flags.copy(zero = data == 0, negative = data > 0x80))
            } else {
                st
            }
        }

        return stateAfterFlagUpdate.copy(
            incrementRegister = address,
            stepCounter = if (execute.hasNextStep) state.stepCounter + 1 else 0,
            instructionRegister = if (state.stepCounter == 0) data else state.instructionRegister,
        )
    }

}
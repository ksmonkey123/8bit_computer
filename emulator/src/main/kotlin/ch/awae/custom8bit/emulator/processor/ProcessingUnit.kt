package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.memory.*

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
        return if (state.stepCounter < 8) {
            fetchStep(state)
        } else {
            executeStep(state)
        }
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

    private fun fetchStep(state: ProcessorState): ProcessorState {
        when (state.stepCounter) {
            0 -> {
                val instruction = memoryBus.read(state.programCounter)
                val control = microcode.control(instruction)
                // initial instruction fetch
                return state.copy(
                    instructionRegister = instruction,
                    incrementRegister = state.programCounter,
                    stepCounter = control.skipFetch * 2 + 1,
                    halted = control.halt,
                )
            }

            4 -> {
                return state.copy(
                    literal2 = memoryBus.read(state.programCounter),
                    incrementRegister = state.programCounter,
                    stepCounter = state.stepCounter + 1,
                )
            }

            6 -> {
                return state.copy(
                    literal1 = memoryBus.read(state.programCounter),
                    incrementRegister = state.programCounter,
                    stepCounter = state.stepCounter + 1,
                )
            }

            3, 5, 7 -> {
                return state.copy(
                    programCounter = (state.incrementRegister + 1) and 0xffff,
                    incrementRegister = (state.incrementRegister + 1) and 0xffff,
                    stepCounter = state.stepCounter + 1,
                )
            }

            else -> throw IllegalStateException("invalid step: ${state.stepCounter}")
        }
    }

    private fun executeStep(state: ProcessorState): ProcessorState {
        if (state.stepCounter !in 8..11) {
            throw IllegalStateException("invalid stepCounter: ${state.stepCounter}")
        }

        val execute = microcode.execute(state.instructionRegister, state.stepCounter and 0x03, state.flags)
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
            null -> state
        }.let {
            if (execute.dataSource == DataSource.READ_ALU) {
                it.copy(flags = it.flags.copy(carry = aluState!!.carry))
            } else {
                it
            }
        }

        val stateAfterAddressSet = stateAfterDataTarget.let { st ->
            when (execute.action as? AddressTarget) {
                AddressTarget.WRITE_PC -> st.copy(programCounter = address)
                AddressTarget.WRITE_STACK_POINTER -> st.copy(stackPointer = address)
                null -> st
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
            stepCounter = if (execute.finalStep) 0 else state.stepCounter + 1,
        )
    }

}
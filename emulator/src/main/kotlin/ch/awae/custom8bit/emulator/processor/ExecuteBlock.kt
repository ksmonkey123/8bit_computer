package ch.awae.custom8bit.emulator.processor

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
    ;

    companion object {
        fun forPort(port: Int): DataSource? {
            if (port == 0) {
                return null
            }
            return entries.find { it.port == port }
                ?: throw java.lang.IllegalArgumentException("unsupported dataSource port: $port")
        }
    }
}

enum class DataTarget(val port: Int) {
    WRITE_ALU_INPUT(0),
    WRITE_REG_A(1),
    WRITE_REG_B(2),
    WRITE_REG_C(3),
    WRITE_REG_D(4),
    WRITE_MEMORY(5),
    WRITE_PC_LOW(6),
    WRITE_PC_HIGH(7),
    ;

    companion object {
        fun forPort(port: Int): DataTarget {
            return DataTarget.entries.find { it.port == port }
                ?: throw IllegalArgumentException("unsupported dataTarget port $port")
        }
    }
}

sealed interface Action {
    val command: Int

    companion object {
        fun forCommand(command: Int): Action {
            return AluOperation.entries.find { it.command == command }
                ?: AddressTarget.entries.find { it.command == command }
                ?: throw IllegalArgumentException("unsupported action: $command")
        }
    }
}

enum class AddressSource(val port: Int) {
    ADR_LITERAL(0),
    ADR_REG_CD(1),
    ADR_INCREMENTER(2),
    ADR_INCREMENTER_DECREMENT(3),
    ADR_STACK_POINTER(4),
    ;

    companion object {
        fun forPort(port: Int): AddressSource {
            return AddressSource.entries.find { it.port == port }
                ?: throw IllegalArgumentException("unsupported addressSource port $port")
        }
    }
}

enum class AluOperation(override val command: Int) : Action {
    AND(0),
    IOR(1),
    XOR(2),
    INVERT(3),
    DECREMENT(4),
    INCREMENT(5),
    ADDITION(6),
    SUBTRACTION(7),
    COMPLEMENT(8),
    SHIFT_LEFT(9),
    SHIFT_RIGHT(10),
}

enum class AddressTarget(override val command: Int) : Action {
    WRITE_PC(16),
    WRITE_REG_CD(17),
    WRITE_STACK_POINTER(18),
}

data class ExecuteBlock(
    val finalStep: Boolean,
    val dataSource: DataSource?,
    val dataTarget: DataTarget?,
    val addressSource: AddressSource,
    val action: Action?,
) {
    constructor(raw: Int) : this(
        (raw and 0x0080) != 0,
        DataSource.forPort(raw and 0x000f),
        DataTarget.forPort((raw and 0x0070) shr 4),
        AddressSource.forPort((raw and 0xe000) shr 13),
        Action.forCommand((raw and 0x1f00) shr 8),
    )
}
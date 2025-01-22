package ch.awae.custom8bit.emulator.processor

data class ControlBlock(
    val skipFetch: Int,
    val carryUpdate: Boolean?,
    val halt: Boolean,
) {
    constructor(raw: Int) : this(
        (raw and 0x03).also {
            if (it == 0) throw java.lang.IllegalArgumentException("bad control byte: $raw (bad fetch size)")
        },
        when (raw and 0x0c) {
            0 -> null
            8 -> false
            12 -> true
            else -> throw IllegalArgumentException("bad control byte: $raw (bad carry block)")
        },
        (raw and 0x80) != 0
    )
}

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
    WRITE_REG_A(1),
    WRITE_REG_B(2),
    WRITE_REG_C(3),
    WRITE_REG_D(4),
    WRITE_MEMORY(5),
    WRITE_PC_LOW(6),
    WRITE_PC_HIGH(7),
    ;

    companion object {
        fun forPort(port: Int): DataTarget? {
            if (port == 0) {
                return null
            }
            return DataTarget.entries.find { it.port == port }
                ?: throw IllegalArgumentException("unsupported dataTarget port $port")
        }
    }
}

sealed interface Action {
    val command: Int

    companion object {
        fun forCommand(command: Int): Action? {
            if (command == 0) {
                return null
            }
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

class Microcode(
    val addressingRom: ByteArray,
    val actionRom: ByteArray
) {

    constructor(raw: Pair<ByteArray, ByteArray>) : this(raw.first, raw.second)

    fun control(instruction: Int): ControlBlock {
        return ControlBlock(addressingRom[0x0700 + instruction].toInt() and 0xff)
    }

    fun execute(instruction: Int, step: Int, flags: Flags): ExecuteBlock {
        val address = (((step shl 3) + flags.encoded) shl 8) + instruction
        return ExecuteBlock((addressingRom[address].toInt() and 0xff) + ((actionRom[address].toInt() and 0xff) shl 8))
    }

}
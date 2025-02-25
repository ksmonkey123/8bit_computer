package ch.awae.custom8bit.emulator.processor

data class ProcessorState(
    val halted: Boolean = false,
    val stepCounter: Int = 0,
    val instructionRegister: Int = 0,
    val programCounter: Int = 0,
    val stackPointer: Int = 0,
    val incrementRegister: Int = 0,
    val aluInput: Int = 0,
    val flags: Flags = Flags(),
    val literal1: Int = 0,
    val literal2: Int = 0,
    val registerA: Int = 0,
    val registerB: Int = 0,
    val registerC: Int = 0,
    val registerD: Int = 0,
) {
    fun getAddress(source: AddressSource): Int = when (source) {
        AddressSource.ADR_LITERAL -> ((literal2 shl 8) and 0xff00) + (literal1 and 0x00ff)
        AddressSource.ADR_REG_CD -> ((registerD shl 8) and 0xff00) + (registerC and 0x00ff)
        AddressSource.ADR_INCREMENTER_INCREMENT -> (incrementRegister + 1) and 0xffff
        AddressSource.ADR_INCREMENTER_DECREMENT -> (incrementRegister - 1) and 0xffff
        AddressSource.ADR_STACK_POINTER -> stackPointer and 0xffff
        AddressSource.ADR_INCREMENTER_OFFSET_POSITIVE -> (incrementRegister + (literal1 and 0x00ff)) and 0xffff
        AddressSource.ADR_INCREMENTER_OFFSET_NEGATIVE -> (incrementRegister - (literal1 and 0x00ff)) and 0xffff
    }
}

data class Flags(
    val carry: Boolean = false,
    val zero: Boolean = false,
    val negative: Boolean = false,
) {
    val encoded: Int
        get() {
            var result = 0
            if (carry) {
                result += 0x01
            }
            if (zero) {
                result += 0x02
            }
            if (negative) {
                result += 0x04
            }
            return result
        }
}
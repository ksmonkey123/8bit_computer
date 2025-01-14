package ch.awae.custom8bit.microcode

data class CompiledOperation(
    val opcode: Int,
    val fetch: Fetch,
    val execute: Execute,
) {

    data class Fetch(
        val fetchSize: Int,
        val carryUpdate: Boolean?,
        val aluOperation: Int,
    )

    enum class DataRead {
        REGISTER, ALU, MEMORY, LITERAL
    }

    enum class DataWrite {
        REGISTER, MEMORY
    }

    data class Execute(
        val dataRead: DataRead?,
        val dataWrite: DataWrite?,
        val addressFromRegisters: Boolean,
        val branch: Boolean
    )

}
package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.microcode.*

data class ExecuteBlock(
    val hasNextStep: Boolean,
    val dataSource: DataSource,
    val dataTarget: DataTarget,
    val addressSource: AddressSource,
    val action: Action,
) {
    constructor(raw: Int) : this(
        (raw and 0x0080) != 0,
        getDataSource(raw and 0x000f),
        getDataTarget((raw and 0x0070) shr 4),
        getAddressSource((raw and 0xe000) shr 13),
        getAction((raw and 0x1f00) shr 8),
    )
}

fun getDataTarget(port: Int) : DataTarget {
    return DataTarget.entries.find { it.port == port } ?: throw IllegalArgumentException("Invalid data target $port")
}

fun getDataSource(port: Int) : DataSource {
    return DataSource.entries.find { it.port == port } ?: throw IllegalArgumentException("Invalid data source $port")
}

fun getAddressSource(port: Int) : AddressSource {
    return AddressSource.entries.find { it.port == port } ?: throw IllegalArgumentException("Invalid address source $port")
}

fun getAction(command: Int) : Action {
    return AluOperation.entries.find { it.command == command }
        ?: AddressTarget.entries.find { it.command == command }
        ?: SequencerCommand.entries.find { it.command == command }
        ?: throw IllegalArgumentException("Unknown command: $command")
}

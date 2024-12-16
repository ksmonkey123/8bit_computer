package ch.awae.custom8bitemulator.hardware

/**
 * A DataBus that combines 2..4 byte busses into a single 32bit bus.
 */
class CombinedByteBus private constructor(private val busses: List<DataBus?>) : DataBus {

    constructor(byte0: DataBus, byte1: DataBus)
            : this(listOf(byte0, byte1))

    constructor(byte0: DataBus, byte1: DataBus, byte2: DataBus)
            : this(listOf(byte0, byte1, byte2))

    constructor(byte0: DataBus, byte1: DataBus, byte2: DataBus, byte3: DataBus)
            : this(listOf(byte0, byte1, byte2, byte3))

    private fun getComposedValue(extractor: (DataBus) -> UInt): UInt {
        return busses.mapIndexed { index, dataBus ->
            if (dataBus == null)
                0u
            else
                (extractor(dataBus) and 0xffu) shl (index * 8)
        }.reduceOrNull(UInt::or) ?: 0u
    }

    override val state: UInt
        get() = getComposedValue(DataBus::state)

    override val contention: UInt
        get() = getComposedValue(DataBus::contention)

}

package ch.awae.custom8bit.assembler.bytecode

class SymbolMapBuilder {

    private val internalMap: MutableMap<String, Int> = mutableMapOf()

    val symbolMap: Map<String, Int>
        get() = internalMap

    fun add(key: String, value: Int) {
        if (internalMap.containsKey(key)) {
            throw IllegalArgumentException("$key is already known at position ${internalMap[key]}")
        }
        internalMap[key] = value
    }

    fun addAll(map: Map<String, Int>) {
        map.forEach { (key, value) -> add(key, value) }
    }

}
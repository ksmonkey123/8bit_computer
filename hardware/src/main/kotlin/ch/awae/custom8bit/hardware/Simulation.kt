package ch.awae.custom8bit.hardware

enum class ElementType {
    WIRE,
    COMPONENT,
}

abstract class SimulationElement(val type: ElementType, val name: String? = null) {

    open fun tick(tickID: Long = -1) {}
    open fun getSubElements(): List<SimulationElement> = emptyList()

    override fun toString(): String {
        return name ?: super.toString()
    }
}

class Simulation(vararg elements: SimulationElement) {

    private val elements: Map<ElementType, MutableSet<SimulationElement>> =
        ElementType.entries.associateWith { mutableSetOf() }

    init {
        for (element in elements) {
            addElement(element)
        }
    }

    fun addElement(element: SimulationElement) {
        element.getSubElements().forEach {
            addElement(it)
        }
        elements.getValue(element.type).add(element)
    }

    private var tickCounter = 0L

    fun tick(tickCount: Int = 1) {
        if (tickCount <= 0) {
            throw IllegalArgumentException("tickCount must be > 0")
        }

        repeat(tickCount) {
            tickOnce()
        }
    }

    /**
     * Tick until the escape condition is met.
     *
     * The escape condition is evaluated at the end of every tick.
     * If the limit is exceeded, the method returns.
     *
     * @param escapeCondition the escape condition
     * @param limit the maximum number of ticks to perform
     * @return the number of performed ticks. Returns `-1` if limit
     * has been reached without satisfying an escape condition.
     */
    fun tickUntil(escapeCondition: () -> Boolean, limit: Int): Int {
        repeat(limit) { idx ->
            tickOnce()

            if (escapeCondition()) {
                return idx + 1
            }
        }
        return -1
    }

    private fun tickOnce() {
        for (entry in ElementType.entries) {
            for (element in elements.getValue(entry)) {
                element.tick(tickCounter)
            }
        }
        tickCounter += 1
    }


}

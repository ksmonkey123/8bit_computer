package ch.awae.custom8bitemulator

enum class ElementType {
    COMPONENT,
    WIRE,
}

abstract class SimulationElement(val type: ElementType) {
    abstract fun tick(tickID: Long)
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
        elements.getValue(element.type).add(element)
    }

    private var tickCounter = 0L

    fun tick(tickCount: Long = 1) {
        if (tickCount <= 0) {
            throw IllegalArgumentException("tickCount must be > 0")
        }

        for (i in 1..tickCount) {
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
    fun tickUntil(escapeCondition: () -> Boolean, limit: Long): Long {
        for (tickNumber in 1..limit) {
            tickOnce()

            if (escapeCondition()) {
                return tickNumber
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
package ch.awae.custom8bit.hardware

import org.junit.jupiter.api.*
import kotlin.test.*
import kotlin.test.Test

class SimulationTest {

    @Test
    fun addingComponentDoesNotTickItImmediately() {
        val component = MockElement()
        Simulation(component)

        assertNull(component.lastTickID)
        assertEquals(0, component.tickCount)
    }

    @Test
    fun testSimulationTicks() {
        val component = MockElement()
        val sim = Simulation(component)

        sim.tick()
        assertEquals(0, component.lastTickID)
        assertEquals(1, component.tickCount)
        sim.tick(100)
        assertEquals(100, component.lastTickID)
        assertEquals(101, component.tickCount)
    }

    @Test
    fun testInvalidTickCount() {
        val component = MockElement()
        val sim = Simulation(component)

        assertThrows<IllegalArgumentException> { sim.tick(-1) }

        // component should not have ticked
        assertNull(component.lastTickID)
        assertEquals(0, component.tickCount)
    }

    @Test
    fun testEscapeCondition() {
        var invocationCount = 0
        fun condition(): Boolean {
            invocationCount++
            return invocationCount == 20
        }


        val component = MockElement()
        val sim = Simulation(component)

        assertEquals(20, sim.tickUntil(::condition, 1000))
        assertEquals(20, component.tickCount)
        assertEquals(19, component.lastTickID)
    }

    @Test
    fun testEscapeConditionNotReached() {
        val component = MockElement()
        val sim = Simulation(component)

        assertEquals(-1, sim.tickUntil({ false }, 1000))
        assertEquals(999, component.lastTickID)
        assertEquals(1000, component.tickCount)
    }

    @Test
    fun testTickUntilReturnsRelativeCount() {
        var invocationCount = 0
        fun condition(): Boolean {
            invocationCount++
            return invocationCount == 20
        }


        val component = MockElement()
        val sim = Simulation(component)

        // tick a few times before running the test to offset absolute tick counts
        sim.tick(10)

        assertEquals(20, sim.tickUntil(::condition, 1000))
        assertEquals(30, component.tickCount)
        assertEquals(29, component.lastTickID)
    }

    @Test
    fun testComponentOnlyRegisteredOnce() {
        val component = MockElement()
        val sim = Simulation(component, component)
        sim.addElement(component)

        sim.tick()
        assertEquals(0, component.lastTickID)
        assertEquals(1, component.tickCount)
    }

    @Test
    fun testEmptySimulationRunsOK() {
        Simulation().tick(10000)
    }

    @Test
    fun testAddingComponentsDuringRunningSimulation() {
        val sim = Simulation()
        val componentA = MockElement()
        sim.addElement(componentA)

        sim.tick(1000)
        assertEquals(1000, componentA.tickCount)
        assertEquals(999, componentA.lastTickID)

        val componentB = MockElement()
        sim.addElement(componentB)

        assertNull(componentB.lastTickID)
        assertEquals(0, componentB.tickCount)

        sim.tick(1000)

        assertEquals(2000, componentA.tickCount)
        assertEquals(1999, componentA.lastTickID)
        assertEquals(1000, componentB.tickCount)
        // tick ids are global, so new component gets same as old component
        assertEquals(1999, componentB.lastTickID)
    }

    @Test
    fun testAddingNestedElement() {
        val internal = MockElement()
        val container = MockNestedElement(internal)

        val sim = Simulation(container)

        assertNull(internal.lastTickID)
        assertNull(container.lastTickID)
        assertEquals(0, internal.tickCount)
        assertEquals(0, container.tickCount)

        sim.tick()

        assertEquals(0, internal.lastTickID)
        assertEquals(0, container.lastTickID)
        assertEquals(1, internal.tickCount)
        assertEquals(1, container.tickCount)
    }

    open class MockElement : SimulationElement(ElementType.COMPONENT) {

        var lastTickID: Long? = null
        var tickCount: Long = 0

        override fun tick(tickID: Long) {
            lastTickID = tickID
            tickCount++
        }
    }

    class MockNestedElement(val nested: SimulationElement) : MockElement() {
        override fun getSubElements(): List<SimulationElement> {
            return listOf(nested)
        }
    }

}

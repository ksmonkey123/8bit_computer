package ch.awae.custom8bitemulator

import kotlin.test.*

class SimulationTest {

    @Test
    fun addingComponentDoesNotTickItImmediately() {
        val component = MockElement()
        val sim = Simulation(component)

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

        sim.tick()
        assertEquals(0, component.lastTickID)
        assertEquals(1, component.tickCount)
    }

    class MockElement : SimulationElement(ElementType.COMPONENT) {

        var lastTickID: Long? = null
        var tickCount: Long = 0

        override fun tick(tickID: Long) {
            lastTickID = tickID
            tickCount++
        }
    }

}

package ch.awae.custom8bit.emulator.hardware.modules.registers

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.ic.*
import ch.awae.custom8bit.emulator.hardware.wiring.*

class StackPointer(
    addressBus: WritableBus,
    push: DataSignal,
    pop: DataSignal,
    clock: DataSignal,
    reset: DataSignal,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    private val bufferBusHigh = StandardWritableBus(false, toString() + "-bufferBusHigh")
    private val bufferBusLow = StandardWritableBus(false, toString() + "-bufferBusLow")

    private val adderBusHigh = StandardWritableBus(false, toString() + "-adderBusHigh")
    private val adderBusLow = StandardWritableBus(false, toString() + "-adderBusLow")

    private val bufferHigh = OctalDFlipFlop(adderBusHigh, clock, reset, bufferBusHigh, toString() + "-bufferHigh")
    private val bufferLow = OctalDFlipFlop(adderBusLow, clock, reset, bufferBusLow, toString() + "-bufferLow")

    private val subtractBus = push.bus(0xffu, 0x00u)

    private val carryLowHigh = StandardWritableSignal(false, toString() + "-carryLowHigh")
    private val adderHigh =
        OctalFullAdder(bufferBusHigh, subtractBus, carryLowHigh, adderBusHigh, null, toString() + "-adderHigh")
    private val adderLow =
        OctalFullAdder(bufferBusLow, subtractBus, pop, adderBusLow, carryLowHigh, toString() + "-adderLow")

    private val pushDriver = DualOctalTristateDrivers(
        CombinedByteBus(adderBusLow, adderBusHigh),
        push,
        addressBus,
        toString() + "-pushDriver"
    )
    private val popDriver = DualOctalTristateDrivers(
        CombinedByteBus(bufferBusLow, bufferBusHigh),
        pop,
        addressBus,
        toString() + "-popDriver"
    )

    override fun getSubElements(): List<SimulationElement> {
        return listOf(
            adderBusLow,
            adderBusHigh,
            bufferBusLow,
            bufferBusHigh,
            bufferHigh,
            bufferLow,
            carryLowHigh,
            adderHigh,
            adderLow,
            pushDriver,
            popDriver
        )
    }
}
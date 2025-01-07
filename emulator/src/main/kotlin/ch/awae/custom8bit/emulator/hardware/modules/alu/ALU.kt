package ch.awae.custom8bit.emulator.hardware.modules.alu

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.emulator.hardware.ic.*
import ch.awae.custom8bit.emulator.hardware.wiring.*

class ALU(
    inputA: DataBus,
    inputB: DataBus,
    inputL: DataBus,
    carryIn: DataSignal,
    enable: DataSignal,
    command: DataBus,
    output: WritableBus,
    carryOut: WritableSignal,
    name: String? = null,
) : SimulationElement(ElementType.COMPONENT, name) {

    // control logic
    private val controlBus = StandardWritableBus(false, toString() + "-control")
    private val controlCircuit = AluControlCircuit(command, enable, controlBus, toString() + "-controlCircuit")

    // a inverter
    private val primaryBus = StandardWritableBus(false, toString() + "-primary")
    private val aInverter = BusXor(inputA, controlBus.bit(2), primaryBus, toString() + "-aInverter")

    // b/l selector
    private val secondaryBus = StandardWritableBus(false, toString() + "-secondary")
    private val bSelector =
        OctalTristateDriver(inputB, controlBus.bit(1).inverted(), secondaryBus, toString() + "-bSelector")
    private val lSelector =
        OctalTristateDriver(inputL, controlBus.bit(0).inverted(), secondaryBus, toString() + "-lSelector")

    // sub units
    private val mathBus = StandardWritableBus(false, toString() + "-math")
    private val logicBus = StandardWritableBus(false, toString() + "-logic")
    private val rollBus = StandardWritableBus(false, toString() + "-roll")

    private val mathCarryOut = StandardWritableSignal(false, toString() + "-mathCarryOut")
    private val rollCarryOut = StandardWritableSignal(false, toString() + "-rollCarryOut")

    private val mathUnit =
        MathUnit(primaryBus, secondaryBus, carryIn, command, mathBus, mathCarryOut, toString() + "-mu")
    private val logicUnit = LogicUnit(primaryBus, secondaryBus, command, logicBus, toString() + "-lu")
    private val rollUnit = RollUnit(primaryBus, carryIn, command, rollBus, rollCarryOut, toString() + "-ru")

    // output aggregation

    private val mathDriver =
        OctalTristateDriver(mathBus, controlBus.bit(4).inverted(), output, toString() + "-mathDriver")
    private val logicDriver =
        OctalTristateDriver(logicBus, controlBus.bit(3).inverted(), output, toString() + "-logicDriver")
    private val rollDriver =
        OctalTristateDriver(rollBus, controlBus.bit(5).inverted(), output, toString() + "-rollDriver")


    private val cOutAggregator =
        SignalChoice(mathCarryOut, rollCarryOut, controlBus.bit(4), carryOut, toString() + "-cOutAggregator")

    override fun getSubElements(): List<SimulationElement> {
        return listOf(
            controlBus,
            controlCircuit,
            primaryBus,
            secondaryBus,
            aInverter,
            bSelector,
            lSelector,
            mathBus,
            logicBus,
            rollBus,
            mathCarryOut,
            rollCarryOut,
            mathUnit,
            logicUnit,
            rollUnit,
            mathDriver,
            logicDriver,
            rollDriver,
            cOutAggregator,
        )
    }

}
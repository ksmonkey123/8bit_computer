package ch.awae.custom8bit.hardware.hardware.modules.alu

import ch.awae.custom8bit.hardware.*
import ch.awae.custom8bit.hardware.hardware.ic.*
import ch.awae.custom8bit.hardware.hardware.wiring.*

/**
 * Commands:
 *
 * 0000 -> nibble swap (high input bis as carry out)
 * 0001 -> shift left through carry
 * 0010 -> shift right through carry
 * 0011 -> 2s complement with carry
 * 0100 -> decrement with carry
 * 0101 -> increment with carry
 * 0110 -> add with carry
 * 0111 -> subtract with carry
 * 1000 -> identity
 * 1001 -> bitwise and
 * 1010 -> bitwise inclusive or
 * 1011 -> bitwise exclusive or
 * 1100 -> bitwise inverse
 * 1101 -> bitwise not and
 * 1110 -> bitwise inclusive not or
 * 1111 -> bitwise exclusive not or
 *
 * Additional 5th bit (MSB) controls if literal L1 is used instead of regB
 */
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
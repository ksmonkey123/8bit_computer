package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.*
import ch.awae.custom8bit.microcode.AluOperation

data class AluOutput(
    val data: Int,
    val carry: Boolean,
)

object ALU {

    fun calculate(state: ProcessorState, operation: AluOperation): AluOutput {
        return when (operation) {
            AluOperation.AND -> AluOutput(state.registerA and state.aluInput, false)
            AluOperation.IOR -> AluOutput(state.registerA or state.aluInput, false)
            AluOperation.XOR -> AluOutput(state.registerA xor state.aluInput, false)
            AluOperation.INVERT -> AluOutput(state.aluInput.inv() and 0xff, false)
            AluOperation.DECREMENT -> (state.aluInput + 255 + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.INCREMENT -> (state.aluInput + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.ADDITION -> (state.registerA + state.aluInput + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.SUBTRACTION -> (state.registerA + (state.aluInput.inv() and 0xff) + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }
            AluOperation.COMPLEMENT -> ((state.aluInput.inv() and 0xff) + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.SHIFT_LEFT -> AluOutput((state.aluInput.shl(1) + state.flags.carry.toInt()) and 0xff, state.aluInput > 0x7f)
            AluOperation.SHIFT_RIGHT -> AluOutput((state.aluInput.ushr(1) + (state.flags.carry.toInt().shl(7) )) and 0xff, (state.aluInput and 0x01) != 0)

        }
    }

}
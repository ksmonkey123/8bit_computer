package ch.awae.custom8bit.emulator.processor

import ch.awae.custom8bit.emulator.*

data class AluOutput(
    val data: Int,
    val carry: Boolean,
)

object ALU {

    fun calculate(state: ProcessorState, operation: AluOperation?): AluOutput {
        return when (operation) {
            null -> AluOutput(state.registerA, state.flags.carry)
            AluOperation.AND -> AluOutput(state.registerA and state.registerB, state.flags.carry)
            AluOperation.IOR -> AluOutput(state.registerA or state.registerB, state.flags.carry)
            AluOperation.XOR -> AluOutput(state.registerA xor state.registerB, state.flags.carry)
            AluOperation.INVERT -> AluOutput(state.registerA.inv() and 0xff, state.flags.carry)
            AluOperation.NAND -> AluOutput((state.registerA and state.registerB).inv() and 0xff, state.flags.carry)
            AluOperation.INOR -> AluOutput((state.registerA or state.registerB).inv() and 0xff, state.flags.carry)
            AluOperation.XNOR -> AluOutput((state.registerA xor state.registerB).inv() and 0xff, state.flags.carry)
            AluOperation.DECREMENT -> (state.registerA + 255 + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.INCREMENT -> (state.registerA + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.ADDITION -> (state.registerA + state.registerB + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.SUBTRACTION -> (state.registerA + (state.registerB.inv() and 0xff) + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.NIBBLE_SWAP -> AluOutput(
                ((state.registerA and 0xf0) shr 4) + ((state.registerA and 0x0f) shl 4),
                state.registerA > 0x7f
            )

            AluOperation.COMPLEMENT -> ((state.registerA.inv() and 0xff) + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.SHIFT_LEFT -> AluOutput(state.registerA.shl(1) and 0xff, state.registerA > 0x7f)
            AluOperation.SHIFT_RIGHT -> AluOutput(state.registerA.ushr(1) and 0xff, (state.registerA and 0x01) != 0)
            AluOperation.LITERAL_AND -> AluOutput(state.registerA and state.literal1, state.flags.carry)
            AluOperation.LITERAL_IOR -> AluOutput(state.registerA or state.literal1, state.flags.carry)
            AluOperation.LITERAL_XOR -> AluOutput(state.registerA xor state.literal1, state.flags.carry)
            AluOperation.LITERAL_NAND -> AluOutput(
                (state.registerA and state.literal1).inv() and 0xff,
                state.flags.carry
            )

            AluOperation.LITERAL_INOR -> AluOutput(
                (state.registerA or state.literal1).inv() and 0xff,
                state.flags.carry
            )

            AluOperation.LITERAL_XNOR -> AluOutput(
                (state.registerA xor state.literal1).inv() and 0xff,
                state.flags.carry
            )

            AluOperation.LITERAL_ADDITION -> (state.registerA + state.literal1 + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }

            AluOperation.LITERAL_SUBTRACTION -> (state.registerA + (state.literal1.inv() and 0xff) + state.flags.carry.toInt()).let {
                AluOutput(
                    it and 0xff,
                    it > 0xff
                )
            }
        }
    }

}
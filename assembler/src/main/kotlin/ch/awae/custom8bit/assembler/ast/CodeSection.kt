package ch.awae.custom8bit.assembler.ast

data class CodeSection(
    val startAt: Int,
    val instructions: List<Instruction>,
) {

    val size = instructions.sumOf { it.size }

    val symbols: Map<String, Int> = run {
        val map = mutableMapOf<String, Int>()
        var cursor = startAt
        for (instruction in instructions) {
            if (instruction is LabelledInstruction) {
                map[instruction.label] = cursor
            }
            cursor += instruction.size
        }
        map
    }

    infix fun collidesWith(other: CodeSection): Boolean {
        return this.startAt < (other.startAt + other.size) && other.startAt < (this.startAt + this.size)
    }

}

package ch.awae.custom8bit.assembler.bytecode

import ch.awae.custom8bit.assembler.ast.Program

class BytecodeGenerationException(message: String) : RuntimeException(message)

inline fun <T> List<T>.pairwise(block: (T, T) -> Unit) {
    this.forEachIndexed { ix, x ->
        this.forEachIndexed { iy, y ->
            if (iy > ix)
                block(x, y)
        }
    }
}

fun compileToByteCode(program: Program) {
    // validate that code blocks don't overlap
    program.codeSections.pairwise { x, y ->
        if (x collidesWith y) {
            throw BytecodeGenerationException("code sections ${x.startAt} and ${y.startAt} collide")
        }
    }



}

data class PlacedConstant

fun placeConstants(program: Program) {}
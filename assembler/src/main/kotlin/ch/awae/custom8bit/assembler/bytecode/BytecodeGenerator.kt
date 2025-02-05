package ch.awae.custom8bit.assembler.bytecode

import ch.awae.custom8bit.assembler.*
import ch.awae.custom8bit.assembler.ast.*

class BytecodeGenerationException(message: String) : RuntimeException(message)

typealias SymbolMap = Map<String, Int>

inline fun <T> List<T>.pairwise(block: (T, T) -> Unit) {
    this.forEachIndexed { ix, x ->
        this.forEachIndexed { iy, y ->
            if (iy > ix)
                block(x, y)
        }
    }
}

fun compileToByteCode(program: Program) : List<BytecodeFragment> {
    // validate that code blocks don't overlap
    program.codeSections.pairwise { x, y ->
        if (x collidesWith y) {
            throw BytecodeGenerationException("code sections ${x.startAt} and ${y.startAt} collide")
        }
    }

    val mapBuilder = SymbolMapBuilder()

    val fragments = mutableListOf<BytecodeFragment>()

    // collect code symbols
    program.codeSections.forEach { mapBuilder.addAll(it.symbols) }
    // collect variable symbols
    program.variables.forEach { mapBuilder.add(it.symbol, it.position) }
    // place constants
    fragments.addAll(placeConstants(program, mapBuilder))

    val symbols = mapBuilder.symbolMap
    // we now have all constant placements and all symbols. time for bytecode generation
    for (codeSection in program.codeSections) {
        fragments.add(generateBytecode(codeSection, symbols))
    }

    return fragments.sortedBy { it.startAt }
}

fun placeConstants(program: Program, mapBuilder: SymbolMapBuilder): List<BytecodeFragment> {
    val reservedBlocks = mutableSetOf<IntRange>()
    program.codeSections.forEach { reservedBlocks.add(it.range) }

    val placedConstants = mutableListOf<BytecodeFragment>()

    program.constants.forEach { const ->
        var start = 0x0000

        findPlacement@ while (true) {
            val range = start until (start + const.size)

            if (!reservedBlocks.any { it overlaps range }) {
                // not overlapping any known range, we can place it!
                reservedBlocks.add(range)
                placedConstants.add(BytecodeFragment(start, const.data))
                mapBuilder.add(const.symbol, start)
                break@findPlacement
            }

            start++
        }

    }

    return placedConstants
}
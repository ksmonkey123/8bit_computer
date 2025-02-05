package ch.awae.custom8bit.assembler.bytecode

import ch.awae.custom8bit.assembler.*
import ch.awae.custom8bit.assembler.ast.*
import org.springframework.stereotype.Component

class BytecodeGenerationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

typealias SymbolMap = Map<String, Int>

@Component
class BytecodeGenerator {

    private val logger = createLogger()

    private inline fun <T> List<T>.pairwise(block: (T, T) -> Unit) {
        this.forEachIndexed { ix, x ->
            this.forEachIndexed { iy, y ->
                if (iy > ix)
                    block(x, y)
            }
        }
    }

    private fun verifyNoSectionCollisions(codeSections: List<CodeSection>) {
        logger.info("verifying code sections do not overlap")

        var hasError = false
        codeSections.pairwise { x, y ->
            if (x collidesWith y) {
                logger.error("code sections ${x.startAt}:${x.size} and ${y.startAt}:${y.size} collide")
                hasError = true
            }
        }
        if (hasError) {
            throw BytecodeGenerationException("code sections are overlapping. cannot compile")
        }
    }

    fun compileToByteCode(program: Program): ByteArray {
        logger.info(
            "compiling ${program.codeSections.size} code section(s)," +
                    " ${program.variables.size} variable(s)," +
                    " ${program.constants.size} constant(s)"
        )

        verifyNoSectionCollisions(program.codeSections)

        val mapBuilder = SymbolMapBuilder()
        val fragments = mutableListOf<BytecodeFragment>()

        // collect code symbols
        logger.info("collecting instruction labels")
        program.codeSections.forEach {
            mapBuilder.addAll(it.symbols.also { list ->
                logger.info("collected ${list.size} label(s) from code section ${it.startAt.toHex(2)}:${it.size}")
                list.forEach { s ->
                    logger.info("  ${s.key}: ${s.value.toHex(2)}")
                }
            })
        }
        // collect variable symbols
        logger.info("collected ${program.variables.size} variable label(s)")
        program.variables.forEach {
            mapBuilder.add(it.symbol, it.position)
            logger.info("  ${it.symbol}: ${it.position.toHex(2)}")
        }
        // place constants
        fragments.addAll(placeConstants(program, mapBuilder))

        val symbols = mapBuilder.symbolMap
        // we now have all constant placements and all symbols. time for bytecode generation
        for (codeSection in program.codeSections) {
            logger.info("generating bytecode for code section ${codeSection.startAt.toHex(2)}:${codeSection.size}")
            try {
                fragments.add(generateBytecode(codeSection, symbols))
            } catch (ex: Exception) {
                logger.error("bytecode generation for code section ${codeSection.startAt.toHex(2)}:${codeSection.size}", ex)
                throw BytecodeGenerationException(ex.message ?: "bytecode generation error", ex)
            }
        }

        return assembleFragments(fragments.sortedBy { it.startAt })
    }

    private fun assembleFragments(fragments: List<BytecodeFragment>): ByteArray {
        logger.info("assembling ${fragments.size} fragment(s)")
        val buffer = ByteArray(8192) { -1 }

        for (fragment in fragments) {
            logger.info("  placing fragment at ${fragment.startAt.toHex(2)}..${(fragment.startAt + fragment.data.size - 1).toHex(2)}")
            fragment.data.copyInto(buffer, destinationOffset = fragment.startAt)
        }

        logger.info("assembly done")
        return buffer
    }

    private fun placeConstants(program: Program, mapBuilder: SymbolMapBuilder): List<BytecodeFragment> {
        logger.info("placing ${program.constants.size} constant(s)")
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
                    logger.info("  ${const.symbol}: ${start.toHex(2)}")
                    break@findPlacement
                }

                start++
            }

        }

        return placedConstants
    }
}

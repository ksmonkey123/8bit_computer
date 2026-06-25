package ch.awae.custom8bit.assembler

import ch.awae.binfiles.BinaryFile
import ch.awae.custom8bit.assembler.bytecode.*
import ch.awae.custom8bit.assembler.parser.*

object Assembler {
    private val parser: Parser = Parser()
    private val bytecodeGenerator: BytecodeGenerator = BytecodeGenerator()

    fun assemble(code: String): BinaryFile {
        return bytecodeGenerator.compileToByteCode(parser.parseProgram(code))
    }

}
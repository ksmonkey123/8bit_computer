package ch.awae.custom8bit.assembler

import ch.awae.custom8bit.assembler.bytecode.*
import ch.awae.custom8bit.assembler.parser.*

object Assembler {
    private val parser: Parser = Parser()
    private val bytecodeGenerator: BytecodeGenerator = BytecodeGenerator()

    fun assemble(code: String): ByteArray {
        return bytecodeGenerator.compileToByteCode(parser.parseProgram(code))
    }

}
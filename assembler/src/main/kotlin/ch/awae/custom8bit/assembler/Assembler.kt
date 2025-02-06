package ch.awae.custom8bit.assembler

import ch.awae.custom8bit.assembler.bytecode.*
import ch.awae.custom8bit.assembler.parser.*
import org.springframework.stereotype.*

@Component
class Assembler(
    private val parser: Parser,
    private val bytecodeGenerator: BytecodeGenerator,
) {

    fun assemble(code: String): ByteArray {
        return bytecodeGenerator.compileToByteCode(parser.parseProgram(code))
    }

}
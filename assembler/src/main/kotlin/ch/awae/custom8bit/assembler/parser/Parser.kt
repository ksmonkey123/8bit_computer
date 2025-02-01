package ch.awae.custom8bit.assembler.parser

import AssemblerLexer
import AssemblerParser
import ch.awae.custom8bit.assembler.ast.*
import org.antlr.v4.runtime.*

class Parser {

    fun parseProgram(input: String): Program {
        if (!input.endsWith('\n')) {
            return parseProgram(input + '\n')
        }

        val inputCharStream = CharStreams.fromString(input)
        val tokenStream = CommonTokenStream(AssemblerLexer(inputCharStream))
        val parser = AssemblerParser(tokenStream)
        val listener = HasErrorErrorListener()
        parser.addErrorListener(listener)
        val program = parser.program()

        if (listener.hasError) {
            throw IllegalStateException("parser had an error")
        }

        return program.toProgram()
    }

}


class HasErrorErrorListener : BaseErrorListener() {

    var hasError = false
        private set

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        hasError = true
    }
}

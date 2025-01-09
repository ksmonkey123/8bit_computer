package ch.awae.custom8bit.microcode

import java.nio.file.*

fun main() {
    val compiledCode = INSTRUCTION_SET.values.flatMap {
        OpcodeCompiler.compile(it)
    }.toMap()

    // generate binary

    val binary = ByteArray(8192) { adr ->
        compiledCode.getOrDefault(adr, 0x00).toByte()
    }

    Files.write(Path.of("microcode.bin"), binary)

}
package ch.awae.custom8bit.microcode

import java.nio.file.*

fun main() {
    val binary = compileInstructionSet()
    Files.write(Path.of("microcode.bin"), binary)
}
package ch.awae.custom8bit.microcode

import java.nio.file.*
import kotlin.io.path.*

fun main() {
    val data = Compiler.compileInstructionSet(INSTRUCTION_SET)

    Files.write(Path("microcode_low.bin"), data.first)
    Files.write(Path("microcode_high.bin"), data.second)
}
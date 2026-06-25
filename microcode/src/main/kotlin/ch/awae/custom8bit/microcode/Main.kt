package ch.awae.custom8bit.microcode

import ch.awae.binfiles.BinaryFile
import ch.awae.binfiles.hex.HexFileWriter
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.outputStream

fun main() {
    val data = Compiler.compileInstructionSet(INSTRUCTION_SET)

    writeToFile(BinaryFile(data.first), Path("microcode_low.hex"))
    writeToFile(BinaryFile(data.second), Path("microcode_high.hex"))
}

private fun writeToFile(data: BinaryFile, path: Path) {
    HexFileWriter(path.outputStream()).use {
        it.write(data)
    }
}
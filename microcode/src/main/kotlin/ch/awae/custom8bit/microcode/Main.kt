package ch.awae.custom8bit.microcode

import ch.awae.binfiles.BinaryFile
import ch.awae.binfiles.hex.HexFileWriter
import java.nio.file.*
import kotlin.io.path.*

fun main() {
    val data = Compiler.compileInstructionSet(INSTRUCTION_SET)

    val lowBinfile = BinaryFile().also {
        data.first.forEachIndexed { index, value ->
            it.addByte(index, value)
        }
    }

    val highBinfile = BinaryFile().also {
        data.second.forEachIndexed { index, value ->
            it.addByte(index, value)
        }
    }

    HexFileWriter(Files.newOutputStream(Path("microcode_low.hex"))).use {
        it.write(lowBinfile)
        it.flush()
    }

    HexFileWriter(Files.newOutputStream(Path("microcode_high.hex"))).use {
        it.write(highBinfile)
        it.flush()
    }

    Files.write(Path("microcode_low.bin"), data.first)
    Files.write(Path("microcode_high.bin"), data.second)
}
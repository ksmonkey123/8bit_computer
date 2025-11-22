package ch.awae.custom8bit.assembler

import ch.awae.binfiles.hex.HexFileWriter
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

@Component
class AssemblerRunner : CommandLineRunner {

    private val logger = createLogger()

    override fun run(vararg args: String?) {
        assembleFile(args[0] ?: throw IllegalArgumentException("first argument must be input filename"))
    }

    fun assembleFile(filename: String) {
        logger.info("assembling file {}", filename)
        val input = Files.readString(Paths.get(filename))
        val output = Assembler.assemble(input)
        logger.info("program size: {} bytes", output.currentSize)
        logger.info("saving output to {}.hex", filename)

        HexFileWriter(Files.newOutputStream(Paths.get("$filename.hex"))).use {
            it.write(output)
            it.flush()
        }
        logger.info("done")
    }

}
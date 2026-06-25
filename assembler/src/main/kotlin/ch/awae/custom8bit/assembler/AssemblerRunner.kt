package ch.awae.custom8bit.assembler

import ch.awae.binfiles.hex.HexFileWriter
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.outputStream

@Component
class AssemblerRunner : CommandLineRunner {

    private val logger = createLogger()

    override fun run(vararg args: String) {
        require(args.isNotEmpty()) { "first argument must be input filename" }
        assembleFile(args[0])
    }

    fun assembleFile(filename: String) {
        logger.info("assembling file {}", filename)
        val input = Files.readString(Paths.get(filename))
        val output = Assembler.assemble(input)
        logger.info("program size (bounds): {} bytes", output.currentSize)
        logger.info("saving output to {}.hex", filename)
        HexFileWriter(Paths.get("$filename.hex").outputStream()).use {
            it.write(output)
        }
        logger.info("done")
    }

}
package ch.awae.custom8bit.assembler

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

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
        logger.info("program size: {} bytes", output.size)
        logger.info("saving output to {}.bin", filename)
        Files.write(Paths.get("$filename.bin"), output)
        logger.info("done")
    }

}
package ch.awae.custom8bit.assembler

import org.springframework.boot.*
import org.springframework.stereotype.*
import java.nio.file.*

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
        logger.info("program size: {} bytes", output.size)
        logger.info("saving output to {}.bin", filename)
        Files.write(Paths.get("$filename.bin"), output)
        logger.info("done")
    }

}
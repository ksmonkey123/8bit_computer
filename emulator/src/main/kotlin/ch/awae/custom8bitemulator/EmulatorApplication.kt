package ch.awae.custom8bitemulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmulatorApplication

fun main(args: Array<String>) {
    runApplication<EmulatorApplication>(*args)
}

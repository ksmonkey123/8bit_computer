package ch.awae.custom8bit.assembler

import ch.awae.custom8bit.assembler.parser.*

fun main() {

    val simplestProgram = """
        .vars
        0x2000: output[1]
        
        .code 0x0000
            mov AB #0x0101
            mov *output A
            mov *output A
            mov D #11
        loop:
            add B
            mov *output A
            swap B
            decbnz D loop
            halt
    """.trimIndent()

    val ast = Parser().parseProgram(simplestProgram)


}

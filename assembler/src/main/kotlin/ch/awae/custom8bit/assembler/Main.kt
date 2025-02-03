package ch.awae.custom8bit.assembler

import ch.awae.custom8bit.assembler.parser.*

fun main() {

    val simplestProgram = """
        .data
            limit[1] = 11
        
        .vars
        0x2000: output[1]
        
        .code 0x0000
            mov AB #0x0101
            mov *output A
            mov *output A
            mov D #11
        loop:
            cfc
            adc B
            mov *output A
            swp B
            cfc
            dec D
            bnz loop
            hlt
    """.trimIndent()

    val ast = Parser().parseProgram(simplestProgram)


}

import ch.awae.custom8bit.assembler.*
import ch.awae.custom8bit.emulator.*
import kotlin.test.*

class FibonacciTest {

    class OutputCapture : PeripheralDeviceBase() {
        private val output = mutableListOf<Int>()
        override fun write(address: Byte, data: Byte) {
            output.add(data.toInt() and 0xff)
        }

        val capturedData: List<Int>
            get() = output.toList()
    }

    @Test
    fun `fibonacci with subroutine`() {
        val source = """
            .vars
            	0x4000: output[1]
            
            .code 0x0000
            	mov AB #0x0101
            	mov *output A
            	mov *output B
            	mov D #11
            loop:
            	jsr next
            	; new next fibonacci number is in A
            	mov *output A
            	; decrement D and repeat if not done
            	cfc
            	dec D
            	bnz loop
            	hlt
            
            ; calculate next number and publish it
            next:
            	swp B
            	cfc
            	adc B
            	ret
        """.trimIndent()

        val program = Assembler.assemble(source)
        val capture = OutputCapture()

        Emulator(program, 0 to capture).runToCompletion()

        assertEquals(
            listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233), capture.capturedData
        )

    }

    @Test
    fun `fibonacci from source`() {
        val source = """
            .vars
            	0x4000: output[1]
            
            .code 0x0000
            	mov AB #0x0101
            	mov *output A
            	mov *output B
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

        val program = Assembler.assemble(source)
        val capture = OutputCapture()

        Emulator(program, 0 to capture).runToCompletion()

        assertEquals(
            listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233), capture.capturedData
        )

    }

}


package ch.awae.custom8bit.assembler

import org.slf4j.*

infix fun IntRange.overlaps(other: IntRange): Boolean {
    return (this.first <= other.last) && (other.first <= this.last)

}

inline fun <reified T> T.createLogger(): Logger = LoggerFactory.getLogger(T::class.java)

fun Int.toHex(bytes: Int): String = "0x" + this.toString(16).padStart(2 * bytes, '0')
package ch.awae.custom8bit.emulator

import org.slf4j.*

inline fun <reified T> T.createLogger(): Logger = LoggerFactory.getLogger(T::class.java)

fun Int.toHex(bytes: Int): String = "0x" + this.toString(16).takeLast(bytes * 2).padStart(bytes * 2, '0')
fun Boolean.toInt() = if (this) 1 else 0
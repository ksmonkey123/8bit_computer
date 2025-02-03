package ch.awae.custom8bit.assembler

infix fun IntRange.overlaps(other: IntRange) : Boolean {
    return (this.first <= other.last) && (other.first <= this.last)

}
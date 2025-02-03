package ch.awae.custom8bit.assembler.ast

data class VarsSection(
    val variables: List<Variable>,
) {

    fun join(other: VarsSection): VarsSection {
        val occupiedByThis = variables.flatMap { v -> v.position until (v.position + v.size) }
        val occupiedByOther = variables.flatMap { v -> v.position until (v.position + v.size) }
        if (occupiedByThis.intersect(occupiedByOther.toSet()).isNotEmpty()) {
            throw IllegalArgumentException("variable sections overlapping: $this, $other")
        }

        return VarsSection(this.variables + other.variables)
    }
}


data class Variable(
    val symbol: String,
    val size: Int,
    val position: Int,
)

fun AssemblerParser.VariableSectionContext.toVarsSection(): VarsSection {
    val startAt = this.startAt?.toInt()

    val unplaced = mutableListOf<FieldDeclaration>()
    val fixedPlace = mutableListOf<Pair<Int, FieldDeclaration>>()

    for (statement in this.variableStatement()) {
        when (statement) {
            is AssemblerParser.VariableDeclarationContext -> {
                unplaced.add(statement.fieldDeclaration().toFieldDeclaration())
            }

            is AssemblerParser.VariableDeclarationWithFixedPositionContext -> {
                fixedPlace.add(statement.pos.toInt() to statement.fieldDeclaration().toFieldDeclaration())
            }

            else -> throw ParsingException(this)
        }
    }

    val variables = mutableListOf<Variable>()


    val usedPositions = mutableSetOf<Int>()
    fun checkAndReservePositions(start: Int, size: Int): Boolean {
        val newPositions = start.until(start + size).toSet()
        if (usedPositions.intersect(newPositions).isNotEmpty()) {
            return false
        }
        // range is ok, reserve it
        usedPositions.addAll(newPositions)
        return true
    }

    // place fixed variables
    for ((pos, field) in fixedPlace) {
        if (checkAndReservePositions(pos, field.size)) {
            variables.add(Variable(field.symbol, field.size, pos))
        } else {
            throw IllegalArgumentException("fixed variable cannot be placed. overlapping another one!")
        }
    }

    // place dynamic variables wherever possible
    for (fd in unplaced) {
        var pos = startAt
            ?: throw IllegalArgumentException("cannot have unplaced variables in variable section without starting point")
        while (true) {
            if (checkAndReservePositions(pos, fd.size)) {
                // success, we found a place for the variable
                variables.add(Variable(fd.symbol, fd.size, pos))
                break
            } else {
                pos++
            }
        }
    }

    return VarsSection(variables.toList().sortedBy { it.position })
}

fun AssemblerParser.FieldDeclarationContext.toFieldDeclaration(): FieldDeclaration =
    when (this) {
        //is AssemblerParser.SimpleFieldContext -> FieldDeclaration(this.SYMBOL().text, 1)
        is AssemblerParser.FieldWithSizeContext -> FieldDeclaration(this.SYMBOL().text, this.size.toInt())
        else -> throw ParsingException(this)
    }


data class FieldDeclaration(val symbol: String, val size: Int)
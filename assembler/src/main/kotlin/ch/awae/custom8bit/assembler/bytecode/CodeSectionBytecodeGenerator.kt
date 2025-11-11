package ch.awae.custom8bit.assembler.bytecode

import ch.awae.custom8bit.assembler.ast.*

fun generateBytecode(codeSection: CodeSection, symbolMap: SymbolMap): BytecodeFragment {
    val builder = BytecodeFragment.Builder(codeSection.startAt)

    for (instruction in codeSection.instructions) {
        compileInstruction(instruction, symbolMap, builder)
    }

    return builder.build()
}

fun compileInstruction(instruction: Instruction, symbolMap: SymbolMap, builder: BytecodeFragment.Builder) {
    when (instruction) {
        // labelled instruction can simply be compiled recursively by unpacking
        is LabelledInstruction -> compileInstruction(instruction.instruction, symbolMap, builder)
        is CompilingInstruction -> builder.append(*instruction.compile(symbolMap))
        is ShiftInstruction -> builder.append(instruction.operation.opcode)
        is RegisterCopyInstruction -> builder.append(instruction.opcode)
        is StackAllocationInstruction -> builder.append(0x8e, instruction.amount)
        is StackFreeInstruction -> builder.append(0x8f, instruction.amount)
        is CarryUpdateInstruction -> builder.append(if (instruction.value) 0xfd else 0xfc)
        is ChangeInterruptState -> builder.append(if (instruction.value) 0xf3 else 0xf2)
        is ReturnFromInterruptInstruction -> builder.append(0xf1)
        HaltInstruction -> builder.append(0xff)
        NopInstruction -> builder.append(0xfe)
        ReturnInstruction -> builder.append(0xbb)
    }
}

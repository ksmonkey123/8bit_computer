package ch.awae.custom8bit.microcode

import ch.awae.custom8bit.microcode.AddressSource.*
import ch.awae.custom8bit.microcode.AddressTarget.*
import ch.awae.custom8bit.microcode.AluOperation.*
import ch.awae.custom8bit.microcode.DataSource.*
import ch.awae.custom8bit.microcode.DataTarget.*

val INSTRUCTION_SET: Set<Operation> = setOf(
    Operation(
        0x00, "AND B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x01, "AND C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x02, "AND D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x03, "AND i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x04, "AND (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x05, "AND (CD)", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_REG_CD),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x06,
        "IOR B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x07, "IOR C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x08, "IOR D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x09, "IOR i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x0a, "IOR (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x0b, "IOR (CD)", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_REG_CD),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x0c, "NOT A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x0d, "NOT B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = INVERT),
    ),
    Operation(
        0x0e, "NOT C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = INVERT),
    ),
    Operation(
        0x0f, "NOT D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = INVERT),
    ),
    Operation(
        0x10, "XOR B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x11, "XOR C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x12, "XOR D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x13, "XOR i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x14, "XOR (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x15, "XOR (CD)", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_REG_CD),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x16, "SHL", 0, false,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    Operation(
        0x17, "RLC", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    Operation(
        0x18, "RL", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get top bit into carry (write to A necessary to not rewrite alu input)
        step1 = MicroOp(dataTarget = WRITE_REG_A, action = SHIFT_LEFT),
        // do real shift with carry to get it into the bottom bit
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    Operation(
        0x19, "USHR", 0, false,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x1a, "ASHR", 0, false,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get top bit into carry (write to A necessary to not rewrite alu input)
        step1 = MicroOp(dataTarget = WRITE_REG_A, action = SHIFT_LEFT),
        // real shift right with replicated top bit
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x1b, "RRC", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x1c, "RR", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get bottom bit into carry (write to A necessary to not rewrite alu input)
        step1 = MicroOp(dataTarget = WRITE_REG_A, action = SHIFT_RIGHT),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x1d, "SWAP B", 0,
        // move B into ALU
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        // copy A to B
        step1 = MicroOp(READ_REG_A, WRITE_REG_B),
        // write !B to ALU
        step2 = MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!B (=B) to A
        step3 = MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x1e, "SWAP C", 0,
        // move C into ALU
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        // copy A to C
        step1 = MicroOp(READ_REG_A, WRITE_REG_C),
        // write !C to ALU
        step2 = MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!C (=C) to A
        step3 = MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x1f, "SWAP D", 0,
        // move D into ALU
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        // copy A to D
        step1 = MicroOp(READ_REG_A, WRITE_REG_D),
        // write !D to ALU
        step2 = MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!D (=D) to A
        step3 = MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x20, "ADD B", 0, false,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x21, "ADDC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x22, "ADD C", 0, false,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x23, "ADDC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x24, "ADD D", 0, false,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x25, "ADDC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x26, "ADD i", 1, false,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x27, "ADDC i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x28, "ADD (L)", 2, false,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x29, "ADDC (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x2a, "ADD (CD)", 0, false,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_REG_CD),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x2b, "ADDC (CD)", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_REG_CD),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x2c, "SUB B", 0, true,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x2d, "SUBC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x2e, "SUB C", 0, true,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x2f, "SUBC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x30, "SUB D", 0, true,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x31, "SUBC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x32, "SUB i", 1, true,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x33, "SUBC i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x34, "SUB (L)", 2, true,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x35, "SUBC (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x36, "SUB (CD)", 0, true,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_REG_CD),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x37, "SUBC (CD)", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_REG_CD),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x38, "ISUB B", 0, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_REG_B, WRITE_REG_A),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x39, "ISUBC B", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_REG_B, WRITE_REG_A),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x3a, "ISUB i", 1, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_LITERAL_1, WRITE_REG_A),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x3b, "ISUBC i", 1,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_LITERAL_1, WRITE_REG_A),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x3c, "ISUB (L)", 2, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x3d, "ISUBC (L)", 2,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x3e, "ISUB (CD)", 0, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_REG_CD),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x3f, "ISUBC (CD)", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_REG_CD),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x40, "DEC A", 0, false,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = DECREMENT),
    ),
    Operation(
        0x41, "DEC B", 0, false,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = DECREMENT),
    ),
    Operation(
        0x42, "DEC C", 0, false,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = DECREMENT),
    ),
    Operation(
        0x43, "DEC D", 0, false,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = DECREMENT),
    ),
    Operation(
        0x44, "DECC A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = DECREMENT),
    ),
    Operation(
        0x45, "DECC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = DECREMENT),
    ),
    Operation(
        0x46, "DECC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = DECREMENT),
    ),
    Operation(
        0x47, "DECC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = DECREMENT),
    ),
    Operation(
        0x48, "INC A", 0, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = INCREMENT),
    ),
    Operation(
        0x49, "INC B", 0, true,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = INCREMENT),
    ),
    Operation(
        0x4a, "INC C", 0, true,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = INCREMENT),
    ),
    Operation(
        0x4b, "INC D", 0, true,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = INCREMENT),
    ),
    Operation(
        0x4c, "INCC A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = INCREMENT),
    ),
    Operation(
        0x4d, "INCC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = INCREMENT),
    ),
    Operation(
        0x4e, "INCC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = INCREMENT),
    ),
    Operation(
        0x4f, "INCC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = INCREMENT),
    ),
    Operation(
        0x50, "COMP A", 0, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = COMPLEMENT),
    ),
    Operation(
        0x51, "COMP B", 0, true,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = COMPLEMENT),
    ),
    Operation(
        0x52, "COMP C", 0, true,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = COMPLEMENT),
    ),
    Operation(
        0x53, "COMP D", 0, true,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = COMPLEMENT),
    ),
    Operation(
        0x54, "COMPC A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = COMPLEMENT),
    ),
    Operation(
        0x55, "COMPC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = COMPLEMENT),
    ),
    Operation(
        0x56, "COMPC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = COMPLEMENT),
    ),
    Operation(
        0x57, "COMPC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = COMPLEMENT),
    ),
    // 0x58:5f [8] available
    Operation(0x60, "MOV A A", 0, step0 = MicroOp(READ_REG_A, WRITE_REG_A)),
    Operation(0x61, "MOV A B", 0, step0 = MicroOp(READ_REG_B, WRITE_REG_A)),
    Operation(0x62, "MOV A C", 0, step0 = MicroOp(READ_REG_C, WRITE_REG_A)),
    Operation(0x63, "MOV A D", 0, step0 = MicroOp(READ_REG_D, WRITE_REG_A)),
    Operation(0x64, "MOV B A", 0, step0 = MicroOp(READ_REG_A, WRITE_REG_B)),
    Operation(0x65, "MOV B B", 0, step0 = MicroOp(READ_REG_B, WRITE_REG_B)),
    Operation(0x66, "MOV B C", 0, step0 = MicroOp(READ_REG_C, WRITE_REG_B)),
    Operation(0x67, "MOV B D", 0, step0 = MicroOp(READ_REG_D, WRITE_REG_B)),
    Operation(0x68, "MOV C A", 0, step0 = MicroOp(READ_REG_A, WRITE_REG_C)),
    Operation(0x69, "MOV C B", 0, step0 = MicroOp(READ_REG_B, WRITE_REG_C)),
    Operation(0x6a, "MOV C C", 0, step0 = MicroOp(READ_REG_C, WRITE_REG_C)),
    Operation(0x6b, "MOV C D", 0, step0 = MicroOp(READ_REG_D, WRITE_REG_C)),
    Operation(0x6c, "MOV D A", 0, step0 = MicroOp(READ_REG_A, WRITE_REG_D)),
    Operation(0x6d, "MOV D B", 0, step0 = MicroOp(READ_REG_B, WRITE_REG_D)),
    Operation(0x6e, "MOV D C", 0, step0 = MicroOp(READ_REG_C, WRITE_REG_D)),
    Operation(0x6f, "MOV D D", 0, step0 = MicroOp(READ_REG_D, WRITE_REG_D)),
    Operation(0x70, "LOAD A (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL)),
    Operation(0x71, "LOAD B (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_LITERAL)),
    Operation(0x72, "LOAD C (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL)),
    Operation(0x73, "LOAD D (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_LITERAL)),
    Operation(0x74, "LOAD A (CD)", 0, step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_REG_CD)),
    Operation(0x75, "LOAD B (CD)", 0, step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_REG_CD)),
    Operation(
        0x76, "LOAD AB (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER),
    ),
    Operation(
        0x77, "LOAD CD (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER),
    ),
    Operation(
        0x78, "LOAD AB (CD)", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER),
    ),
    Operation(
        0x79, "LOAD CD (CD)", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER),
    ),
    Operation(
        0x7a, "LOAD AB i", 2,
        step0 = MicroOp(READ_LITERAL_1, WRITE_REG_A),
        step1 = MicroOp(READ_LITERAL_2, WRITE_REG_B),
    ),
    Operation(
        0x7b, "LOAD CD i", 2,
        step0 = MicroOp(READ_LITERAL_1, WRITE_REG_C),
        step1 = MicroOp(READ_LITERAL_2, WRITE_REG_D),
    ),
    Operation(0x7c, "LOAD A i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_A)),
    Operation(0x7d, "LOAD B i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_B)),
    Operation(0x7e, "LOAD C i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_C)),
    Operation(0x7f, "LOAD D i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_D)),
    Operation(0x80, "STORE A (L)", 2, step0 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x81, "STORE B (L)", 2, step0 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x82, "STORE C (L)", 2, step0 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x83, "STORE D (L)", 2, step0 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x84, "STORE A (CD)", 0, step0 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_REG_CD)),
    Operation(0x85, "STORE B (CD)", 0, step0 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_REG_CD)),
    Operation(
        0x86, "STORE AB (L)", 2,
        step0 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    Operation(
        0x87, "STORE CD (L)", 2,
        step0 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_LITERAL),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    Operation(
        0x88, "STORE AB (CD)", 0,
        step0 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_REG_CD),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    // 0x89:8f [7] available
    Operation(
        0x90, "PUSH A", 0,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT, WRITE_STACK_POINTER),
    ),
    Operation(
        0x91, "PUSH B", 0,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT, WRITE_STACK_POINTER),
    ),
    Operation(
        0x92, "PUSH C", 0,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT, WRITE_STACK_POINTER),
    ),
    Operation(
        0x93, "PUSH D", 0,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT, WRITE_STACK_POINTER),
    ),
    Operation(
        0x94, "POP A", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_STACK_POINTER),
        step1 = MicroOp(addressSource = ADR_INCREMENTER, action = WRITE_STACK_POINTER),
    ),
    Operation(
        0x95, "POP B", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_STACK_POINTER),
        step1 = MicroOp(addressSource = ADR_INCREMENTER, action = WRITE_STACK_POINTER),
    ),
    Operation(
        0x96, "POP C", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_STACK_POINTER),
        step1 = MicroOp(addressSource = ADR_INCREMENTER, action = WRITE_STACK_POINTER),
    ),
    Operation(
        0x97, "POP D", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_STACK_POINTER),
        step1 = MicroOp(addressSource = ADR_INCREMENTER, action = WRITE_STACK_POINTER),
    ),
    Operation(
        0x98, "PEEK A", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_STACK_POINTER),
    ),
    Operation(
        0x99, "PEEK B", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_STACK_POINTER),
    ),
    Operation(
        0x9a, "PEEK C", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_STACK_POINTER),
    ),
    Operation(
        0x9b, "PEEK D", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_STACK_POINTER),
    ),
    // 0x9c:9f [4] available
    // 0xa0:af [16] available
    Operation(
        0xb0, "BCC i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.CARRY_CLEAR),
    ),
    Operation(
        0xb1, "BCS i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.CARRY_SET),
    ),
    Operation(
        0xb2, "BZ i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.ZERO),
    ),
    Operation(
        0xb3, "BNZ i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_ZERO),
    ),
    Operation(
        0xb4, "BLZ i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NEGATIVE),
    ),
    Operation(
        0xb5, "BGZ i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.POSITIVE),
    ),
    Operation(
        0xb6, "BLEZ i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_POSITIVE),
    ),
    Operation(
        0xb7, "BGEZ i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_NEGATIVE),
    ),
    Operation(
        0xb8, "GOTO i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC)
    ),
    // 0xb9 [1] available
    Operation(
        0xba, "CALL i", 2,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_PC_HIGH, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT),
        step2 = MicroOp(READ_PC_LOW, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT, WRITE_STACK_POINTER),
        step3 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC),
    ),
    Operation(
        0xbb, "RETURN", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_PC_LOW, ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_PC_HIGH, ADR_INCREMENTER),
        step2 = MicroOp(addressSource = ADR_INCREMENTER, action = WRITE_STACK_POINTER),
    ),
    // 0xbc:bf [4] available
    // 0xc0:cf [16] available
    // 0xd0:df [16] available
    // 0xe0:ef [16] available
    // 0xf0:fd [14] available
    Operation(0xfe, "NOP", 0),
    Operation(0xff, "HALT", 0, halt = true),
)
package ch.awae.custom8bit.microcode

import ch.awae.custom8bit.microcode.AddressSource.*
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
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
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
        // shift once to get top bit into carry
        step1 = MicroOp(action = SHIFT_LEFT),
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
        // shift left to get top bit into carry for replication
        step1 = MicroOp(action = SHIFT_LEFT),
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
        step1 = MicroOp(action = SHIFT_RIGHT),
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
        0x24, "ADD d", 0, false,
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
        0x30, "SUB d", 0, true,
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
        0x3e, "ISUB (CD)", 2, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_REG_CD),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x3f, "ISUBC (CD)", 2,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_REG_CD),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x40, "DEC A", 2, false,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = DECREMENT),
    ),
    Operation(
        0x41, "DEC B", 2, false,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = DECREMENT),
    ),
    Operation(
        0x42, "DEC C", 2, false,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = DECREMENT),
    ),
    Operation(
        0x43, "DEC D", 2, false,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = DECREMENT),
    ),
    Operation(
        0x44, "DECC A", 2,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = DECREMENT),
    ),
    Operation(
        0x45, "DECC B", 2,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = DECREMENT),
    ),
    Operation(
        0x46, "DECC C", 2,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = DECREMENT),
    ),
    Operation(
        0x47, "DECC D", 2,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = DECREMENT),
    ),
    Operation(
        0x40, "INC A", 2, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = INCREMENT),
    ),
    Operation(
        0x41, "INC B", 2, true,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = INCREMENT),
    ),
    Operation(
        0x42, "INC C", 2, true,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = INCREMENT),
    ),
    Operation(
        0x43, "INC D", 2, true,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = INCREMENT),
    ),
    Operation(
        0x44, "INCC A", 2,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = INCREMENT),
    ),
    Operation(
        0x45, "INCC B", 2,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = INCREMENT),
    ),
    Operation(
        0x46, "INCC C", 2,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = INCREMENT),
    ),
    Operation(
        0x47, "INCC D", 2,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = INCREMENT),
    ),
    Operation(
        0x48, "COMP A", 0, true,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = COMPLEMENT),
    ),
    Operation(
        0x49, "COMP B", 0, true,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = COMPLEMENT),
    ),
    Operation(
        0x4a, "COMP C", 0, true,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = COMPLEMENT),
    ),
    Operation(
        0x4b, "COMP D", 0, true,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = COMPLEMENT),
    ),
    Operation(
        0x4c, "COMPC A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = COMPLEMENT),
    ),
    Operation(
        0x4d, "COMPC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = COMPLEMENT),
    ),
    Operation(
        0x4e, "COMPC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = COMPLEMENT),
    ),
    Operation(
        0x4f, "COMPC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = COMPLEMENT),
    ),
    Operation(0x50, "MOV A A", 0, step0 = MicroOp(READ_REG_A, WRITE_REG_A)),
    Operation(0x51, "MOV A B", 0, step0 = MicroOp(READ_REG_B, WRITE_REG_A)),
    Operation(0x52, "MOV A C", 0, step0 = MicroOp(READ_REG_C, WRITE_REG_A)),
    Operation(0x53, "MOV A D", 0, step0 = MicroOp(READ_REG_D, WRITE_REG_A)),
    Operation(0x54, "MOV B A", 0, step0 = MicroOp(READ_REG_A, WRITE_REG_B)),
    Operation(0x55, "MOV B B", 0, step0 = MicroOp(READ_REG_B, WRITE_REG_B)),
    Operation(0x56, "MOV B C", 0, step0 = MicroOp(READ_REG_C, WRITE_REG_B)),
    Operation(0x57, "MOV B D", 0, step0 = MicroOp(READ_REG_D, WRITE_REG_B)),
    Operation(0x58, "MOV C A", 0, step0 = MicroOp(READ_REG_A, WRITE_REG_C)),
    Operation(0x59, "MOV C B", 0, step0 = MicroOp(READ_REG_B, WRITE_REG_C)),
    Operation(0x5a, "MOV C C", 0, step0 = MicroOp(READ_REG_C, WRITE_REG_C)),
    Operation(0x5b, "MOV C D", 0, step0 = MicroOp(READ_REG_D, WRITE_REG_C)),
    Operation(0x5c, "MOV D A", 0, step0 = MicroOp(READ_REG_A, WRITE_REG_D)),
    Operation(0x5d, "MOV D B", 0, step0 = MicroOp(READ_REG_B, WRITE_REG_D)),
    Operation(0x5e, "MOV D C", 0, step0 = MicroOp(READ_REG_C, WRITE_REG_D)),
    Operation(0x5f, "MOV D D", 0, step0 = MicroOp(READ_REG_D, WRITE_REG_D)),
    Operation(0x60, "LOAD A (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL)),
    Operation(0x61, "LOAD B (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_LITERAL)),
    Operation(0x62, "LOAD C (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL)),
    Operation(0x63, "LOAD D (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_LITERAL)),
    Operation(0x64, "LOAD A (CD)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_REG_CD)),
    Operation(0x65, "LOAD B (CD)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_REG_CD)),
    Operation(0x66, "LOAD C (CD)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_REG_CD)),
    Operation(0x67, "LOAD D (CD)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_REG_CD)),
    // 0x68:6b
    Operation(0x6c, "LOAD A i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_A)),
    Operation(0x6d, "LOAD B i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_B)),
    Operation(0x6e, "LOAD C i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_C)),
    Operation(0x6f, "LOAD D i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_D)),
    Operation(0x70, "STORE A (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL)),
    Operation(0x71, "STORE B (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_LITERAL)),
    Operation(0x72, "STORE C (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL)),
    Operation(0x73, "STORE D (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_LITERAL)),
    Operation(0x74, "STORE A (CD)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_REG_CD)),
    Operation(0x75, "STORE B (CD)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_REG_CD)),
    Operation(0x76, "STORE C (CD)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_REG_CD)),
    Operation(0x77, "STORE D (CD)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_REG_CD)),

// management operations
    Operation(0xfe, "NOP", 0),
    Operation(0xff, "HALT", 0, halt = true),
)
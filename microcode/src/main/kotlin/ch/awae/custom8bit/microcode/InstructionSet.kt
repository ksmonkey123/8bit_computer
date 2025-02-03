package ch.awae.custom8bit.microcode

import ch.awae.custom8bit.microcode.AddressSource.*
import ch.awae.custom8bit.microcode.AddressTarget.*
import ch.awae.custom8bit.microcode.AluOperation.*
import ch.awae.custom8bit.microcode.DataSource.*
import ch.awae.custom8bit.microcode.DataTarget.*

val INSTRUCTION_SET: Set<Operation> = setOf(
    Operation(
        0x00, "ADC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x01, "ADC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x02, "ADC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x03, "ADC i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x04, "ADC (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x05, "ADC (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x06, "ADC (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x08, "SBC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x09, "SBC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0a, "SBC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0b, "SBC i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0c, "SBC (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0d, "SBC (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0e, "SBC (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x10, "AND B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x11, "AND C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x12, "AND D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x13, "AND i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x14, "AND (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x15, "AND (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x16, "AND (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x18,
        "IOR B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x19, "IOR C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1a, "IOR D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1b, "IOR i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1c, "IOR (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1d, "IOR (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1e, "IOR (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x20, "XOR B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x21, "XOR C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x22, "XOR D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x23, "XOR i", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x24, "XOR (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x25, "XOR (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x26, "XOR (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x28, "CMP B", 0, true,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x29, "CMP C", 0, true,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2a, "CMP D", 0, true,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2b, "CMP i", 1, true,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2c, "CMP (L)", 2, true,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2d, "CMP (CD + l)", 1, true,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2e, "CMP (SP + l)", 1, true,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x30, "DEC A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = DECREMENT),
    ),
    Operation(
        0x31, "DEC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = DECREMENT),
    ),
    Operation(
        0x32, "DEC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = DECREMENT),
    ),
    Operation(
        0x33, "DEC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = DECREMENT),
    ),
    Operation(
        0x34, "INC A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = INCREMENT),
    ),
    Operation(
        0x35, "INC B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = INCREMENT),
    ),
    Operation(
        0x36, "INC C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = INCREMENT),
    ),
    Operation(
        0x37, "INC D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = INCREMENT),
    ),
    Operation(
        0x38, "NEG A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = COMPLEMENT),
    ),
    Operation(
        0x39, "NEG B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = COMPLEMENT),
    ),
    Operation(
        0x3a, "NEG C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = COMPLEMENT),
    ),
    Operation(
        0x3b, "NEG D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = COMPLEMENT),
    ),
    Operation(
        0x3c, "NOT A", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x3d, "NOT B", 0,
        step0 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_B, action = INVERT),
    ),
    Operation(
        0x3e, "NOT C", 0,
        step0 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_C, action = INVERT),
    ),
    Operation(
        0x3f, "NOT D", 0,
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_D, action = INVERT),
    ),
    Operation(
        0x40, "RLC", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    Operation(
        0x41, "RL", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get top bit into carry (write to A necessary to not rewrite alu input)
        step1 = MicroOp(dataTarget = WRITE_REG_A, action = SHIFT_LEFT),
        // do real shift with carry to get it into the bottom bit
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    Operation(
        0x42, "RRC", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x43, "RR", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get bottom bit into carry (write to A necessary to not rewrite alu input)
        step1 = MicroOp(dataTarget = WRITE_REG_A, action = SHIFT_RIGHT),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x44, "RRA", 0, false,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get top bit into carry (write to A necessary to not rewrite alu input)
        step1 = MicroOp(dataTarget = WRITE_REG_A, action = SHIFT_LEFT),
        // real shift right with replicated top bit
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x45, "SWAP B", 0,
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
        0x46, "SWAP C", 0,
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
        0x47, "SWAP D", 0,
        // move D into ALU
        step0 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        // copy A to D
        step1 = MicroOp(READ_REG_A, WRITE_REG_D),
        // write !D to ALU
        step2 = MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!D (=D) to A
        step3 = MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    // 0x44:4f [12] available
    Operation(
        0x50, "LOAD A (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x51, "LOAD B (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x52, "LOAD C (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x53, "LOAD D (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x54, "STORE A (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x55, "STORE B (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x56, "STORE C (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x57, "STORE D (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    // 0x5c:5f [4] available
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
    Operation(0x70, "MOV A (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL)),
    Operation(0x71, "MOV B (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_LITERAL)),
    Operation(0x72, "MOV C (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL)),
    Operation(0x73, "MOV D (L)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_LITERAL)),
    Operation(
        0x74, "MOV A (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x75, "MOV B (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x76, "MOV C (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x77, "MOV D (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x78, "MOV A (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x79, "MOV B (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x7a, "MOV C (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x7b, "MOV D (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(0x7c, "MOV A i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_A)),
    Operation(0x7d, "MOV B i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_B)),
    Operation(0x7e, "MOV C i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_C)),
    Operation(0x7f, "MOV D i", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_D)),
    Operation(0x80, "STORE A (L)", 2, step0 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x81, "STORE B (L)", 2, step0 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x82, "STORE C (L)", 2, step0 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x83, "STORE D (L)", 2, step0 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_LITERAL)),
    Operation(
        0x84, "STORE A (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x85, "STORE B (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x86, "STORE C (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x87, "STORE D (CD + l)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x88, "STORE A (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x89, "STORE B (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8a, "STORE C (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8b, "STORE D (SP + l)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8c, "MOV (L) AB", 2,
        step0 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    Operation(
        0x8d, "MOV (L) CD", 2,
        step0 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_LITERAL),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    Operation(
        0x8e, "SALLOC i8", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(addressSource = ADR_INCREMENTER_OFFSET_NEGATIVE, action = WRITE_STACK_POINTER),
    ),
    Operation(
        0x8f, "SFREE i8", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(addressSource = ADR_INCREMENTER_OFFSET_POSITIVE, action = WRITE_STACK_POINTER),
    ),
    Operation(0x90, "MOV AB AB", 0),
    Operation(
        0x91, "MOV AB CD", 0,
        step0 = MicroOp(READ_REG_C, WRITE_REG_A),
        step1 = MicroOp(READ_REG_D, WRITE_REG_B),
    ),
    Operation(
        0x92, "MOV CD AB", 0,
        step0 = MicroOp(READ_REG_A, WRITE_REG_C),
        step1 = MicroOp(READ_REG_B, WRITE_REG_D),
    ),
    Operation(0x93, "MOV CD CD", 0),
    Operation(
        0x94, "MOV AB (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER),
    ),
    Operation(
        0x95, "MOV CD (L)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER),
    ),
    Operation(
        0x96, "MOV AB (CD + L)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER),
    ),
    Operation(
        0x97, "MOV CD (CD + L)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER),
    ),
    Operation(
        0x98, "MOV AB (SP + L)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER),
    ),
    Operation(
        0x99, "MOV CD (SP + L)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER),
    ),
    Operation(
        0x9a, "MOV AB i16", 2,
        step0 = MicroOp(READ_LITERAL_1, WRITE_REG_A),
        step1 = MicroOp(READ_LITERAL_2, WRITE_REG_B),
    ),
    Operation(
        0x9b, "MOV CD i16", 2,
        step0 = MicroOp(READ_LITERAL_1, WRITE_REG_C),
        step1 = MicroOp(READ_LITERAL_2, WRITE_REG_D),
    ),
    Operation(
        0x9c, "MOV (CD + L) AB", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    Operation(
        0x9d, "MOV (CD + L) CD", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    Operation(
        0x9e, "MOV (SP + L) AB", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    Operation(
        0x9f, "MOV (SP + L) CD", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER),
    ),
    // 0xa0:af [12] available
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
        0xb6, "BNP i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_POSITIVE),
    ),
    Operation(
        0xb7, "BNN i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_NEGATIVE),
    ),
    Operation(
        0xb8, "JMP i", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC)
    ),
    // 0xb9 [1] available
    Operation(
        0xba, "JSR i", 2,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_PC_HIGH, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT),
        step2 = MicroOp(READ_PC_LOW, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT, WRITE_STACK_POINTER),
        step3 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC),
    ),
    Operation(
        0xbb, "RET", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_PC_LOW, ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_PC_HIGH, ADR_INCREMENTER),
        step2 = MicroOp(addressSource = ADR_INCREMENTER, action = WRITE_STACK_POINTER),
    ),
    // 0xbc:bf [4] available
    // 0xc0:cf [16] available
    // 0xd0:df [16] available
    // 0xe0:ef [16] available
    // 0xf0:fb [12] available
    Operation(0xfc, "CFC", 0, false),
    Operation(0xfd, "CFS", 0, true),
    Operation(0xfe, "NOP", 0),
    Operation(0xff, "HLT", 0, halt = true),
)
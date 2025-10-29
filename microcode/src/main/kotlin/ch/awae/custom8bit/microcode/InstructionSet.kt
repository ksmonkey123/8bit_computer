package ch.awae.custom8bit.microcode

import ch.awae.custom8bit.microcode.AddressSource.*
import ch.awae.custom8bit.microcode.AddressTarget.WRITE_PC
import ch.awae.custom8bit.microcode.AddressTarget.WRITE_STACK_POINTER
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
        0x03, "ADC i8", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x04, "ADC (i16)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x05, "ADC (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x06, "ADC (SP + i8)", 1,
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
        0x0b, "SBC i8", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0c, "SBC (i16)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0d, "SBC (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0e, "SBC (SP + i8)", 1,
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
        0x13, "AND i8", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x14, "AND (i16)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x15, "AND (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x16, "AND (SP + i8)", 1,
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
        0x1b, "IOR i8", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1c, "IOR (i16)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1d, "IOR (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1e, "IOR (SP + i8)", 1,
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
        0x23, "XOR i8", 1,
        step0 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x24, "XOR (i16)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x25, "XOR (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x26, "XOR (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x28, "CMP B", 0,
        // set ALU input to 0xff
        step0 = MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        step1 = MicroOp(READ_ALU, action = SHIFT_LEFT),
        step2 = MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        step3 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x29, "CMP C", 0,
        // set ALU input to 0xff
        step0 = MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        step1 = MicroOp(READ_ALU, action = SHIFT_LEFT),
        step2 = MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        step3 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2a, "CMP D", 0,
        // set ALU input to 0xff
        step0 = MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        step1 = MicroOp(READ_ALU, action = SHIFT_LEFT),
        step2 = MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        step3 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2b, "CMP i8", 1,
        // set ALU input to 0xff
        step0 = MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        step1 = MicroOp(READ_ALU, action = SHIFT_LEFT),
        step2 = MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        step3 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2c, "CMP (i16)", 2,
        // set ALU input to 0xff
        step0 = MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        step1 = MicroOp(READ_ALU, action = SHIFT_LEFT),
        step2 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        step3 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2d, "CMP (CD + i8)", 1,
        // set ALU input to 0xff
        step0 = MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag. also move CD into incrementer
        step1 = MicroOp(READ_ALU, addressSource = ADR_REG_CD, action = SHIFT_LEFT),
        step2 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step3 = MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2e, "CMP (SP + i8)", 1,
        // set ALU input to 0xff
        step0 = MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag. also move SP into incrementer
        step1 = MicroOp(READ_ALU, addressSource = ADR_STACK_POINTER, action = SHIFT_LEFT),
        step2 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        step3 = MicroOp(READ_ALU, action = SUBTRACTION),
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
    // roll left through carry
    Operation(
        0x40, "RLC", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    // roll left without carry
    Operation(
        0x41, "RL", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get top bit into carry (write to A necessary to not rewrite alu input)
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
        // do real shift with carry to get it into the bottom bit
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    // roll right through carry
    Operation(
        0x42, "RRC", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    // roll right without carry
    Operation(
        0x43, "RR", 0,
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get bottom bit into carry (write to A necessary to not rewrite alu input)
        step1 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    // roll right arithmetic. (retain top bit)
    Operation(
        0x44, "RRA", 0,
        // execute ALU logic command to clear carry flag.
        step0 = MicroOp(READ_ALU, WRITE_ALU_INPUT, action = AND),
        step1 = MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get top bit into carry (write to A necessary to not rewrite alu input)
        step2 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
        // real shift right with replicated top bit
        step3 = MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x48, "SWAP B", 0,
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
        0x49, "SWAP C", 0,
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
        0x4a, "SWAP D", 0,
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
        0x4b, "SWAP (i16)", 2,
        // move MEM[L] into ALU
        step0 = MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        // copy A to MEM[L]
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL),
        // write !D to ALU
        step2 = MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!D (=D) to A
        step3 = MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x4c, "SWAP (CD + i8)", 1,
        // move A to ALU
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT, ADR_REG_CD),
        // move M to A
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        // move !A to ALU
        step2 = MicroOp(READ_ALU, WRITE_ALU_INPUT, ADR_REG_CD, action = INVERT),
        // move !!A (=A) to M
        step3 = MicroOp(READ_ALU, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE, action = INVERT)
    ),
    Operation(
        0x4d, "SWAP (SP + i8)", 1,
        // move A to ALU
        step0 = MicroOp(READ_REG_A, WRITE_ALU_INPUT, ADR_STACK_POINTER),
        // move M to A
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        // move !A to ALU
        step2 = MicroOp(READ_ALU, WRITE_ALU_INPUT, ADR_STACK_POINTER, action = INVERT),
        // move !!A (=A) to M
        step3 = MicroOp(READ_ALU, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE, action = INVERT)
    ),
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
    Operation(0x70, "MOV A (i16)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL)),
    Operation(0x71, "MOV B (i16)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_LITERAL)),
    Operation(0x72, "MOV C (i16)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL)),
    Operation(0x73, "MOV D (i16)", 2, step0 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_LITERAL)),
    Operation(
        0x74, "MOV A (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x75, "MOV B (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x76, "MOV C (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x77, "MOV D (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x78, "MOV A (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x79, "MOV B (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x7a, "MOV C (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x7b, "MOV D (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(0x7c, "MOV A i8", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_A)),
    Operation(0x7d, "MOV B i8", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_B)),
    Operation(0x7e, "MOV C i8", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_C)),
    Operation(0x7f, "MOV D i8", 1, step0 = MicroOp(READ_LITERAL_1, WRITE_REG_D)),
    Operation(0x80, "STORE A (i16)", 2, step0 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x81, "STORE B (i16)", 2, step0 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x82, "STORE C (i16)", 2, step0 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_LITERAL)),
    Operation(0x83, "STORE D (i16)", 2, step0 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_LITERAL)),
    Operation(
        0x84, "STORE A (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x85, "STORE B (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x86, "STORE C (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x87, "STORE D (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x88, "STORE A (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x89, "STORE B (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8a, "STORE C (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8b, "STORE D (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8c, "MOV (i16) AB", 2,
        step0 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL),
        step1 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x8d, "MOV (i16) CD", 2,
        step0 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_LITERAL),
        step1 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x8e, "SPA i8", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(addressSource = ADR_INCREMENTER_OFFSET_NEGATIVE, action = WRITE_STACK_POINTER),
    ),
    Operation(
        0x8f, "SPF i8", 1,
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
        0x94, "MOV AB (i16)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x95, "MOV CD (i16)", 2,
        step0 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x96, "MOV AB (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x97, "MOV CD (CD + i8)", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x98, "MOV AB (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x99, "MOV CD (SP + i8)", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_INCREMENT),
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
        0x9c, "MOV (CD + i8) AB", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x9d, "MOV (CD + i8) CD", 1,
        step0 = MicroOp(addressSource = ADR_REG_CD),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x9e, "MOV (SP + i8) AB", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x9f, "MOV (SP + i8) CD", 1,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        step2 = MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0xb0, "BCC i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.CARRY_CLEAR),
    ),
    Operation(
        0xb1, "BCS i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.CARRY_SET),
    ),
    Operation(
        0xb2, "BZ i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.ZERO),
    ),
    Operation(
        0xb3, "BNZ i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_ZERO),
    ),
    Operation(
        0xb4, "BLZ i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NEGATIVE),
    ),
    Operation(
        0xb5, "BGZ i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.POSITIVE),
    ),
    Operation(
        0xb6, "BNP i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_POSITIVE),
    ),
    Operation(
        0xb7, "BNN i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_NEGATIVE),
    ),
    Operation(
        0xb8, "JMP i16", 2,
        step0 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC)
    ),
    Operation(
        0xba, "JSR i16", 2,
        step0 = MicroOp(addressSource = ADR_STACK_POINTER),
        step1 = MicroOp(READ_PC_HIGH, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT),
        step2 = MicroOp(READ_PC_LOW, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT, WRITE_STACK_POINTER),
        step3 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC),
    ),
    Operation(
        0xbb, "RET", 0,
        step0 = MicroOp(READ_MEMORY, WRITE_LITERAL_1, ADR_STACK_POINTER),
        step1 = MicroOp(READ_MEMORY, WRITE_LITERAL_2, ADR_INCREMENTER_INCREMENT),
        step2 = MicroOp(addressSource = ADR_INCREMENTER_INCREMENT, action = WRITE_STACK_POINTER),
        step3 = MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC),
    ),
    Operation(
        0xfc, "CFC", 0,
        // read AND from ALU to get a '0' into the carry flag
        step0 = MicroOp(READ_ALU, WRITE_LITERAL_2, action = AND),
    ),
    Operation(
        0xfd, "CFS", 0,
        // move 0xff into ALU
        step0 = MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift to get a '1' into the carry flag.
        step1 = MicroOp(READ_ALU, WRITE_LITERAL_2, action = SHIFT_LEFT),
    ),
    Operation(0xfe, "NOP", 0),
    Operation(0xff, "HLT", 0,
        step0 = MicroOp(action= SequencerCommand.HALT)),
)
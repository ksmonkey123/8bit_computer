package ch.awae.custom8bit.microcode

import ch.awae.custom8bit.microcode.AddressSource.*
import ch.awae.custom8bit.microcode.AddressTarget.WRITE_PC
import ch.awae.custom8bit.microcode.AddressTarget.WRITE_STACK_POINTER
import ch.awae.custom8bit.microcode.AluOperation.*
import ch.awae.custom8bit.microcode.DataSource.*
import ch.awae.custom8bit.microcode.DataTarget.*

val INSTRUCTION_SET: Set<Operation> = setOf(
    Operation(
        0x00, "ADC B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x01, "ADC C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x02, "ADC D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x03, "ADC i8",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x04, "ADC (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x05, "ADC (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x06, "ADC (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = ADDITION),
    ),
    Operation(
        0x08, "SBC B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x09, "SBC C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0a, "SBC D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0b, "SBC i8",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0c, "SBC (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0d, "SBC (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x0e, "SBC (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = SUBTRACTION),
    ),
    Operation(
        0x10, "AND B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x11, "AND C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x12, "AND D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x13, "AND i8",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x14, "AND (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x15, "AND (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x16, "AND (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = AND),
    ),
    Operation(
        0x18,
        "IOR B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x19, "IOR C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1a, "IOR D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1b, "IOR i8",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1c, "IOR (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1d, "IOR (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x1e, "IOR (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = IOR),
    ),
    Operation(
        0x20, "XOR B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x21, "XOR C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x22, "XOR D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x23, "XOR i8",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x24, "XOR (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x25, "XOR (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x26, "XOR (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, WRITE_REG_A, action = XOR),
    ),
    Operation(
        0x28, "CMP B",
        MicroOp.WRITE_PC,
        // set ALU input to 0xff
        MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        MicroOp(READ_ALU, action = SHIFT_LEFT),
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x29, "CMP C",
        MicroOp.WRITE_PC,
        // set ALU input to 0xff
        MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        MicroOp(READ_ALU, action = SHIFT_LEFT),
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2a, "CMP D",
        MicroOp.WRITE_PC,
        // set ALU input to 0xff
        MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        MicroOp(READ_ALU, action = SHIFT_LEFT),
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2b, "CMP i8",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        // set ALU input to 0xff
        MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        MicroOp(READ_ALU, action = SHIFT_LEFT),
        MicroOp(READ_LITERAL_1, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2c, "CMP (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        // set ALU input to 0xff
        MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag.
        MicroOp(READ_ALU, action = SHIFT_LEFT),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2d, "CMP (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        // set ALU input to 0xff
        MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag. also move CD into incrementer
        MicroOp(READ_ALU, addressSource = ADR_REG_CD, action = SHIFT_LEFT),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x2e, "CMP (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        // set ALU input to 0xff
        MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift left to get a '1' into the carry flag. also move SP into incrementer
        MicroOp(READ_ALU, addressSource = ADR_STACK_POINTER, action = SHIFT_LEFT),
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_ALU, action = SUBTRACTION),
    ),
    Operation(
        0x30, "DEC A",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = DECREMENT),
    ),
    Operation(
        0x31, "DEC B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_B, action = DECREMENT),
    ),
    Operation(
        0x32, "DEC C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_C, action = DECREMENT),
    ),
    Operation(
        0x33, "DEC D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_D, action = DECREMENT),
    ),
    Operation(
        0x34, "INC A",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = INCREMENT),
    ),
    Operation(
        0x35, "INC B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_B, action = INCREMENT),
    ),
    Operation(
        0x36, "INC C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_C, action = INCREMENT),
    ),
    Operation(
        0x37, "INC D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_D, action = INCREMENT),
    ),
    Operation(
        0x38, "NEG A",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = COMPLEMENT),
    ),
    Operation(
        0x39, "NEG B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_B, action = COMPLEMENT),
    ),
    Operation(
        0x3a, "NEG C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_C, action = COMPLEMENT),
    ),
    Operation(
        0x3b, "NEG D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_D, action = COMPLEMENT),
    ),
    Operation(
        0x3c, "NOT A",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x3d, "NOT B",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_B, action = INVERT),
    ),
    Operation(
        0x3e, "NOT C",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_C, action = INVERT),
    ),
    Operation(
        0x3f, "NOT D",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_D, action = INVERT),
    ),
    // roll left through carry
    Operation(
        0x40, "RLC",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    // roll left without carry
    Operation(
        0x41, "RL",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get top bit into carry (write to A necessary to not rewrite alu input)
        MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
        // do real shift with carry to get it into the bottom bit
        MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
    ),
    // roll right through carry
    Operation(
        0x42, "RRC",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    // roll right without carry
    Operation(
        0x43, "RR",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get bottom bit into carry (write to A necessary to not rewrite alu input)
        MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
        MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    // roll right arithmetic. (retain top bit)
    Operation(
        0x44, "RRA",
        MicroOp.WRITE_PC,
        // execute ALU logic command to clear carry flag.
        MicroOp(READ_ALU, WRITE_ALU_INPUT, action = AND),
        MicroOp(READ_REG_A, WRITE_ALU_INPUT),
        // shift once to get top bit into carry (write to A necessary to not rewrite alu input)
        MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_LEFT),
        // real shift right with replicated top bit
        MicroOp(READ_ALU, WRITE_REG_A, action = SHIFT_RIGHT),
    ),
    Operation(
        0x48, "SWAP B",
        MicroOp.WRITE_PC,
        // move B into ALU
        MicroOp(READ_REG_B, WRITE_ALU_INPUT),
        // copy A to B
        MicroOp(READ_REG_A, WRITE_REG_B),
        // write !B to ALU
        MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!B (=B) to A
        MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x49, "SWAP C",
        MicroOp.WRITE_PC,
        // move C into ALU
        MicroOp(READ_REG_C, WRITE_ALU_INPUT),
        // copy A to C
        MicroOp(READ_REG_A, WRITE_REG_C),
        // write !C to ALU
        MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!C (=C) to A
        MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x4a, "SWAP D",
        MicroOp.WRITE_PC,
        // move D into ALU
        MicroOp(READ_REG_D, WRITE_ALU_INPUT),
        // copy A to D
        MicroOp(READ_REG_A, WRITE_REG_D),
        // write !D to ALU
        MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!D (=D) to A
        MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x4b, "SWAP (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        // move MEM[L] into ALU
        MicroOp(READ_MEMORY, WRITE_ALU_INPUT, ADR_LITERAL),
        // copy A to MEM[L]
        MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL),
        // write !D to ALU
        MicroOp(READ_ALU, WRITE_ALU_INPUT, action = INVERT),
        // write !!D (=D) to A
        MicroOp(READ_ALU, WRITE_REG_A, action = INVERT),
    ),
    Operation(
        0x4c, "SWAP (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        // move A to ALU
        MicroOp(READ_REG_A, WRITE_ALU_INPUT, ADR_REG_CD),
        // move M to A
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        // move !A to ALU
        MicroOp(READ_ALU, WRITE_ALU_INPUT, ADR_REG_CD, action = INVERT),
        // move !!A (=A) to M
        MicroOp(READ_ALU, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE, action = INVERT)
    ),
    Operation(
        0x4d, "SWAP (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        // move A to ALU
        MicroOp(READ_REG_A, WRITE_ALU_INPUT, ADR_STACK_POINTER),
        // move M to A
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        // move !A to ALU
        MicroOp(READ_ALU, WRITE_ALU_INPUT, ADR_STACK_POINTER, action = INVERT),
        // move !!A (=A) to M
        MicroOp(READ_ALU, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE, action = INVERT)
    ),
    Operation(
        0x50, "LOAD A (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x51, "LOAD B (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x52, "LOAD C (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x53, "LOAD D (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x54, "STORE A (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x55, "STORE B (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x56, "STORE C (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x57, "STORE D (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(0x60, "MOV A A", MicroOp.WRITE_PC, MicroOp(READ_REG_A, WRITE_REG_A)),
    Operation(0x61, "MOV A B", MicroOp.WRITE_PC, MicroOp(READ_REG_B, WRITE_REG_A)),
    Operation(0x62, "MOV A C", MicroOp.WRITE_PC, MicroOp(READ_REG_C, WRITE_REG_A)),
    Operation(0x63, "MOV A D", MicroOp.WRITE_PC, MicroOp(READ_REG_D, WRITE_REG_A)),
    Operation(0x64, "MOV B A", MicroOp.WRITE_PC, MicroOp(READ_REG_A, WRITE_REG_B)),
    Operation(0x65, "MOV B B", MicroOp.WRITE_PC, MicroOp(READ_REG_B, WRITE_REG_B)),
    Operation(0x66, "MOV B C", MicroOp.WRITE_PC, MicroOp(READ_REG_C, WRITE_REG_B)),
    Operation(0x67, "MOV B D", MicroOp.WRITE_PC, MicroOp(READ_REG_D, WRITE_REG_B)),
    Operation(0x68, "MOV C A", MicroOp.WRITE_PC, MicroOp(READ_REG_A, WRITE_REG_C)),
    Operation(0x69, "MOV C B", MicroOp.WRITE_PC, MicroOp(READ_REG_B, WRITE_REG_C)),
    Operation(0x6a, "MOV C C", MicroOp.WRITE_PC, MicroOp(READ_REG_C, WRITE_REG_C)),
    Operation(0x6b, "MOV C D", MicroOp.WRITE_PC, MicroOp(READ_REG_D, WRITE_REG_C)),
    Operation(0x6c, "MOV D A", MicroOp.WRITE_PC, MicroOp(READ_REG_A, WRITE_REG_D)),
    Operation(0x6d, "MOV D B", MicroOp.WRITE_PC, MicroOp(READ_REG_B, WRITE_REG_D)),
    Operation(0x6e, "MOV D C", MicroOp.WRITE_PC, MicroOp(READ_REG_C, WRITE_REG_D)),
    Operation(0x6f, "MOV D D", MicroOp.WRITE_PC, MicroOp(READ_REG_D, WRITE_REG_D)),
    Operation(
        0x70,
        "MOV A (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL)
    ),
    Operation(
        0x71,
        "MOV B (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_REG_B, ADR_LITERAL)
    ),
    Operation(
        0x72,
        "MOV C (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL)
    ),
    Operation(
        0x73,
        "MOV D (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_REG_D, ADR_LITERAL)
    ),
    Operation(
        0x74, "MOV A (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x75, "MOV B (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x76, "MOV C (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x77, "MOV D (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x78, "MOV A (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x79, "MOV B (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x7a, "MOV C (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x7b, "MOV D (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(0x7c, "MOV A i8", MicroOp.FETCH_L1, MicroOp.WRITE_PC, MicroOp(READ_LITERAL_1, WRITE_REG_A)),
    Operation(0x7d, "MOV B i8", MicroOp.FETCH_L1, MicroOp.WRITE_PC, MicroOp(READ_LITERAL_1, WRITE_REG_B)),
    Operation(0x7e, "MOV C i8", MicroOp.FETCH_L1, MicroOp.WRITE_PC, MicroOp(READ_LITERAL_1, WRITE_REG_C)),
    Operation(0x7f, "MOV D i8", MicroOp.FETCH_L1, MicroOp.WRITE_PC, MicroOp(READ_LITERAL_1, WRITE_REG_D)),
    Operation(
        0x80,
        "STORE A (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL)
    ),
    Operation(
        0x81,
        "STORE B (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_B, WRITE_MEMORY, ADR_LITERAL)
    ),
    Operation(
        0x82,
        "STORE C (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_MEMORY, ADR_LITERAL)
    ),
    Operation(
        0x83,
        "STORE D (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_D, WRITE_MEMORY, ADR_LITERAL)
    ),
    Operation(
        0x84, "STORE A (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x85, "STORE B (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x86, "STORE C (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x87, "STORE D (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x88, "STORE A (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x89, "STORE B (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8a, "STORE C (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8b, "STORE D (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE)
    ),
    Operation(
        0x8c, "MOV (i16) AB",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_MEMORY, ADR_LITERAL),
        MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x8d, "MOV (i16) CD",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_MEMORY, ADR_LITERAL),
        MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x8e, "SPA i8",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(addressSource = ADR_INCREMENTER_OFFSET_NEGATIVE, action = WRITE_STACK_POINTER),
    ),
    Operation(
        0x8f, "SPF i8",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(addressSource = ADR_INCREMENTER_OFFSET_POSITIVE, action = WRITE_STACK_POINTER),
    ),
    Operation(0x90, "MOV AB AB", MicroOp.WRITE_PC),
    Operation(
        0x91, "MOV AB CD",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_C, WRITE_REG_A),
        MicroOp(READ_REG_D, WRITE_REG_B),
    ),
    Operation(
        0x92, "MOV CD AB",
        MicroOp.WRITE_PC,
        MicroOp(READ_REG_A, WRITE_REG_C),
        MicroOp(READ_REG_B, WRITE_REG_D),
    ),
    Operation(0x93, "MOV CD CD", MicroOp.WRITE_PC),
    Operation(
        0x94, "MOV AB (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_LITERAL),
        MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x95, "MOV CD (i16)",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_REG_C, ADR_LITERAL),
        MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x96, "MOV AB (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x97, "MOV CD (CD + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x98, "MOV AB (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_A, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_MEMORY, WRITE_REG_B, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x99, "MOV CD (SP + i8)",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_REG_C, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_MEMORY, WRITE_REG_D, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x9a, "MOV AB i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_LITERAL_1, WRITE_REG_A),
        MicroOp(READ_LITERAL_2, WRITE_REG_B),
    ),
    Operation(
        0x9b, "MOV CD i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(READ_LITERAL_1, WRITE_REG_C),
        MicroOp(READ_LITERAL_2, WRITE_REG_D),
    ),
    Operation(
        0x9c, "MOV (CD + i8) AB",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x9d, "MOV (CD + i8) CD",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_REG_CD),
        MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x9e, "MOV (SP + i8) AB",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_A, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_REG_B, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0x9f, "MOV (SP + i8) CD",
        MicroOp.FETCH_L1,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_REG_C, WRITE_MEMORY, ADR_INCREMENTER_OFFSET_POSITIVE),
        MicroOp(READ_REG_D, WRITE_MEMORY, ADR_INCREMENTER_INCREMENT),
    ),
    Operation(
        0xb0, "BCC i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.CARRY_CLEAR),
    ),
    Operation(
        0xb1, "BCS i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.CARRY_SET),
    ),
    Operation(
        0xb2, "BZ i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.ZERO),
    ),
    Operation(
        0xb3, "BNZ i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_ZERO),
    ),
    Operation(
        0xb4, "BLZ i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NEGATIVE),
    ),
    Operation(
        0xb5, "BGZ i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.POSITIVE),
    ),
    Operation(
        0xb6, "BNP i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_POSITIVE),
    ),
    Operation(
        0xb7, "BNN i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC).condition(Condition.NOT_NEGATIVE),
    ),
    Operation(
        0xb8, "JMP i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC)
    ),
    Operation(
        0xba, "JSR i16",
        MicroOp.FETCH_L1,
        MicroOp.FETCH_L2,
        MicroOp.WRITE_PC,
        MicroOp(addressSource = ADR_STACK_POINTER),
        MicroOp(READ_PC_HIGH, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT),
        MicroOp(READ_PC_LOW, WRITE_MEMORY, ADR_INCREMENTER_DECREMENT, WRITE_STACK_POINTER),
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC),
    ),
    Operation(
        0xbb, "RET",
        MicroOp.WRITE_PC,
        MicroOp(READ_MEMORY, WRITE_LITERAL_1, ADR_STACK_POINTER),
        MicroOp(READ_MEMORY, WRITE_LITERAL_2, ADR_INCREMENTER_INCREMENT),
        MicroOp(addressSource = ADR_INCREMENTER_INCREMENT, action = WRITE_STACK_POINTER),
        MicroOp(addressSource = ADR_LITERAL, action = WRITE_PC),
    ),
    Operation(
        0xfc, "CFC",
        MicroOp.WRITE_PC,
        // read AND from ALU to get a '0' into the carry flag
        MicroOp(READ_ALU, WRITE_LITERAL_2, action = AND),
    ),
    Operation(
        0xfd, "CFS",
        MicroOp.WRITE_PC,
        // move 0xff into ALU
        MicroOp(dataTarget = WRITE_ALU_INPUT),
        // shift to get a '1' into the carry flag.
        MicroOp(READ_ALU, WRITE_LITERAL_2, action = SHIFT_LEFT),
    ),
    Operation(0xfe, "NOP", MicroOp.WRITE_PC),
    Operation(
        0xff, "HLT", MicroOp(action = SequencerCommand.HALT)
    ),
)
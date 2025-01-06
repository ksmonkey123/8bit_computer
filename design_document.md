DESIGN DOCUMENT
===============

## Registers

### General Purpose

The computer contains 4 general purpose 8-bit registers named `regA` through `regD`

* `regA` and `regB` are connected to the ALU.
* `regC` and `regD` can be used for indirect memory addressing.

### Literal Argument Register

The `regL` literal register can hold a one-byte literal opcode extension.
This register cannot be directly adressed and is only interacted with through microcode.

## ALU Design

The ALU is split into 3 parts: The _arithmetic_, _logic_ and _roll_ units.
All 3 units operate on regA and optionally regB.

The calculation result can be stored in registers A through D

### Arithmetic Unit

The arithmetic unit is controlled by a 3 bit control bus defining the operation.

| Operation | Mnemonic | Result      | Description          |
|----------:|:---------|:------------|:---------------------|
|       000 | DEC      | A + 255 + 0 | Decrement            |
|       001 | DECC     | A + 255 + C | Decrement with Carry |
|       010 | INC      | A + 0 + 1   | Increment            |
|       011 | INCC     | A + 0 + C   | Increment with Carry |
|       100 | ADD      | A + B + 0   | Add                  |
|       101 | ADDC     | A + B + C   | Add with Carry       |
|       110 | SUB      | A + !B + 1  | Subtract             |
|       111 | SUBC     | A + !B + C  | Subtract with Carry  |

#### Opcode structure

* Bit 2 indicates if the input from `regB` should be used.
    * If not, the intermediate bus must be pulled high. (yielding a value of `255`)
* Bit 1 indicates if input B (`regB` or `255`) should be bitwise inverted.
* Bit 0 indicates if the carry flag is used as an input.
    * If not, the input (0, 1) is taken from bit 1.

### Logic Unit

| Operation | Mnemonic | Result   | Description                       |
|----------:|:---------|:---------|:----------------------------------|
|       000 | IDENT    | A        | Identity Operation                |
|       001 | AND      | A * B    | Bitwise And                       |
|       010 | IOR      | A + B    | Bitwise Inclusive Or              |
|       011 | XOR      | A != B   | Bitwise Exclusive Or              |
|       100 | NOT      | !A       | Bitwise Unary Not                 |
|       101 | NAND     | !(A * B) | Bitwise Not And                   |
|       110 | INOR     | !(A + B) | Bitwise Inclusive Not Or          |
|       111 | XNOR     | A == B   | Bitwise Equals (Exclusive Not OR) |

#### Opcode structure

* Bits 1 and 2 select the operation to perform:
    * `00`: Identity of `regA`
    * `01`: AND
    * `10`: IOR
    * `11`: XOR
* Bit 0 indicates if the output should be inverted.

### Roll Unit

The roll unit implements 4 operations that did not fit the ALU:

* `shl`: roll left through carry
* `shr`: roll right through carry
* `swp`: swap high and low nibbles
* `neg`: calculate 2's complement

| Operation | Mnemonic | Result                                | Description                               |
|----------:|:---------|:--------------------------------------|:------------------------------------------|
|        00 | swp      | ((A & 0x0f) << 4) + ((A & 0xf0) >> 4) | Nibble Swap (high input bit as Carry-Out) |
|        01 | shl      | (A << 1) + C                          | Roll Left through Carry                   |
|        10 | shr      | (A >> 1) + (C << 8)                   | Roll Right through Carry                  |
|        11 | neg      | !A + 1                                | 2s Complement                             |

### ALU integration

All ALU sections always active.
The `A`-Side of the ALU is always fed with the current value of `regA`.
The `B`-Side of the ALU can be fed by both `regB` and `regL`.

2 additional lines:

| Operation | ALU Mode | Registers         | Notes                                                    |
|:----------|:---------|:------------------|:---------------------------------------------------------|
| 00xxx     | Logic    | `regA` and `regB` |                                                          |
| 01xxx     | Math     | `regA` and `regB` |                                                          |
| 10xxx     | Logic    | `regA` and `regL` | operations `000` and `100` redundant                     |
| 110xx     | Roll     | `regA`            |                                                          |
| 11011     | Math*    | `regA`            | 2's complement calculation Address in Range of Roll unit |
| 111xx     | Math     | `regA` and `regL` | only higher-order 2-argument functions implemented       |

#### Rules:

* Logic Unit is active when bit 3 is `0`
* Math Unit is active when bit 3 is `1`, unless command is `11000`, `11001` or `11010`
    * `regL` used instead of `regB` when bit 4 is `1`
    * `regA` is inverted if command is `11011`
* Roll Unit is active when command is `11000`, `11001` or `11010`

## Instruction Set

### Register Ids

Register Addresses are represented as 2-bit numbers:

| Bits | Register |
|:-----|:---------|
| `00` | A        |
| `11` | B        |
| `01` | C        |
| `10` | D        |

### Table

| Operation   |   Fetch | Mnemonic      | Flags | Description                                        |
|:------------|--------:|:--------------|:------|:---------------------------------------------------|
| `0000.00xx` | 0 bytes | IDENT regX    | Z,N   | Identity Function                                  |
| `0000.01xx` | 0 bytes | AND regX      | Z,N   | Bitwise And                                        |
| `0000.10xx` | 0 bytes | IOR regX      | Z,N   | Bitwise Inclusive Or                               |
| `0000.11xx` | 0 bytes | XOR regX      | Z,N   | Bitwise Exclusive Or                               |
| `0001.00xx` | 0 bytes | NOT regX      | Z,N   | Bitwise Complement                                 |
| `0001.01xx` | 0 bytes | NAND regX     | Z,N   | Bitwise Not-And                                    |
| `0001.10xx` | 0 bytes | NIOR regX     | Z,N   | Bitwise Inclusive Not-Or                           |
| `0001.11xx` | 0 bytes | NXOR regX     | Z,N   | Bitwise Exclusive Not-Or                           |
| `0010.00xx` | 0 bytes | DEC regX      | Z,N,C | Decrement                                          |
| `0010.01xx` | 0 bytes | DECC regX     | Z,N,C | Decrement with Carry-In                            |
| `0010.10xx` | 0 bytes | INC regX      | Z,N,C | Increment                                          |
| `0010.11xx` | 0 bytes | INCC regX     | Z,N,C | Increment with Carry-In                            |
| `0011.00xx` | 0 bytes | ADD regX      | Z,N,C | Addition                                           |
| `0011.01xx` | 0 bytes | ADDC regX     | Z,N,C | Addition with Carry-In                             |
| `0011.10xx` | 0 bytes | SUB regX      | Z,N,C | Subtraction                                        |
| `0011.11xx` | 0 bytes | SUBC regX     | Z,N,C | Subtraction with Carry-In                          |
| `0100.01xx` |  1 byte | LAND regX     | Z,N   | Bitwise And with Literal                           |
| `0100.10xx` |  1 byte | LIOR regX     | Z,N   | Bitwise Inclusive Or with Literal                  |
| `0100.11xx` |  1 byte | LXOR regX     | Z,N   | Bitwise Exclusive Or with Literal                  |
| `0101.01xx` |  1 byte | LNAND regX    | Z,N   | Bitwise Not-And with Literal                       |
| `0101.10xx` |  1 byte | LNIOR regX    | Z,N   | Bitwise Inclusive Not-Or with Literal              |
| `0101.11xx` |  1 byte | LNXOR regX    | Z,N   | Bitwise Exclusive Not-Or with Literal              |
| `0110.00xx` | 0 bytes | NSWP regX     | Z,N,C | Nibble Swap                                        |
| `0110.01xx` | 0 bytes | SHL regX      | Z,N,C | Roll Left through Carry                            |
| `0110.10xx` | 0 bytes | SHR regX      | Z,N,C | Roll Right through Carry                           |
| `0110.11xx` | 0 bytes | COMPC regX    | Z,N,C | 2s Complement with Carry                           |
| `0111.00xx` |  1 byte | LADD regX     | Z,N,C | Literal Addition                                   |
| `0111.01xx` |  1 byte | LADDC regX    | Z,N,C | Literal Addition with Carry-In                     |
| `0111.10xx` |  1 byte | LSUB regX     | Z,N,C | Literal Subtraction                                |
| `0111.11xx` |  1 byte | LSUBC regX    | Z,N,C | Literal Subtraction with Carry-In                  |
| `1000.00xx` | 0 bytes | ILOAD regX    | Z,N   | Indirect Memory Load (Address in registers CD)     |
| `1000.01xx` | 0 bytes | CLR regX      | Z,N   | Register Clear                                     |
| `1001.xx00` | 0 bytes | ISTORE regX   | -     | Indirect Memory Load (Address in registers CD)     |
| `1010.00xx` |  1 byte | LLOAD regX    | Z,N   | Literal Load                                       |
| `1011.yyxx` | 0 bytes | MOV regX regY | Z,N   | Register Copy from regY to regX                    |
| `1100.00xx` | 2 bytes | MLOAD regX    | Z,N   | Memory Load from Literal Address                   |
| `1101.xx00` | 2 bytes | MSTORE regX   | -     | Memory Write to Literal Address                    |
| `1110.0000` | 0 bytes | DYNBX         | -     | Dynamic Branching (Target Address in registers CD) |
| `1110.1110` | 0 bytes | CLRC          | C     | Clear Carry Flag                                   |
| `1110.1111` | 0 bytes | SETC          | C     | Set Carry Flag                                     |
| `1111.0000` | 2 bytes | BXC           |       | conditionally branch if Carry                      |
| `1111.0001` | 2 bytes | BXZ           |       | conditionally branch if Zero                       |
| `1111.0010` | 2 bytes | BXN           |       | conditionally branch if Negative                   |
| `1111.0011` | 2 bytes | BXP           |       | conditionally branch if Positive                   |
| `1111.0100` | 2 bytes | BXNC          |       | conditionally branch if not Carry                  |
| `1111.0101` | 2 bytes | BXNZ          |       | conditionally branch if not Zero                   |
| `1111.0110` | 2 bytes | BXNN          |       | conditionally branch if not Negative               |
| `1111.0111` | 2 bytes | BXNP          |       | conditionally branch if not Positive               |
| `1111.1000` | 2 bytes | BX            |       | branch to literal address                          |

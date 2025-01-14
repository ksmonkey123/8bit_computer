DESIGN DOCUMENT
===============

## Registers

### General Purpose

The computer contains 4 general purpose 8-bit registers named `regA` through `regD`

* `regA` and `regB` are connected to the ALU.
* `regC` and `regD` can be used for indirect memory addressing.

### Literal Argument Register

The `regL` literal register can hold a one-byte literal opcode extension.
This register cannot be directly addressed and is only interacted with through microcode.

## ALU Design

The ALU is split into 3 parts: The _arithmetic_, _logic_ and _roll_ units.
All 3 units operate on regA and optionally regB.

The calculation result can be stored in registers A through D

### Arithmetic Unit

The arithmetic unit is controlled by a 2 bit control bus defining the operation.

| Operation | Mnemonic | Result      | Description          |
|----------:|:---------|:------------|:---------------------|
|        00 | DECC     | A + 255 + C | Decrement with Carry |
|        01 | INCC     | A + 0 + C   | Increment with Carry |
|        10 | ADDC     | A + B + C   | Add with Carry       |
|        11 | SUBC     | A + !B + C  | Subtract with Carry  |

#### Opcode structure

* Bit 1 indicates if the input from `regB` should be used.
    * If not, the intermediate bus must be pulled high. (yielding a value of `255`)
* Bit 0 indicates if input B (`regB` or `255`) should be bitwise inverted.

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

The roll unit implements 3 operations that did not fit the ALU:

* `shl`: roll left through carry
* `shr`: roll right through carry
* `swp`: swap high and low nibbles

| Operation | Mnemonic | Result                                | Description                                               |
|----------:|:---------|:--------------------------------------|:----------------------------------------------------------|
|        00 | swp      | ((A & 0x0f) << 4) + ((A & 0xf0) >> 4) | Nibble Swap (high input bit as Carry-Out)                 |
|        01 | comp     | no output                             | reserved for 2s complement calculation in arithmetic unit |
|        10 | shl      | (A << 1) + C                          | Roll Left through Carry                                   |
|        11 | shr      | (A >> 1) + (C << 8)                   | Roll Right through Carry                                  |

### ALU integration

All ALU sections always active.
The `A`-Side of the ALU is always fed with the current value of `regA`.
The `B`-Side of the ALU can be fed by both `regB` and `regL`.

2 additional lines:

| Operation | ALU Mode | Registers         | Notes                                |
|:----------|:---------|:------------------|:-------------------------------------|
| 00xxx     | Logic    | `regA` and `regB` | operation `000` unused               |
| 010xx     | Math     | `regA` and `regB` |                                      |
| 01101     | Math*    | `regA` and `regB` | 2s complement                        |
| 011xx     | Roll     | `regA` and `regB` |                                      |
| 10xxx     | Logic    | `regA` and `regL` | operations `000` and `100` redundant |
| 10xxx     | Logic    | `regA` and `regL` | operation `000` unused               |
| 110xx     | Math     | `regA` and `regL` | operations `00` and `01` redundant   |
| 11101     | Math*    | `regA` and `regL` | 2s complement. redundant             |
| 111xx     | Roll     | `regA` and `regL` | redundant                            |

#### Rules:

* Logic Unit is active when bit 3 is `0`
* Math Unit is active when bit 3 is `1`, unless command is `x1100`, `x1110` or `x1111`
    * `regL` used instead of `regB` when bit 4 is `1`
    * `regA` is inverted if command is `x1101`
* Roll Unit is active when command is `x1100`, `x1110`, `x1111`

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
| `0000.00xx` |       0 | NSWP regX     | Z,N,C | Nibble Swap                                        |
| `0000.01xx` |       0 | SHL regX      | Z,N,C | Roll Left through Carry                            |
| `0000.10xx` |       0 | SHR regX      | Z,N,C | Roll Right through Carry                           |
| `0001.00xx` |       0 | DECC regX     | Z,N,C | Decrement with Carry-In                            |
| `0001.01xx` |       0 | INCC regX     | Z,N,C | Increment with Carry-In                            |
| `0001.10xx` |       0 | ADDC regX     | Z,N,C | Addition with Carry-In                             |
| `0001.11xx` |       0 | SUBC regX     | Z,N,C | Subtraction with Carry-In                          |
| `0010.01xx` |       0 | AND regX      | Z,N   | Bitwise And                                        |
| `0010.10xx` |       0 | IOR regX      | Z,N   | Bitwise Inclusive Or                               |
| `0010.11xx` |       0 | XOR regX      | Z,N   | Bitwise Exclusive Or                               |
| `0011.00xx` |       0 | NOT regX      | Z,N   | Bitwise Complement                                 |
| `0011.01xx` |       0 | NAND regX     | Z,N   | Bitwise Not-And                                    |
| `0011.10xx` |       0 | INOR regX     | Z,N   | Bitwise Inclusive Not-Or                           |
| `0011.11xx` |       0 | XNOR regX     | Z,N   | Bitwise Exclusive Not-Or                           |
| `0100.00xx` |       1 | LADD reg      | Z,N,C | Literal Addition                                   |
| `0100.01xx` |       1 | LSUB regX     | Z,N,C | Literal Subtraction                                |
| `0100.10xx` |       1 | LADDC regX    | Z,N,C | Literal Addition with Carry-In                     |
| `0100.11xx` |       1 | LSUBC regX    | Z,N,C | Literal Subtraction with Carry-In                  |
| `0101.00xx` |       0 | COMPC regX    | Z,N,C | 2s Complement with Carry                           |
| `0101.01xx` |       1 | LAND regX     | Z,N   | Bitwise And with Literal                           |
| `0101.10xx` |       1 | LIOR regX     | Z,N   | Bitwise Inclusive Or with Literal                  |
| `0101.11xx` |       1 | LXOR regX     | Z,N   | Bitwise Exclusive Or with Literal                  |
| `0110.00xx` |       0 | COMP regX     | Z,N,C | 2s Complement                                      |
| `0110.01xx` |       1 | LNAND regX    | Z,N   | Bitwise Not-And with Literal                       |
| `0110.10xx` |       1 | LINOR regX    | Z,N   | Bitwise Inclusive Not-Or with Literal              |
| `0110.11xx` |       1 | LXNOR regX    | Z,N   | Bitwise Exclusive Not-Or with Literal              |
| `0111.00xx` |       0 | DEC regX      | Z,N,C | Decrement                                          |
| `0111.10xx` |       0 | INC regX      | Z,N,C | Increment                                          |
| `0111.00xx` |       0 | ADD regX      | Z,N,C | Addition                                           |
| `0111.10xx` |       0 | SUB regX      | Z,N,C | Subtraction                                        |
| `1000.yyxx` |       0 | MOV regX regY | Z,N   | Register Copy from regY to regX                    |
| `1001.00xx` |       2 | LOAD regX     | Z,N   | Memory Load from Literal Address                   |
| `1001.01xx` |       0 | ILOAD regX    | Z,N   | Indirect Memory Load (Address in registers CD)     |
| `1001.10xx` |       1 | LLOAD regX    | Z,N   | Literal Load                                       |
| `1001.11xx` |       0 | CLR regX      | Z,N   | Register Clear                                     |
| `1010.xx00` |       2 | STORE regX    | -     | Memory Write to Literal Address                    |
| `1010.xx01` |       0 | ISTORE regX   | -     | Indirect Memory Load (Address in registers CD)     |
| `1011.0000` | 2 bytes | BRCS          | -     | conditionally branch if carry set                  |
| `1011.0001` | 2 bytes | BRCC          | -     | conditionally branch if carry not set              |
| `1011.0010` | 2 bytes | BRZ           | -     | conditionally branch if zero                       |
| `1011.0011` | 2 bytes | BRNZ          | -     | conditionally branch if not zero                   |
| `1011.0100` | 2 bytes | BRLZ          | -     | conditionally branch if negative                   |
| `1011.0101` | 2 bytes | BRGZ          | -     | conditionally branch if positive                   |
| `1011.0110` | 2 bytes | BRNLZ         | -     | conditionally branch if not negative               |
| `1011.0111` | 2 bytes | BRNGZ         | -     | conditionally branch if not positive               |
| `1011.1000` | 2 bytes | GOTO          | -     | branch to literal address                          |
| `1011.1001` | 0 bytes | RGOTO         | -     | Dynamic Branching (Target Address in registers CD) |
| `1111.000x` | 0 bytes | SETC x        | C     | Set/Clear Carry Flag                               |
| `1111.1111` | 0 bytes | NOP           | -     | no-op                                              |

Simple ALU
==========

A Simplified ALU does not need to manage the carry flag on its own.
This reduces the total amount of operations needed to 16:

| Operation | Mnemonic | Result                                | Description                               |
|----------:|:---------|:--------------------------------------|:------------------------------------------|
|   00 00xx | NSWP     | ((A & 0x0f) << 4) + ((A & 0xf0) >> 4) | Nibble Swap (high input bit as Carry-Out) |
|   00 01xx | SHL      | (A << 1) + C                          | Roll Left through Carry                   |
|   00 10xx | SHR      | (A >> 1) + (C << 8)                   | Roll Right through Carry                  |
|   00 11xx | CMP      | !A + C                                | 2s Complement with Carry                  |
|   01 00xx | DEC      | A + 255 + C                           | Decrement with Carry                      |
|   01 01xx | INC      | A + 0 + C                             | Increment with Carry                      |
|   01 10xx | ADD      | A + B + C                             | Add with Carry                            |
|   01 11xx | SUB      | A + !B + C                            | Subtract with Carry                       |
|   10 00xx | IDENT    | A                                     | Identity Operation                        |
|   10 01xx | AND      | A * B                                 | Bitwise And                               |
|   10 10xx | IOR      | A + B                                 | Bitwise Inclusive Or                      |
|   10 11xx | XOR      | A != B                                | Bitwise Exclusive Or                      |
|   11 00xx | NOT      | !A                                    | Bitwise Unary Not                         |
|   11 01xx | NAND     | !(A * B)                              | Bitwise Not And                           |
|   11 10xx | INOR     | !(A + B)                              | Bitwise Inclusive Not Or                  |
|   11 11xx | XNOR     | A == B                                | Bitwise Equals (Exclusive Not OR)         |

The Carry Flag is only relevant for the first 8 instructions. The 8 final logic instructions do not touch the carry
flag at all!

Another bit is added to control the origin of the B channel. (`regB` or `regL`)

Including target register parametrization (4 options) and the channel control (2 options), this yield a total of
16 * 8 = 128 operations. If we were to add a second command with a preset carry value, this would jump to 256.
This would consume the entire address space. This must be managed on the instruction decoder level: ALU instructions
that do not care about the carry bit can be re-allocated to other tasks.
Simple ALU
==========

A Simplified ALU does not need to manage the carry flag on its own.
This reduces the total amount of operations needed to 16:

| Operation | Mnemonic | Result                                | Description                               |
|----------:|:---------|:--------------------------------------|:------------------------------------------|
|      0000 | IDENT    | A                                     | Identity Operation                        |
|      0001 | AND      | A * B                                 | Bitwise And                               |
|      0010 | IOR      | A + B                                 | Bitwise Inclusive Or                      |
|      0011 | XOR      | A != B                                | Bitwise Exclusive Or                      |
|      0100 | NOT      | !A                                    | Bitwise Unary Not                         |
|      0101 | NAND     | !(A * B)                              | Bitwise Not And                           |
|      0110 | INOR     | !(A + B)                              | Bitwise Inclusive Not Or                  |
|      0111 | XNOR     | A == B                                | Bitwise Equals (Exclusive Not OR)         |
|      1000 | DEC      | A + 255 + C                           | Decrement with Carry                      |
|      1001 | INC      | A + 0 + C                             | Increment with Carry                      |
|      1010 | ADD      | A + B + C                             | Add with Carry                            |
|      1011 | SUB      | A + !B + C                            | Subtract with Carry                       |
|      1100 | NSWP     | ((A & 0x0f) << 4) + ((A & 0xf0) >> 4) | Nibble Swap (high input bit as Carry-Out) |
|      1101 | CMP      | !A + 0 + C                            | 2s Complement with Carry                  |
|      1110 | SHL      | (A << 1) + C                          | Roll Left through Carry                   |
|      1111 | SHR      | (A >> 1) + (C << 8)                   | Roll Right through Carry                  |

The Carry Flag is only relevant for the first 8 instructions. The 8 final logic instructions do not touch the carry
flag at all!

Another bit is added to control the origin of the B channel. (`regB` or `regL`)

* if `bit4` is low, use `regB`, else use `regL`
* if `bit3` is low, `Logic Unit` is active.
* if `bit3` is high,
    * if `bit2` is low or input is `x1101`, `Math Unit` is active.
    * else `Roll Unit` is active.
* special cases: if control signal is `x1101`, then `regA` is inverted

Including target register parametrization (4 options) and the channel control (2 options), this yield a total of
16 * 8 = 128 operations. If we were to add a second command with a preset carry value, this would jump to 256.
This would consume the entire address space. This must be managed on the instruction decoder level: ALU instructions
that do not care about the carry bit can be re-allocated to other tasks.
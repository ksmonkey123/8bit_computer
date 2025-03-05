The _Arithmetic Logic Unit_ provides calculation abilities.

The ALU contains a dedicated input register ($ALU_{IN}$). It is also connected to the internal value of [[General Purpose Registers|regA]].[[General Purpose Registers|]]

The ALU also contains the 1-bit [[Carry Flag]] ($F_C$). The carry flag is updated every instruction. see the detailed documentation of the [[#Segments]].

The ALU is controlled by a 4 bit control bus from the [[Operation Decoder]].

The ALU output is presented to the [[Data Bus]] whenever the ALU is selected as the current [[Data Target]]. The Carry flag is only updated when the ALU output is selected.
## Instructions

| Instruction | Mnemonic | Description              |
| ----------: | -------- | ------------------------ |
|           0 | `AND`    | bitwise AND              |
|           1 | `IOR`    | bitwise inclusive OR     |
|           2 | `XOR`    | bitwise exclusive OR     |
|           3 | `INV`    | bitwise inverse          |
|           4 | `RRC`    | roll right through carry |
|           5 | `RRL`    | roll left through carry  |
|           6 | -        | -                        |
|           7 | -        | -                        |
|           8 | `INC`    | increment with carry     |
|           9 | `CMP`    | 2s complement with carry |
|          10 | `ADC`    | addition with carry      |
|          11 | `SBC`    | subtraction with carry   |
|          12 | `DEC`    | decrement with carry     |
|          13 | -        | -                        |
|          14 | -        | -                        |
|          15 | -        | -                        |
## Segments

The ALU consists of 3 calculation segments:
* [[ALU Logic Unit]]
* [[ALU Roll Unit]]
* [[ALU Math Unit]]

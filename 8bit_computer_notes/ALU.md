The _Arithmetic Logic Unit_ provides calculation abilities.

The ALU contains a dedicated input register ($ALU_{IN}$). It is also connected to the internal value of $A$ ([[General Purpose Registers]]).

This internal input register is written to when $ALU_{IN}$ is selected as the current [[Data Target]].

The ALU output is presented to the [[Data Bus]] whenever the ALU is selected as the current [[Data Source]]. When referenced as a data source, the ALU output is called $ALU_{OUT}$.

The ALU also contains the 1-bit [[Carry Flag]] ($F_C$). The carry flag is updated every instruction when the ALU output is selected. see the detailed documentation of the [[#Segments]].

The ALU operation is selected by the lowest 4 bit of the [[Action Selector]].
## Instructions

| Instruction | Mnemonic | Description              |
| ----------: | -------- | ------------------------ |
|           0 | `AND`    | bitwise AND              |
|           1 | `IOR`    | bitwise inclusive OR     |
|           2 | `XOR`    | bitwise exclusive OR     |
|           3 | `INV`    | bitwise inverse          |
|           4 | `RRC`    | roll right through carry |
|           5 | `RLC`    | roll left through carry  |
|           6 | -        | _unused_                 |
|           7 | -        | _unused_                 |
|           8 | `INC`    | increment with carry     |
|           9 | `CMP`    | 2s complement with carry |
|          10 | `ADC`    | addition with carry      |
|          11 | `SBC`    | subtraction with carry   |
|          12 | `DEC`    | decrement with carry     |
|          13 | -        | _unused_                 |
|          14 | -        | _unused_                 |
|          15 | -        | _unused_                 |
## Segments
The ALU consists of 3 calculation segments.
### Logic Unit
The Logic unit implements simple bitwise logic operations.

All operations clear the [[Carry Flag]]!

| Command | Operation | Data Result           | $F_C$ Result |
| ------- | --------- | --------------------- | ------------ |
| 0       | `AND`     | $ALU_{IN} \wedge A$   | $0$          |
| 1       | `IOR`     | $ALU_{IN} \vee A$     | $0$          |
| 2       | `XOR`     | $ALU_{IN} \oplus A$   | $0$          |
| 3       | `INV`     | $\overline{ALU_{IN}}$ | $0$          |

>[!hint] Missing Operations
>Not all bitwise operations are implemented. Notably `NAND`, `NOR` and `XNOR` are missing.
>
>These can be constructed in 2 steps by simply executing the binary operation first, followed by an `INV` instruction.
### Roll Unit
The roll unit manages 2 simple roll operations. These are useful for example for simple multiplication or division by a factor of 2.

All roll operations pass through the [[Carry Flag]]. The bit that _leaves_ the 8-bit value is pushed into the carry flag, while the _fresh_ bit is initialized by the previous value of the carry flag.

| Cmd | Operation | Value Result                            | Carry Result                                      |
| --- | --------- | --------------------------------------- | ------------------------------------------------- |
| 4   | `RRC`     | $\frac{ALU_{IN}}{2} + 128 * F_C \mod 8$ | $ALU_{IN} \mod 2$                                 |
| 5   | `RLC`     | $2 * ALU_{IN} + F_C \mod 8$             | $\left\lfloor \frac{ALU_{IN}}{128} \right\rfloor$ |
#### Undefined Operations
The undefined operations 6 and 7 show the following behaviour:

| Cmd | Operation | Value Result                            | Carry Result                                      |
| --- | --------- | --------------------------------------- | ------------------------------------------------- |
| 6   | `RRC`     | $\frac{ALU_{IN}}{2} + 128 * F_C \mod 8$ | $ALU_{IN} \mod 2$                                 |
| 7   | `RLC`     | $2 * ALU_{IN} + F_C \mod 8$             | $\left\lfloor \frac{ALU_{IN}}{128} \right\rfloor$ |

### Math Unit
The math unit manages addition, subtraction and derived operations.

The [[Carry Flag]] indicates an overflow for additive operations (`INC` and `ADC`) and the lack of an underflow for subtractive operations (`CMP`, `DEC` and `SBC`).

| Cmd | Operation | Value Result                                                  | Carry Result     | "Neutral" Carry Input |
| --- | --------- | ------------------------------------------------------------- | ---------------- | --------------------- |
| 8   | `INC`     | $ALU_{IN} + F_C$                                              | $1$ on overflow  | $1$                   |
| 9   | `CMP`     | $\overline{ALU_{IN}} + F_C$<br>$- ALU_{IN} - (1 - F_C)$       | $0$ on underflow | $1$                   |
| 10  | `ADC`     | $A + ALU_{IN} + F_C$                                          | $1$ on overflow  | $0$                   |
| 11  | `SBC`     | $A + \overline{ALU_{IN}} + F_C$<br>$A - ALU_{IN} - (1 - F_C)$ | $0$ on underflow | $1$                   |
| 12  | `DEC`     | $ALU_{IN} + 255 + F_C$<br>$ALU_{IN} - (1 - F_C)$              | $0$ on underflow | $0$                   |

>[!tip] Carry Input
>The provided carry input values are the ones required to be set for correct single-byte operation.
>For multi-byte calculations, initialize the carry flag before the lowest byte and then simply calculate byte by byte without modifying the carry flag between bytes.

#### Undefined Operations
The undefined operations 13 - 15 show the following behaviour:

| Cmd | Operation | Value Result                                               | Carry Result     |
| --- | --------- | ---------------------------------------------------------- | ---------------- |
| 13  | `CMP - 1` | $255 + \overline{ALU_{IN}} + F_C$<br>$-ALU_{IN} - 2 + F_C$ | $0$ on underflow |
| 14  | `DEC`     | $ALU_{IN} + 255 + F_C$<br>$ALU_{IN} - (1 - F_C)$           | $0$ on underflow |
| 15  | `CMP - 1` | $255 + \overline{ALU_{IN}} + F_C$<br>$-ALU_{IN} - 2 + F_C$ | $0$ on underflow |


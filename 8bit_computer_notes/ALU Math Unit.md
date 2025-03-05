The math unit manages addition, subtraction and derived operations.

The [[Carry Flag]] indicates an overflow for additive operations (`INC` and `ADC`) and the lack of an underflow for subtractive operations (`CMP`, `DEC` and `SBC`).

| Cmd | Operation | Value Result                                                  | Carry Result   | Carry Input |
| --- | --------- | ------------------------------------------------------------- | -------------- | ----------- |
| 8   | `INC`     | $ALU_{IN} + F_C$                                              | 1 on overflow  | 1           |
| 9   | `CMP`     | $\overline{ALU_{IN}} + F_C$<br>$- ALU_{IN} - (1 - F_C)$       | 0 on underflow | 1           |
| 10  | `ADC`     | $A + ALU_{IN} + F_C$                                          | 1 on overflow  | 0           |
| 11  | `SBC`     | $A + \overline{ALU_{IN}} + F_C$<br>$A - ALU_{IN} - (1 - F_C)$ | 0 on underflow | 1           |
| 12  | `DEC`     | $ALU_{IN} + 255 + F_C$<br>$ALU_{IN} - (1 - F_C)$              | 0 on underflow | 0           |


>[!tip] Carry Input
>The provided carry input values are the ones required to be set for correct single-byte operation.
>For multi-byte calculations, initialize the carry flag before the lowest byte and then simply calculate byte by byte without modifying the carry flag between bytes.


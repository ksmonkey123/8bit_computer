The _Data Source_ is a 4-bit [[Execution Microcode]] parameter.

It controls which potential data source publishes its data onto the [[Data Bus]].

| Code | Source                                                  |
| ---- | ------------------------------------------------------- |
| 0    | constant 255 (no source selected, Data Bus pulled high) |
| 1    | $A$ [[General Purpose Registers]]                       |
| 2    | $B$ [[General Purpose Registers]]                       |
| 3    | $C$ [[General Purpose Registers]]                       |
| 4    | $D$ [[General Purpose Registers]]                       |
| 5    | $L_1$ [[Literal Registers]]                             |
| 6    | $L_2$ [[Literal Registers]]                             |
| 7    | _unused, reserved for later_                            |
| 8    | $PC_L$ [[Program Counter]] (low byte)                   |
| 9    | $PC_H$ [[Program Counter]] (high byte)                  |
| 10   | $SP_L$ [[Stack Pointer]] (low byte)                     |
| 11   | $SP_H$ [[Stack Pointer]] (high byte)                    |
| 12   | _unused, reserved for later_                            |
| 13   | _unused, reserved for later_                            |
| 14   | $ALU_{OUT}$ ([[ALU]] output)                            |
| 15   | [[Memory Device]]                                       |


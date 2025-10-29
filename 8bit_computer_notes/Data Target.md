The _Data Target_ is a 3-bit [[Microcode]] parameter.

It controls which potential data target the current value on the [[Data Bus]] is written to.
The target device may be the same as the [[Data Source]], though it is rarely sensible.

| Code | Target                            |
| ---- | --------------------------------- |
| 0    | $ALU_{IN}$ ([[ALU]] input)        |
| 1    | $A$ [[General Purpose Registers]] |
| 2    | B [[General Purpose Registers]]   |
| 3    | $C$ [[General Purpose Registers]] |
| 4    | $D$ [[General Purpose Registers]] |
| 5    | $L_1$ [[Literal Registers]]       |
| 6    | $L_2$ [[Literal Registers]]       |
| 7    | [[Memory Device]]                 |

>[!WARNING]
Since all 8 options are associated with a valid target device, it is not possible to not write to a device. A sensible default is to simply write to the ALU input (Code 0).

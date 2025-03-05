The _Data Source_ is a [[Microcode]] parameter.

It controls which potential data source publishes its data onto the [[Data Bus]].

| Code | Source                                                  |
| ---- | ------------------------------------------------------- |
| 0    | constant 255 (no source selected, Data Bus pulled high) |
| 1    | [[General Purpose Registers\|regA]]                     |
| 2    | [[General Purpose Registers\|regB]]                     |
| 3    | [[General Purpose Registers\|regC]]                     |
| 4    | [[General Purpose Registers\|regD]]                     |
| 5    | [[Literal Registers\|regL1]]                            |
| 6    | [[Literal Registers\|regL2]]                            |
| 7    | _unused, reserved for later_                            |
| 8    | [[Program Counter]] (low byte)                          |
| 9    | [[Program Counter]] (high byte)                         |
| 10   | [[Stack Pointer]] (low byte)                            |
| 11   | [[Stack Pointer]] (high byte)                           |
| 12   | _unused, reserved for later_                            |
| 13   | _unused, reserved for later_                            |
| 14   | [[ALU]] output                                          |
| 15   | [[Memory Device]]                                       |


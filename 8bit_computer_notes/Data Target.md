The _Data Target_ is a [[Microcode]] parameter.

It controls which potential data target the current value on the [[Data Bus]] is written to.
The target device may be the same as the [[Data Source]], though it is rarely sensible.

| Code | Target                              |
| ---- | ----------------------------------- |
| 0    | [[ALU]] input                       |
| 1    | [[General Purpose Registers\|regA]] |
| 2    | [[General Purpose Registers\|regB]] |
| 3    | [[General Purpose Registers\|regC]] |
| 4    | [[General Purpose Registers\|regD]] |
| 5    | [[Literal Registers\|regL1]]        |
| 6    | [[Literal Registers\|regL2]]        |
| 7    | [[Memory Device]]                   |
>[!WARNING]
Since all 8 options are associated with a valid target device, it is not possible to not write to a device. A sensible default is to simply write to the ALU input (Code 0).

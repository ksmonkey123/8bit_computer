There are 4 general purpose 8-bit registers. They are called $A$, $B$, $C$ and $D$.

All 4 registers are written to from the [[Data Bus]].

Registers $C$ and $D$ can also be read to the [[Address Bus]]. In that case $C$ will be used as the lower byte, $D$ as the upper byte. The combined 16-bit pseudo-register is called $CD$.

Register `regA` is also connected to the [[ALU]] for 2-input operations.

| Register | [[Data Source]] | [[Data Target]] | [[Address Source]] |
| -------- | --------------- | --------------- | ------------------ |
| `regA`   | 1               | 1               | -                  |
| `regB`   | 2               | 2               | -                  |
| `regC`   | 3               | 3               | 5 (low Byte)       |
| `regD`   | 4               | 4               | 5 (high Byte)      |

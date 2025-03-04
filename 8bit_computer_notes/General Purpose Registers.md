There are 4 general purpose 8-bit [[Register|registers]]. They are referenced as `regA`, `regB`, `regC` and `regD`.

All 4 registers are written to from the [[Data Bus]].

Registers `regC` and `regD` can also be read to the [[Address Bus]]. In that case `regC` will be used as the lower byte, `regD` as the upper byte.

Register `regA` is also connected to the [[ALU]] for 2-input operations. This is done via an unbuffered [[Interlink Bus]].

| Register | [[Data Source]] | [[Data Target]] | [[Address Source]] |
| -------- | --------------- | --------------- | ------------------ |
| `regA`   | 1               | 1               | -                  |
| `regB`   | 2               | 2               | -                  |
| `regC`   | 3               | 3               | 5 (low Byte)       |
| `regD`   | 4               | 4               | 5 (high Byte)      |

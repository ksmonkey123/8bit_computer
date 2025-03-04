There are 2 literal [[Register|registers]] `regL1` and `regL2`

These registers are primarily designed to hold [[Extended Instructions]].
They are also usable as _temporary registers_ in [[Microcode]].

Both registers are individually accessible via the [[Data Bus]].
Both registers combined can also be accessed on the [[Address Bus]], where `regL1` will be the low byte, `regL2` the high byte.

The first register - `regL1` - is also connected to the [[Increment Unit]] where it is used for offset calculations. This connection is not buffered and uses an [[Interlink Bus]].

| Register | [[Data Source]] | [[Data Target]] | [[Address Source]] |
| -------- | --------------- | --------------- | ------------------ |
| `regL1`  | 5               | 5               | 4 (low Byte)       |
| `regL2`  | 6               | 6               | 4 (high Byte)      |

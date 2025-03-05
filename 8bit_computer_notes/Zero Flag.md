The zero flag ($F_Z$) indicates that the last value that was passed over the [[Data Bus]] was a zero.

The flag is only updated in one of the following situations:
1. the [[Data Target]] is one of the [[General Purpose Registers]]
2. the [[Data Source]] is the [[ALU]] _and_ the [[Data Target]] is not $L_2$

This rules ensure that any data transfer into a register causes an update. It also ensures that ready any ALU result causes an update. The special case, where the flag is not set when a value is written from the ALU to $L_2$ exists to enable [[Carry Flag]] updates while discarding the ALU calculation result.

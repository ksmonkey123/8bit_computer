There are 2 sign flags that indicate the sign of the last value passed over the [[Data Bus]]:
* $F_Z$: The _Zero Flag_ indicates if a value was $0x00$
* $F_S$: The _Sign Flag_ indicates if a value was negative. (Most significant bit is $1$)

The flags are only updated in one of the following situations:
1. the [[Data Target]] is one of the [[General Purpose Registers]]
2. the [[Data Source]] is the [[ALU]] _and_ the [[Data Target]] is not $L_2$

This rules ensure that any data transfer into a register causes an update. It also ensures that ready any ALU result causes an update. The special case, where the flags are not set when a value is written from the ALU to $L_2$ exists to enable [[Carry Flag]] updates while discarding the ALU calculation result.

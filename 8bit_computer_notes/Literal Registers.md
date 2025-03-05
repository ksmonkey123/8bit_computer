There are 2 literal registers $L_1$ and $L_2$.

These registers are primarily designed to hold [[Extended Instructions]].
They are also usable as _temporary registers_ in [[Microcode]].

Both registers are individually accessible via the [[Data Bus]].
Both registers combined can also be accessed on the [[Address Bus]], where $L_1$ will be the low byte, $L_2$ the high byte. In this case, the combined 16-bit pseudo-register is called $L$.

The first register - $L_1$ - is also connected to the [[Increment Unit]] where it is used for offset calculations.

| Register | [[Data Source]] | [[Data Target]] | [[Address Source]] |
| -------- | --------------- | --------------- | ------------------ |
| $L_1$    | 5               | 5               | 4 (low Byte)       |
| $L_2$    | 6               | 6               | 4 (high Byte)      |

>[!hint]
>The literal registers should be treated as volatile as their values may change during any [[Fetch Phase]]. Their use in microcode is only acceptable if used as a temporary _scratch register_.


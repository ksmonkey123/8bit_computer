The roll unit manages 2 simple roll operations. These are useful for example for simple multiplication or division by a factor of 2.

All roll operations pass through the [[Carry Flag]]. The bit that _leaves_ the 8-bit value is pushed into the carry flag, while the _fresh_ bit is initialized by the previous value of the carry flag.

| Cmd | Operation | Value Result                            | Carry Result                                      |
| --- | --------- | --------------------------------------- | ------------------------------------------------- |
| 4   | `RRC`     | $\frac{ALU_{IN}}{2} + 128 * F_C \mod 8$ | $ALU_{IN} \mod 2$                                 |
| 5   | `RRL`     | $2 * ALU_{IN} + F_C \mod 8$             | $\left\lfloor \frac{ALU_{IN}}{128} \right\rfloor$ |

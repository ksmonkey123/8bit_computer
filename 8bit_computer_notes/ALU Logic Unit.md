The Logic unit implements simple bitwise logic operations.

All operations clear the [[Carry Flag]]!

| Command | Operation | Result                |
| ------- | --------- | --------------------- |
| 0       | `AND`     | $ALU_{IN} \wedge A$   |
| 1       | `IOR`     | $ALU_{IN} \vee A$     |
| 2       | `XOR`     | $ALU_{IN} \oplus A$   |
| 3       | `INV`     | $\overline{ALU_{IN}}$ |

>[!hint] Missing Operations
>Not all bitwise operations are implemented. Notably `NAND`, `NOR` and `XNOR` are missing.
>
>These can be constructed in 2 steps by simply executing the binary operation first, followed by an `INV` instruction.

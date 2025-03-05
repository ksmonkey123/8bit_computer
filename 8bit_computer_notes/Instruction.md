Every program instruction specifies a specific action the computer should perform.

Every instruction can have 1 to 4 [[Execution Step|Execution Steps]]. The execution steps are specified as [[Execution Microcode]]. This flexibility allows for the definition of arbitrary new instructions simply by reprogramming the [[Microcode ROM]].

## Fetch Size

Every instruction is defined by an 8-bit number. Depending on the instruction 1 or 2 additional bytes can also be fetched from subsequent memory locations.

| Fetch Size | Target of Byte 1 | Target of Byte 2 | Target of Byte 3 |
| ---------- | ---------------- | ---------------- | ---------------- |
| 0          | $IR$             | -                | -                |
| 1          | $IR$             | $L_1$            | -                |
| 2          | $IR$             | $L_2$            | $L_1$            |

>[!warning] Endianness
> If we treat $L_1$ and $L_2$ as a single 16-bit number this clearly shows that for a 3-byte instruction the 16-bit literal must be encoded as Big Endian. (The most significant byte is written first.)
> 
> This is different from [[Instruction|Instructions]] reading and writing 16-bit data from and to memory. These use little-endian encoding. (The most significant bit is written in the highest memory address.)




The microcode is a 16-bit wide EEPROM (or rather 2 parallel 8-bit ROMs).

The 13 address bits are mapped as follows:

|   Bit | [[Fetch Phase]]     | [[Execution Phase]] |
| ----: | ------------------- | ------------------- |
| 0 - 7 | $IR$                | $IR$                |
|     8 | `1`                 | $F_C$               |
|     9 | `1`                 | $F_Z$               |
|    10 | `1`                 | $F_S$               |
| 11-12 | $stepNumber \mod 4$ | $stepNumber \mod 4$ |

Depending on the phase the content of the ROM is interpreted differently.

See [[Fetch Microcode]] and [[Execution Microcode]] for details.
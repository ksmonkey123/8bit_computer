The microcode is a 16-bit wide EEPROM (or rather 2 parallel 8-bit ROMs).

The 13 address bits are mapped as follows:

|   Bit | [[Fetch Phase]] | [[Execution Phase]] |
| ----: | --------------- | ------------------- |
| 0 - 7 | $IR$            | $IR$                |
|     8 | `1`             | $F_C$               |
|     9 | `1`             | $F_Z$               |
|    10 | `1`             | $F_S$               |
| 11-12 | $step \mod 4$   | $step \mod 4$       |

Depending on the phase the content of the ROM is interpreted differently.

See [[Fetch Microcode]] and [[Execution Microcode]] for details.

Per instruction and step there are theoretically 8 possible [[Status Flag]] combinations. Of these 6 are valid flag combinations and are used for execution phase microcode. One special invalid flag combination is used for fetch phase microcode.

| $F_C$ | $F_Z$ | $F_S$ | Flag Interpretation | Type            |
| ----- | ----- | ----- | ------------------- | --------------- |
| `0`   | `0`   | `0`   | no carry, positive  | Execution Phase |
| `0`   | `0`   | `1`   | no carry, negative  | Execution Phase |
| `0`   | `1`   | `0`   | no carry, zero      | Execution Phase |
| `0`   | `1`   | `1`   | _invalid_           | _unused_        |
| `1`   | `0`   | `0`   | carry, positive     | Execution Phase |
| `1`   | `0`   | `1`   | carry, negative     | Execution Phase |
| `1`   | `1`   | `0`   | carry, zero         | Execution Phase |
| `1`   | `1`   | `1`   | _invalid_           | Fetch Phase     |

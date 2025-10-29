The _Microcode_ defines the exact behaviour of a single [[Execution Step]].

The microcode is accessed by passing the current [[Instruction]] code, the 3 [[Status Flag]] and the current step number (0 - 15) to the [[#Microcode ROM]]

For every [[Instruction]] and every one of 6 valid [[Status Flag]] combinations a microcode record must be provided. This allows for different microcode for different status flags enabling status dependant behaviour such as conditional branching.

The microcode is stored as a 16-bit value in ROM:

|   Bit | Function                                                                                       |
| ----: | ---------------------------------------------------------------------------------------------- |
|   0-3 | [[Data Source]]                                                                                |
|   4-6 | [[Data Target]]                                                                                |
|     7 | _Proceed flag_ $F_P$. This flag indicates if the current instruction has more steps to follow. |
|  8-12 | [[Action Selector]]                                                                            |
| 13-15 | [[Address Source]]                                                                             |
Bit 7 is passed to the [[Step Sequencer]], the remaining parameters are passed to the [[Instruction Decoder]].
## Step 0: Fetch Step
The first step of every instruction must fetch the next instruction to the $IR$. This is required, as during step 0 the $IR$ still contains the previous instruction:

| Component       | Value     | Meaning       | Comment    |
| --------------- | --------- | ------------- | ---------- |
| Address Source  | $110_2$   | $PC$          |            |
| Data Source     | $1111_2$  | Memory Device |            |
| Data Target     | $000_2$   | ALU input     | irrelevant |
| Action Selector | $00000_2$ | ALU `AND`     | irrelevant |
| Proceed Flag    | $1_2$     | proceed       |            |

>[!Info]
>The correct value for step 0 is `0xC08F`.

## Update of the Program Counter
Any normal operation must update the [[Program Counter]] in order for the processor to be able to proceed. For basic operations that are only 1 byte wide, this should be done in the second step. The most basic version of such a step would be:

| Component       | Value     | Meaning    | Comment                                                                                                                                     |
| --------------- | --------- | ---------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| Address Source  | $000_2$   | $IncR + 1$ | Current $PC$ value was on the address bus in step 0. Therefore in step 1, $(IncR + 1)$ will be the correct value for the _next_ $PC$ value. |
| Data Source     | $0000_2$  | $255$      | irrelevant                                                                                                                                  |
| Data Target     | $000_2$   | ALU input  | irrelevant                                                                                                                                  |
| Action Selector | $10000_2$ | write $PC$ |                                                                                                                                             |
| Proceed Flag    | $1_2$     | proceed    | for a simple `NOP` operation, this may also be $0_2$                                                                                        |

>[!Info]
>The correct value for this minimal step 1 is `0x1080`.

>[!Hint]
>Branching and conditional instructions may manipulate the $PC$ in more specific ways. This guide only describes the basics required for normal linear instructions.

>[!Hint]
Any actions that don't depend on the memory bus and don't require an action selector (mainly register operations) can be performed in the same step.

>[!Warning]
>If any operation does not update $PC$, the execution **will** get stuck on that command.
>
>There may be some edge cases with conditional commands, where such a behaviour may be desired to perform that one instruction until a given status flag changes. But this must be designed *very* carefully!
## Microcode ROM
The microcode is a 16-bit wide EEPROM (or rather 2 parallel 8-bit ROMs) with an address width of 15 bit. (32k words)

The 15 address bits are mapped as follows:

|   Bit | Source |
| ----: | ------ |
| 0 - 7 | $IR$   |
|     8 | $F_C$  |
|     9 | $F_Z$  |
|    10 | $F_S$  |
| 11-14 | $step$ |
>[!Important]
>All address lines (besides the $step$) should be double buffered to ensure none of them can _ever_ change during an [[Execution Step]]. ($step$ does not need to be double buffered as it only changes on step transitions).

Per instruction and step there are theoretically 8 possible [[Status Flag]] combinations. Of these 6 are valid flag combinations and are used for execution phase microcode. Values need to be defined for the invalid combinations. It is however recommended to _halt_ the processor upon encountering such a combination.

| $F_C$ | $F_Z$ | $F_S$ | Flag Interpretation |
| ----- | ----- | ----- | ------------------- |
| `0`   | `0`   | `0`   | no carry, positive  |
| `0`   | `0`   | `1`   | no carry, negative  |
| `0`   | `1`   | `0`   | no carry, zero      |
| `0`   | `1`   | `1`   | _invalid_           |
| `1`   | `0`   | `0`   | carry, positive     |
| `1`   | `0`   | `1`   | carry, negative     |
| `1`   | `1`   | `0`   | carry, zero         |
| `1`   | `1`   | `1`   | _invalid_           |

> [!hint]
> The combination of $F_Z$ and $F_S$ both set is due to the fact that the flags are always updated together based on the same [[Data Bus]] value and the same value cannot be zero and negative at the same time.

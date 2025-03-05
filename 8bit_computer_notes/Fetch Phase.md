The _Fetch Phase_ is the first part of executing an instruction. During this phase the next instruction is loaded.

The fetch phase consists of 2 to 4 steps.

| Step | Action                                                        |
| ---- | ------------------------------------------------------------- |
| 0    | $memory(PC) \rightarrow IR; PC \rightarrow IncR$              |
| 1    | $memory(Incr + 1) \rightarrow L_2; IncR + 1 \rightarrow IncR$ |
| 2    | $memory(Incr + 1) \rightarrow L_1; IncR + 1 \rightarrow IncR$ |
| 3    | $IncR + 1 \rightarrow PC$                                     |

During the first step we next instruction into the [[Instruction Register]].

If the instruction is an extended [[Instruction]], the additional values are loaded into $L_1$ and $L_2$.

The last step finally updates the $PC$ to the location of the next instruction.

This is controlled by [[Fetch Microcode]].

After the first step, we consult the fetch microcode to determine which step to jump to. This is depends on how many bytes of parameters should be fetched.

| # of bytes to fetch | go to step |
| ------------------- | ---------- |
| 0                   | 3          |
| 1                   | 2          |
| 2                   | 1          |
This _jump_ is done by controlling the increment step of the [[Step Sequencer]] at the end of step 0.
## Halt Flag

The fetch microcode also contains a [[Halt Flag]]. When this flag is set, the [[Main Clock]] should be frozen. The [[Halt Flag]] should only be clearable by a [[System Reset]].

The halt flag is updated during the write action of step 7.
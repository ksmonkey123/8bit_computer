The _Execution Cycle_ or _Execution Loop_ is the full cycle in which an [[Instruction]] is loaded and executed.

The execution cycle has 2 phases: The [[Fetch Phase]] and the [[Execution Phase]].

During the fetch phase, a new instruction is loaded from memory. During the execution phase the loaded instruction is executed. After the execution phase the cycle repeats with the fetch phase of the next instruction.

## Steps

The full execution cycle consists of 12 steps:

| Step | Phase               | Phase Step |
| ---: | ------------------- | ---------- |
|    0 | [[Fetch Phase]]     | 0          |
|    1 | Fetch Phase         | -          |
|    2 | Fetch Phase         | -          |
|    3 | Fetch Phase         | 3          |
|    4 | Fetch Phase         | 4          |
|    5 | Fetch Phase         | 5          |
|    6 | Fetch Phase         | 6          |
|    7 | Fetch Phase         | 7          |
|    8 | [[Execution Phase]] | 0          |
|    9 | Execution Phase     | 1          |
|   10 | Execution Phase     | 2          |
|   11 | Execution Phase     | 3          |
>[!todo]
>This should be reworked. [[Improved Fetch Cycle (Idea)]]

## Halt

It is possible for the cycle to be halted. This is done by simply freezing the [[Main Clock]]. This halting is done by a special flag inspected during the fetch phase.
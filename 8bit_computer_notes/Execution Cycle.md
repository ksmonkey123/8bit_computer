The _Execution Cycle_ or _Execution Loop_ is the full cycle in which an [[Instruction]] is loaded and executed.

During the first step, a new instruction is loaded into the [[Instruction Register]] from memory. During the subsequent steps, the instruction is then effectively _executed_.

Each instruction can take between 1 and 16 steps.

## Execution Step
Every execution step takes 1 clock cycle. The behaviour is defined by a single [[Microcode]] value.

During the entire step, we pass the [[Data Source]], [[Data Target]], [[Address Source]] and [[Action Selector]] from the microcode to the [[Instruction Decoder]].
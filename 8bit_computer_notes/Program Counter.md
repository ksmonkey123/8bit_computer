The program counter is a special 16-bit register on the [[Address Bus]].
It is referenced as $PC$.

It is used to hold the [[Memory Address]] of the next [[Instruction]].[^1]

Both 8-bit halves of the program counter can be read on the [[Data Bus]].
In this case the lower half is referenced as $PC_L$. The upper half is called $PC_H$.

[^1]: This is only valid outside a [[Fetch Phase]]
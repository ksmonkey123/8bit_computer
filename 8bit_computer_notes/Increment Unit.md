The increment unit is a special16-bit register on the [[Address Bus]] used for simple arithmetic manipulation of address values. The increment unit is referenced as `IncR` or $IncR$.

Writes to the increment unit cannot be manually controlled. Instead the register value is updated on every single write pulse.

In that way, it is possible to provide an address to the address bus that depends on the address value of a previous [[Execution Step]].

The current value of the increment unit cannot be read directly. The value is passed to an adder unit. Only the result of this can then be provided to the address bus.

The calculation to be performed is controlled by the current [[Address Source]] (internally only the lower 2 bits are used, but whenever the value is 
$\geq 4$ it is ignored.)

| [[Address Source]] | result       |
| ------------------ | ------------ |
| 0                  | $IncR + 1$   |
| 1                  | $IncR - 1$   |
| 2                  | $IncR + L_1$ |
| 3                  | $IncR - L_1$ |
In all cases, over- or underflows are ignored.

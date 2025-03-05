 status flag is a 1-bit value providing some limited information about the status of the system. This information can also influence the behaviour of some [[Instruction|Instructions]].

The computer contains 3 status flags:
* $F_C$ [[Carry Flag]]
* $F_Z$ [[Zero Flag]]
* $F_S$ [[Sign Flag]] (Also called _minus flag_)

The flags cannot be manually updated nor explicitly read as register values. They are only accessible by the [[Instruction Decoder]] (and in the case of the carry flag to the [[ALU]]).
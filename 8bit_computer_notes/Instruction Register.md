The _Instruction Register_ ($IR$) is a special 8-bit register.

It can be written to from the [[Data Bus]] only through dedicated control signals from the [[Step Sequencer]].

It is written to during step 0 of the [[Execution Cycle]].

The value of the register is passed to the [[Microcode ROM]].
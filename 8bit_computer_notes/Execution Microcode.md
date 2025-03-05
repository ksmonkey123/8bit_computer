The _Execution Microcode_ defines the exact behaviour of a single [[Execution Step]].

The execution microcode is accessed by passing the 3 [[Status Flag]] and the current step number (0 - 4) to the microcode ROM.

For every [[Instruction]] and every one of 6 valid [[Status Flag]] combinations a microcode record must be provided. This allows for different microcode for different status flags enabling status dependant behaviour such as conditional branching.

It is a 16-bit value stored in [[Microcode ROM]]

|   Bit | Function                                                                   |
| ----: | -------------------------------------------------------------------------- |
|   0-3 | [[Data Source]]                                                            |
|   4-6 | [[Data Target]]                                                            |
|     7 | indicates, if the current step is the last step of the [[Execution Phase]] |
|  8-12 | [[Action Selector]]                                                        |
| 13-15 | [[Address Source]]                                                         |
Bit 7 is passed to the [[Step Sequencer]], the remaining parameters are passed to the [[Instruction Decoder]].
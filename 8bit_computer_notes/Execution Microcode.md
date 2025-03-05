The _Execution Microcode_ defines the exact behaviour of a single [[Execution Step]].

It is a 16-bit value stored in [[Microcode ROM]].

The execution microcode is accessed by passing the 3 [[Status Flag]] and the current step number (0 - 4) to the microcode ROM.

|   Bit | Function                                                                   |
| ----: | -------------------------------------------------------------------------- |
|   0-3 | [[Data Source]]                                                            |
|   4-6 | [[Data Target]]                                                            |
|     7 | indicates, if the current step is the last step of the [[Execution Phase]] |
|  8-12 | [[Action Selector]]                                                        |
| 13-15 | [[Address Source]]                                                         |

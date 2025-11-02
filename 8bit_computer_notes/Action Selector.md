The _Action Selector_ is a 5-bit [[Microcode]] parameter. It controls the [[ALU]] and additional actions.

| Command   | Operation                                                                                                                             |
| --------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| $0xxxx_2$ | [[ALU]] operation (trailing 4 bits sent to the ALU)                                                                                   |
| $10000_2$ | write the [[Address Bus]] value to $PC$ ([[Program Counter]])                                                                         |
| $10001_2$ | write the [[Address Bus]] value to $SP$ ([[Stack Pointer]])                                                                           |
| $10010_2$ | write the [[Address Bus]] value to $IPC$ ([[Interrupt (Idea)]])                                                                      |
| $10011_2$ | _unused, reserved for future execution features_                                                                                      |
| $10100_2$ | _unused, reserved for future execution features_                                                                                      |
| $10101_2$ | _unused, reserved for future execution features_                                                                                      |
| $10110_2$ | _unused, reserved for future execution features_                                                                                      |
| $10111_2$ | _unused, reserved for future execution features_                                                                                      |
| $11000_2$ | Halt the processor. The corresponding control line is passed to the [[Step Sequencer]] to halt the processor. during the write cycle. |
| $11001_2$ | _unused, reserved for future infrastructure features_                                                                                 |
| $11010_2$ | disable interrupts ([[Interrupt (Idea)]])                                                                                            |
| $11011_2$ | enable interrupts ([[Interrupt (Idea)]])                                                                                             |
| $11100_2$ | _unused, reserved for future infrastructure features_                                                                                 |
| $11101_2$ | _unused, reserved for future infrastructure features_                                                                                 |
| $11110_2$ | _unused, reserved for future infrastructure features_                                                                                 |
| $11111_2$ | _unused, reserved for future infrastructure features_                                                                                 |

>[!warning]
>Because the action selector manages both ALU operations and write operations from the address bus, it is not possible to perform ALU calculations in the same step as an address write.

>[!Hint]
> The action codes in the range $[10001_2, 10111_2]$ are currently unused. These are designed to be used for future features in the instruction execution. These signals are produced by the [[Instruction Decoder]]
> Features that interact primarily with infrastructure components like the [[Instruction Register]], the [[Main Clock]] or the [[Step Sequencer]] should use the range $[11001_2, 11111_2]$. These signals are not produced by the instruction decoder.
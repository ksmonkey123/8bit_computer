The _Action Selector_ is a 5-bit [[Microcode]] parameter.

It controls the [[ALU]] and additional actions.

| Command   | Operation                                                                                                                             |
| --------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| $0xxxx_2$ | [[ALU]] operation (trailing 4 bits sent to the ALU)                                                                                   |
| $10000_2$ | write the [[Address Bus]] value to $PC$ ([[Program Counter]])                                                                         |
| $10001_2$ | write the [[Address Bus]] value to $SP$ ([[Stack Pointer]])                                                                           |
| $11000_2$ | Halt the processor. The corresponding control line is passed to the [[Step Sequencer]] to halt the processor. during the write cycle. |

>[!important]
>Because the action selector manages both ALU operations and write operations from the address bus, it is not possible to perform ALU calculations in the same [[Execution Step]] as an address write.


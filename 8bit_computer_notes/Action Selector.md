The _Action Selector_ is a 5-bit [[Execution Microcode]] parameter.

It controls the [[ALU]] and additional actions.

| Command   | Operation                                                     |
| --------- | ------------------------------------------------------------- |
| $0xxxx_2$ | [[ALU]] operation (trailing 4 bits sent to the ALU)           |
| $10000_2$ | write the [[Address Bus]] value to $PC$ ([[Program Counter]]) |
| $10001_2$ | write the [[Address Bus]] value to $SP$ ([[Stack Pointer]])   |

>[!important]
>Because the action selector manages both ALU operations and write operations from the address bus, it is not possible to perform ALU calculations in the same [[Execution Step]] as an address write.


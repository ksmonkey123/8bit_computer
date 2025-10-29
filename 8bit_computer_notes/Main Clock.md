The _Main Clock_ synchronises all operations in the computer.

A clock cycle starts with the clock signal low.

The rising edge of the clock signal indicates the write pulse, or the instant in which any writes must be performed.

The falling edge of the clock indicates the start of the next cycle.

>[!important]
> When the [[Halt Flag]] is set, the clock must be suspended.

>[!warning]
>After a [[System Reset]] the clock must stay low for some time.
>
>If the clock keeps running internally and is simply _published_ after a reset, it could happen that the reset occurs just before a rising edge. This could leave the first fetch step with too little time to get the first instruction onto the data bus before it is written to the instruction register.
>
>The delay between a reset and the first rising edge must not be shorter than a normal low period!

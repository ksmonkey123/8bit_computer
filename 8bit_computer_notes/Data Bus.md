The data bus is an 8-bit bus. It is the primary means of data transfer in the computer. It is also responsible for providing the [[Instruction Register]] with the next [[Instruction]] to execute.

Data is provided by the device selected as the [[Data Source]].

Data is written to the device selected as the [[Data Target]].

>[!important]
>The data bus needs to be pulled high when idle.
>
> When there's no data source selected, the bus would remain floating. With the mandated pull-up such an _invalid_ data source can be used to present a known value of 255 (-1) to the bus.



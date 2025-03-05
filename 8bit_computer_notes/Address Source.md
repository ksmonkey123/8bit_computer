The _Address Source_ is a [[Microcode]] parameter.

It controls which potential data source publishes its data onto the [[Address Bus]]. 

| Code | Source                                    |
| ---- | ----------------------------------------- |
| 0    | [[Increment Unit]] (Increment Mode)       |
| 1    | [[Increment Unit]] (Decrement Mode)       |
| 2    | [[Increment Unit]] (Positive Offset Mode) |
| 3    | [[Increment Unit]] (Negative Offset Mode) |
| 4    | [[Literal Registers]]                     |
| 5    | [[General Purpose Registers]] ($CD$)      |
| 6    | [[Program Counter]]                       |
| 7    | [[Stack Pointer]]                         |
Since all 8 possible values correspond to a valid data source, it is not possible for no value to be present on the address bus. The default value (0) corresponding to the increment mode of the [[Increment Unit]] also makes sense, as this is the primary mode used during the fetch phase of the [[Execution Cycle]].

The address bus is a 16-bit bus primarily used to provide a memory address to a [[Memory Device]] for instruction fetching and for data access.

The device publishing to the address bus is selected by the [[Address Source]].

Besides publishing a memory address to a memory device, it is also possible to write to some special registers from the address bus. This is controlled by the [[Action Selector]]. The registers that can be written to are:
* [[Stack Pointer]] $SP$
* [[Program Counter]] $PC$

Additionally, the value on the address bus is written to the [[Increment Unit]] on every step automatically.

>[!hint]
>Since all possible address sources are well-defined, there is no need for pull-ups or pull-downs.


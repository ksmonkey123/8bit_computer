The _Fetch Microcode_ is a part of the [[Microcode ROM]] that is accessed during the [[Fetch Phase]]. It contains 2 attributes:

The _Fetch Microcode_ is accessed using the illegal [[Status Flag]] combination $(F_C, F_Z, F_S)$, where all three are set. ^[This is illegal because a value cannot be zero and negative at the same time, so $F_Z$ and $F_S$ cannot be both set at once]

It is important that the same microcode is programmed for
## Fetch Size

The _Fetch Size_ indicates how many parameters should be fetched during the fetch size. For ease of implementation, we don't save the number of parameters we want to fetch, but rather, how much we want to skip.

| Bytes to Fetch | Steps to Skip | Encoded Value |
| -------------- | ------------- | ------------- |
| 0              | 7             | 3             |
| 1              | 5             | 2             |
| 2              | 3             | 1             |

The _Fetch Size_ is stored as a 2-bit value in bits 0 and 1 of the microcode value.

## Halt Flag

Bit 7 of the microcode value indicates to the fetch phase, that the [[Halt Flag]] should be set.
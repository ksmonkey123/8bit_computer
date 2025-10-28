The entire upper half (32KiB) of the [[Address Space]] are used for internal RAM. (Addresses `0x8000-0xFFFF`).

This is implemented as 4 static ram chips (8KiB each) with parallel interfaces. 

All memory locations are always writable and readable.

The physical RAM module also uses an [[Edge Pulse Circuit]] to convert the falling edge of the $\overline{writeMemory}$ into a short ($<10\micro{}s$) low pulse.

#stub 
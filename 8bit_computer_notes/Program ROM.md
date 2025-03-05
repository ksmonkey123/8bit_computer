The _Program ROM_ is a _replaceable_ ROM module holding the program. This can be compared to a GameBoy game cartridge.

The _Program ROM_ can use the address range `0x0000-0x3FFF`.
The lower half (`0x0000-0x1FFF`) must always be implemented, the upper half (`0x2000-0x3FFF`) is optional.

>[!hint]
The _Program ROM_ should be physically connected to the computer using a standard interface. This allows the creation of a standard [[Program Cartridge (Idea)]] design.

When integrated into the computer, the _Program ROM_ must be _read-only_.
#idea

There should be a standard program cartridge design.

Each cartridge would hold 8KiB or 16KiB of EEPROM memory that is addressed over a 16bit address bus.

The interface must have at least the following:
* the lower 14 bits of the [[Address Bus]]
* 1 enable bit
* 1 read trigger flag
* 8 bit [[Data Bus]]
* Power
* a programming interface
	* probably just the write flag

A minimum pin mapping could be:

|     Pin | Function             |
| ------: | -------------------- |
|       0 | Ground               |
|       1 | Power (+5V)          |
|       2 | Output Enable        |
|       3 | Write Enable         |
|  4 - 11 | Data Bus             |
| 12 - 25 | Address Bus (14-bit) |
>[!hint]
>This would - in theory - fit into a standard DB-25 connector

>[!hint]
>The (32KiB) EEPROM chip AT28C256 is pin-compatible with the already validated 8KiB chip AT28C64B. (The 2 unconnected pins in the AT28C64B are used as the additional 2 address lines).
>
>Using this larger chip (price very similar) would be more cost effective and could allow for smaller cartridges.
>
>While the AT28C256 would hold 32KiB of data, we would only use 16KiB of that.

>[!warning]
>If we use the AT28C256 chip, it may be advisable to expose bit 15 of the address bus as well. This may be required to ensure access to the write protection algorithm on the chip.
> 
> A larger interface (e.g. DA-26) may be advised.






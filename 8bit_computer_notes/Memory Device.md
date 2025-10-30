A _Memory Device_ is a hardware component that interacts with the [[Data Bus]] and the [[Address Bus]] to provide or store data.

## Basic Structure
Any memory device has (at least) the following external connections: ^bb7285
 - [[Address Bus]] as an input
 - [[Data Bus]] as a bidirectional connection
 - 2 control signals coming from the [[Instruction Decoder]]:
	 - memory read trigger
	 - memory write trigger

## Basic Operation
Any memory device always listens to the address bus. If the current address is relevant for the device, it is considered _selected_.

>[!hint]
>A memory device can either listen to a specific address or to a range of addresses

### Memory Read
If a device is _selected_ and the _memory read trigger_ is active, the device can present some data to the data bus.

>[!info]
>If a device does not present any data to the data bus, the bus will implicitly be pulled up to a value of 255 (-1)

### Memory Write
If a device is _selected_ and the _memory write trigger_ is active, the device is instructed to consume the value currently present on the data bus.

## Address Space
Every [[Memory Device]] must be integrated into a single global 16-bit address space. It is not possible for multiple devices to share a common address. 

The full 16-bit address space (64 KiB) split into 8 blocks of 8 KiB each. Each block can (depending on the device) be split into 32 logical pages of 256 bytes each.

We can separate the memory address into 2 sections:
- Bits 0-12: Block-local address (13 bit)
	- Bits 0-7: Page-local address (8 bit)
	- Bits 8-12: Block-local page address (5 bit)
- Bits 13-15: Block selector (3 bit)

The blocks are designated as follows:

| Starting | End      | Device                       |
| -------- | -------- | ---------------------------- |
| `0x0000` | `0x1FFF` | [[#Program ROM]]             |
| `0x2000` | `0x3FFF` | Program ROM (optional)       |
| `0x4000` | `0x5FFF` | [[Input and Output Devices]] |
| `0x6000` | `0x7FFF` | _reserved for future use_    |
| `0x8000` | `0x9FFF` | [[#RAM]]                     |
| `0xA000` | `0xBFFF` | RAM                          |
| `0xC000` | `0xDFFF` | RAM                          |
| `0xE000` | `0xFFFF` | RAM                          |

This mapping allows for 16KiB of Program ROM. It also allocates the full upper half (32KiB) of the address space to RAM.

There is also one reserved block, where future expansions can be added. A potential expansion could be a block of paged ram. See [[Paged RAM (Idea)]] for more details.

## Devices 
### Program ROM
The _Program ROM_ is a _replaceable_ ROM module holding the program. This can be compared to a GameBoy game cartridge.

The _Program ROM_ can use the address range `0x0000-0x3FFF`.
The lower half (`0x0000-0x1FFF`) must always be implemented, the upper half (`0x2000-0x3FFF`) is optional.

>[!hint]
The _Program ROM_ should be physically connected to the computer using a standard interface. This allows the creation of a standard [[Program Cartridge (Idea)]] design.

When integrated into the computer, the _Program ROM_ must be _read-only_.

### RAM
The entire upper half (32KiB) of the address space are used for internal RAM. (Addresses `0x8000-0xFFFF`).

This is implemented as 4 static ram chips (8KiB each) with parallel interfaces. 

All memory locations are always writable and readable.

The physical RAM module also uses an [[Edge Pulse Circuit]] to convert the falling edge of the $\overline{writeMemory}$ into a short ($<10\micro{}s$) low pulse.

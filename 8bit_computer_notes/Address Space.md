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
| `0x0000` | `0x1FFF` | [[Program ROM]]              |
| `0x2000` | `0x3FFF` | [[Program ROM]]              |
| `0x4000` | `0x5FFF` | [[Input and Output Devices]] |
| `0x6000` | `0x7FFF` | _reserved for future use_    |
| `0x8000` | `0x9FFF` | [[RAM]]                      |
| `0xA000` | `0xBFFF` | [[RAM]]                      |
| `0xC000` | `0xDFFF` | [[RAM]]                      |
| `0xE000` | `0xFFFF` | [[RAM]]                      |

This mapping allows for 16KiB of [[Program ROM]]. It also allocates the full upper half (32KiB) of the address space to [[RAM]].

There is also one reserved block, where future expansions can be added. A potential expansion could be a block of paged ram. See [[Paged RAM (Idea)]] for more details.
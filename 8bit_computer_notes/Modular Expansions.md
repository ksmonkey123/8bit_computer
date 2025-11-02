The [[Memory Device#Address Space]] block `0x4000..5FFF` is reserved for use as modular expansion devices.

The lower half is currently assigned for that use, while the upper half is reserved for internal use.

Each device is assigned a single 256 byte page of address space. This allows for up to 16 devices.

Each device is only exposed to the lower 8 bytes of the address bus. Device selection is managed on the expansion backplane.

## Backplane
We use a type of backplane for management of the separate interface ports.

This backplane manages device selection and interrupt handling ([[Interrupt (Idea)]]).

## Interface
We use a standard interface for device connections.
Each port exposes the following signals:

| Pin   | Function                                  |
| ----- | ----------------------------------------- |
| 1..8  | data bus bits 0..7                        |
| 9..16 | address bus bit 0..7                      |
| 17    | $\overline{read}$                         |
| 18    | $\overline{write}$                        |
| 19    | $\overline{reset}$ ([[System Reset]])     |
| 20    | $clock$                                   |
| 21    | interrupt request ([[Interrupt (Idea)]]) |
| 22    | _reserved for future use_                 |
| 23,24 | VCC (+5V)                                 |
| 24,26 | GND                                       |

## Requirements for Expansion Devices
1. Any device may only drive the data bus, when the $\overline{read}$ signal is low.
2. Writes must be performed at the falling edge of the $\overline{write}$ signal. Any device that requires a write pulse instead of the falling edge, must provide its own [[Edge Pulse Circuit]].
3. Any device may drive the $\overline{reset}$ signal low to trigger an external reset.
4. No device may drive the $\overline{reset}$ signal high at any point.
5. Any device wanting to trigger an [[Interrupt (Idea)]], shall drive its interrupt request signal high. Once the interrupt condition is cleared, the device shall release the interrupt request signal or drive it low.
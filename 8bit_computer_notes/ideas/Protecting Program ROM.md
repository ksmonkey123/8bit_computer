[[Program Cartridge (Idea)]]

Protect against accidental write-enable by requiring a negative voltage to be present. This negative voltage would only be generated on demand by a programming circuit. This would protect against any accidental write pulses on the cartridge.

## Generating the Negative Voltage
 An inverting charge pump circuit (e.g. [TC1044SCPA](https://www.distrelec.ch/en/charge-pump-inverting-20ma-dip-microchip-tc1044scpa/p/30302149)) could be used.

Whenever the programming circuit wants to _unlock_ the write feature on the cartridge, this negative voltage is required.
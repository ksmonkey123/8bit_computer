A system reset should be triggerable by a push button.

A system reset must reset some registers and flags to a known state.
After a reset the computer shall resume operation at the _reset vector_ `0x0000`.

| Register / Flag                         | State after Reset |
| --------------------------------------- | ----------------- |
| $PC$ [[Program Counter]]                | `0x00`            |
| $SP$ [[Step Sequencer]]                 | `0x00`            |
| $A,B,C,D$ [[General Purpose Registers]] | `0x00`            |
| $L_1,L_2$ [[Literal Registers]]         | `0x00`            |
| $F_Z,F_S,F_H$ [[Status Flag]]           | `0`               |
| $step$ of [[Step Sequencer]]            | `0`               |

Notably, $ALU_{IN}$, $IncR$ and $F_C$ are not reset.
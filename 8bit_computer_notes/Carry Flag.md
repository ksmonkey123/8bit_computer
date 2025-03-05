The carry flag ($F_C$) is an [[ALU]] [[Status Flag]]. It resides in the ALU and is updated by the ALU whenever a value is read from it.

The Carry Flag has 2 basic roles:
* In the [[ALU Math Unit]] it indicates, whether the last addition caused an integer overflow.
* In the [[ALU Roll Unit]] it is used to set the value of the bit that is shifted in, and to store the bit that was shifted out.

>[!hint]
>while the carry flag cannot be explicitly modified by any instructions, it is possible to manipulate it into a known state by performing ALU calculations on an idle [[Data Bus]].
>
>Setting $ALU_{IN}$ to 255 and performing a right-roll (`RRC`) will set the carry flag.
>
>Performing any bitwise operation (e.g. `AND`) clears the carry flag.











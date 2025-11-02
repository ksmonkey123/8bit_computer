#idea 

Memory devices should be able to trigger interrupts.

An interrupt interrupts the normal program flow.

There shall be a special 16-bit register that can only be written to, but not read from. Let's call it the $IPC$ (Interrupt initial program counter). This register can be written to to set the starting address of the interrupt handler in code.

During each fetch phase (sequencer step 0), when an interrupt is requested, a special instruction is loaded into the [[Instruction Register]] that replaces the real instruction and sets up the processor for the interrupt handler. The "Start Interrupt"-Instruction shall push the current register values onto the stack, as well as the current $PC$ value and then branch to the interrupt handler. The corresponding "Return from Interrupt"-Instruction shall restore the register values from the stack and branch to the previous $PC$ value.

There need to be instructions to enable or disable the interrupt feature. (There may be critical sections in code where interrupts would disturb the program).

There should also be an instruction to manually enter the interrupt handler. (Which is easy, as we do need to inject such an instruction into microcode to enable the whole thing. As long as the assembler supports that instruction, we're good to go).

>[!Warning]
>While interrupts preserve position in the program flow and register values, the status flags are not preserved. Arithmetic routines that depend on flag values must be performed with the interrupts disabled.

## Triggering Interrupts from Expansion Devices.

Interrupts are designed to allow an expansion device ([[Modular Expansions]]) to notify the processor of actions it should take. If _any_ device requests an interrupt, the interrupt handler should be entered.

Once the interrupt handler is entered, there should be a few bytes of memory that contain one bit per device. This helps the interrupt handler to "figure out", which device caused the interrupt.

Once the "causing device" is determined, the subsequent handling of the interrupt is device specific. It is important to handle all interrupts, because the expansion device is the thing that produces the interrupt signal. If we exit the interrupt handler while there's still any active interrupt, the interrupt handler will simply be entered again.

>[!Idea]
> The fact that exiting the interrupt handler while there's an active interrupt signal causes the interrupt handler to be entered again can be useful:
> 
> Instead of complex control logic that handles all interrupt conditions, we can just write a handler that handles the first interrupt it "sees" and exists. Additional interrupts will simply be handled by the next invocation of the handler.

## Configuring Interrupts per Device

There should be a way to enable or disable interrupts not only globally, but also on a "per device" level. A few special bytes in memory could be used to "mask" interrupts. This allows a device to be "locked out" of interrupts, if we don't want it to trigger them for a specific program or phase of a program.

## Proposal


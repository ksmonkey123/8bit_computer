The _Step Sequencer_ takes in the [[Main Clock]] and controls the [[Execution Cycle]].

It contains a 4-bit register storing the current step number.

>[!important]
Upon [[System Reset]] the step sequencer **must** be reset to 0

The sequencer updates based on the following rules:
1. On each falling edge of the clock the sequencer advances by 1
2. If the [[Microcode]] for the current step indicates the end of an instruction ($F_P = \bot$), the sequencer resets to 0.

$$
step' = \begin{cases}
step + 1 & F_P = \top\\
0 & F_P = \bot\\
\end{cases}
$$

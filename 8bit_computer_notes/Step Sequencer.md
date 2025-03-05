The _Step Sequencer_ takes in the [[Main Clock]] and controls the [[Execution Cycle]].

It contains a 3-bit register storing the current step.

>[!important]
Upon [[System Reset]] the step sequencer **must** be reset to 0

The sequencer updates based on the following rules:
1. On each falling edge of the clock the sequencer advances by 1
2. If the current value is 0, _additionally_ advance by the _jump value_ provided by the [[Fetch Microcode]].
3. If the current value indicates that we are at the last step, and the [[Execution Microcode]] indicates that we are on the last step, reset. This is only applicable when $step > 0$

$$
step' = \begin{cases}
0 & step \neq 0; lastStep = \top\\
step + 1 & step \neq 0; lastStep = \bot\\
step + 1 + skip & step = 0\\
\end{cases}
$$

## Publishing of Microcode Parameters

Since the step sequencer is the only component that really knows the current step, it is responsible for managing which parameters get sent to the [[Instruction Decoder]] when.

| Step | [[Address Source]] | [[Data Source]] | [[Data Target]] | [[Action Selector]] |
| ---- | ------------------ | --------------- | --------------- | ------------------- |
| 0    | $PC$               | $memory$        | $IR$            | -                   |
| 1    | $IncR + 1$         | $memory$        | $L_2$           | -                   |
| 2    | $IncR + 1$         | $memory$        | $L_1$           | -                   |
| 3    | $IncR + 1$         | 255             | $ALU_{IN}$      | write $PC$          |
| 4    | $ROM_0$            | $ROM_0$         | $ROM_0$         | $ROM_0$             |
| 5    | $ROM_1$            | $ROM_1$         | $ROM_1$         | $ROM_1$             |
| 6    | $ROM_2$            | $ROM_2$         | $ROM_2$         | $ROM_2$             |
| 7    | $ROM_3$            | $ROM_3$         | $ROM_3$         | $ROM_3$             |



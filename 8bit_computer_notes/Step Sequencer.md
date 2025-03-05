The _Step Sequencer_ takes in the [[Main Clock]] and controls the [[Execution Cycle]].

It contains a 4-bit register storing the current step.

>[!important]
Upon [[System Reset]] the step sequencer **must** be reset to 0

The sequencer updates based on the following rules:
1. On each falling edge of the clock the sequencer advances by 1
2. If the current value is 0, additionally advance by the _jump value_ provided by the [[Fetch Microcode]].
3. If the current value indicates that we are in the [[Execution Phase]] (>7), and the [[Execution Microcode]] indicates that we are on the last step, reset.
4. If the current value is 11, reset.

$$
val' = \begin{cases}
0 & val = 11\\
0 & val \geq 8; lastStep = 1\\
val + 1 & val \neq 0\\
val + 1 + 2*skip & val = 0\\
\end{cases}
$$

>[!todo]
>rework based on [[Improved Fetch Cycle (Idea)]]

## Publishing of Microcode Parameters

Since the step sequencer is the only component that really knows the current step, it is responsible for managing which parameters get sent to the [[Instruction Decoder]] when.

| Step | [[Address Source]] | [[Data Source]] | [[Data Target]] | [[Action Selector]] |
| ---- | ------------------ | --------------- | --------------- | ------------------- |
| 0    | $PC$               | $memory$        | $IR$            | -                   |
| 1    | -                  | -               | -               | -                   |
| 2    | -                  | -               | -               | -                   |
| 3    | $IncR + 1$         | 255             | $ALU_{IN}$      | write $PC$          |
| 4    | $PC$               | $memory$        | $L_2$           | -                   |
| 5    | $IncR + 1$         | 255             | $ALU_{IN}$      | write $PC$          |
| 6    | $PC$               | $memory$        | $L_1$           | -                   |
| 7    | $IncR + 1$         | 255             | $ALU_{IN}$      | write $PC$          |
| 8    | $ROM_0$            | $ROM_0$         | $ROM_0$         | $ROM_0$             |
| 9    | $ROM_1$            | $ROM_1$         | $ROM_1$         | $ROM_1$             |
| 10   | $ROM_2$            | $ROM_2$         | $ROM_2$         | $ROM_2$             |
| 11   | $ROM_3$            | $ROM_3$         | $ROM_3$         | $ROM_3$             |

>[!todo]
>rework based on [[Improved Fetch Cycle (Idea)]]


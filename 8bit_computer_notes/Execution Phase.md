During the _Execution Phase_ the fetched instruction is executed.

Each _Execution Phase_ consists of 1 to 4 steps. ([[Execution Step]]).

During this phase we access the [[Execution Microcode]].

The microcode for each step contains a flag indicating if that is the last step to execute. If this flag is set, we don't proceed to the next step, but instead proceed to the next [[Fetch Phase]]. This is done by resetting the [[Step Sequencer]].

Every program instruction specifies a specific action the computer should perform.

Every instruction can have 2 to 16 [[Execution Cycle#Execution Step|Execution Steps]]. The individual actions taken per step are specified in the [[Microcode]]. This flexibility allows for the definition of arbitrary new instructions simply by reprogramming the [[Microcode#Microcode ROM|Microcode ROM]].

Each instruction is defined by an 8-bit opcode. Instructions can also have additional _literal bytes_. These can be loaded and processed during the instructions cycle.

>[!Warning]
>Even if not required for a given instance of an instruction execution (e.g. if the literal contains the target address for a branching instruction), the instruction must make sure that the [[Program Counter]] is updated correctly. Otherwise this literal data may be interpreted as instructions!

>[!Important]
>As the normal data layout for 16-bit instructions is little endian, the same data layout should be used for 16-bit instruction literals.


The _Instruction Decoder_ is responsible for accepting [[Execution Microcode]] parameters and converting them into dedicated physical control lines.

This also includes making sure, that write commands are only sent when the [[Main Clock]] is high.

During the [[Fetch Phase]] the incoming signals are not coming from the [[Microcode ROM]] but are hardcoded.
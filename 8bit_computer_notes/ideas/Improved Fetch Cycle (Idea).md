In the current concept the [[Fetch Phase]] takes 2 to 6 [[Execution Step|Execution Steps]]. These are:

1. $memory(PC) \rightarrow IR; PC \rightarrow IncR$
2. $IncR + 1 \rightarrow PC$
3. $memory(PC) \rightarrow L_2; PC \rightarrow IncR$
4. $IncR + 1 \rightarrow PC$
5. $memory(PC) \rightarrow L_1; PC \rightarrow IncR$
6. $IncR + 1 \rightarrow PC$

By repeatedly accessing and thereby cycling the [[Increment Unit]], we could - in theory - compress this to just 2 to 4 cycles:

1. $memory(PC) \rightarrow IR; PC \rightarrow IncR$
2. $memory(Incr + 1) \rightarrow L_2; IncR + 1 \rightarrow IncR$
3. $memory(Incr + 1) \rightarrow L_1; IncR + 1 \rightarrow IncR$
4. $IncR + 1 \rightarrow PC$

This way the [[Address Source]] logic would be simplified to use the increment unit for every step except for the first one, where the [[Program Counter]] is used.

It would also condense the entire [[Execution Cycle]] to at most 8 steps, allowing for 3-bit step identification.

#idea
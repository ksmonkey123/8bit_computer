#idea

There is still 1 unused block in [[Address Space]]: `0x6000-0x7FFF`

This could be used for paged memory:

A single byte of the space could be used as a _page selector_.
the remaining 8191 bytes of memory would be freely accessible.

This would allow for 256 pages of 8191 bytes each. This would provide an additional 2MB of additional space.

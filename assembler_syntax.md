ASSEMBLER SYNTAX
================

## Parameters

8-bit registers are referenced by their names:
`A`, `B`, `C`, `D`

16-bit registers are referenced by their combinations:
`AB`, `CD`

Only these 2 combinations are accepted, the first letter is always the lower byte.

8-bit and 16-bit immediate values (literals) are prefixed with `#`. There are multiple supported number formats:

* Decimal: `#105`
* Hexadecimal: `#0x69`
* Binary: `#0b01101001`

All numbers are unsigned. Leading zeroes are optional.
All numbers support `_` as a spacing character _within_ the number at any location beyond the base prefix (`#`, `#0x`,
or `#0b`):
`#0b01101001` and `#0b0110_1000` both represent the decimal value `105`. `#0b_0110_1000` is also allowed.

## Dereferencing

In some cases, we want to treat either a 16-bit register or a number literal as a _pointer_ to a memory location.
In these cases, the value must be _dereferenced_. _Dereferencing_ uses the character `$`

A _dereferenced register_ is represented by `$CD`.
A _dereferenced literal_ is represented by the literal value with the `#` replaced with a `$`:
`$105`, `$0x69`, `$0b01101001`

## Organisation directives

An organisation directive fixes the start of all subsequent statements up to the next organisation directive (or the end
of the file) to a given memory address.

```
.org $0x0000
```

If two sections (a section being all statements associated with an organisational directive) overlap, it must be treated
as an error.

### Code-Block Labels
Any statement can be labelled by adding a label to the same line or in a previous line.
Labels are all of the format `[a-z](_?[a-z0-9])*:`

```
main:
    mov A B
    ...
```

Code block labels can be used in place of literals:

```
loop:
    mov A B
    ...
    GOTO .loop
```

### Memory Allocation

#### Uninitialized, fixed placement

```.data variable_name $adr```

#### Uninitialized, random placement

```
.data variable_name length
```

The variable name follows the pattern `[a-zA-Z](_?[a-zA-Z0-9])*`.
The length defines the number of bytes to be allocated.

This does simply set up a memory pointer without initializing the value.
Note that if a memory allocation points to ROM memory, the value will be `0xff`.

#### 
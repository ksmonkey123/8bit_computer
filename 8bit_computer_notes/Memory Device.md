A _Memory Device_ is a hardware component that interacts with the [[Data Bus]] and the [[Address Bus]] to provide or store data.

## Basic Structure

Any memory device has (at least) the following external connections:
 - [[Address Bus]] as an input
 - [[Data Bus]] as a bidirectional connection
 - 2 control signals coming from the [[Instruction Decoder]]:
	 - memory read trigger
	 - memory write trigger

## Basic Operation

Any memory device always listens to the address bus. If the current address is relevant for the device, it is considered _selected_.

>[!hint]
>A memory device can either listen to a specific address or to a range of addresses

### Memory Read

If a device is _selected_ and the _memory read trigger_ is active, the device can present some data to the data bus.

>[!info]
>If a device does not present any data to the data bus, it will implicitly be pulled up to a value of 255 (-1)

### Memory Write

If a device is _selected_ and the _memory write trigger_ is active, the device is instructed to consume the value currently present on the data bus.

## Address Space

See [[Address Space]] for a complete map.
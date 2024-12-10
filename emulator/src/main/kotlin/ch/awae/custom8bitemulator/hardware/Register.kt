package ch.awae.custom8bitemulator.hardware

/**
 * 8-bit general purpose data register.
 *
 * write signal latches data on the rising edge
 * data is presented to bus while the read signal is high
 */
class Register(
    private val dataBus: WritableBus<UByte>,
    private val readSignal: Signal,
    private val writeSignal: Signal,
) {

    val internalBus: DataBus<UByte>
        get() = _internalBus

    private var _internalBus = WritableBus<UByte>(0u)

    init {
        // store on rising edge
        writeSignal.connect {
            if (it.tryValue()) {
                _internalBus.push(this, dataBus.currentState.tryValue())
            }
        }

        // publish internal value on rising edge of readSignal, drop it when removed
        readSignal.connect {
            if (it.tryValue()) {
                dataBus.push(this, _internalBus.currentState.tryValue())
            } else {
                dataBus.push(this, null)
            }
        }

        // publish internal value on change while readSignal is high
        _internalBus.connect {
            if (readSignal.currentState.tryValue()) {
                dataBus.push(this, _internalBus.currentState.tryValue())
            }
        }
    }

}
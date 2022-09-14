package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener

/**
 * Text Watcher  for masked inputs.
 * During user input [OnValueChange.onChanged] will be emmit [Input] with extracted text value,
 * i.e. text without using a mask
 */
class DynamicMaskedInputWatcher private constructor(
    primaryFormat: String,
    editText: EditText,
    private val callback: MaskedInputValueListener
) : MaskedTextChangedListener(primaryFormat, editText, callback), DynamicInputWatcher by callback {

    constructor(primaryFormat: String, onValueChanged: OnValueChange<Input>?, editText: EditText) :
            this(primaryFormat, editText, MaskedInputValueListener(onValueChanged))

}

/**
 * Internal implementation of combining [MaskedTextChangedListener.ValueListener]
 * and [DynamicInputWatcher]. This impl also ensures the correct operation
 * of [setTextSilent] with masked TextWatchers
 */
private class MaskedInputValueListener constructor(
    override var onValueChanged: OnValueChange<Input>?
) : MaskedTextChangedListener.ValueListener, DynamicInputWatcher {

    override fun onTextChanged(
        maskFilled: Boolean,
        extractedValue: String,
        formattedValue: String
    ) {
        onValueChanged?.onChanged(createInput(extractedValue))
    }

    private fun createInput(text: String): Input {
        return Input(
            text = text,
            source = Input.Source.USER
        )
    }
}
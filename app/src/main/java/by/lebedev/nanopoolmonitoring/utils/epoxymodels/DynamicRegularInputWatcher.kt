package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.text.Editable
import android.text.TextWatcher

/**
 * Text Watcher for regular Input fields, which impl [DynamicInputWatcher].
 */
class DynamicRegularInputWatcher(
    override var onValueChanged: OnValueChange<Input>?
) : TextWatcher, DynamicInputWatcher {


    override fun afterTextChanged(s: Editable?) = Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onValueChanged?.onChanged(createInput(s?.toString() ?: ""))
    }

    /** Create Input object with USER source type*/
    private fun createInput(text: String): Input {
        return Input(
            text = text,
            source = Input.Source.USER
        )
    }
}


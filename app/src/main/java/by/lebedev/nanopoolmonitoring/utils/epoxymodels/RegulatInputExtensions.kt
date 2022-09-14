package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.adapters.ListenerUtil
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.utils.isTextDifferent

/**
 * Combined method with [setInputAndCursor] and [setOnInputChange] functionality.
 * Which is ensure right call order of set up methods.
 */
fun EditText.setUpInput(tag: String, input: Input?, callback: OnValueChange<Input>?) {
    setOnInputChange(callback)
    setInputAndCursor(tag, input)
}

/**
 * Bind [input] with [EditText] receiver.
 *  Also update text and cursor position in receiver if it necessary.
 */
fun EditText.setInputAndCursor(tag: String, input: Input?) {
    // Handle null case
    if (input == null) {
        //reset tag
        inputTag = null
        if (isTextDifferent(null, this.text)) {
            // reset text if needed
            text = null
        }
        return
    }

    if (inputTag != tag) {
        inputTag = tag
        if (isTextDifferent(input.text, this.text)) {
            setTextSilent(input.text)
            // If the text changed then we move the cursor to the end of the new text.
            // This allows us to fill in text programmatically if needed, like a search suggestion,
            // but if the user is typing and the view is rebound we won't lose their cursor position.
            setSelection(length())
        }
    } else if (isTextDifferent(input.text, this.text) && input.isFromState) {
        setTextSilent(input.text)
        setSelection(length())
    }
}

/** Property for input tag access*/
var EditText.inputTag: String?
    get() = getTag(R.id.tag_input_id) as? String
    set(value) = setTag(R.id.tag_input_id, value)

/**
 * Set [text] to [EditText] receiver, without triggering of [OnValueChange.onChanged]
 *
 * Note: This method will work correctly if the [TextWatcher] associated with receiver
 * is implemented [DynamicInputWatcher]
 */
fun EditText.setTextSilent(text: CharSequence) {
    val watcher: TextWatcher? =
        ListenerUtil.getListener<TextWatcher>(this, R.id.tag_text_dynamic_watcher)
    if (watcher is DynamicInputWatcher) {
        val callback = watcher.onValueChanged
        watcher.onValueChanged = null
        setText(text)
        watcher.onValueChanged = callback
    } else {
        setText(text)
    }
}

/**
 * Set [OnValueChange] for regular [Input].
 *
 * Note: This method is safe from TextWatcher duplication. Only one instance of [OnValueChange]
 * can be linked to EditText at one times.
 */
fun EditText.setOnInputChange(callback: OnValueChange<Input>?) {
    // get previous TextWatcher for this EditText
    val watcher: TextWatcher? =
        ListenerUtil.getListener<TextWatcher>(
            this,
            R.id.tag_text_dynamic_watcher
        )
    if (watcher != null && watcher is DynamicRegularInputWatcher) {
        // watcher exist and is DynamicRegularInputWatcher
        // NOTE: is check cast NOT for DynamicInputWatcher
        watcher.onValueChanged = callback
    } else {
        // if watcher is null or watcher is not class of Regular input Watcher
        //remove previous
        removeTextChangedListener(watcher)
        // create new RegularInputWatcher
        val newWatcher =
            DynamicRegularInputWatcher(callback)
        addTextChangedListener(newWatcher)
        ListenerUtil.trackListener(
            this, newWatcher,
            R.id.tag_text_dynamic_watcher
        )
    }
}

/** Method for clearing text, TextWatcher and inputTag. */
fun EditText.clearSetUp() {
    val watcher: TextWatcher? =
        ListenerUtil.getListener<TextWatcher>(
            this,
            R.id.tag_text_dynamic_watcher
        )
    removeTextChangedListener(watcher)
    inputTag = null
    setTag(R.id.tag_text_dynamic_watcher, null)
    if (isTextDifferent(null, this.text)) {
        // reset text if needed
        text = null
    }
}
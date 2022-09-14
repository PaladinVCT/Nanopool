package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.text.TextWatcher

/**
 * Common interface for TextWatchers which should emmit new Input object,
 * by call [OnValueChange.onChanged] during user input.
 *
 * Designed to prevent the allocation of [TextWatcher] objects
 * when happening repeat assigned of [onValueChanged].
 *
 * The implementation of this interface by TextWatcher ensures the correct operation
 * of [setTextSilent].
 */
interface DynamicInputWatcher {
    var onValueChanged: OnValueChange<Input>?
}
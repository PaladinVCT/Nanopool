package by.lebedev.nanopoolmonitoring.utils.epoxymodels

interface OnValueChange<T> {
    fun onChanged(value: T)
}

interface OnTextChanged : OnValueChange<String>
interface OnInputChanged : OnValueChange<Input>
interface OnBooleanChanged : OnValueChange<Boolean>
interface OnIntChanged : OnValueChange<Int>
interface OnFloatChanged : OnValueChange<Float>
interface OnDoubleChanged : OnValueChange<Float>



inline fun <T> OnValueChange(crossinline block: (value: T) -> Unit) =
    object : OnValueChange<T> {
        override fun onChanged(value: T) = block(value)
    }

inline fun OnTextChanged(crossinline block: (value: String) -> Unit) =
    object : OnTextChanged {
        override fun onChanged(value: String) = block(value)
    }

inline fun OnInputChanged(crossinline block: (value: Input) -> Unit) =
    object : OnInputChanged {
        override fun onChanged(value: Input) = block(value)
    }

inline fun OnBooleanChanged(crossinline block: (value: Boolean) -> Unit) =
    object : OnBooleanChanged {
        override fun onChanged(value: Boolean) = block(value)
    }

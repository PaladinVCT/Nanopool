package by.lebedev.nanopoolmonitoring.utils.epoxymodels

/**
 * Wrapper under text for InputField(as EditText) in MxRx + Epoxy arch,
 * which also has info about emitter([source] param) of Input object.
 *
 * @param text is textual representation in InputField
 * @param source is information flag that lets know who the input emitter is. If text was changed in
 * ViewModel should use [Source.STATE] flag or if text came form user input use [Source.USER]
 */
data class Input constructor(
    val text: String = "",
    val source: Source = Source.STATE
) : CharSequence by text {
    enum class Source {
        USER,
        STATE;
    }

    override fun toString() = text
}

/** Method for create Input text object with specifying the source as [Input.Source.USER]. */
fun inputUser(text: String) = Input(text = text, source = Input.Source.USER)

/** Method for create Input text object with specifying the source as [Input.Source.STATE]. */
fun inputState(text: String) = Input(text = text, source = Input.Source.STATE)

val Input.isFromUser: Boolean
    get() = source == Input.Source.USER

val Input.isFromState: Boolean
    get() = source == Input.Source.STATE



package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.core.view.setPadding
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.epoxy.*
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/**
 * General model to represent TextInputLayout into EpoxyController.
 *
 * In this model callback props annotated with [ModelProp.options] equal
 * [ModelProp.Option.IgnoreRequireHashCode] instead [CallbackProp], it make sense in case
 * when your model needs to be updated after changing of callbacks props.
 *
 * For [CallbackProp] annotation, callbacks props will be updated only if props mutate
 * from null -> NotNullObject and vice versa. Therefore when you try update you callback from
 * NotNullObject_1 -> NotNullObject_2, new NotNullObject_2 callback **will be no assigned** into
 * props at model. **This can cause difficult to catch bugs**, where you keep links to some
 * _ViewState's_ objects inside the callback, and after view state changing you can expect
 * that your callback will be reassigned with new callback which already keep links to new
 * _ViewState's objects, but with [CallbackProp] annotation it will be no happened.
 * Also see suggested solutions from [Epoxy Wiki](https://github.com/airbnb/epoxy/wiki/DoNotHash)
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
open class InputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val inputView by lazy { findViewById<TextInputLayout>(R.id.input) }

    @set:ModelProp
    var input: Input? = null

    @set:ModelProp
    var tag: String = ""

    @set:TextProp
    var mask: CharSequence? = null

    @set:TextProp
    var digits: CharSequence? = null

    @set:ModelProp
    var inputFilters: List<InputFilter>? = null

    @set:ModelProp(options = [ModelProp.Option.IgnoreRequireHashCode])
    var onChange: OnValueChange<Input>? = null

    @set:ModelProp(options = [ModelProp.Option.IgnoreRequireHashCode])
    var onEndIconClickListener: OnClickListener? = null

    @set:ModelProp(options = [ModelProp.Option.IgnoreRequireHashCode])
    var onChildClickListener: OnClickListener? = null

    @set:ModelProp(options = [ModelProp.Option.IgnoreRequireHashCode])
    var focusListener: OnFocusChangeListener? = null


    /**
     * @set:ModelProp isn't using here because, initial value won't be assign in Generated Java Model
     * value will be default for the corresponding primitive type
     */
    private var endIconMode: Int = 0
    private var maxLength: Int = 0
    private var inputType: Int = 0


    init {
        inflate(context, R.layout.view_input, this)

//        inputView.editText?.doOnTextChanged { text, start, before, count ->
//            val language = LocaleHelper.getLanguage(context)
//
//            Timber.e("language = $language, text = $text")
//
//            if (isAllCapsText) {
//
//            }
////            if (language.contains("AZ", true)) {
////                mergeInputFilters()
////            }
//        }
    }

    @JvmOverloads
    @ModelProp
    fun setMaxLength(length: Int = 255) {
        maxLength = length
    }

    @JvmOverloads
    @ModelProp
    fun setEndIconMode(mode: Int = TextInputLayout.END_ICON_NONE) {
        endIconMode = mode
    }

    @set:ModelProp
    var isAllCapsText: Boolean = false

    @ModelProp
    @JvmOverloads
    fun isEnabled(isEnabled: Boolean = true) {
        inputView.isEnabled = isEnabled
    }

    @JvmOverloads
    @TextProp
    fun setHint(hint: CharSequence? = null) {
        inputView.updateHint(hint)
    }

    @JvmOverloads
    @TextProp
    fun setHelperText(helperText: CharSequence? = null) {
        inputView.updateHelperText(helperText)
    }

    @JvmOverloads
    @ModelProp
    fun helperTextEnabled(isEnabled: Boolean = false) {
        inputView.isHelperTextEnabled = isEnabled
    }

    @JvmOverloads
    @TextProp
    fun setError(error: CharSequence? = null) {
        inputView.updateError(error)
    }


    @JvmOverloads
    @TextProp
    fun setPrefixText(prefixText: CharSequence? = null) {
        inputView.prefixText = prefixText
    }

    @JvmOverloads
    @TextProp
    fun setSuffixText(suffixText: CharSequence? = null) {
        inputView.suffixText = suffixText
    }

    @JvmOverloads
    @ModelProp
    fun setInputType(inputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES) {
        this.inputType = inputType
    }

    @JvmOverloads
    @ModelProp
    fun errorEnabled(isEnabled: Boolean = false) {
        inputView.isErrorEnabled = isEnabled
    }

    @JvmOverloads
    @ModelProp
    fun counterEnabled(isEnabled: Boolean = false) {
        inputView.isCounterEnabled = isEnabled
    }

    @JvmOverloads
    @ModelProp
    fun setCounterMaxLength(counterMaxLength: Int = -1) {
        inputView.counterMaxLength = counterMaxLength
    }

    @JvmOverloads
    @ModelProp(group = "padding")
    fun setPadding(padding: PaddingView? = null) {
        setViewPadding(padding)
    }

    @ModelProp(group = "padding")
    fun setPaddingDp(@Dimension(unit = Dimension.DP) paddingDp: Int) {
        val px = context.dpToPx(paddingDp)
        setPadding(px)
    }

    @ModelProp(group = "padding")
    fun setPaddingRes(@DimenRes paddingRes: Int) {
        val px = context.resToPx(paddingRes)
        setPadding(px, px, px, px)
    }

    @JvmOverloads
    @ModelProp
    fun setFocusableInput(focusable: Boolean = true) {
        inputView.editText?.isFocusable = focusable
        inputView.editText?.isFocusableInTouchMode = focusable
    }

    @JvmOverloads
    @ModelProp
    fun setCursorVisible(isCursorVisible: Boolean = true) {
        inputView.editText?.isCursorVisible = isCursorVisible
    }

    @JvmOverloads
    @ModelProp
    fun setVisible(isVisible: Boolean = true) {
        inputView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @JvmOverloads
    @ModelProp(group = "endIcon")
    fun setEndIconRes(@DrawableRes drawable: Int = 0) {
        inputView.setEndIconDrawable(drawable)
    }

    @ModelProp(group = "endIcon", options = [ModelProp.Option.IgnoreRequireHashCode])
    fun setEndIconDrawable(drawable: Drawable?) {
        inputView.endIconDrawable = drawable
    }

    @ModelProp(group = "minWidth")
    fun setMinWidthDp(@Dimension(unit = Dimension.DP) paddingDp: Int) {
        val px = context.dpToPx(paddingDp)
        minimumWidth = px
        inputView.minimumWidth = px
    }

    @JvmOverloads
    @ModelProp(group = "minWidth")
    fun setMinWidthRes(@DimenRes paddingRes: Int = 0) {
        val px = context.resToPx(paddingRes)
        minimumWidth = px
        inputView.minimumWidth = px
    }

    @AfterPropsSet
    fun update() {
        val digits = digits
        if (digits != null) {
            val keyListener = DigitsSmartKeyListener(digits.toString())
            inputView.editText?.updateKeyListener(keyListener)
        } else {
            inputView.editText?.updateInputType(inputType)
        }

        inputView.editText?.filters = mergeInputFilters()

        val mask: String? = mask?.toString()
        if (mask.isNullOrBlank()) {
            inputView.editText?.setUpInput(tag, input, onChange)
        } else {
            inputView.editText?.setUpMaskedInput(tag, mask, input, onChange)
        }

        if (endIconMode != inputView.endIconMode) {
            inputView.endIconMode = endIconMode
        }

        if (onEndIconClickListener == null) {
            inputView.setEndIconOnClickListener(onChildClickListener)
        } else {
            inputView.setEndIconOnClickListener(onEndIconClickListener)
        }
        inputView.editText?.setOnClickListener(onChildClickListener)

        inputView.editText?.onFocusChangeListener = focusListener
    }

    private fun mergeInputFilters(): Array<InputFilter> {
        val merged: List<InputFilter> = inputFilters.orEmpty().toMutableList().apply {
            if (isAllCapsText) {


//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//                    val imm = getSystemService(context, InputMethodManager::class.java)
//                    val keyboardLang =
//                        imm?.currentInputMethodSubtype?.languageTag//?.split("-")?.get(0)
//                    Timber.e("keyboard lang = $keyboardLang")
//                    if (!keyboardLang.isNullOrEmpty()) {
//                        val locale = Locale(keyboardLang)
//                        add(InputFilter.AllCaps(locale))
//                    } else add(InputFilter.AllCaps())
//                } else
                    add(InputFilter.AllCaps())
            }
            if (maxLength >= 0) {
                add(InputFilter.LengthFilter(maxLength))
            }
        }
        return merged.toTypedArray()
    }


    @OnViewRecycled
    fun clear() {
        isEnabled(true)
        //setHint()
        setHelperText()
        helperTextEnabled()
        setError()
        setPrefixText()
        setInputType()
        errorEnabled()
        setPadding()
        setFocusableInput()
        setCursorVisible()
        setVisible()
        setEndIconRes(0)
        setMinWidthRes()
        counterEnabled()
        setCounterMaxLength()
        input = null
        mask = null
        digits = null
        inputFilters = null
        onChange = null
        onEndIconClickListener = null
        onChildClickListener = null
        focusListener = null
        inputView.editText?.filters = emptyArray()
        inputView.endIconMode = TextInputLayout.END_ICON_NONE
        inputView.setEndIconOnClickListener(null)
        inputView.updateError(null)
        inputView.editText?.setOnClickListener(null)
        inputView.editText?.clearSetUp()
    }

}
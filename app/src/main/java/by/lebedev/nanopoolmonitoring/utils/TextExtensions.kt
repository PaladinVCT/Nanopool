@file:JvmName("TextUtils")

package by.lebedev.nanopoolmonitoring.utils


import android.content.Context
import android.text.Editable
import android.text.SpannedString
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.core.text.*
import by.lebedev.nanopoolmonitoring.data.entities.approximated.ApproximatedEarnings
import com.google.android.material.textfield.TextInputLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener

var TextInputLayout.text: String
    get() = editText!!.text.toString()
    set(value) = editText!!.setText(value)

var EditText.content: String
    get() = text.toString()
    set(value) = setText(value)

var TextInputLayout.textNotEmpty: String
    get() = editText!!.text.toString()
    set(value) {
        if (value.isNotBlank()) editText!!.setText(value)
    }

fun TextInputLayout.setText(@StringRes text: Int) = editText?.setText(text)

fun TextInputLayout.isEmpty() = text.isEmpty()

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

        override fun afterTextChanged(s: Editable) {
            afterTextChanged(s.toString())
        }
    })
}

fun EditText.setEditable(isEditable: Boolean) {
    isFocusable = isEditable
    isFocusableInTouchMode = isEditable
    isClickable = isEditable
    isLongClickable = isEditable
    isCursorVisible = isEditable
}

fun TextInputLayout.setMask(
    mask: String,
    showHint: Boolean = false,
    handleValue: (extractedValue: String) -> Unit
) {
    editText?.setMask(mask, showHint, handleValue)
}

fun EditText.setMask(
    mask: String,
    showHint: Boolean = false,
    handleValue: (extractedValue: String) -> Unit
) {
    val listener = MaskedTextChangedListener.installOn(
        this,
        mask,
        object : MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(
                maskFilled: Boolean,
                extractedValue: String,
                formattedValue: String
            ) {
                handleValue(extractedValue)
            }
        }
    )
    if (showHint) hint = listener.placeholder()
}

fun TextInputLayout.setOnChildsClickListener(listener: ((View) -> Unit)?) {
    editText?.setOnClickListener(listener)
    setEndIconOnClickListener(listener)
}


fun String.toUnderlinedSpanned(): SpannedString = buildSpannedString {
    underline {
        append(this@toUnderlinedSpanned)
    }
}

@JvmOverloads
fun separateByFour(accNumber: String): String {
    return accNumber.chunked(4).joinToString(separator = " ")
}

fun roundToDigit(amount: Double?, digit: Int): String {
    return amount?.round(digit)?.toBigDecimal()?.toPlainString() ?: "0.0"
}

fun roundToDigitFiat(
    aprx: ApproximatedEarnings?,
    timeframe: TimeFrame,
    fiat: String,
    digit: Int = 2
): String {

    return when (timeframe) {
        TimeFrame.SECONDS -> ""
        TimeFrame.MINUTE -> {
            when (fiat) {
                "USD" -> roundToDigit(aprx?.earnings?.minute?.dollars, digit)
                "RUR" -> roundToDigit(aprx?.earnings?.minute?.rubles, digit)
                "EUR" -> roundToDigit(aprx?.earnings?.minute?.euros, digit)
                "GBP" -> roundToDigit(aprx?.earnings?.minute?.pounds, digit)
                "CNY" -> roundToDigit(aprx?.earnings?.minute?.yuan, digit)
                else -> ""
            }
        }

        TimeFrame.HOUR -> {
            when (fiat) {
                "USD" -> roundToDigit(aprx?.earnings?.hour?.dollars, digit)
                "RUR" -> roundToDigit(aprx?.earnings?.hour?.rubles, digit)
                "EUR" -> roundToDigit(aprx?.earnings?.hour?.euros, digit)
                "GBP" -> roundToDigit(aprx?.earnings?.hour?.pounds, digit)
                "CNY" -> roundToDigit(aprx?.earnings?.hour?.yuan, digit)
                else -> ""
            }
        }
        TimeFrame.DAY -> {
            when (fiat) {
                "USD" -> roundToDigit(aprx?.earnings?.day?.dollars, digit)
                "RUR" -> roundToDigit(aprx?.earnings?.day?.rubles, digit)
                "EUR" -> roundToDigit(aprx?.earnings?.day?.euros, digit)
                "GBP" -> roundToDigit(aprx?.earnings?.day?.pounds, digit)
                "CNY" -> roundToDigit(aprx?.earnings?.day?.yuan, digit)
                else -> ""
            }
        }
        TimeFrame.WEEK -> {
            when (fiat) {
                "USD" -> roundToDigit(aprx?.earnings?.week?.dollars, digit)
                "RUR" -> roundToDigit(aprx?.earnings?.week?.rubles, digit)
                "EUR" -> roundToDigit(aprx?.earnings?.week?.euros, digit)
                "GBP" -> roundToDigit(aprx?.earnings?.week?.pounds, digit)
                "CNY" -> roundToDigit(aprx?.earnings?.week?.yuan, digit)
                else -> ""
            }
        }
        TimeFrame.MONTH -> {
            when (fiat) {
                "USD" -> roundToDigit(aprx?.earnings?.month?.dollars, digit)
                "RUR" -> roundToDigit(aprx?.earnings?.month?.rubles, digit)
                "EUR" -> roundToDigit(aprx?.earnings?.month?.euros, digit)
                "GBP" -> roundToDigit(aprx?.earnings?.month?.pounds, digit)
                "CNY" -> roundToDigit(aprx?.earnings?.month?.yuan, digit)
                else -> ""
            }
        }
        TimeFrame.INFINITE -> ""
    }

}

fun String.roundToNearestValue(): String {
    var digit = 2
    var amount = this.toDoubleOrNull()?.round(2) ?: 0.0

    while (amount == 0.0) {
        if (digit==8) break
        digit++
        amount = this.toDoubleOrNull()?.round(digit) ?: 0.0
    }
    return amount.toBigDecimal().toString()
}
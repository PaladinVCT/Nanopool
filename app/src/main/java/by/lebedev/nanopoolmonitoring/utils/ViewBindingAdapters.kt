package by.lebedev.nanopoolmonitoring.utils


import android.net.Uri
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.MenuRes
import androidx.annotation.Px
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import by.lebedev.nanopoolmonitoring.R
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.slider.Slider
import com.google.android.material.tabs.TabLayout
import java.io.File
import java.util.regex.Pattern


/**
 * Data Binding adapters specific to the app.
 */

@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.isVisible = show
}

@BindingAdapter("visibleInvisible")
fun showHideInvisible(view: View, show: Boolean) {
    view.isInvisible = !show
}

@BindingAdapter("addDividerItemDecoration")
fun addDividerItemDecoration(recyclerView: RecyclerView, required: Boolean) {
    if (required) {
        recyclerView.addDividerItemDecoration()
    }
}

/**
 * Locking focusable behaviour (to block the keyboard) and don't allow text inputting.
 */
@BindingAdapter("nonEditableClickable")
fun setNonEditableClickable(editText: EditText, isNotEnable: Boolean) {
    editText.apply {
        if (isNotEnable) {
            isFocusable = false
            isCursorVisible = false
        } else {
            isFocusable = true
            isCursorVisible = true
        }
    }
}

@BindingAdapter("inflateMenu")
fun setMenu(toolbar: Toolbar, @MenuRes resId: Int?) {
    resId?.let { toolbar.inflateMenu(it) }
}

@BindingAdapter("textHtml")
fun TextView.setHtml(htmlString: String) {
    text = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

@BindingAdapter("srcFromFile")
fun ImageView.setImageFromFile(imageFile: File?) {
    setImageURI(imageFile?.let { Uri.fromFile(it) })
}

@BindingAdapter("bindTabSelected")
fun bindTabSelected(view: TabLayout, index: Int) {
    if (view.selectedTabPosition != index) {
        view.getTabAt(index)?.select()
    }
}

@BindingAdapter(value = ["bindQuery", "bindQuerySubmit"], requireAll = true)
fun bindQuery(view: SearchView, query: String, submit: Boolean) {
    if (!TextUtils.equals(view.query, query)) {
        view.setQuery(query, submit)
    }
}

@BindingAdapter("onChange")
fun onChange(view: Slider, onSliderChange: Slider.OnChangeListener) {
    view.addOnChangeListener(onSliderChange)
}

@BindingAdapter("onChange")
fun onChange(view: SeekBar, onSliderChange: SeekBar.OnSeekBarChangeListener) {
    view.setOnSeekBarChangeListener(onSliderChange)
}

@BindingAdapter("value")
fun setValue(view: Slider, value: Float) {
    view.value = value
}

@InverseBindingAdapter(attribute = "value")
fun getValue(view: Slider): Float {
    return view.value
}

@BindingAdapter("hideCursor")
fun EditText.hideCursor(flag: Boolean?) {
    if (flag != null) {
        this.isCursorVisible = !flag
    } else {
        this.isCursorVisible = true
    }

    if (this.filters.isEmpty()) this.filters = arrayOf(object : InputFilter {
        val mPattern = Pattern.compile("[0-9]{0," + 7 + "}+((\\.[0-9]{0," + 1 + "})?)||(\\.)?")
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence {
            val matcher = mPattern.matcher(dest ?: "")
            if (!matcher.matches() || (source == "." && dstart == 0)) return ""
            return source ?: "0.00"
        }
    })
}

@BindingAdapter("setAmountValue")
fun EditText.setAmountValue(value: String) {
    this.setText(value)
    this.setSelection(value.length)
}

@BindingAdapter("setFocusChangedListener")
fun EditText.setFocusChangeListener(value: View.OnFocusChangeListener) {
    this.onFocusChangeListener = value
}

@BindingAdapter("setTextRes")
fun TextView.setTextRes(resId: Int) {
    this.setText(resId)
}

@BindingAdapter("isPlaying")
fun LottieAnimationView.isPlaying(flag: Boolean) {
    if (flag) this.playAnimation()
    else this.cancelAnimation()
}

@BindingAdapter(value = ["setTotalWorkers", "isLoading"], requireAll = true)
fun TextView.setTotalWorkers(number: Int, isLoading: Boolean) {
    text =
        if (isLoading) this.context.getString(R.string.common_loading) else this.context.getString(
            R.string.total_workers,
            number
        )
}

@BindingAdapter(value = ["styleAliveWorkers", "styleDeadWorkers"], requireAll = true)
fun TextView.styleWorkers(alive: Int, dead: Int) {
    val spannnedText = SpannableString("$alive\n$dead")
    spannnedText.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(this.context, R.color.orange)),
        alive.toString().length,
        spannnedText.length,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    text = spannnedText
}

@BindingAdapter("setSpinnerListener")
fun Spinner.setSpinnerListener(listener: OnItemSelectedListener) {
    onItemSelectedListener = listener
}
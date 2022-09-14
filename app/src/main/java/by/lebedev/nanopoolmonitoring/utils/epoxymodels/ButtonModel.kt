package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.core.view.setPadding
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.utils.PaddingView
import by.lebedev.nanopoolmonitoring.utils.dpToPx
import by.lebedev.nanopoolmonitoring.utils.resToPx
import by.lebedev.nanopoolmonitoring.utils.setViewPadding
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ButtonModel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val buttonView by lazy { findViewById<TextView>(R.id.button) }

    init {
        inflate(context, R.layout.view_button, this)
    }

    @TextProp
    @JvmOverloads
    fun setText(text: CharSequence? = null) {
        buttonView.text = text
    }

    @CallbackProp
    @JvmOverloads
    fun onClick(onClick: OnClickListener? = null) {
        buttonView.setOnClickListener(onClick)
    }

    @ModelProp
    @JvmOverloads
    fun setHeight(height: Int = LayoutParams.WRAP_CONTENT) {
        buttonView.layoutParams = buttonView.layoutParams.also {
            val prams = it as LayoutParams
            prams.height = height
        }
    }

    @ModelProp
    @JvmOverloads
    fun isEnabled(isEnabled: Boolean = true) {
        buttonView.isEnabled = isEnabled
    }

    @ModelProp
    @JvmOverloads
    fun setWidth(width: Int = LayoutParams.WRAP_CONTENT) {
        buttonView.layoutParams = buttonView.layoutParams.also {
            val prams = it as LayoutParams
            prams.width = width
        }
    }

    @ModelProp
    @JvmOverloads
    fun setGravity(gravity: Int = Gravity.NO_GRAVITY) {
        buttonView.layoutParams = buttonView.layoutParams.also { params ->
            if (params is LayoutParams) {
                params.gravity = gravity
            }
        }
    }

    @ModelProp(group = "padding")
    fun setPadding(padding: PaddingView?) {
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
        setPadding(px)
    }
}
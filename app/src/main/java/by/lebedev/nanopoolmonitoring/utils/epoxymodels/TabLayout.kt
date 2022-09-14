package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        addView(LinearLayout(context).also {
            val params = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            params.gravity = Gravity.CENTER_HORIZONTAL
            it.layoutParams = params
            it.orientation = HORIZONTAL
        })
    }
    @ModelProp
    fun setCount(count: Int) {
        // TODO: Inflate <count> tiny-little Check Boxes.
    }
    @ModelProp
    fun setChecked(index: Int) {
        // TODO: Check the little-tiny Check Box at <index>.
    }
}
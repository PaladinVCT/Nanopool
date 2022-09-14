package by.lebedev.nanopoolmonitoring.utils.decorators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import by.lebedev.nanopoolmonitoring.utils.PaddingView
import by.lebedev.nanopoolmonitoring.utils.toPxType
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel

typealias ModelFilter = (EpoxyModel<*>) -> Boolean

/**
 * DividerItemDecoration is a [RecyclerView.ItemDecoration] that can be used as a divider
 * between view into [RecyclerView] which are related to models which matched to [filterModels]
 * predicate
 */
class DividerModelDecoration(
    private val context: Context, val controller: EpoxyController,
    padding: PaddingView = PaddingView.px(),
    private var filterModels: ModelFilter? = null
) : ItemDecoration() {
    companion object {
        private const val TAG = "DividerItem"
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    /**
     * @return the [Drawable] for this divider.
     */
    var drawable: Drawable?
        private set

    private var paddingView: PaddingView = padding.toPxType(context)

    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
     */
    private val mBounds = Rect()


    /**
     * Creates a divider [RecyclerView.ItemDecoration] that can be used with a
     * [LinearLayoutManager].
     *
     * @param context Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be [.HORIZONTAL] or [.VERTICAL].
     */
    init {
        val a = context.obtainStyledAttributes(ATTRS)
        drawable = a.getDrawable(0)
        a.recycle()
    }

    /**
     * Sets the [Drawable] for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */

    fun setDrawable(drawable: Drawable) {
        this.drawable = drawable
    }

    fun setPaddingView(padding: PaddingView) {
        paddingView = padding.toPxType(context)
    }

    fun setFilter(filter: ModelFilter?) {
        filterModels = filter
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.layoutManager == null || drawable == null) {
            return
        }
        drawVertical(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft + paddingView.left
            right = parent.width - parent.paddingRight - paddingView.right
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0 + paddingView.left
            right = parent.width - paddingView.right
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i) ?: continue
            if (!matchModel(parent, child)) continue

            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - drawable!!.intrinsicHeight
            drawable!!.setBounds(left, top, right, bottom)
            drawable!!.draw(canvas)
        }
        canvas.restore()
    }

    private fun matchModel(parent: RecyclerView, child: View): Boolean {
        val adapterPosition = parent.getChildAdapterPosition(child)
        return if (adapterPosition != RecyclerView.NO_POSITION) {
            filterModels?.invoke(controller.adapter.getModelAtPosition(adapterPosition)) ?: false
        } else false

    }


    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (drawable == null) {
            outRect[0, 0, 0] = 0
            return
        }
        outRect[0, 0, 0] = drawable!!.intrinsicHeight
    }

}
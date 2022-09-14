package by.lebedev.nanopoolmonitoring.utils.decorators

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.utils.getThemeColor


class LinePagerIndicatorDecoration(context: Context) : RecyclerView.ItemDecoration() {

    val DP = Resources.getSystem().displayMetrics.density
    private var indicatorHeight = 8 * DP
    private var indicatorStrokeWidth = 8 * DP
    private var indicatorItemPadding = 8 * DP
    private var indicatorItemLength = 8 * DP
    private val highlightColor: Int = context.getColor(R.color.orange)
    private val regularColor: Int = context.getColor(R.color.regular_dot_color)
    private val interpolator = AccelerateDecelerateInterpolator()
    private val cornerRadius = indicatorStrokeWidth / 2
    private var showIndicatorsIfSingle = true

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        color = Color.BLACK
    }

    private val paintHighlight = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        color = highlightColor

    }

    fun setShowIndicatorsIfSingle(isSingleShow: Boolean) {
        showIndicatorsIfSingle = isSingleShow
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (showIndicatorsIfSingle || state.itemCount > 1) {
            outRect.bottom = indicatorHeight.toInt()
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (!showIndicatorsIfSingle && state.itemCount <= 1) {
            return
        }
        val itemCount = state.itemCount

        when (itemCount) {
            in 2..9 -> setSize(8)
            in 10..20 -> setSize(6)
            in 21..1000 -> setSize(4)
        }

        val totalLength = calculateIndicatorLength(itemCount)
        val startX = (parent.width - totalLength) / 2f
        val startY = parent.height - parent.paddingBottom - indicatorHeight / 2f
        clip(c, startX, startY, itemCount)
        drawIndicator(c, startX, startY, itemCount)
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val activePosition = layoutManager.findFirstVisibleItemPosition()
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }

        val activeChild = layoutManager.findViewByPosition(activePosition)!!
        val step = indicatorItemLength + indicatorItemPadding
        val left: Float = resolveLeftEdge(parent, activeChild).toFloat()
        val width = resolveWidth(parent, activeChild).toFloat()
        val progress =
            interpolator.getInterpolation(((-left) / width)) * step
        drawHighlightIndicator(c, startX, startY, activePosition, progress, itemCount)
    }

    private fun setSize(sizeMultiplier: Int) {
        indicatorHeight = sizeMultiplier * DP
        indicatorStrokeWidth = sizeMultiplier * DP
        indicatorItemPadding = sizeMultiplier * DP
        indicatorItemLength = sizeMultiplier * DP
    }

    /**
     * Method for calculation of the left edge of [activeChild] with all margins and padding
     * If [activeChild] in start of span, left edge can be = 0
     */
    private fun resolveLeftEdge(parent: RecyclerView, activeChild: View): Int {
        val padding = activeChild.paddingLeft + parent.paddingLeft
        val margins = activeChild.marginLeft + parent.marginLeft
        return activeChild.left - padding - margins
    }

    /**
     * Method for calculation width of [activeChild] with all margins and padding
     */
    private fun resolveWidth(parent: RecyclerView, activeChild: View): Int {
        val padding =
            activeChild.paddingStart + activeChild.paddingEnd + parent.paddingStart + parent.paddingEnd
        val margins =
            activeChild.marginStart + activeChild.marginEnd + parent.marginStart + parent.marginEnd
        return activeChild.width + padding + margins
    }

    private val clipPath = Path()

    private fun clip(c: Canvas, startX: Float, startY: Float, itemCount: Int) {
        val step = indicatorItemLength + indicatorItemPadding
        clipPath.reset()
        paint.color = regularColor
        val safeWidth = 0
        repeat(itemCount) { index ->
            clipPath.addRoundRect(
                startX + step * index - safeWidth,
                startY - indicatorStrokeWidth / 2 - safeWidth,
                startX + indicatorItemLength + step * index + safeWidth,
                startY + indicatorStrokeWidth / 2 + safeWidth,
                cornerRadius,
                cornerRadius,
                Path.Direction.CW
            )
        }
        c.clipPath(clipPath)
    }

    private fun drawIndicator(c: Canvas, startX: Float, startY: Float, itemCount: Int) {
        val step = indicatorItemLength + indicatorItemPadding
        paint.color = regularColor
        repeat(itemCount) { index ->
            c.drawRoundRect(
                startX + step * index,
                startY - indicatorStrokeWidth / 2,
                startX + indicatorItemLength + step * index,
                startY + indicatorStrokeWidth / 2,
                cornerRadius,
                cornerRadius,
                paint
            )
        }
    }

    private fun drawHighlightIndicator(
        c: Canvas,
        startX: Float,
        startY: Float,
        activePosition: Int,
        progress: Float,
        itemCount: Int
    ) {
        val step = indicatorItemLength + indicatorItemPadding
        var offset = progress
        //first line piece
        c.drawRoundRect(
            startX + step * activePosition + offset,
            startY - indicatorStrokeWidth / 2,
            startX + indicatorItemLength + step * activePosition + offset,
            startY + indicatorStrokeWidth / 2,
            cornerRadius,
            cornerRadius,
            paintHighlight
        )
    }

    private fun calculateIndicatorLength(itemCount: Int): Float {
        if (itemCount <= 0) return 0f
        return itemCount * indicatorItemLength + (itemCount - 1) * indicatorItemPadding
    }
}
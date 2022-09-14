package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.*
import com.airbnb.epoxy.ModelView.Size
import by.lebedev.nanopoolmonitoring.utils.decorators.LinePagerIndicatorDecoration


@ModelView(saveViewState = false, autoLayout = Size.MATCH_WIDTH_WRAP_HEIGHT)
class CoinCarousel(context: Context) : Carousel(context) {

    var onPageChange: ((position: Int) -> Unit)? = null
    var onItemClick: ((position: Int) -> Unit)? = null

    private val onScrollListener =
        object : RecyclerView.OnScrollListener(), OnLayoutChangeListener {

            var activePosition = RecyclerView.NO_POSITION

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                processChangeEvent(rv)
            }

            override fun onLayoutChange(
                recyclerView: View,
                left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                processChangeEvent(recyclerView as RecyclerView)
            }

            private fun processChangeEvent(rv: RecyclerView) {
                (rv.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (activePosition != position && position != RecyclerView.NO_POSITION) {
                        activePosition = position
                        onPageChange?.invoke(activePosition)
                    }
                }
            }

        }

    private val indicatorDecoration = LinePagerIndicatorDecoration(
        context
    ).apply {
        setShowIndicatorsIfSingle(false)
    }

    init {
        addOnScrollListener(onScrollListener)
        addOnLayoutChangeListener(onScrollListener)
        addItemDecoration(indicatorDecoration)
    }

    @JvmOverloads
    @CallbackProp
    fun onPageChange(callback: ((position: Int) -> Unit)? = null) {
        onPageChange = callback
    }

    @JvmOverloads
    @CallbackProp
    fun onItemClick(callback: ((position: Int) -> Unit)? = null) {
        onItemClick = callback
    }

    @ModelProp(group = "currentItem")
    @JvmOverloads
    fun setCurrentItem(position: Int = 0) {
        if (scrollState == SCROLL_STATE_IDLE) {
            scrollToPosition(position)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @ModelProp
    fun removeDotsIndicator(disable:Boolean) {
        if (disable)
        removeItemDecoration(indicatorDecoration)
    }

    @ModelProp(group = "currentItem")
    fun setCurrentItemSmooth(position: Int) {
        if (scrollState == SCROLL_STATE_IDLE && position != RecyclerView.NO_POSITION) {
            (layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                // TODO You should add this, to avoid triggering [onPageChange] with the same value
                //  after setCurrentItemSmooth call. BUT now this line produce bug with locked PayButton
                //  in CardToCardTemplate, and may be something else in another place
                //onScrollListener.activePosition = position
                val previousPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

                if (previousPosition != RecyclerView.NO_POSITION) {
                    smoothScrollToPosition(position)
                } else {
                    // if the view has not been attached to the screen before, use instant scroll
                    scrollToPosition(position)
                }
            }
        }
    }

    override fun getDefaultSpacingBetweenItemsDp(): Int {
        return 0
    }

    @OnViewRecycled
    override fun clear() {
        super.clear()
        onScrollListener.activePosition = RecyclerView.NO_POSITION
    }
}
package by.lebedev.nanopoolmonitoring.utils


import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import by.lebedev.nanopoolmonitoring.utils.uiwidgets.HashrateLineChart
import by.lebedev.nanopoolmonitoring.utils.uiwidgets.SharesBarChart
import by.lebedev.nanopoolmonitoring.utils.uiwidgets.SharesChartData
import by.lebedev.nanopoolmonitoring.utils.uiwidgets.SharesMarkerView
import com.afollestad.materialdialogs.MaterialDialog
import com.airbnb.epoxy.DiffResult
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.OnModelBuildFinishedListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.squareup.picasso.Picasso
import java.util.*


inline fun EpoxyController.doOnModelBuildFinished(crossinline action: () -> Unit) {
    val singleShotListener = object : OnModelBuildFinishedListener {
        override fun onModelBuildFinished(result: DiffResult) {
            removeModelBuildListener(this)
            action()
        }
    }
    addModelBuildListener(singleShotListener)
}


fun RecyclerView.addDividerItemDecoration() {
    val lm = layoutManager as LinearLayoutManager
    val dividerItemDecoration = DividerItemDecoration(context, lm.orientation)
    addItemDecoration(dividerItemDecoration)
}

fun Fragment.setupOnBackPressed(lifecycleOwner: LifecycleOwner,sp:SharedPreferences){
    requireActivity().onBackPressedDispatcher.addCallback(lifecycleOwner) {
        MaterialDialog(requireContext()).apply {
            title(R.string.warning)
            message(R.string.warning_log_out)
            positiveButton(res = R.string.common_yes) {
                sp.logout()
                SessionBearer.resetWallet()
                requireActivity().finish()
            }
            negativeButton(res = R.string.common_no) {
                dismiss()
            }
        }.show()
    }
}

fun ImageView.setTint(@ColorRes colorRes: Int) {
    val color = ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    ImageViewCompat.setImageTintList(this, color)
}

fun ImageView.setTintColor(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

@BindingAdapter("setDrawableRes")
fun ImageView.setSrcRes(@DrawableRes drawableRes: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
}

@BindingAdapter("loadImageFromNet")
fun ImageView.loadImageFromNet(url:String) {
    Picasso.get().load(url).into(this)
}

@BindingAdapter("setShimmerTrigger")
fun ShimmerFrameLayout.setShimmerTrigger(flag: Boolean) {
    if (flag) {
        showShimmer(true)
    } else {
        stopShimmer()
        hideShimmer()
    }
}

@BindingAdapter("setChartData")
fun View.setChartData(data: ChartData?) {
    if (data != null) {
        when (this) {
            is HashrateLineChart -> setHashrates(data)
            is SharesBarChart -> setShares(data)
        }
    }
}

@BindingAdapter(
    value = ["setCircularProgress", "setCircularProgressAnimationTime"],
    requireAll = false
)
fun CircularProgressBar.setCircularProgress(progress: Float, animationTime: Long = 1000L) {
    setProgressWithAnimation(progress, animationTime)
}

@BindingAdapter("setLayoutWeight")
fun TextView.setLayoutWeight(weight: Float) {
    this.layoutParams = TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weight)
}
package by.lebedev.nanopoolmonitoring.utils.uiwidgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import by.lebedev.nanopoolmonitoring.utils.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HashrateLineChart @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : LineChart(context, attr, defStyle) {

    private val hashrateChartData = ArrayList<HashrateChartData>()

    init {
        isDragEnabled = false
        isScaleYEnabled = false
        description.isEnabled = false
        legend.isEnabled = false
        isDoubleTapToZoomEnabled = false
        setPinchZoom(false)
        setDrawGridBackground(false)

        val x: XAxis = xAxis
        x.apply {
            textColor = (ContextCompat.getColor(context, R.color.orange))
            textSize = 12f
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
        }
        x.setValueFormatter { value, axis ->
            value.toLong().toFormattedDate(DatePatterns.HOUR_AND_MINUTES_PATTERN.value)
        }

        val y = axisLeft

        y.apply {
            textColor = (ContextCompat.getColor(context, R.color.orange))
            textSize = 12f
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            setDrawLabels(true)
            isGranularityEnabled = true
            axisMinimum = 0.0F
        }

        y.setValueFormatter { value, axis ->

            var hashrateName = SessionBearer.wallet?.walletCoin?.hashrate()

            val hashrate: Float = when (SessionBearer.wallet?.walletCoin?.ticker) {
                "eth", "ethw", "etc"-> {
                    if (value in 1f..1000f) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateSmall()
                        value
                    } else if (value > 1_000_000_000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        value.div(1_000_000_000)
                    } else value.div(1_000)
                }
                "zec" -> {
                    if (value in 1f..1000f) {
                        value
                    } else {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        value.div(1000)
                    }
                }

                "xmr" -> {
                    if (value in 1f..1000_000f) {
                        value
                    } else {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        value.div(1000_000)
                    }
                }
                "rvn" -> {
                    if (value >1_000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        value.div(1000)
                    } else {
                        value
                    }
                }
                "cfx" -> {
                    if (value >1_000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        value.div(1000)
                    } else {
                        value
                    }
                }
                "ergo" -> {
                    if (value >1_000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        value.div(1000)
                    } else {
                        value
                    }
                }
                else -> 0.0f
            }

            context.getString(
                R.string.hashrate_res,
                hashrate,
                hashrateName
            )
        }

        axisRight.isEnabled = false
        setNoDataText(context.getString(R.string.common_loading))
        setNoDataTextColor(ContextCompat.getColor(context, R.color.orange))

        invalidate()
        requestLayout()
    }

    fun setHashrates(data: ChartData) {
        this.hashrateChartData.clear()
        val chartDataOrdered: TreeMap<Long, Long> = TreeMap(Collections.reverseOrder())

        data.data.forEach {
            chartDataOrdered[it.date] = it.hashrate
        }

        chartDataOrdered.entries.forEachIndexed { index, mutableEntry ->
            if (index <= 19) {
                this.hashrateChartData.add(
                    HashrateChartData(
                        date = mutableEntry.key.times(1000F),
                        value = mutableEntry.value.toFloat()
                    )
                )
            }
        }

        updateDataSet()
        invalidate()
    }

    private fun updateDataSet() {
        val values = hashrateChartData.sortedBy {
            it.date
        }.mapIndexed { index, hashrateChartData ->
            Entry(hashrateChartData.date, hashrateChartData.value, hashrateChartData)
        }

        val set1: LineDataSet

        if (data != null &&
            data.dataSetCount > 0
        ) {
            set1 = data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            data.notifyDataChanged()
            notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, "DataSet 1")
            set1.apply {
                this.setDrawCircleHole(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.05f
                setDrawFilled(true)
                setDrawCircles(true)
                lineWidth = 1.5f
                circleRadius = 2f
                setCircleColor(ContextCompat.getColor(context, R.color.orange))
                fillDrawable = ContextCompat.getDrawable(context, R.drawable.bg_chart_gradient)
                color = ContextCompat.getColor(context, R.color.orange)
                fillAlpha = 100
                setDrawHorizontalHighlightIndicator(false)
                setDrawVerticalHighlightIndicator(false)
                fillFormatter =
                    IFillFormatter { dataSet, dataProvider -> getAxisLeft().getAxisMinimum() }
            }

            val mv = HashrateMarkerView(context, R.layout.custom_marker_view)
            mv.chartView = this
            marker = mv


            // create a data object with the data sets
            val data = LineData(set1)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            setData(data)
            invalidate()
            requestLayout()
            animateXY(1000, 1000)
        }
    }
}

data class HashrateChartData(
    val value: Float,
    val date: Float
)
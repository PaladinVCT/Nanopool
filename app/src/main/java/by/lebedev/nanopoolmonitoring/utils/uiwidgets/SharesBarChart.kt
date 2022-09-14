package by.lebedev.nanopoolmonitoring.utils.uiwidgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import by.lebedev.nanopoolmonitoring.utils.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SharesBarChart @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : BarChart(context, attr, defStyle) {

    private val hashrateChartData = ArrayList<SharesChartData>()

    init {
        isDragEnabled = false
        isScaleYEnabled = false
        description.isEnabled = false
        legend.isEnabled = false
        isDoubleTapToZoomEnabled = false
        setPinchZoom(false)
        setDrawGridBackground(false)
        extraLeftOffset = 16f

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

        axisRight.isEnabled = false
        setNoDataText(context.getString(R.string.common_loading))
        setNoDataTextColor(ContextCompat.getColor(context, R.color.orange))

        invalidate()
        requestLayout()
    }

    fun setShares(data: ChartData) {
        this.hashrateChartData.clear()
        val chartDataOrdered: TreeMap<Long, Long> = TreeMap(Collections.reverseOrder())

        data.data.forEach {
            chartDataOrdered[it.date] = it.shares
        }

        chartDataOrdered.entries.forEachIndexed { index, mutableEntry ->
            if (index <= 19) {
                this.hashrateChartData.add(
                    SharesChartData(
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
        }.mapIndexed { index, sharesChartData ->
            BarEntry(sharesChartData.date, sharesChartData.value, hashrateChartData)
        }

        val set1: BarDataSet

        if (data != null &&
            data.dataSetCount > 0
        ) {
            set1 = data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            data.notifyDataChanged()
            notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "DataSet 1")
            set1.apply {
                barBorderColor = ContextCompat.getColor(context, R.color.orange_alpha_50)
                barBorderWidth = 5f
                barShadowColor = ContextCompat.getColor(context, R.color.orange_alpha_50)
            }
            val mv = SharesMarkerView(context, R.layout.custom_marker_view)
            mv.chartView = this
            marker = mv


            // create a data object with the data sets
            val data = BarData(set1)
            data.setValueTextSize(9f)
            data.barWidth = 20f

            data.setDrawValues(false)
            setFitBars(true)
            // set data
            setData(data)
            invalidate()
            requestLayout()
            animateXY(1000, 1000)
        }
    }
}

data class SharesChartData(
    val value: Float,
    val date: Float
)
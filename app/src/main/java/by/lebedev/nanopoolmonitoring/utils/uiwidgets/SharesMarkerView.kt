package by.lebedev.nanopoolmonitoring.utils.uiwidgets

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.utils.SessionBearer
import by.lebedev.nanopoolmonitoring.utils.hashrate
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils

class SharesMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {
    private val tvContent: TextView = findViewById(R.id.tvContent)

    @SuppressLint("SetTextI18n")
    override fun refreshContent(
        e: Entry,
        highlight: Highlight
    ) {
        if (e is CandleEntry) {
            tvContent.text = Utils.formatNumber(
                e.high,
                1,
                true
            )
        } else {
            tvContent.text = Utils.formatNumber(
                e.y,
                1,
                true
            )
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

}

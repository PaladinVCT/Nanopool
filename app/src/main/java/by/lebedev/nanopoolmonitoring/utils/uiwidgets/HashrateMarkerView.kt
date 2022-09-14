package by.lebedev.nanopoolmonitoring.utils.uiwidgets

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.utils.SessionBearer
import by.lebedev.nanopoolmonitoring.utils.hashrate
import by.lebedev.nanopoolmonitoring.utils.hashrateBig
import by.lebedev.nanopoolmonitoring.utils.hashrateSmall
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils

class HashrateMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {
    private val tvContent: TextView = findViewById(R.id.tvContent)

    @SuppressLint("SetTextI18n")
    override fun refreshContent(
        e: Entry,
        highlight: Highlight
    ) {
        if (e is CandleEntry) {
            tvContent.text = "${
                Utils.formatNumber(
                    e.high,
                    1,
                    true
                )
            } ${SessionBearer.wallet?.walletCoin?.hashrate()}"
        } else {

            var hashrateName = SessionBearer.wallet?.walletCoin?.hashrate()

            val value: Float = when (SessionBearer.wallet?.walletCoin?.ticker) {
                "eth","etc" -> {
                    if (e.y < 1000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateSmall()
                        e.y
                    } else if (e.y > 1_000_000_000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        e.y.div(1_000_000_000)
                    } else e.y.div(1000)
                }
                "zec" -> {
                    if (e.y in 1f..1000f) {
                        e.y
                    } else {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        e.y.div(1000)
                    }
                }
                "xmr" -> {
                    if (e.y in 1f..1000_000f) {
                        e.y
                    } else {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        e.y.div(1000_000)
                    }
                }
                "rvn" -> {
                    if (e.y >1_000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        e.y.div(1000)
                    } else {
                        e.y
                    }
                }
                "cfx" -> {
                    if (e.y >1_000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        e.y.div(1000)
                    } else {
                        e.y
                    }
                }
                "ergo" -> {
                    if (e.y >1_000) {
                        hashrateName = SessionBearer.wallet?.walletCoin?.hashrateBig()
                        e.y.div(1000)
                    } else {
                        e.y
                    }
                }
                else -> 0.0f
            }

            tvContent.text = "${
                Utils.formatNumber(
                    value,
                    1,
                    true
                )
            } $hashrateName"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

}

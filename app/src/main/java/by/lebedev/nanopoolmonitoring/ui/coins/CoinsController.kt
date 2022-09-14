package by.lebedev.nanopoolmonitoring.ui.coins


import android.content.Context
import by.lebedev.nanopoolmonitoring.coin
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class CoinsController @Inject constructor(
    private val context: Context
) : TypedEpoxyController<CoinsViewState>() {

    var callbacks: Callbacks? = null

    interface Callbacks {
    }

    override fun buildModels(data: CoinsViewState) {
        data.ccCoinList.filter {
            if (!data.querySearchText.isNullOrEmpty()) {
                it.name.lowercase().contains(data.querySearchText)
                        || it.symbol.lowercase().contains(data.querySearchText)
            } else true
        }.forEachIndexed { index, cmcCoin ->
            coin {
                id("coin_${index}")
                coin(cmcCoin)
            }
        }
    }
}

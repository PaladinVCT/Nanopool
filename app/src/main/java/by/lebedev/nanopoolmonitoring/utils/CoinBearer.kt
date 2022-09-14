package by.lebedev.nanopoolmonitoring.utils

import android.os.Parcelable
import by.lebedev.nanopoolmonitoring.ItemCoinBindingModel_
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import com.airbnb.epoxy.EpoxyModel
import kotlinx.parcelize.Parcelize

const val WALLET_ID = "Wallet id"

object CoinBearer {
    val coinList = listOf(
        Coin(R.drawable.eth_logo, CoinName.ETHEREUM.value, CoinTicker.ETHEREUM.value),
        Coin(
            R.drawable.etc_logo,
            CoinName.ETHEREUM_CLASSIC.value,
            CoinTicker.ETHEREUM_CLASSIC.value
        ),
        Coin(R.drawable.zec_logo, CoinName.ZCASH.value, CoinTicker.ZCASH.value),
        Coin(R.drawable.xmr_logo, CoinName.MONERO.value, CoinTicker.MONERO.value),
        Coin(R.drawable.rvn_logo, CoinName.RAVEN.value, CoinTicker.RAVEN.value),
        Coin(R.drawable.cfx_logo, CoinName.CONFLUX.value, CoinTicker.CONFLUX.value),
        Coin(R.drawable.erg_logo, CoinName.ERGO.value, CoinTicker.ERGO.value),
    )

    val coinModels = arrayListOf<EpoxyModel<*>>().apply {
        coinList.forEach {
            add(ItemCoinBindingModel_().id(it.name).coinImageRes(it.coinImageRes).coinName(it.name))
        }
    }
}

object SessionBearer {
    var wallet: Wallet? = null
    var isPremium: Boolean = false

    fun resetWallet() {
        this.wallet = null
    }
}

@Parcelize
data class Coin(
    val coinImageRes: Int,
    val name: String,
    val ticker: String
) : Parcelable

enum class CoinName(override val value: String) : StorableValueEnum<String> {
    ETHEREUM("ethereum"),
    ETHEREUM_CLASSIC("ethereum classic"),
    ZCASH("zcash"),
    MONERO("monero"),
    RAVEN("raven"),
    CONFLUX("conflux"),
    ERGO("ergo");
}

enum class CoinTicker(override val value: String) : StorableValueEnum<String> {
    ETHEREUM("eth"),
    ETHEREUM_CLASSIC("etc"),
    ZCASH("zec"),
    MONERO("xmr"),
    RAVEN("rvn"),
    CONFLUX("cfx"),
    ERGO("ergo");
}

fun hashrateTicker() = SessionBearer.wallet?.walletCoin?.hashrate()

fun coinTicker() = SessionBearer.wallet?.walletCoin?.ticker?.uppercase()

fun coinHashrateModifier() = when (SessionBearer.wallet?.walletCoin?.ticker) {
    "zec" -> 5.64
    "xmr" -> 52.5
    "rvn" -> 0.168
    "ergo" -> 0.18
    "cfx" -> 0.9
    else -> 1.0
}
package by.lebedev.nanopoolmonitoring.ui.earnings


import by.lebedev.nanopoolmonitoring.data.entities.analytics.CoinAnalytics
import by.lebedev.nanopoolmonitoring.data.entities.approximated.ApproximatedEarnings
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.AverageHashrates
import by.lebedev.nanopoolmonitoring.data.entities.pool.NanopoolPoolInfo
import by.lebedev.nanopoolmonitoring.data.entities.prices.CoinFiatPrices
import by.lebedev.nanopoolmonitoring.utils.capitalizeFirstLetter
import by.lebedev.nanopoolmonitoring.utils.percent
import com.airbnb.mvrx.MavericksState

data class EarningsViewState(
    val isLoading: Boolean = false,
    val isCalculatorLoading: Boolean = false,
    val isCalculatorExpanded: Boolean = false,
    val averageHashrates: AverageHashrates? = null,
    val coinFiatPrices: CoinFiatPrices? = null,
    val coinAnalytics: CoinAnalytics? = null,
    val approximatedEarnings: ApproximatedEarnings? = null,
    val calculatorApproximatedEarnings: ApproximatedEarnings? = null,
    val nanopoolHashrate: NanopoolPoolInfo? = null,
    val nanopoolMinersNumber: NanopoolPoolInfo? = null,
    val nanopoolShareCoefficient: NanopoolPoolInfo? = null,
    val coinPriceSelectedFiat: String = "USD",
    val earningsSelectedFiat: String = "USD",
    val athSelectedFiat: String = "USD",
    val athChangeSelectedFiat: String = "USD",
    val calculatorSelectedFiat: String = "USD",
    val globalFiatSelection: Int = 0,
    val enteredHashrate: String = ""

) : MavericksState {

    val coinFiatPrice = when (coinPriceSelectedFiat) {
        "USD" -> coinFiatPrices?.priceUsd
        "RUR" -> coinFiatPrices?.priceRur
        "EUR" -> coinFiatPrices?.priceEur
        "GBP" -> coinFiatPrices?.priceGbp
        "CNY" -> coinFiatPrices?.priceCny
        else -> ""
    }

    val coinAthPrice = when (athSelectedFiat) {
        "USD" -> coinAnalytics?.marketData?.ath?.usd
        "RUR" -> coinAnalytics?.marketData?.ath?.rub
        "EUR" -> coinAnalytics?.marketData?.ath?.eur
        "GBP" -> coinAnalytics?.marketData?.ath?.gbp
        "CNY" -> coinAnalytics?.marketData?.ath?.cny
        else -> ""
    }

    val coinAthChange = when (athChangeSelectedFiat) {
        "USD" -> coinAnalytics?.marketData?.athChangePercentage?.usd?.percent()
        "RUR" -> coinAnalytics?.marketData?.athChangePercentage?.rub?.percent()
        "EUR" -> coinAnalytics?.marketData?.athChangePercentage?.eur?.percent()
        "GBP" -> coinAnalytics?.marketData?.athChangePercentage?.gbp?.percent()
        "CNY" -> coinAnalytics?.marketData?.athChangePercentage?.cny?.percent()
        else -> ""
    }

}
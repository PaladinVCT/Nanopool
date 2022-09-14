package by.lebedev.nanopoolmonitoring.ui.dashboard


import by.lebedev.nanopoolmonitoring.data.entities.approximated.ApproximatedEarnings
import by.lebedev.nanopoolmonitoring.data.entities.balance.Balance
import by.lebedev.nanopoolmonitoring.data.entities.chart.CalculatedChartData
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import by.lebedev.nanopoolmonitoring.data.entities.general.GeneralInfo
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.AverageHashrates
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.CurrentHashrate
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.LastReportedHashrate
import by.lebedev.nanopoolmonitoring.data.entities.payout.PayoutLimit
import by.lebedev.nanopoolmonitoring.data.entities.prices.CoinFiatPrices
import by.lebedev.nanopoolmonitoring.utils.generateHashrate
import com.airbnb.epoxy.VisibilityState
import com.airbnb.mvrx.MavericksState

data class DashboardViewState(
    val wallets: List<Wallet> = listOf(),
    val averageHashrates: AverageHashrates? = null,
    val lastReportedHashrate: LastReportedHashrate? = null,
    val currentHashrate: CurrentHashrate? = null,
    val chartData: ChartData? = null,
    val calculatedChartData: CalculatedChartData? = null,
    val approximatedEarnings: ApproximatedEarnings? = null,
    val payoutLimit: PayoutLimit? = null,
    val generalInfo: GeneralInfo? = null,
    val coinFiatPrices: CoinFiatPrices? = null,
    val balance: Balance? = null,
    val payoutVisibilityState: Int = VisibilityState.INVISIBLE,
    val timeToNextPayoutRes: String? = null,
    val globalSelectedFiat: String = "USD",
    val isLoading: Boolean = false

) : MavericksState {

    val circularProgress = if (balance != null && payoutLimit != null) {
        kotlin.math.abs(
            balance.balance.div(payoutLimit.payout.toFloat()).times(100.0)
        ).toFloat()
    } else 0f

    val generatedChartData = chartData?.generateHashrate()

    val coinPrice = when (globalSelectedFiat) {
        "USD" -> coinFiatPrices?.priceUsd?.toDouble()
        "RUR" -> coinFiatPrices?.priceRur?.toDouble()
        "EUR" -> coinFiatPrices?.priceEur?.toDouble()
        "GBP" -> coinFiatPrices?.priceGbp?.toDouble()
        "CNY" -> coinFiatPrices?.priceCny?.toDouble()
        else -> 0.0
    }
}
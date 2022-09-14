package by.lebedev.nanopoolmonitoring.ui.dashboard


import android.content.Context
import by.lebedev.nanopoolmonitoring.*
import by.lebedev.nanopoolmonitoring.data.mappers.chart.CalculatedChartDataMapper
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class DashboardController @Inject constructor(
    private val context: Context,
    private val calculatedChartDataMapper: CalculatedChartDataMapper
) : TypedEpoxyController<DashboardViewState>() {

    var callbacks: Callbacks? = null

    interface Callbacks {
        fun onPayoutProgressVisibilityChanged(visibilityState: Int)
    }

    override fun buildModels(data: DashboardViewState) {
        buildHashrates(data)
        buildBalance(data)
        if (data.chartData?.data?.isNotEmpty() == true) {
            buildHashrateChart(data)
            buildSharesChart(data)
        }
        buildProgress(data)
        buildPayout(data)
        buildAverageHashrate24Hour(data)
        buildUnconfirmedBalance(data)
    }

    private fun buildUnconfirmedBalance(data: DashboardViewState) {
        commonInfoBlock {
            id("unconfirmed_balance")
            titleInfo(
                this@DashboardController.context.getString(
                    R.string.unconfirmed_balance
                )
            )
            valueInfo(
                if (data.isLoading) this@DashboardController.context.getString(R.string.common_loading) else data.generalInfo?.unconfirmedBalance.plus(
                    " ${coinTicker()}"
                )
            )
            shimmerTrigger(data.isLoading)
        }
    }

    private fun buildAverageHashrate24Hour(data: DashboardViewState) {
        commonInfoBlock {
            id("average_hashrate_24hours")
            titleInfo(
                this@DashboardController.context.getString(
                    R.string.average_hashrate_for_last_24_hours
                )
            )
            valueInfo(
                if (data.isLoading) this@DashboardController.context.getString(R.string.common_loading) else data.averageHashrates?.h24?.roundPlusHashrate(
                    2
                )
            )
            shimmerTrigger(data.isLoading)
        }
    }

    private fun buildPayout(data: DashboardViewState) {
        commonInfoBlock {
            id("payout_limit")
            titleInfo(
                this@DashboardController.context.getString(
                    R.string.payout_limit
                )
            )
            valueInfo(
                if (data.isLoading) this@DashboardController.context.getString(R.string.common_loading) else data.payoutLimit?.payout?.round(
                    2
                ).toString().plus(" ${coinTicker()}")
            )
            shimmerTrigger(data.isLoading)
        }
    }

    private fun buildHashrates(
        data: DashboardViewState
    ) {
        dashboardMinerHashrateTables {
            id("hashrate_tables")
            currentCalculatedHashrate(
                if (data.currentHashrate == null) this@DashboardController.context.getString(R.string.common_loading) else
                    this@DashboardController.context.getString(
                        R.string.hashrate_res,
                        data.currentHashrate.data,
                        SessionBearer.wallet?.walletCoin?.hashrate()
                    )
            )
            averageHashrateSixHours(
                if (data.averageHashrates == null) this@DashboardController.context.getString(
                    R.string.common_loading
                ) else
                    this@DashboardController.context.getString(
                        R.string.hashrate_res,
                        data.averageHashrates.h6,
                        SessionBearer.wallet?.walletCoin?.hashrate()
                    )
            )
            lastReportedHashrate(
                if (data.lastReportedHashrate == null) this@DashboardController.context.getString(R.string.common_loading) else
                    this@DashboardController.context.getString(
                        R.string.hashrate_res,
                        data.lastReportedHashrate.data,
                        SessionBearer.wallet?.walletCoin?.hashrate()
                    )
            )
            shimmerTrigger(data.isLoading)
        }
    }

    private fun buildBalance(
        data: DashboardViewState
    ) {
        dashboardMinerBalanceTable {
            id("balance_table")
            balance(
                if (data.balance == null || data.coinPrice == null) this@DashboardController.context.getString(
                    R.string.common_loading
                ) else
                    this@DashboardController.context.getString(
                        R.string.balance_res,
                        data.balance.balance,
                        SessionBearer.wallet?.walletCoin?.ticker?.uppercase()
                    )
                        .plus("\n ~${(data.coinPrice.times(data.balance.balance)).round(2)} ${data.globalSelectedFiat}")
            )
            shimmerTrigger(data.isLoading)
        }
    }

    private fun buildProgress(data: DashboardViewState) {
        payoutProgress {
            id("payout_progress")
            circularProgress(if (data.payoutVisibilityState != 0) data.circularProgress else 0f)
            circularProgressAnimationTime(1000L)
            circularProgressRes(
                this@DashboardController.context.getString(
                    R.string.circular_progress_res,
                    data.circularProgress
                )
            )
            payoutLimitRes(if (data.isLoading) this@DashboardController.context.getString(R.string.common_loading) else data.timeToNextPayoutRes)
            shimmerTrigger(data.isLoading)
            onVisibilityStateChanged { _, _, visibilityState ->
                this@DashboardController.callbacks?.onPayoutProgressVisibilityChanged(
                    visibilityState
                )
            }
        }
    }

    private fun buildSharesChart(data: DashboardViewState) {
        title {
            id("shares_chart_title")
            title(this@DashboardController.context.getString(R.string.shares_chart))
        }

        sharesChart {
            id("shares_chart")
            chartData(data.chartData)
            shimmerTrigger(data.isLoading)
        }
    }

    private fun buildHashrateChart(data: DashboardViewState) {

        val maxCalculatedHashrate = data.calculatedChartData?.data?.map {
            it.calculatedHashrate
        }?.toList()?.maxOrNull() ?: 0L

        if (data.lastReportedHashrate?.data != 0.0) {
            title {
                id("reported_hashrate_chart_title")
                title(this@DashboardController.context.getString(R.string.reported_hashrate_chart))
            }

            hashrateChart {
                id("reported_hashrate_chart")
                chartData(data.chartData)
                shimmerTrigger(data.isLoading)
            }
        } else if (maxCalculatedHashrate != 0L) {
            title {
                id("calculated_hashrate_chart_title")
                title(this@DashboardController.context.getString(R.string.calculated_hashrate_chart))
            }
            hashrateChart {
                id("calculated_hashrate_chart")
                chartData(this@DashboardController.calculatedChartDataMapper.map(data.calculatedChartData))
                shimmerTrigger(data.isLoading)
            }
        } else {
            title {
                id("generated_hashrate_chart_title")
                title(this@DashboardController.context.getString(R.string.calculated_hashrate_chart))
            }
            hashrateChart {
                id("generated_hashrate_chart")
                chartData(data.generatedChartData)
                shimmerTrigger(data.isLoading)
            }
        }
    }
}

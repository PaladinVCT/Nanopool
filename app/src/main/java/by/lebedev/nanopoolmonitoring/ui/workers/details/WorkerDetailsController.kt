package by.lebedev.nanopoolmonitoring.ui.workers.details


import android.content.Context
import by.lebedev.nanopoolmonitoring.*
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class WorkerDetailsController @Inject constructor(
    private val context: Context
) : TypedEpoxyController<WorkerDetailsViewState>() {

    var callbacks: Callbacks? = null

    interface Callbacks {
    }

    override fun buildModels(data: WorkerDetailsViewState) {
        dashboardMinerHashrateTables {
            id("hashrate_tables")
            currentCalculatedHashrate(
                if (data.currentHashrate == null) this@WorkerDetailsController.context.getString(R.string.common_loading) else
                    this@WorkerDetailsController.context.getString(
                        R.string.hashrate_res,
                        data.currentHashrate.data,
                        SessionBearer.wallet?.walletCoin?.hashrate()
                    )
            )
            averageHashrateSixHours(
                if (data.averageHashrates == null) this@WorkerDetailsController.context.getString(
                    R.string.common_loading
                ) else
                    this@WorkerDetailsController.context.getString(
                        R.string.hashrate_res,
                        data.averageHashrates.h6,
                        SessionBearer.wallet?.walletCoin?.hashrate()
                    )
            )
            lastReportedHashrate(
                if (data.lastReportedHashrate == null) this@WorkerDetailsController.context.getString(
                    R.string.common_loading
                ) else
                    this@WorkerDetailsController.context.getString(
                        R.string.hashrate_res,
                        data.lastReportedHashrate.data,
                        SessionBearer.wallet?.walletCoin?.hashrate()
                    )
            )
            shimmerTrigger(data.isLoading)
        }

        if (data.lastReportedHashrate?.data != 0.0) {
            title {
                id("hashrate_chart_title")
                title(this@WorkerDetailsController.context.getString(R.string.reported_hashrate_chart))
            }

            hashrateChart {
                id("${data.workerName}_hashrate_chart")
                chartData(data.chartData)
                shimmerTrigger(data.isLoading)
            }
        }else{
            title {
                id("generated_hashrate_chart_title")
                title(this@WorkerDetailsController.context.getString(R.string.calculated_hashrate_chart))
            }
            hashrateChart {
                id("generated_hashrate_chart")
                chartData(data.generatedChartData)
                shimmerTrigger(data.isLoading)
            }
        }

        title {
            id("shares_chart_title")
            title(this@WorkerDetailsController.context.getString(R.string.shares_chart))
        }

        sharesChart {
            id("${data.workerName}_shares_chart")
            chartData(data.chartData)
            shimmerTrigger(data.isLoading)
        }

        commonInfoBlock {
            id("worker_rating")
            titleInfo(
                this@WorkerDetailsController.context.getString(
                    R.string.rating
                )
            )
            valueInfo(
                if (data.isLoading) this@WorkerDetailsController.context.getString(R.string.common_loading)
                else data.workerRating.toString()
            )
            shimmerTrigger(data.isLoading)
        }

        commonInfoBlock {
            id("worker_last_share_time")
            titleInfo(
                this@WorkerDetailsController.context.getString(
                    R.string.last_share_found
                )
            )
            valueInfo(
                if (data.isLoading) this@WorkerDetailsController.context.getString(R.string.common_loading)
                else data.workerLastShareTime?.toFormattedDate(DatePatterns.MONTH_DAY_HOUR_AND_MINUTES_PATTERN.value)
            )
            shimmerTrigger(data.isLoading)
        }
    }
}

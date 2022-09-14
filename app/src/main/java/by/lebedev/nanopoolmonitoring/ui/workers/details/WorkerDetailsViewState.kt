package by.lebedev.nanopoolmonitoring.ui.workers.details


import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.AverageHashrates
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.CurrentHashrate
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.LastReportedHashrate
import by.lebedev.nanopoolmonitoring.utils.generateHashrate
import com.airbnb.mvrx.MavericksState

data class WorkerDetailsViewState(
    val isLoading: Boolean = false,
    val workerName: String? = null,
    val averageHashrates: AverageHashrates? = null,
    val lastReportedHashrate: LastReportedHashrate? = null,
    val currentHashrate: CurrentHashrate? = null,
    val chartData: ChartData? = null,
    val workerRating: Long? = null,
    val workerLastShareTime: Long? = null

) : MavericksState {
    constructor(args: WorkerDetailsNavArgs) : this(
        workerName = args.workerName,
        workerRating = args.workerRating,
        workerLastShareTime = args.workerLastShareTime
    )

    val generatedChartData = chartData?.generateHashrate()

}
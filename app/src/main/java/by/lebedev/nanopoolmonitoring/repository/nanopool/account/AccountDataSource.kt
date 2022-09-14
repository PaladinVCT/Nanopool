package by.lebedev.nanopoolmonitoring.repository.nanopool.account


import by.lebedev.nanopoolmonitoring.data.db.mappers.toListMapper
import by.lebedev.nanopoolmonitoring.data.entities.approximated.ApproximatedEarnings
import by.lebedev.nanopoolmonitoring.data.entities.balance.Balance
import by.lebedev.nanopoolmonitoring.data.entities.chart.CalculatedChartData
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import by.lebedev.nanopoolmonitoring.data.entities.general.GeneralInfo
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.AverageHashrates
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.CurrentHashrate
import by.lebedev.nanopoolmonitoring.data.mappers.hashrate.AverageHashratesResponseMapper
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.LastReportedHashrate
import by.lebedev.nanopoolmonitoring.data.entities.payout.PayoutLimit
import by.lebedev.nanopoolmonitoring.data.entities.pool.NanopoolPoolInfo
import by.lebedev.nanopoolmonitoring.data.entities.prices.CoinFiatPrices
import by.lebedev.nanopoolmonitoring.data.entities.workers.Worker
import by.lebedev.nanopoolmonitoring.data.mappers.approximated.ApproximatedEarningsResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.hashrate.CurrentHashrateResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.hashrate.LastReportedHashrateResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.balance.BalanceResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.chart.CalculatedChartDataResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.chart.ChartDataResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.general.GeneralInfoResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.payout.PayoutLimitResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.pool.NanopoolPoolInfoResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.prices.CoinFiatPricesResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.worker.WorkerResponseMapper
import by.lebedev.nanopoolmonitoring.di.Nanopool
import by.lebedev.nanopoolmonitoring.network.NanopoolService
import by.lebedev.nanopoolmonitoring.repository.RetrofitRunner
import by.lebedev.nanopoolmonitoring.utils.Result
import javax.inject.Inject
import javax.inject.Provider

class AccountDataSource @Inject constructor(
    @Nanopool
    private val service: Provider<NanopoolService>,
    private val averageHashratesResponseMapper: AverageHashratesResponseMapper,
    private val lastReportedHashrateResponseMapper: LastReportedHashrateResponseMapper,
    private val currentHashrateResponseMapper: CurrentHashrateResponseMapper,
    private val chartDataResponseMapper: ChartDataResponseMapper,
    private val calculatedChartDataResponseMapper: CalculatedChartDataResponseMapper,
    private val balanceResponseMapper: BalanceResponseMapper,
    private val approximatedEarningsResponseMapper: ApproximatedEarningsResponseMapper,
    private val payoutLimitResponseMapper: PayoutLimitResponseMapper,
    private val generalInfoResponseMapper: GeneralInfoResponseMapper,
    private val workerResponseMapper: WorkerResponseMapper,
    private val coinFiatPricesResponseMapper: CoinFiatPricesResponseMapper,
    private val nanopoolPoolInfoResponseMapper: NanopoolPoolInfoResponseMapper,
    private val retrofitRunner: RetrofitRunner
) {

    suspend fun loadAverageAccountHashrate(
        coinTicker: String,
        account: String
    ): Result<AverageHashrates> {
        return retrofitRunner.invoke(averageHashratesResponseMapper) {
            service.get().loadAverageAccountHashrates(coinTicker, account)
        }
    }

    suspend fun loadLastReportedHashrate(
        coinTicker: String,
        account: String
    ): Result<LastReportedHashrate> {
        return retrofitRunner.invoke(lastReportedHashrateResponseMapper) {
            service.get().loadLastReportedHashrate(coinTicker, account)
        }
    }

    suspend fun loadCurrentHashrate(
        coinTicker: String,
        account: String
    ): Result<CurrentHashrate> {
        return retrofitRunner.invoke(currentHashrateResponseMapper) {
            service.get().loadCurrentHashrate(coinTicker, account)
        }
    }

    suspend fun loadBalance(
        coinTicker: String,
        account: String
    ): Result<Balance> {
        return retrofitRunner.invoke(balanceResponseMapper) {
            service.get().loadBalance(coinTicker, account)
        }
    }

    suspend fun loadChartData(
        coinTicker: String,
        account: String
    ): Result<ChartData> {
        return retrofitRunner.invoke(chartDataResponseMapper) {
            service.get().loadChartData(coinTicker, account)
        }
    }

    suspend fun loadCalculatedChartData(
        coinTicker: String,
        account: String
    ): Result<CalculatedChartData> {
        return retrofitRunner.invoke(calculatedChartDataResponseMapper) {
            service.get().loadCalculatedChartData(coinTicker, account)
        }
    }

    suspend fun loadApproximatedEarnings(
        coinTicker: String,
        hashrate: Double
    ): Result<ApproximatedEarnings> {
        return retrofitRunner.invoke(approximatedEarningsResponseMapper) {
            service.get().loadApproximatedEarnings(coinTicker, hashrate)
        }
    }

    suspend fun loadPayoutSettings(
        coinTicker: String,
        account: String
    ): Result<PayoutLimit> {
        return retrofitRunner.invoke(payoutLimitResponseMapper) {
            service.get().loadPayoutSettings(coinTicker, account)
        }
    }

    suspend fun loadGeneralInfo(
        coinTicker: String,
        account: String
    ): Result<GeneralInfo> {
        return retrofitRunner.invoke(generalInfoResponseMapper) {
            service.get().loadGeneralInfo(coinTicker, account)
        }
    }

    suspend fun loadWorkers(
        coinTicker: String,
        account: String
    ): Result<List<Worker>> {
        return retrofitRunner.invoke(workerResponseMapper.toListMapper()) {
            service.get().loadWorkers(coinTicker, account).data?: emptyList()
        }
    }
    suspend fun loadAverageWorkerHashrates(
        coinTicker: String,
        account: String,
        workerName: String
    ): Result<AverageHashrates> {
        return retrofitRunner.invoke(averageHashratesResponseMapper) {
            service.get().loadAverageWorkerHashrates(coinTicker, account,workerName)
        }
    }
    suspend fun loadWorkerLastReportedHashrate(
        coinTicker: String,
        account: String,
        workerName: String
    ): Result<LastReportedHashrate> {
        return retrofitRunner.invoke(lastReportedHashrateResponseMapper) {
            service.get().loadWorkerLastReportedHashrate(coinTicker, account,workerName)
        }
    }
    suspend fun loadWorkerCurrentHashrate(
        coinTicker: String,
        account: String,
        workerName: String
    ): Result<CurrentHashrate> {
        return retrofitRunner.invoke(currentHashrateResponseMapper) {
            service.get().loadWorkerCurrentHashrate(coinTicker, account,workerName)
        }
    }
    suspend fun loadWorkerChartData(
        coinTicker: String,
        account: String,
        workerName: String
    ): Result<ChartData> {
        return retrofitRunner.invoke(chartDataResponseMapper) {
            service.get().loadWorkerChartData(coinTicker, account,workerName)
        }
    }
    suspend fun loadCoinFiatPrices(
        coinTicker: String
    ): Result<CoinFiatPrices> {
        return retrofitRunner.invoke(coinFiatPricesResponseMapper) {
            service.get().loadCoinFiatPrices(coinTicker)
        }
    }
    suspend fun loadNanopoolMinersNumber(
        coinTicker: String
    ): Result<NanopoolPoolInfo> {
        return retrofitRunner.invoke(nanopoolPoolInfoResponseMapper) {
            service.get().loadNanopoolMinersNumber(coinTicker)
        }
    }
    suspend fun loadNanopoolHashrate(
        coinTicker: String
    ): Result<NanopoolPoolInfo> {
        return retrofitRunner.invoke(nanopoolPoolInfoResponseMapper) {
            service.get().loadNanopoolHashrate(coinTicker)
        }
    }
    suspend fun loadNanopoolShareCoefficient(
        coinTicker: String
    ): Result<NanopoolPoolInfo> {
        return retrofitRunner.invoke(nanopoolPoolInfoResponseMapper) {
            service.get().loadNanopoolShareCoefficient(coinTicker)
        }
    }

}
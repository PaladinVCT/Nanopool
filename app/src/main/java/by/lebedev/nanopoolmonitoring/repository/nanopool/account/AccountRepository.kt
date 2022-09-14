package by.lebedev.nanopoolmonitoring.repository.nanopool.account


import by.lebedev.nanopoolmonitoring.data.entities.approximated.ApproximatedEarnings
import by.lebedev.nanopoolmonitoring.data.entities.balance.Balance
import by.lebedev.nanopoolmonitoring.data.entities.chart.CalculatedChartData
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import by.lebedev.nanopoolmonitoring.data.entities.general.GeneralInfo
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.AverageHashrates
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.CurrentHashrate
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.LastReportedHashrate
import by.lebedev.nanopoolmonitoring.data.entities.payout.PayoutLimit
import by.lebedev.nanopoolmonitoring.data.entities.pool.NanopoolPoolInfo
import by.lebedev.nanopoolmonitoring.data.entities.prices.CoinFiatPrices
import by.lebedev.nanopoolmonitoring.data.entities.workers.Worker
import by.lebedev.nanopoolmonitoring.utils.getOrThrow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val accountDataSource: AccountDataSource
) {

    suspend fun loadAverageAccountHashrate(
        coinTicker: String,
        account: String
    ): AverageHashrates {
        return accountDataSource.loadAverageAccountHashrate(coinTicker, account).getOrThrow()
    }

    suspend fun loadLastReportedHashrate(
        coinTicker: String,
        account: String
    ): LastReportedHashrate {
        return accountDataSource.loadLastReportedHashrate(coinTicker, account).getOrThrow()
    }

    suspend fun loadCurrentHashrate(
        coinTicker: String,
        account: String
    ): CurrentHashrate {
        return accountDataSource.loadCurrentHashrate(coinTicker, account).getOrThrow()
    }

    suspend fun loadBalance(
        coinTicker: String,
        account: String
    ): Balance {
        return accountDataSource.loadBalance(coinTicker, account).getOrThrow()
    }

    suspend fun loadChartData(
        coinTicker: String,
        account: String
    ): ChartData {
        return accountDataSource.loadChartData(coinTicker, account).getOrThrow()
    }

    suspend fun loadCalculatedChartData(
        coinTicker: String,
        account: String
    ): CalculatedChartData {
        return accountDataSource.loadCalculatedChartData(coinTicker, account).getOrThrow()
    }

    suspend fun loadApproximatedEarnings(
        coinTicker: String,
        hashrate: Double
    ): ApproximatedEarnings {
        return accountDataSource.loadApproximatedEarnings(coinTicker, hashrate).getOrThrow()
    }

    suspend fun loadPayoutSettings(
        coinTicker: String,
        account: String
    ): PayoutLimit {
        return accountDataSource.loadPayoutSettings(coinTicker, account).getOrThrow()
    }

    suspend fun loadGeneralInfo(
        coinTicker: String,
        account: String
    ): GeneralInfo {
        return accountDataSource.loadGeneralInfo(coinTicker, account).getOrThrow()
    }

    suspend fun loadWorkers(
        coinTicker: String,
        account: String
    ): List<Worker> {
        return accountDataSource.loadWorkers(coinTicker, account).getOrThrow()
    }

    suspend fun loadAverageWorkerHashrates(
        coinTicker: String,
        account: String,
        workerName: String
    ): AverageHashrates {
        return accountDataSource.loadAverageWorkerHashrates(coinTicker, account, workerName)
            .getOrThrow()
    }

    suspend fun loadWorkerLastReportedHashrate(
        coinTicker: String,
        account: String,
        workerName: String
    ): LastReportedHashrate {
        return accountDataSource.loadWorkerLastReportedHashrate(coinTicker, account, workerName)
            .getOrThrow()
    }

    suspend fun loadWorkerCurrentHashrate(
        coinTicker: String,
        account: String,
        workerName: String
    ): CurrentHashrate {
        return accountDataSource.loadWorkerCurrentHashrate(coinTicker, account, workerName)
            .getOrThrow()
    }

    suspend fun loadWorkerChartData(
        coinTicker: String,
        account: String,
        workerName: String
    ): ChartData {
        return accountDataSource.loadWorkerChartData(coinTicker, account, workerName)
            .getOrThrow()
    }

    suspend fun loadCoinFiatPrices(
        coinTicker: String
    ): CoinFiatPrices {
        return accountDataSource.loadCoinFiatPrices(coinTicker)
            .getOrThrow()
    }

    suspend fun loadNanopoolMinersNumber(
        coinTicker: String
    ): NanopoolPoolInfo {
        return accountDataSource.loadNanopoolMinersNumber(coinTicker)
            .getOrThrow()
    }

    suspend fun loadNanopoolHashrate(
        coinTicker: String
    ): NanopoolPoolInfo {
        return accountDataSource.loadNanopoolHashrate(coinTicker)
            .getOrThrow()
    }

    suspend fun loadNanopoolShareCoefficient(
        coinTicker: String
    ): NanopoolPoolInfo {
        return accountDataSource.loadNanopoolShareCoefficient(coinTicker)
            .getOrThrow()
    }

}
package by.lebedev.nanopoolmonitoring.domain.coingecko

import by.lebedev.nanopoolmonitoring.data.entities.analytics.CoinAnalytics
import by.lebedev.nanopoolmonitoring.data.entities.cc.CcCoin
import by.lebedev.nanopoolmonitoring.data.entities.wallet.WalletExistence
import by.lebedev.nanopoolmonitoring.domain.wallets.CheckWalletExistsUseCase
import by.lebedev.nanopoolmonitoring.domain.worker.LoadAverageWorkerChartDataUseCase
import by.lebedev.nanopoolmonitoring.repository.cc.CcRepository
import by.lebedev.nanopoolmonitoring.repository.coingecko.CoinGeckoRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class LoadCoinAnalyticsUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val coinGeckoRepository: CoinGeckoRepository
) : UseCase<LoadCoinAnalyticsUseCase.Params, CoinAnalytics>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): CoinAnalytics {
        return coinGeckoRepository.loadCoinAnalytics(
            coinName = params.coinName
        )
    }

    operator fun invoke(
        coinName: String
    ) =
        invoke(
            Params(
                coinName = coinName
            )
        )

    data class Params(
        val coinName: String
    )
}
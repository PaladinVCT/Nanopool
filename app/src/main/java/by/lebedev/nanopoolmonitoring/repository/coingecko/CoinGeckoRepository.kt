package by.lebedev.nanopoolmonitoring.repository.coingecko


import by.lebedev.nanopoolmonitoring.data.entities.analytics.CoinAnalytics
import by.lebedev.nanopoolmonitoring.utils.getOrThrow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinGeckoRepository @Inject constructor(
    private val coinGeckoDataSource: CoinGeckoDataSource
) {

    suspend fun loadCoinAnalytics(
        coinName: String
    ): CoinAnalytics {
        return coinGeckoDataSource.loadCoinAnalytics(coinName).getOrThrow()
    }

}
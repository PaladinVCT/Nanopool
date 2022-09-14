package by.lebedev.nanopoolmonitoring.repository.coingecko
import by.lebedev.nanopoolmonitoring.data.entities.analytics.CoinAnalytics
import by.lebedev.nanopoolmonitoring.data.mappers.analytics.CoinAnalyticsResponseMapper
import by.lebedev.nanopoolmonitoring.di.CoinGecko
import by.lebedev.nanopoolmonitoring.utils.Result
import by.lebedev.nanopoolmonitoring.network.CoinGeckoService
import by.lebedev.nanopoolmonitoring.repository.RetrofitRunner
import javax.inject.Inject
import javax.inject.Provider

class CoinGeckoDataSource @Inject constructor(
    @CoinGecko
    private val service: Provider<CoinGeckoService>,
    private val retrofitRunner: RetrofitRunner,
    private val coinAnalyticsResponseMapper: CoinAnalyticsResponseMapper
) {

    suspend fun loadCoinAnalytics(
        coinName: String
    ): Result<CoinAnalytics> {
        return retrofitRunner.invoke(coinAnalyticsResponseMapper) {
            service.get().loadCoinAnalytics(coinName)
        }
    }

}
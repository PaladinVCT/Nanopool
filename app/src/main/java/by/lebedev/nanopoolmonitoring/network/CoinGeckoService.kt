package by.lebedev.nanopoolmonitoring.network


import by.lebedev.nanopoolmonitoring.data.dto.analytics.CoinAnalyticsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinGeckoService {

    @GET("coins/{coinName}")
    suspend fun loadCoinAnalytics(
        @Path("coinName") coinName: String
    ): CoinAnalyticsResponse

}
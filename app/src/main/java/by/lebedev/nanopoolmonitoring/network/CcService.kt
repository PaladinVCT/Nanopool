package by.lebedev.nanopoolmonitoring.network


import by.lebedev.nanopoolmonitoring.data.dto.cmc.CmcCoinListResponse
import by.lebedev.nanopoolmonitoring.data.dto.coincap.CcCoinsListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CcService {

    @GET("cryptocurrency/listings/latest")
    suspend fun loadCmcAllCoins(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("limit") limit: Int = 5000
    ): CmcCoinListResponse

    @GET("assets")
    suspend fun loadCoinCapAllCoins(
    ): CcCoinsListResponse

}
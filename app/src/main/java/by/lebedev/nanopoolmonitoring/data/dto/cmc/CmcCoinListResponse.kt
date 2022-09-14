package by.lebedev.nanopoolmonitoring.data.dto.cmc


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CmcCoinListResponse(
    @Json(name = "data")
    val cmcCoinList: List<CmcCoinResponse>? = null
) {
    @JsonClass(generateAdapter = true)
    data class CmcCoinResponse(
        @Json(name = "circulating_supply")
        val circulatingSupply: Double? = null,
        @Json(name = "cmc_rank")
        val cmcRank: Int? = null,
        @Json(name = "id")
        val cmcId: Int? = null,
        @Json(name = "max_supply")
        val maxSupply: Double? = null,
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "num_market_pairs")
        val numMarketPairs: Int? = null,
        @Json(name = "quote")
        val quote: QuoteResponse? = null,
        @Json(name = "slug")
        val slug: String? = null,
        @Json(name = "symbol")
        val symbol: String? = null,
        @Json(name = "total_supply")
        val totalSupply: Double? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class QuoteResponse(
            @Json(name = "USD")
            val usd: UsdResponse? = null
        ) {
            @JsonClass(generateAdapter = true)
            data class UsdResponse(
                @Json(name = "market_cap")
                val marketCap: Double? = null,
                @Json(name = "market_cap_dominance")
                val marketCapDominance: Double? = null,
                @Json(name = "percent_change_1h")
                val percentChange1h: Double? = null,
                @Json(name = "percent_change_24h")
                val percentChange24h: Double? = null,
                @Json(name = "percent_change_30d")
                val percentChange30d: Double? = null,
                @Json(name = "percent_change_60d")
                val percentChange60d: Double? = null,
                @Json(name = "percent_change_7d")
                val percentChange7d: Double? = null,
                @Json(name = "percent_change_90d")
                val percentChange90d: Double? = null,
                @Json(name = "price")
                val price: Double? = null,
                @Json(name = "volume_24h")
                val volume24h: Double? = null,
                @Json(name = "volume_change_24h")
                val volumeChange24h: Double? = null
            )
        }
    }
}
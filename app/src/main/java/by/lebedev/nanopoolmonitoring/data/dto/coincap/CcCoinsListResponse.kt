package by.lebedev.nanopoolmonitoring.data.dto.coincap


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CcCoinsListResponse(
    @Json(name = "data")
    val `coinListResponse`: List<CcCoinResponse>? = null
) {
    @JsonClass(generateAdapter = true)
    data class CcCoinResponse(
        @Json(name = "changePercent24Hr")
        val changePercent24Hr: String? = null,
        @Json(name = "explorer")
        val explorer: String? = null,
        @Json(name = "id")
        val id: String? = null,
        @Json(name = "marketCapUsd")
        val marketCapUsd: String? = null,
        @Json(name = "maxSupply")
        val maxSupply: String? = null,
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "priceUsd")
        val priceUsd: String? = null,
        @Json(name = "rank")
        val rank: String? = null,
        @Json(name = "supply")
        val supply: String? = null,
        @Json(name = "symbol")
        val symbol: String? = null,
        @Json(name = "volumeUsd24Hr")
        val volumeUsd24Hr: String? = null
    )
}
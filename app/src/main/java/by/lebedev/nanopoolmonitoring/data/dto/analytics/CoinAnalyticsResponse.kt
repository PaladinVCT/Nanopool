package by.lebedev.nanopoolmonitoring.data.dto.analytics


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinAnalyticsResponse(
    @Json(name = "hashing_algorithm")
    val hashingAlgorithm: String? = null,
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "market_data")
    val marketData: MarketDataResponse? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "sentiment_votes_down_percentage")
    val sentimentVotesDownPercentage: Double? = null,
    @Json(name = "sentiment_votes_up_percentage")
    val sentimentVotesUpPercentage: Double? = null,
    @Json(name = "symbol")
    val symbol: String? = null
) {

    @JsonClass(generateAdapter = true)
    data class MarketDataResponse(
        @Json(name = "ath")
        val ath: AthResponse? = null,
        @Json(name = "ath_change_percentage")
        val athChangePercentage: AthChangePercentageResponse? = null,
        @Json(name = "ath_date")
        val athDate: AthDateResponse? = null,
    ) {
        @JsonClass(generateAdapter = true)
        data class AthResponse(
            @Json(name = "cny")
            val cny: Double? = null,
            @Json(name = "eur")
            val eur: Double? = null,
            @Json(name = "gbp")
            val gbp: Double? = null,
            @Json(name = "rub")
            val rub: Double? = null,
            @Json(name = "usd")
            val usd: Double? = null
        )

        @JsonClass(generateAdapter = true)
        data class AthChangePercentageResponse(
            @Json(name = "cny")
            val cny: Double? = null,
            @Json(name = "eur")
            val eur: Double? = null,
            @Json(name = "gbp")
            val gbp: Double? = null,
            @Json(name = "rub")
            val rub: Double? = null,
            @Json(name = "usd")
            val usd: Double? = null
        )

        @JsonClass(generateAdapter = true)
        data class AthDateResponse(
            @Json(name = "cny")
            val cny: String? = null,
            @Json(name = "eur")
            val eur: String? = null,
            @Json(name = "gbp")
            val gbp: String? = null,
            @Json(name = "rub")
            val rub: String? = null,
            @Json(name = "usd")
            val usd: String? = null
        )
    }
}
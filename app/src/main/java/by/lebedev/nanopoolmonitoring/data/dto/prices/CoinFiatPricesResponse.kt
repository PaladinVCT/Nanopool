package by.lebedev.nanopoolmonitoring.data.dto.prices


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinFiatPricesResponse(
    @Json(name = "data")
    val `data`: Data? = null,
    @Json(name = "status")
    val status: Boolean? = null
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "price_btc")
        val priceBtc: Double? = null,
        @Json(name = "price_cny")
        val priceCny: Double? = null,
        @Json(name = "price_eur")
        val priceEur: Double? = null,
        @Json(name = "price_gbp")
        val priceGbp: Double? = null,
        @Json(name = "price_rur")
        val priceRur: Double? = null,
        @Json(name = "price_usd")
        val priceUsd: Double? = null
    )
}
package by.lebedev.nanopoolmonitoring.data.dto.approximated


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApproximatedEarningsResponse(
    @Json(name = "data")
    val `data`: DataResponse? = null,
    @Json(name = "status")
    val status: Boolean? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataResponse(
        @Json(name = "day")
        val day: DayResponse? = null,
        @Json(name = "hour")
        val hour: HourResponse? = null,
        @Json(name = "minute")
        val minute: MinuteResponse? = null,
        @Json(name = "month")
        val month: MonthResponse? = null,
        @Json(name = "prices")
        val prices: PricesResponse? = null,
        @Json(name = "week")
        val week: WeekResponse? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class DayResponse(
            @Json(name = "bitcoins")
            val bitcoins: Double? = null,
            @Json(name = "coins")
            val coins: Double? = null,
            @Json(name = "dollars")
            val dollars: Double? = null,
            @Json(name = "euros")
            val euros: Double? = null,
            @Json(name = "pounds")
            val pounds: Double? = null,
            @Json(name = "rubles")
            val rubles: Double? = null,
            @Json(name = "yuan")
            val yuan: Double? = null
        )

        @JsonClass(generateAdapter = true)
        data class HourResponse(
            @Json(name = "bitcoins")
            val bitcoins: Double? = null,
            @Json(name = "coins")
            val coins: Double? = null,
            @Json(name = "dollars")
            val dollars: Double? = null,
            @Json(name = "euros")
            val euros: Double? = null,
            @Json(name = "pounds")
            val pounds: Double? = null,
            @Json(name = "rubles")
            val rubles: Double? = null,
            @Json(name = "yuan")
            val yuan: Double? = null
        )

        @JsonClass(generateAdapter = true)
        data class MinuteResponse(
            @Json(name = "bitcoins")
            val bitcoins: Double? = null,
            @Json(name = "coins")
            val coins: Double? = null,
            @Json(name = "dollars")
            val dollars: Double? = null,
            @Json(name = "euros")
            val euros: Double? = null,
            @Json(name = "pounds")
            val pounds: Double? = null,
            @Json(name = "rubles")
            val rubles: Double? = null,
            @Json(name = "yuan")
            val yuan: Double? = null
        )

        @JsonClass(generateAdapter = true)
        data class MonthResponse(
            @Json(name = "bitcoins")
            val bitcoins: Double? = null,
            @Json(name = "coins")
            val coins: Double? = null,
            @Json(name = "dollars")
            val dollars: Double? = null,
            @Json(name = "euros")
            val euros: Double? = null,
            @Json(name = "pounds")
            val pounds: Double? = null,
            @Json(name = "rubles")
            val rubles: Double? = null,
            @Json(name = "yuan")
            val yuan: Double? = null
        )

        @JsonClass(generateAdapter = true)
        data class PricesResponse(
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

        @JsonClass(generateAdapter = true)
        data class WeekResponse(
            @Json(name = "bitcoins")
            val bitcoins: Double? = null,
            @Json(name = "coins")
            val coins: Double? = null,
            @Json(name = "dollars")
            val dollars: Double? = null,
            @Json(name = "euros")
            val euros: Double? = null,
            @Json(name = "pounds")
            val pounds: Double? = null,
            @Json(name = "rubles")
            val rubles: Double? = null,
            @Json(name = "yuan")
            val yuan: Double? = null
        )
    }
}
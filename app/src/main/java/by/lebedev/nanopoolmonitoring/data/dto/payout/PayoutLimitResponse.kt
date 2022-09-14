package by.lebedev.nanopoolmonitoring.data.dto.payout


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PayoutLimitResponse(
    @Json(name = "data")
    val `data`: DataResponse? = null,
    @Json(name = "status")
    val status: Boolean? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataResponse(
        @Json(name = "payout")
        val payout: Double? = null
    )
}
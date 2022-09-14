package by.lebedev.nanopoolmonitoring.data.dto.hashrate


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AverageHashratesResponse(
    @Json(name = "data")
    val `data`: DataResponse? = null,
    @Json(name = "status")
    val status: Boolean? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataResponse(
        @Json(name = "h1")
        val h1: Double? = null,
        @Json(name = "h12")
        val h12: Double? = null,
        @Json(name = "h24")
        val h24: Double? = null,
        @Json(name = "h3")
        val h3: Double? = null,
        @Json(name = "h6")
        val h6: Double? = null
    )
}
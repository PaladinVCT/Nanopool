package by.lebedev.nanopoolmonitoring.data.dto.chart


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CalculatedChartDataResponse(
    @Json(name = "data")
    val `data`: List<DataResponse>? = null,
    @Json(name = "status")
    val status: Boolean? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataResponse(
        @Json(name = "date")
        val date: Long? = null,
        @Json(name = "hashrate")
        val hashrate: Long? = null
    )
}
package by.lebedev.nanopoolmonitoring.data.dto.hashrate


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HashrateResponse(
    @Json(name = "data")
    val `data`: Double? = null,
    @Json(name = "status")
    val status: Boolean? = null
)
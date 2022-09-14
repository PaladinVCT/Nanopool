package by.lebedev.nanopoolmonitoring.data.dto.balance


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalanceResponse(
    @Json(name = "data")
    val balance: Double? = null,
    @Json(name = "status")
    val status: Boolean? = null
)
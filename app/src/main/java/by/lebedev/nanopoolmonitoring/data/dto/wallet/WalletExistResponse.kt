package by.lebedev.nanopoolmonitoring.data.dto.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WalletExistResponse(
    @Json(name = "data")
    val message: String? = null,
    @Json(name = "error")
    val error: String? = null,
    @Json(name = "status")
    val status: Boolean? = null
)
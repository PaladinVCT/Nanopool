package by.lebedev.nanopoolmonitoring.data.dto.workers


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkersResponse(
    @Json(name = "data")
    val `data`: List<WorkerResponse>? = null,
    @Json(name = "status")
    val status: Boolean? = null
) {
    @JsonClass(generateAdapter = true)
    data class WorkerResponse(
        @Json(name = "hashrate")
        val hashrate: Double? = null,
        @Json(name = "id")
        val id: String? = null,
        @Json(name = "lastShare")
        val lastShare: Long? = null,
        @Json(name = "rating")
        val rating: Long? = null,
        @Json(name = "uid")
        val uid: Int? = null
    )
}
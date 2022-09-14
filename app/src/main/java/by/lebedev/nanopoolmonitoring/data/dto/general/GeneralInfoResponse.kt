package by.lebedev.nanopoolmonitoring.data.dto.general


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeneralInfoResponse(
    @Json(name = "data")
    val `data`: DataResponse? = null,
    @Json(name = "status")
    val status: Boolean? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataResponse(
        @Json(name = "account")
        val account: String? = null,
        @Json(name = "avgHashrate")
        val avgHashrate: AvgHashrateResponse? = null,
        @Json(name = "balance")
        val balance: String? = null,
        @Json(name = "hashrate")
        val hashrate: String? = null,
        @Json(name = "unconfirmed_balance")
        val unconfirmedBalance: String? = null,
        @Json(name = "workers")
        val workers: List<WorkerResponse?>? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class AvgHashrateResponse(
            @Json(name = "h1")
            val h1: String? = null,
            @Json(name = "h12")
            val h12: String? = null,
            @Json(name = "h24")
            val h24: String? = null,
            @Json(name = "h3")
            val h3: String? = null,
            @Json(name = "h6")
            val h6: String? = null
        )

        @JsonClass(generateAdapter = true)
        data class WorkerResponse(
            @Json(name = "h1")
            val h1: String? = null,
            @Json(name = "h12")
            val h12: String? = null,
            @Json(name = "h24")
            val h24: String? = null,
            @Json(name = "h3")
            val h3: String? = null,
            @Json(name = "h6")
            val h6: String? = null,
            @Json(name = "hashrate")
            val hashrate: String? = null,
            @Json(name = "id")
            val id: String? = null,
            @Json(name = "lastshare")
            val lastshare: Long? = null,
            @Json(name = "rating")
            val rating: Int? = null,
            @Json(name = "uid")
            val uid: Int? = null
        )
    }
}
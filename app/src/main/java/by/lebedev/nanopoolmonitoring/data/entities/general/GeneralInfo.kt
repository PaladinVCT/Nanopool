package by.lebedev.nanopoolmonitoring.data.entities.general


data class GeneralInfo(
    val status: Boolean,
    val account: String,
    val avgHashrate: AvgHashrate,
    val balance: String,
    val hashrate: String,
    val unconfirmedBalance: String,
    val workers: List<Worker>
) {
    data class Worker(
        val h1: String,
        val h12: String,
        val h24: String,
        val h3: String,
        val h6: String,
        val hashrate: String,
        val id: String,
        val lastshare: Long,
        val rating: Int,
        val uid: Int
    )

    data class AvgHashrate(
        val h1: String,
        val h12: String,
        val h24: String,
        val h3: String,
        val h6: String
    )
}

package by.lebedev.nanopoolmonitoring.data.entities.workers


data class Worker(
    val id: String,
    val hashrate: Double,
    val lastShare: Long,
    val rating: Long,
    val uid: Int
)
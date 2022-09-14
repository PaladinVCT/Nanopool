package by.lebedev.nanopoolmonitoring.data.entities.chart


data class ChartData(
    val `data`: List<Data>,
    val status: Boolean
) {
    data class Data(
        val date: Long,
        val hashrate: Long,
        val shares: Long = 0
    )
}
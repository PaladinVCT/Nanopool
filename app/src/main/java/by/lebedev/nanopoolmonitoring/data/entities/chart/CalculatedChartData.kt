package by.lebedev.nanopoolmonitoring.data.entities.chart


data class CalculatedChartData(
    val `data`: List<Data>,
    val status: Boolean
) {
    data class Data(
        val date: Long,
        val calculatedHashrate: Long,
    )
}
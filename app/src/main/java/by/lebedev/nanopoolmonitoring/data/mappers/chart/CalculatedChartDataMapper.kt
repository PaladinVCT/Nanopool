package by.lebedev.nanopoolmonitoring.data.mappers.chart


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.entities.chart.CalculatedChartData
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculatedChartDataMapper @Inject constructor() :
    Mapper<CalculatedChartData?, ChartData> {
    override fun map(from: CalculatedChartData?): ChartData {
        return ChartData(
            status = from?.status?:false,
            data = from?.data?.map {
                mapData(it)
            }?: emptyList()
        )
    }

    private fun mapData(from: CalculatedChartData.Data?): ChartData.Data {
        return ChartData.Data(
            date = from?.date ?: 0L,
            hashrate = from?.calculatedHashrate ?: 0L
        )
    }
}
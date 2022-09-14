package by.lebedev.nanopoolmonitoring.data.mappers.chart


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.chart.CalculatedChartDataResponse
import by.lebedev.nanopoolmonitoring.data.entities.chart.CalculatedChartData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculatedChartDataResponseMapper @Inject constructor() :
    Mapper<CalculatedChartDataResponse, CalculatedChartData> {
    override fun map(from: CalculatedChartDataResponse): CalculatedChartData {
        return CalculatedChartData(
            status = from.status ?: false,
            data = from.data?.map {
                mapData(it)
            } ?: emptyList()
        )
    }

    private fun mapData(from: CalculatedChartDataResponse.DataResponse?): CalculatedChartData.Data {
        return CalculatedChartData.Data(
            date = from?.date ?: 0L,
            calculatedHashrate = from?.hashrate?.times(1000) ?: 0L
        )
    }
}
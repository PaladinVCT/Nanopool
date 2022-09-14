package by.lebedev.nanopoolmonitoring.data.mappers.chart


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.chart.ChartDataResponse
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChartDataResponseMapper @Inject constructor() :
    Mapper<ChartDataResponse, ChartData> {
    override fun map(from: ChartDataResponse): ChartData {
        return ChartData(
            status = from.status ?: false,
            data = from.data?.map {
                mapData(it)
            } ?: emptyList()
        )
    }

    private fun mapData(from: ChartDataResponse.DataResponse?): ChartData.Data {
        return ChartData.Data(
            date = from?.date ?: 0L,
            hashrate = from?.hashrate ?: 0L,
            shares = from?.shares ?: 0L
        )

    }
}
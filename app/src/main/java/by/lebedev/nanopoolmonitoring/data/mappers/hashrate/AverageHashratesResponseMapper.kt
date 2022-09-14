package by.lebedev.nanopoolmonitoring.data.mappers.hashrate


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.hashrate.AverageHashratesResponse
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.AverageHashrates
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AverageHashratesResponseMapper @Inject constructor() :
    Mapper<AverageHashratesResponse, AverageHashrates> {
    override fun map(from: AverageHashratesResponse): AverageHashrates {
        return AverageHashrates(
            h1 = from.data?.h1 ?: 0.0,
            h3 = from.data?.h3 ?: 0.0,
            h6 = from.data?.h6 ?: 0.0,
            h12 = from.data?.h12 ?: 0.0,
            h24 = from.data?.h24 ?: 0.0
        )
    }
}
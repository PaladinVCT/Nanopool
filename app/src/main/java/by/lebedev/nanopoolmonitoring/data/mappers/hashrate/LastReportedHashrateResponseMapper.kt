package by.lebedev.nanopoolmonitoring.data.mappers.hashrate


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.hashrate.HashrateResponse
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.LastReportedHashrate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastReportedHashrateResponseMapper @Inject constructor() :
    Mapper<HashrateResponse, LastReportedHashrate> {
    override fun map(from: HashrateResponse): LastReportedHashrate {
        return LastReportedHashrate(
            status = from.status ?: false,
            data = from.data ?: 0.0
        )
    }
}
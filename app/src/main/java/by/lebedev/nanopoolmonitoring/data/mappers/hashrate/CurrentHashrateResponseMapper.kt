package by.lebedev.nanopoolmonitoring.data.mappers.hashrate


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.hashrate.HashrateResponse
import by.lebedev.nanopoolmonitoring.data.entities.hashrate.CurrentHashrate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentHashrateResponseMapper @Inject constructor() :
    Mapper<HashrateResponse, CurrentHashrate> {
    override fun map(from: HashrateResponse): CurrentHashrate {
        return CurrentHashrate(
            status = from.status ?: false,
            data = from.data ?: 0.0
        )
    }
}
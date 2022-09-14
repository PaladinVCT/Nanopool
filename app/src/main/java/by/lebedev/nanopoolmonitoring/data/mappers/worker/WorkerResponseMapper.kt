package by.lebedev.nanopoolmonitoring.data.mappers.worker


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.wallet.WalletExistResponse
import by.lebedev.nanopoolmonitoring.data.dto.workers.WorkersResponse
import by.lebedev.nanopoolmonitoring.data.entities.wallet.WalletExistence
import by.lebedev.nanopoolmonitoring.data.entities.workers.Worker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerResponseMapper @Inject constructor() :
    Mapper<WorkersResponse.WorkerResponse, Worker> {
    override fun map(from: WorkersResponse.WorkerResponse): Worker {
        return Worker(
            uid = from.uid ?: 0,
            rating = from.rating ?: 0,
            id = from.id.orEmpty(),
            hashrate = from.hashrate ?: 0.0,
            lastShare = from.lastShare?.times(1000) ?: 0
        )
    }
}
package by.lebedev.nanopoolmonitoring.data.mappers.worker.autoupdate


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.entities.workers.Worker
import by.lebedev.nanopoolmonitoring.data.entities.workers.WorkerAutoUpdate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerAutoUpdateFromWorkerMapper @Inject constructor() :
    Mapper<Worker, WorkerAutoUpdate> {
    override fun map(from: Worker): WorkerAutoUpdate {
        return WorkerAutoUpdate(
            name = from.id,
            isAlive = from.hashrate != 0.0
        )
    }
}
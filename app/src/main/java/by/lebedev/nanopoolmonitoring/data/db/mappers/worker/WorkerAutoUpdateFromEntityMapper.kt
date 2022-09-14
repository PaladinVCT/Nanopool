package by.lebedev.nanopoolmonitoring.data.db.mappers.worker

import by.lebedev.nanopoolmonitoring.data.db.entity.WorkerAutoUpdateEntity
import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.entities.workers.WorkerAutoUpdate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerAutoUpdateFromEntityMapper @Inject constructor() :
    Mapper<WorkerAutoUpdateEntity, WorkerAutoUpdate> {
    override fun map(from: WorkerAutoUpdateEntity): WorkerAutoUpdate {
        return WorkerAutoUpdate(
            name = from.workerName,
            isAlive = from.isAlive
        )
    }
}
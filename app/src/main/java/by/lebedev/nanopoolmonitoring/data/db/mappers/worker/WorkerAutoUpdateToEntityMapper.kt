package by.lebedev.nanopoolmonitoring.data.db.mappers.worker

import by.lebedev.nanopoolmonitoring.data.db.entity.WorkerAutoUpdateEntity
import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.entities.workers.WorkerAutoUpdate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerAutoUpdateToEntityMapper @Inject constructor() :
    Mapper<WorkerAutoUpdate, WorkerAutoUpdateEntity> {
    override fun map(from: WorkerAutoUpdate): WorkerAutoUpdateEntity {
        return WorkerAutoUpdateEntity(
            workerName = from.name,
            isAlive = from.isAlive
        )
    }
}
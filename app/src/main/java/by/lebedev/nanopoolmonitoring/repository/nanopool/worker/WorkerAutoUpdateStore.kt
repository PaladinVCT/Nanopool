package by.lebedev.nanopoolmonitoring.repository.nanopool.worker


import by.lebedev.nanopoolmonitoring.data.db.dao.WorkerAutoUpdateDao
import by.lebedev.nanopoolmonitoring.data.db.mappers.toListMapper
import by.lebedev.nanopoolmonitoring.data.db.mappers.worker.WorkerAutoUpdateFromEntityMapper
import by.lebedev.nanopoolmonitoring.data.db.mappers.worker.WorkerAutoUpdateToEntityMapper
import by.lebedev.nanopoolmonitoring.data.entities.workers.WorkerAutoUpdate
import javax.inject.Inject

class WorkerAutoUpdateStore @Inject constructor(
    private val workerAutoUpdateFromEntityMapper: WorkerAutoUpdateFromEntityMapper,
    private val workerAutoUpdateToEntityMapper: WorkerAutoUpdateToEntityMapper,
    private val workerAutoUpdateDao: WorkerAutoUpdateDao
) {

    suspend fun getWorkersAutoUpdate(): List<WorkerAutoUpdate> =
        workerAutoUpdateDao.getWorkers().map { entities ->
            workerAutoUpdateFromEntityMapper.map(entities)
        }

    suspend fun clearWorkersAutoUpdate() {
        workerAutoUpdateDao.clearWorkers()
    }

    suspend fun insertWorkersAutoUpdate(workers: List<WorkerAutoUpdate>) =
        workerAutoUpdateDao.insertWorkers(workerAutoUpdateToEntityMapper.toListMapper().map(workers))

}
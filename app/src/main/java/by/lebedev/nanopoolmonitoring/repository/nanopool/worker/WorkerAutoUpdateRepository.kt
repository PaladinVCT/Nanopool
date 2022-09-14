package by.lebedev.nanopoolmonitoring.repository.nanopool.worker


import by.lebedev.nanopoolmonitoring.data.entities.workers.WorkerAutoUpdate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerAutoUpdateRepository @Inject constructor(
    private val workerAutoUpdateStore: WorkerAutoUpdateStore
) {

    suspend fun getWorkersAutoUpdate(): List<WorkerAutoUpdate> =
        workerAutoUpdateStore.getWorkersAutoUpdate()

    suspend fun clearWorkersAutoUpdate() = workerAutoUpdateStore.clearWorkersAutoUpdate()

    suspend fun insertWorkersAutoUpdate(workers: List<WorkerAutoUpdate>) =
        workerAutoUpdateStore.insertWorkersAutoUpdate(workers)
}
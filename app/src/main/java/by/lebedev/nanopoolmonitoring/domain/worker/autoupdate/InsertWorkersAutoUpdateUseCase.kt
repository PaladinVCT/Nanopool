package by.lebedev.nanopoolmonitoring.domain.worker.autoupdate

import by.lebedev.nanopoolmonitoring.data.entities.workers.WorkerAutoUpdate
import by.lebedev.nanopoolmonitoring.repository.nanopool.worker.WorkerAutoUpdateStore
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class InsertWorkersAutoUpdateUseCase @Inject constructor(
    private val workerAutoUpdateStore: WorkerAutoUpdateStore,
    dispatchers: AppCoroutineDispatchers
) : UseCase<List<WorkerAutoUpdate>, Unit>() {
    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: List<WorkerAutoUpdate>) {
        workerAutoUpdateStore.insertWorkersAutoUpdate(params)
    }
}
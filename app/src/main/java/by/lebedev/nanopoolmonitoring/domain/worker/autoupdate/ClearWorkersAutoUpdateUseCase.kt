package by.lebedev.nanopoolmonitoring.domain.worker.autoupdate

import by.lebedev.nanopoolmonitoring.repository.nanopool.worker.WorkerAutoUpdateStore
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class ClearWorkersAutoUpdateUseCase @Inject constructor(
    private val workerAutoUpdateStore: WorkerAutoUpdateStore,
    dispatchers: AppCoroutineDispatchers
) : UseCase<Unit, Unit>() {
    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Unit){
        workerAutoUpdateStore.clearWorkersAutoUpdate()
    }

}
package by.lebedev.nanopoolmonitoring.domain.cc

import by.lebedev.nanopoolmonitoring.data.entities.cc.CcCoin
import by.lebedev.nanopoolmonitoring.domain.worker.LoadAverageWorkerChartDataUseCase
import by.lebedev.nanopoolmonitoring.repository.cc.CcRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class LoadCoinCapAllCoinsUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val ccRepository: CcRepository
) : UseCase<Unit, List<CcCoin>>() {

    override val dispatcher = dispatchers.io
    override suspend fun doWork(params: Unit): List<CcCoin> {
        return ccRepository.loadCoinCapAllCoins()
    }
}
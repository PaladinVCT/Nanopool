package by.lebedev.nanopoolmonitoring.domain.worker

import by.lebedev.nanopoolmonitoring.data.entities.hashrate.CurrentHashrate
import by.lebedev.nanopoolmonitoring.repository.nanopool.account.AccountRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class LoadWorkerCurrentHashrateUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository
) : UseCase<LoadWorkerCurrentHashrateUseCase.Params, CurrentHashrate>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): CurrentHashrate {
        return accountRepository.loadWorkerCurrentHashrate(
            coinTicker = params.coinTicker,
            account = params.account,
            workerName = params.workerName
        )
    }

    operator fun invoke(
        coinTicker: String,
        account: String,
        workerName: String
    ) =
        invoke(
            Params(
                coinTicker = coinTicker,
                account = account,
                workerName = workerName
            )
        )

    data class Params(
        val coinTicker: String,
        val account: String,
        val workerName: String
    )
}
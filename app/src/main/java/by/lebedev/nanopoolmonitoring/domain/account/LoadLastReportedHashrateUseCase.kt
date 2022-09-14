package by.lebedev.nanopoolmonitoring.domain.account

import by.lebedev.nanopoolmonitoring.data.entities.hashrate.LastReportedHashrate
import by.lebedev.nanopoolmonitoring.repository.nanopool.account.AccountRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class LoadLastReportedHashrateUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository
) : UseCase<LoadLastReportedHashrateUseCase.Params, LastReportedHashrate>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): LastReportedHashrate {
        return accountRepository.loadLastReportedHashrate(
            coinTicker = params.coinTicker,
            account = params.account
        )
    }

    operator fun invoke(
        coinTicker: String,
        account: String
    ) =
        invoke(
            Params(
                coinTicker = coinTicker,
                account = account
            )
        )

    data class Params(
        val coinTicker: String,
        val account: String
    )
}
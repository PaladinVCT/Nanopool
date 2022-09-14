package by.lebedev.nanopoolmonitoring.domain.account

import by.lebedev.nanopoolmonitoring.data.entities.approximated.ApproximatedEarnings
import by.lebedev.nanopoolmonitoring.repository.nanopool.account.AccountRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class LoadApproximatedEarningsUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository
) : UseCase<LoadApproximatedEarningsUseCase.Params, ApproximatedEarnings>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): ApproximatedEarnings {
        return accountRepository.loadApproximatedEarnings(
            coinTicker = params.coinTicker,
            hashrate = params.hashrate
        )
    }

    operator fun invoke(
        coinTicker: String,
        hashrate: Double
    ) =
        invoke(
            Params(
                coinTicker = coinTicker,
                hashrate = hashrate
            )
        )

    data class Params(
        val coinTicker: String,
        val hashrate: Double
    )
}
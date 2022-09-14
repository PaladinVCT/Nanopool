package by.lebedev.nanopoolmonitoring.domain.pool

import by.lebedev.nanopoolmonitoring.data.entities.pool.NanopoolPoolInfo
import by.lebedev.nanopoolmonitoring.repository.nanopool.account.AccountRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class LoadNanopoolHashrateUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository
) : UseCase<LoadNanopoolHashrateUseCase.Params, NanopoolPoolInfo>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): NanopoolPoolInfo {
        return accountRepository.loadNanopoolHashrate(
            coinTicker = params.coinTicker
        )
    }

    operator fun invoke(
        coinTicker: String
    ) =
        invoke(
            Params(
                coinTicker = coinTicker
            )
        )

    data class Params(
        val coinTicker: String
    )
}
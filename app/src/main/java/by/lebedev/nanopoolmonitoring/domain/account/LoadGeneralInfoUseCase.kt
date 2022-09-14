package by.lebedev.nanopoolmonitoring.domain.account

import by.lebedev.nanopoolmonitoring.data.entities.general.GeneralInfo
import by.lebedev.nanopoolmonitoring.data.entities.payout.PayoutLimit
import by.lebedev.nanopoolmonitoring.repository.nanopool.account.AccountRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class LoadGeneralInfoUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository
) : UseCase<LoadGeneralInfoUseCase.Params, GeneralInfo>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): GeneralInfo {
        return accountRepository.loadGeneralInfo(
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
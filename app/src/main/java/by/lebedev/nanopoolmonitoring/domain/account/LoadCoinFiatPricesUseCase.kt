package by.lebedev.nanopoolmonitoring.domain.account

import by.lebedev.nanopoolmonitoring.data.entities.prices.CoinFiatPrices
import by.lebedev.nanopoolmonitoring.repository.nanopool.account.AccountRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class LoadCoinFiatPricesUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository
) : UseCase<LoadCoinFiatPricesUseCase.Params, CoinFiatPrices>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): CoinFiatPrices {
        return accountRepository.loadCoinFiatPrices(
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
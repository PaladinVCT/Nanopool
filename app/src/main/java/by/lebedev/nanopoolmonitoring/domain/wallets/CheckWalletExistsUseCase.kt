package by.lebedev.nanopoolmonitoring.domain.wallets

import by.lebedev.nanopoolmonitoring.data.entities.wallet.WalletExistence
import by.lebedev.nanopoolmonitoring.repository.nanopool.wallets.WalletsRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import javax.inject.Inject

class CheckWalletExistsUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val walletsRepository: WalletsRepository
) : UseCase<CheckWalletExistsUseCase.Params, WalletExistence>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): WalletExistence {
        return walletsRepository.checkWalletExists(
            coinTicker = params.coinTicker,
            address = params.address
        )
    }

    operator fun invoke(
        coinTicker: String,
        address: String
    ) =
        invoke(
            Params(
                coinTicker = coinTicker,
                address = address
            )
        )

    data class Params(
        val coinTicker: String,
        val address: String
    )
}
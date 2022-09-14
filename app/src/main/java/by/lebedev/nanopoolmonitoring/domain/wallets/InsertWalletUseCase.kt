package by.lebedev.nanopoolmonitoring.domain.wallets

import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.repository.nanopool.wallets.WalletsRepository
import by.lebedev.nanopoolmonitoring.utils.Coin
import by.lebedev.nanopoolmonitoring.utils.WorkUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class InsertWalletUseCase @Inject constructor(
    private val walletsRepository: WalletsRepository,
    override val scope: CoroutineScope
) : WorkUseCase<InsertWalletUseCase.Params>() {

    data class Params(
        val walletName: String,
        val walletAddress: String,
        val walletCoin: Coin
    )

    override suspend fun doWork(params: Params) {

        walletsRepository.insertWallet(
            Wallet(
                walletCoin = params.walletCoin,
                walletAddress = params.walletAddress,
                walletName = params.walletName
            )
        )
    }
}
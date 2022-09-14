package by.lebedev.nanopoolmonitoring.domain.wallets

import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.repository.nanopool.wallets.WalletsStore
import by.lebedev.nanopoolmonitoring.utils.WorkUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DeleteWalletUseCase @Inject constructor(
    private val walletsStore: WalletsStore,
    override val scope: CoroutineScope
) : WorkUseCase<DeleteWalletUseCase.Params>() {

    data class Params(
        val wallet: Wallet
    )

    override suspend fun doWork(params: Params) {
        walletsStore.deleteWallet(params.wallet.serverId)
    }
}
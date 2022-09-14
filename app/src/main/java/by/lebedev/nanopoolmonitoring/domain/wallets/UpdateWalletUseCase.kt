package by.lebedev.nanopoolmonitoring.domain.wallets


import by.lebedev.nanopoolmonitoring.repository.nanopool.wallets.WalletsRepository
import by.lebedev.nanopoolmonitoring.utils.WorkUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class UpdateWalletUseCase @Inject constructor(
    private val walletsRepository: WalletsRepository,
    override val scope: CoroutineScope
) : WorkUseCase<UpdateWalletUseCase.Params>() {

    data class Params(
        val serverId: Long,
        val walletName: String,
        val walletAddress: String,
        val walletCoinTicker: String
    )

    override suspend fun doWork(params: Params) {

        walletsRepository.updateWallet(
            serverId = params.serverId,
            walletCoinTicker = params.walletCoinTicker,
            walletAddress = params.walletAddress,
            walletName = params.walletName

        )
    }
}
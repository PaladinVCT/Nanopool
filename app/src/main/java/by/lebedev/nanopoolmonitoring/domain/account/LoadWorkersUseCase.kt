package by.lebedev.nanopoolmonitoring.domain.account


import android.content.SharedPreferences
import by.lebedev.nanopoolmonitoring.data.entities.workers.Worker
import by.lebedev.nanopoolmonitoring.repository.nanopool.account.AccountRepository
import by.lebedev.nanopoolmonitoring.repository.nanopool.wallets.WalletsRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.SessionBearer
import by.lebedev.nanopoolmonitoring.utils.UseCase
import by.lebedev.nanopoolmonitoring.utils.WALLET_ID
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LoadWorkersUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository,
    private val walletsRepository: WalletsRepository,
    private val sp: SharedPreferences
) : UseCase<LoadWorkersUseCase.Params, List<Worker>>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): List<Worker> {

        return if (params.account.isEmpty() || params.coinTicker.isEmpty()) {
            walletsRepository.observeWallets().take(1)
                .collect {
                SessionBearer.wallet = it.firstOrNull {
                    it.serverId == sp.getLong(WALLET_ID, 0)
                }
            }
            accountRepository.loadWorkers(
                coinTicker = SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                account = SessionBearer.wallet?.walletAddress.orEmpty()
            )
        } else {
            accountRepository.loadWorkers(
                coinTicker = params.coinTicker,
                account = params.account
            )
        }
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
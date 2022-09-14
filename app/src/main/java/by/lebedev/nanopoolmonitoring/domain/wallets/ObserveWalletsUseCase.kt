package by.lebedev.nanopoolmonitoring.domain.wallets

import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.repository.nanopool.wallets.WalletsRepository
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.SubjectUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWalletsUseCase @Inject constructor(
    private val walletsRepository: WalletsRepository,
    dispatchers: AppCoroutineDispatchers
) : SubjectUseCase<Unit, List<Wallet>>() {
    override val dispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<List<Wallet>> =
        walletsRepository.observeWallets()
}
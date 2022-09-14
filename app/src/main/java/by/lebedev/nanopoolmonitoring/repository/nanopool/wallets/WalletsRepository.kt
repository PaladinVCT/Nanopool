package by.lebedev.nanopoolmonitoring.repository.nanopool.wallets


import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.data.entities.wallet.WalletExistence
import by.lebedev.nanopoolmonitoring.utils.getOrThrow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletsRepository @Inject constructor(
    private val walletsStore: WalletsStore,
    private val walletDataSource: WalletDataSource
) {

    fun observeWallets(): Flow<List<Wallet>> =
        walletsStore.observeWallets()

    fun observeWalletsByName(walletName: String): Flow<List<Wallet>> =
        walletsStore.observeWalletsByName(walletName)

    fun observeWalletsByNumber(walletNumber: String): Flow<List<Wallet>> =
        walletsStore.observeWalletsByNumber(walletNumber)

    fun observeWalletsByCoin(walletCoinName: String): Flow<List<Wallet>> =
        walletsStore.observeWalletsByCoin(walletCoinName)

   suspend fun updateWallet(
        serverId: Long,
        walletName: String,
        walletAddress: String,
        walletCoinTicker: String
    ) = walletsStore.updateWallet(
        serverId = serverId,
        walletName = walletName,
        walletAddress = walletAddress,
       walletCoinTicker = walletCoinTicker
    )

    suspend fun deleteWallet(id: Long) = walletsStore.deleteWallet(id)

    suspend fun insertWallet(wallet: Wallet) = walletsStore.insertWallet(wallet)

    suspend fun checkWalletExists(
        coinTicker: String,
        address: String
    ): WalletExistence {
        return walletDataSource.checkWalletExists(coinTicker, address).getOrThrow()
    }

}
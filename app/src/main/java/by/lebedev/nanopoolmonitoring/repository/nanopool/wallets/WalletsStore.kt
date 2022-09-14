package by.lebedev.nanopoolmonitoring.repository.nanopool.wallets


import by.lebedev.nanopoolmonitoring.data.db.dao.WalletDao
import by.lebedev.nanopoolmonitoring.data.db.mappers.toListMapper
import by.lebedev.nanopoolmonitoring.data.db.mappers.wallet.WalletFromEntityMapper
import by.lebedev.nanopoolmonitoring.data.db.mappers.wallet.WalletToEntityMapper
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.utils.toCoin
import by.lebedev.nanopoolmonitoring.utils.textToCoinTicker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletsStore @Inject constructor(
    private val walletToEntityMapper: WalletToEntityMapper,
    private val walletFromEntityMapper: WalletFromEntityMapper,
    private val walletDao: WalletDao
) {

    fun observeWallets(): Flow<List<Wallet>> =
        walletDao.getWalletsFlow().map { entities ->
            walletFromEntityMapper.toListMapper().map(entities)
        }

    fun observeWalletsByName(walletName: String): Flow<List<Wallet>> =
        walletDao.getWalletsByNameFlow(walletName).map { entities ->
            walletFromEntityMapper.toListMapper().map(entities)
        }

    fun observeWalletsByNumber(walletNumber: String): Flow<List<Wallet>> =
        walletDao.getWalletsByNumberFlow(walletNumber).map { entities ->
            walletFromEntityMapper.toListMapper().map(entities)
        }

    fun observeWalletsByCoin(walletCoinName: String): Flow<List<Wallet>> =
        walletDao.getWalletsByCoinFlow(walletCoinName).map { entities ->
            walletFromEntityMapper.toListMapper().map(entities)
        }

   suspend fun updateWallet(
        serverId: Long,
        walletName: String,
        walletAddress: String,
        walletCoinTicker: String
    ) {
       walletDao.updateWalletName(
           serverId = serverId,
           walletName = walletName
       )
       walletDao.updateWalletAddress(
           serverId = serverId,
           walletAddress = walletAddress
       )
       walletDao.updateWalletCoinName(
           serverId = serverId,
           walletCoinName = walletCoinTicker.textToCoinTicker().toCoin().name
       )
       walletDao.updateWalletCoinTicker(
           serverId = serverId,
           walletCoinTicker = walletCoinTicker
       )

   }


    suspend fun deleteWallet(id: Long) = walletDao.delete(id)

    suspend fun insertWallet(wallet: Wallet) =
        walletDao.insertWallet(walletToEntityMapper.map(wallet))

}
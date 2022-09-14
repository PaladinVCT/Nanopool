package by.lebedev.nanopoolmonitoring.data.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import by.lebedev.nanopoolmonitoring.data.db.entity.WalletEntity
import kotlinx.coroutines.flow.Flow


@Dao
abstract class WalletDao {
    @Query("SELECT * FROM wallet_entity WHERE wallet_address = :walletName")
    abstract fun getWalletsByNameFlow(walletName: String): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallet_entity WHERE wallet_address = :walletNumber")
    abstract fun getWalletsByNumberFlow(walletNumber: String): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallet_entity WHERE wallet_coin_name = :walletCoinName")
    abstract fun getWalletsByCoinFlow(walletCoinName: String): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallet_entity")
    abstract fun getWalletsFlow(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallet_entity")
    abstract fun getWallets(): List<WalletEntity>

    @Query("DELETE FROM wallet_entity WHERE server_id = :serverId")
    abstract suspend fun delete(serverId: Long)

    @Query("UPDATE wallet_entity SET wallet_name =:walletName WHERE server_id =:serverId")
    abstract suspend fun updateWalletName(
        serverId: Long,
        walletName: String
    )
    @Query("UPDATE wallet_entity SET wallet_address =:walletAddress WHERE server_id =:serverId")
    abstract suspend fun updateWalletAddress(
        serverId: Long,
        walletAddress: String
    )
    @Query("UPDATE wallet_entity SET wallet_coin_name =:walletCoinName WHERE server_id =:serverId")
    abstract suspend fun updateWalletCoinName(
        serverId: Long,
        walletCoinName: String
    )
    @Query("UPDATE wallet_entity SET wallet_coin_ticker =:walletCoinTicker WHERE server_id =:serverId")
    abstract suspend fun updateWalletCoinTicker(
        serverId: Long,
        walletCoinTicker: String
    )

    @Insert
    abstract suspend fun insertWallet(entity: WalletEntity)
}
package by.lebedev.nanopoolmonitoring.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "wallet_entity")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) override var id: Long = 0,
    @ColumnInfo(name = "server_id") val serverId: Long,
    @ColumnInfo(name = "wallet_address") val walletAddress: String,
    @ColumnInfo(name = "wallet_name") val walletName: String,
    @ColumnInfo(name = "wallet_coin_name") val walletCoinName: String,
    @ColumnInfo(name = "wallet_coin_ticker") val walletCoinTicker: String
) : NanopoolEntity

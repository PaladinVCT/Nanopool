package by.lebedev.nanopoolmonitoring.data.db.mappers.wallet

import by.lebedev.nanopoolmonitoring.data.db.entity.WalletEntity
import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletToEntityMapper @Inject constructor(): Mapper<Wallet, WalletEntity> {
    override fun map(from: Wallet): WalletEntity {
        return WalletEntity(
            serverId = from.serverId,
            walletAddress = from.walletAddress,
            walletName = from.walletName,
            walletCoinName = from.walletCoin.name,
            walletCoinTicker = from.walletCoin.ticker
        )
    }
}
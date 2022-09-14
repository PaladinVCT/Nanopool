package by.lebedev.nanopoolmonitoring.data.db.mappers.wallet

import by.lebedev.nanopoolmonitoring.data.db.entity.WalletEntity
import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.utils.Coin
import by.lebedev.nanopoolmonitoring.utils.CoinBearer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletFromEntityMapper @Inject constructor() : Mapper<WalletEntity, Wallet> {
    override fun map(from: WalletEntity): Wallet {
        return Wallet(
            serverId = from.serverId,
            walletName = from.walletName,
            walletAddress = from.walletAddress,
            walletCoin = Coin(
                coinImageRes = CoinBearer.coinList.first {
                    it.name == from.walletCoinName
                }.coinImageRes,
                name = from.walletCoinName,
                ticker = from.walletCoinTicker
            )
        )
    }
}
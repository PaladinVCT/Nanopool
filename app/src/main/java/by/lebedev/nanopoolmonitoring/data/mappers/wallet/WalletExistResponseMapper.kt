package by.lebedev.nanopoolmonitoring.data.mappers.wallet


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.wallet.WalletExistResponse
import by.lebedev.nanopoolmonitoring.data.entities.wallet.WalletExistence
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletExistResponseMapper @Inject constructor() :
    Mapper<WalletExistResponse, WalletExistence> {
    override fun map(from: WalletExistResponse): WalletExistence {
        return WalletExistence(
            messageSuccess = from.message.orEmpty(),
            messageError = from.error.orEmpty(),
            status = from.status ?: false
        )
    }
}
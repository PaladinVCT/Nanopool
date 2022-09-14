package by.lebedev.nanopoolmonitoring.data.mappers.balance


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.balance.BalanceResponse
import by.lebedev.nanopoolmonitoring.data.entities.balance.Balance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BalanceResponseMapper @Inject constructor() :
    Mapper<BalanceResponse, Balance> {
    override fun map(from: BalanceResponse): Balance {
        return Balance(
            status = from.status ?: false,
            balance = from.balance ?: 0.0
        )
    }
}
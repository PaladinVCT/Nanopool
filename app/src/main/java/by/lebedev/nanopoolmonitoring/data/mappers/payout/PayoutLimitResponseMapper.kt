package by.lebedev.nanopoolmonitoring.data.mappers.payout


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.payout.PayoutLimitResponse
import by.lebedev.nanopoolmonitoring.data.entities.payout.PayoutLimit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PayoutLimitResponseMapper @Inject constructor() :
    Mapper<PayoutLimitResponse, PayoutLimit> {
    override fun map(from: PayoutLimitResponse): PayoutLimit {
        return PayoutLimit(
            status = from.status ?: false,
            payout = from.data?.payout?:0.0
        )
    }
}
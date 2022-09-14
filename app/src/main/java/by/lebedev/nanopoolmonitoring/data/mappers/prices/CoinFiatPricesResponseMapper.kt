package by.lebedev.nanopoolmonitoring.data.mappers.prices


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.payout.PayoutLimitResponse
import by.lebedev.nanopoolmonitoring.data.dto.prices.CoinFiatPricesResponse
import by.lebedev.nanopoolmonitoring.data.entities.payout.PayoutLimit
import by.lebedev.nanopoolmonitoring.data.entities.prices.CoinFiatPrices
import by.lebedev.nanopoolmonitoring.utils.roundBigDecimalString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinFiatPricesResponseMapper @Inject constructor() :
    Mapper<CoinFiatPricesResponse, CoinFiatPrices> {
    override fun map(from: CoinFiatPricesResponse): CoinFiatPrices {
        return CoinFiatPrices(
            priceUsd = from.data?.priceUsd?.roundBigDecimalString(2).orEmpty(),
            priceRur = from.data?.priceRur?.roundBigDecimalString(2).orEmpty(),
            priceGbp = from.data?.priceGbp?.roundBigDecimalString(2).orEmpty(),
            priceEur = from.data?.priceEur?.roundBigDecimalString(2).orEmpty(),
            priceCny = from.data?.priceCny?.roundBigDecimalString(2).orEmpty(),
            priceBtc = from.data?.priceBtc?.roundBigDecimalString(2).orEmpty()
        )
    }
}
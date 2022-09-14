package by.lebedev.nanopoolmonitoring.data.mappers.analytics


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.analytics.CoinAnalyticsResponse
import by.lebedev.nanopoolmonitoring.data.dto.payout.PayoutLimitResponse
import by.lebedev.nanopoolmonitoring.data.entities.analytics.CoinAnalytics
import by.lebedev.nanopoolmonitoring.data.entities.payout.PayoutLimit
import by.lebedev.nanopoolmonitoring.utils.roundBigDecimalString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinAnalyticsResponseMapper @Inject constructor() :
    Mapper<CoinAnalyticsResponse, CoinAnalytics> {
    override fun map(from: CoinAnalyticsResponse): CoinAnalytics {
        return CoinAnalytics(
            id = from.id.orEmpty(),
            name = from.name.orEmpty(),
            symbol = from.symbol.orEmpty(),
            hashingAlgorithm = from.hashingAlgorithm.orEmpty(),
            sentimentVotesDownPercentage = from.sentimentVotesDownPercentage ?: 0.0,
            sentimentVotesUpPercentage = from.sentimentVotesUpPercentage ?: 0.0,
            marketData = mapMarketDataResponse(from.marketData)
        )
    }

    private fun mapMarketDataResponse(from: CoinAnalyticsResponse.MarketDataResponse?): CoinAnalytics.MarketData {
        return CoinAnalytics.MarketData(
            ath = mapAthResponse(from?.ath),
            athChangePercentage = mapAthChangePercentageResponse(from?.athChangePercentage),
            athDate = mapAthDateResponse(from?.athDate),
        )
    }

    private fun mapAthResponse(from: CoinAnalyticsResponse.MarketDataResponse.AthResponse?): CoinAnalytics.MarketData.Ath {
        return CoinAnalytics.MarketData.Ath(
            cny = from?.cny?.roundBigDecimalString(2).orEmpty(),
            usd = from?.usd?.roundBigDecimalString(2).orEmpty(),
            eur = from?.eur?.roundBigDecimalString(2).orEmpty(),
            gbp = from?.gbp?.roundBigDecimalString(2).orEmpty(),
            rub = from?.rub?.roundBigDecimalString(2).orEmpty()
        )
    }

    private fun mapAthChangePercentageResponse(from: CoinAnalyticsResponse.MarketDataResponse.AthChangePercentageResponse?): CoinAnalytics.MarketData.AthChangePercentage {
        return CoinAnalytics.MarketData.AthChangePercentage(
            cny = from?.cny?.roundBigDecimalString(2).orEmpty(),
            usd = from?.usd?.roundBigDecimalString(2).orEmpty(),
            eur = from?.eur?.roundBigDecimalString(2).orEmpty(),
            gbp = from?.gbp?.roundBigDecimalString(2).orEmpty(),
            rub = from?.rub?.roundBigDecimalString(2).orEmpty()
        )
    }

    private fun mapAthDateResponse(from: CoinAnalyticsResponse.MarketDataResponse.AthDateResponse?): CoinAnalytics.MarketData.AthDate {
        return CoinAnalytics.MarketData.AthDate(
            cny = from?.cny.orEmpty(),
            usd = from?.usd.orEmpty(),
            eur = from?.eur.orEmpty(),
            gbp = from?.gbp.orEmpty(),
            rub = from?.rub.orEmpty()
        )
    }
}
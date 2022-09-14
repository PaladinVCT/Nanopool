package by.lebedev.nanopoolmonitoring.data.entities.analytics



data class CoinAnalytics(
    val hashingAlgorithm: String,
    val id: String,
    val marketData: MarketData,
    val name: String,
    val sentimentVotesDownPercentage: Double,
    val sentimentVotesUpPercentage: Double,
    val symbol: String
) {

    data class MarketData(
        val ath: Ath,
        val athChangePercentage: AthChangePercentage,
        val athDate: AthDate,
    ) {
        data class Ath(
            val cny: String,
            val eur: String,
            val gbp: String,
            val rub: String,
            val usd: String
        )

        data class AthChangePercentage(
            val cny: String,
            val eur: String,
            val gbp: String,
            val rub: String,
            val usd: String
        )

        data class AthDate(
            val cny: String,
            val eur: String,
            val gbp: String,
            val rub: String,
            val usd: String
        )
    }
}
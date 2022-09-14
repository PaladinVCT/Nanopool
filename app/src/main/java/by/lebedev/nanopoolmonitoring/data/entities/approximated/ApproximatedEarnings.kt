package by.lebedev.nanopoolmonitoring.data.entities.approximated


data class ApproximatedEarnings(
    val earnings: Earnings,
    val status: Boolean
) {
    data class Earnings(
        val day: Day,
        val hour: Hour,
        val minute: Minute,
        val month: Month,
        val prices: Prices,
        val week: Week
    ) {
        data class Day(
            val bitcoins: Double,
            val coins: Double,
            val dollars: Double,
            val euros: Double,
            val pounds: Double,
            val rubles: Double,
            val yuan: Double
        )

        data class Hour(
            val bitcoins: Double,
            val coins: Double,
            val dollars: Double,
            val euros: Double,
            val pounds: Double,
            val rubles: Double,
            val yuan: Double
        )

        data class Minute(
            val bitcoins: Double,
            val coins: Double,
            val dollars: Double,
            val euros: Double,
            val pounds: Double,
            val rubles: Double,
            val yuan: Double
        )

        data class Month(
            val bitcoins: Double,
            val coins: Double,
            val dollars: Double,
            val euros: Double,
            val pounds: Double,
            val rubles: Double,
            val yuan: Double
        )

        data class Prices(
            val priceBtc: Double,
            val priceCny: Double,
            val priceEur: Double,
            val priceGbp: Double,
            val priceRur: Double,
            val priceUsd: Double
        )

        data class Week(
            val bitcoins: Double,
            val coins: Double,
            val dollars: Double,
            val euros: Double,
            val pounds: Double,
            val rubles: Double,
            val yuan: Double
        )
    }
}
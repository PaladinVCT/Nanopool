package by.lebedev.nanopoolmonitoring.data.mappers.cmc


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.coincap.CcCoinsListResponse
import by.lebedev.nanopoolmonitoring.data.entities.cc.CcCoin
import by.lebedev.nanopoolmonitoring.utils.roundToNearestValue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CcCoinListResponseMapper @Inject constructor() :
    Mapper<CcCoinsListResponse, List<CcCoin>> {
    override fun map(from: CcCoinsListResponse): List<CcCoin> {
        return from.coinListResponse?.map {
            mapCcCoinResponse(it)
        } ?: emptyList()
    }

    private fun mapCcCoinResponse(from: CcCoinsListResponse.CcCoinResponse): CcCoin {
        return CcCoin(
            name = from.name.orEmpty(),
            symbol = from.symbol.orEmpty(),
            maxSupply = from.maxSupply.orEmpty().roundToNearestValue(),
            id = from.id.orEmpty(),
            priceUsd = from.priceUsd.orEmpty().roundToNearestValue(),
            changePercent24Hr = from.changePercent24Hr.orEmpty().roundToNearestValue().plus("%"),
            coinImageUrl = "https://assets.coincap.io/assets/icons/${from.symbol?.lowercase()}@2x.png",
            explorer = from.explorer.orEmpty(),
            marketCapUsd = from.marketCapUsd.orEmpty().roundToNearestValue(),
            rank = from.rank.orEmpty(),
            supply = from.supply.orEmpty().roundToNearestValue(),
            volumeUsd24Hr = from.volumeUsd24Hr.orEmpty().roundToNearestValue()
        )
    }
}
package by.lebedev.nanopoolmonitoring.repository.cc


import by.lebedev.nanopoolmonitoring.data.entities.cc.CcCoin
import by.lebedev.nanopoolmonitoring.utils.getOrThrow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CcRepository @Inject constructor(
    private val cmcDataSource: CcDataSource
) {

    suspend fun loadCoinCapAllCoins(
    ): List<CcCoin> {
        return cmcDataSource.loadCoinCapAllCoins().getOrThrow()
    }

}
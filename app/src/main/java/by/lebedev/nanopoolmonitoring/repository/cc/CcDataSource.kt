package by.lebedev.nanopoolmonitoring.repository.cc
import by.lebedev.nanopoolmonitoring.data.entities.cc.CcCoin
import by.lebedev.nanopoolmonitoring.data.mappers.cmc.CcCoinListResponseMapper
import by.lebedev.nanopoolmonitoring.di.CC
import by.lebedev.nanopoolmonitoring.utils.Result
import by.lebedev.nanopoolmonitoring.network.CcService
import by.lebedev.nanopoolmonitoring.repository.RetrofitRunner
import javax.inject.Inject
import javax.inject.Provider

class CcDataSource @Inject constructor(
    @CC
    private val service: Provider<CcService>,
    private val retrofitRunner: RetrofitRunner,
    private val ccCoinListResponseMapper: CcCoinListResponseMapper
) {

    suspend fun loadCoinCapAllCoins(
    ): Result<List<CcCoin>> {
        return retrofitRunner.invoke(ccCoinListResponseMapper) {
            service.get().loadCoinCapAllCoins()
        }
    }

}
package by.lebedev.nanopoolmonitoring.data.mappers.general


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.general.GeneralInfoResponse
import by.lebedev.nanopoolmonitoring.data.entities.general.GeneralInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneralInfoResponseMapper @Inject constructor() :
    Mapper<GeneralInfoResponse, GeneralInfo> {
    override fun map(from: GeneralInfoResponse): GeneralInfo {
        return GeneralInfo(
            status = from.status ?: false,
            account = from.data?.account.orEmpty(),
            hashrate = from.data?.hashrate.orEmpty(),
            balance = from.data?.balance.orEmpty(),
            unconfirmedBalance = from.data?.unconfirmedBalance.orEmpty(),
            avgHashrate = mapAvgHashrate(from.data?.avgHashrate),
            workers = from.data?.workers?.map { mapWorkers(it) } ?: emptyList()
        )
    }

    private fun mapAvgHashrate(from: GeneralInfoResponse.DataResponse.AvgHashrateResponse?): GeneralInfo.AvgHashrate {
        return GeneralInfo.AvgHashrate(
            h1 = from?.h1.orEmpty(),
            h3 = from?.h3.orEmpty(),
            h6 = from?.h6.orEmpty(),
            h12 = from?.h12.orEmpty(),
            h24 = from?.h24.orEmpty()
        )
    }

    private fun mapWorkers(from: GeneralInfoResponse.DataResponse.WorkerResponse?): GeneralInfo.Worker {
        return GeneralInfo.Worker(
            h1 = from?.h1.orEmpty(),
            h3 = from?.h3.orEmpty(),
            h6 = from?.h6.orEmpty(),
            h12 = from?.h12.orEmpty(),
            h24 = from?.h24.orEmpty(),
            hashrate = from?.hashrate.orEmpty(),
            id = from?.id.orEmpty(),
            rating = from?.rating ?: 0,
            lastshare = from?.lastshare ?: 0,
            uid = from?.uid ?: 0
        )
    }
}
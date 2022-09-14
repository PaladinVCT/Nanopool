package by.lebedev.nanopoolmonitoring.data.mappers.pool


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.pool.NanopoolPoolInfoResponse
import by.lebedev.nanopoolmonitoring.data.entities.pool.NanopoolPoolInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NanopoolPoolInfoResponseMapper @Inject constructor() :
    Mapper<NanopoolPoolInfoResponse, NanopoolPoolInfo> {
    override fun map(from: NanopoolPoolInfoResponse): NanopoolPoolInfo {
        return NanopoolPoolInfo(
            status = from.status ?: false,
            data = from.data ?: 0.0
        )
    }
}
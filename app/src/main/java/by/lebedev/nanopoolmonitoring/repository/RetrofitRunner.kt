@file:Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")

package by.lebedev.nanopoolmonitoring.repository


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.mappers.MapFunction
import by.lebedev.nanopoolmonitoring.data.mappers.toMapper
import by.lebedev.nanopoolmonitoring.utils.Result
import by.lebedev.nanopoolmonitoring.utils.ResultErrorCatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitRunner @Inject constructor(
    private val errorCatcher: ResultErrorCatcher
) {

    suspend fun <R, E> invoke(
        mapper: Mapper<R, E>,
        request: suspend () -> R
    ): Result<E> {
        return errorCatcher.catch {
            val response = request()
            mapper.map(response)
        }

    }

    suspend fun <R, E> invoke(
        mapper: MapFunction<R, E>,
        request: suspend () -> R
    ): Result<E> =
        invoke(mapper.toMapper(), request)


    suspend operator fun <R> invoke(request: suspend () -> R): Result<R> =
        invoke({ it }, request)

}
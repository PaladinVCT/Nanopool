package by.lebedev.nanopoolmonitoring.data.mappers.errors

import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.error.HttpError
import by.lebedev.nanopoolmonitoring.utils.errorMessage
import by.lebedev.nanopoolmonitoring.utils.errorResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpErrorMapper @Inject constructor(moshi: Moshi) :
    Mapper<HttpException, String> {

    private val errorAdapter: JsonAdapter<HttpError> = moshi.adapter(HttpError::class.java)

    override fun map(from: HttpException): String {
        return kotlin.runCatching {
            errorAdapter.fromJson(from.errorResponse!!)!!.errorMessage
        }.getOrElse {
            from.errorMessage
        }
    }
}
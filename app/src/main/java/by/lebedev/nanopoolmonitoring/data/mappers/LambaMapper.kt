@file:Suppress("NOTHING_TO_INLINE")

package by.lebedev.nanopoolmonitoring.data.mappers

import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper

typealias MapFunction<T, R> = (T) -> R
typealias ListMapFunction<E, T> = MapFunction<List<E>, List<T>>

inline fun <T, R> MapFunction<T, R>.toMapper() =
    object : Mapper<T, R> {
        override fun map(from: T) = invoke(from)
    }


fun <F1, F2, T> Mapper2Source<F1, F2, T>.toMapperFrom1(from2: F2): Mapper<F1, T> {
    return fun(f1: F1): T { return map(f1, from2) }.toMapper()
}

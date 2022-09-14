package by.lebedev.nanopoolmonitoring.data.db.mappers

typealias EntityListMapper<E, T> = EntityMapper<List<E>, List<T>>

/**
 * Two way mapper for database entity
 *
 * @param E class of database entity
 * @param T class for data representation on UI
 */
interface EntityMapper<E, T> {
    fun toEntity(from: T): E
    fun fromEntity(from: E): T
}

fun <E, T> EntityMapper<E, T>.toListMapper(): EntityListMapper<E, T> =
    object : EntityListMapper<E, T> {
        override fun toEntity(from: List<T>): List<E> = from.map(this@toListMapper::toEntity)

        override fun fromEntity(from: List<E>): List<T> = from.map(this@toListMapper::fromEntity)
    }

fun <E, T> EntityMapper<E, T>.toEntityOrNull(from: T?): E? {
    return from?.let { toEntity(it) }
}

fun <E, T> EntityMapper<E, T>.fromEntityOrNull(from: E?): T? {
    return from?.let { fromEntity(it) }
}

interface Mapper<F, T> {
    fun map(from: F): T
}

typealias ListMapper<F, T> = Mapper<List<F>, List<T>>

fun <F, T> Mapper<F, T>.toListMapper(): ListMapper<F, T> = object : ListMapper<F, T> {
    override fun map(from: List<F>): List<T> = from.map(this@toListMapper::map)
}
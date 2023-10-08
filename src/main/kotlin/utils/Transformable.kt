package utils

fun interface Transformable<T> {
    fun convert(): T
}

fun <T> List<Transformable<T>>.transform() = map { it.convert() }
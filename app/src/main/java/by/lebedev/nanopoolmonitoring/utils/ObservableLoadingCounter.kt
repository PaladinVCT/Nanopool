package by.lebedev.nanopoolmonitoring.utils

import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val _count = AtomicInteger()
    val count: Int get() = _count.get()

    private val loadingState = MutableStateFlow(_count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }

    fun addLoader() {
        loadingState.tryEmit(_count.incrementAndGet())
    }

    fun removeLoader() {
        loadingState.tryEmit(_count.decrementAndGet())
    }
}
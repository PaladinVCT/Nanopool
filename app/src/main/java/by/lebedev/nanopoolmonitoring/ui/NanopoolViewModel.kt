package by.lebedev.nanopoolmonitoring.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.lebedev.nanopoolmonitoring.utils.BaseNavEvent
import by.lebedev.nanopoolmonitoring.utils.Event
import com.airbnb.mvrx.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*


open class NanopoolViewModel <S : MavericksState>(initialState: S) :
MavericksViewModel<S>(initialState) {

    protected val errorEventBus = MutableLiveData<Event<Throwable>>()
    val onErrorEvent: LiveData<Event<Throwable>>
        get() = errorEventBus

    protected val messageResEvent = MutableLiveData<Event<Int>>()
    val onMessageResEvent: LiveData<Event<Int>>
        get() = messageResEvent

    protected val navigateEvent = MutableLiveData<Event<BaseNavEvent>>()
    val onNavigateEvent: LiveData<Event<BaseNavEvent>>
        get() = navigateEvent

    fun <T> Flow<T>.launch(): Job = launchIn(viewModelScope)
}
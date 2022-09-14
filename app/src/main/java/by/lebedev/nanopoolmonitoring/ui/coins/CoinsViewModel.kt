package by.lebedev.nanopoolmonitoring.ui.coins


import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.hiltMavericksViewModelFactory
import by.lebedev.nanopoolmonitoring.domain.cc.LoadCoinCapAllCoinsUseCase
import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class CoinsViewModel @AssistedInject constructor(
    @Assisted initialState: CoinsViewState,
    private val loadCoinCapAllCoinsUseCase: LoadCoinCapAllCoinsUseCase
) : NanopoolViewModel<CoinsViewState>(initialState) {

    init {
        loadAllCoins()
    }

    fun onQueryTextChange(text:String?){
        setState { copy(querySearchText = text?.lowercase()) }
    }

    fun loadAllCoins() {
        loadCoinCapAllCoinsUseCase()
            .trackLoading { setState { copy(isLoading = it) } }
            .trackError(errorEventBus)
            .trackSuccess {
                setState { copy(ccCoinList = it) }
            }.launch()
    }

    fun onOptionsClick() {
        navigateEvent.postValue(
            Event(NavCommand(R.id.showOptionsFragment))
        )
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<CoinsViewModel, CoinsViewState> {
        override fun create(state: CoinsViewState): CoinsViewModel
    }

    companion object :
        MavericksViewModelFactory<CoinsViewModel, CoinsViewState> by hiltMavericksViewModelFactory()
}
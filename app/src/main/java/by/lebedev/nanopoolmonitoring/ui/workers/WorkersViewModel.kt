package by.lebedev.nanopoolmonitoring.ui.workers


import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.hiltMavericksViewModelFactory
import by.lebedev.nanopoolmonitoring.domain.account.LoadGeneralInfoUseCase
import by.lebedev.nanopoolmonitoring.domain.account.LoadWorkersUseCase
import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import by.lebedev.nanopoolmonitoring.ui.workers.details.WorkerDetailsNavArgs
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class WorkersViewModel @AssistedInject constructor(
    @Assisted initialState: WorkersViewState,
    private val loadWorkersUseCase: LoadWorkersUseCase,
) : NanopoolViewModel<WorkersViewState>(initialState) {

    fun loadWorkers() {
        loadWorkersUseCase.invoke(
            LoadWorkersUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading { setState { copy(isLoading = it) } }
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(workers = it) } }
            .launch()
    }

    fun onTotalWorkersClick() = withState { state ->
        setState {
            copy(
                areWorkersExpanded = !state.areWorkersExpanded
            )
        }
    }

    fun onAllFilterClick() {
        setState {
            copy(
                isAllFilterSelected = true,
                isAliveFilterSelected = false,
                isDeadFilterSelected = false,
            )
        }
    }

    fun onAliveFilterClick() {
        setState {
            copy(
                isAllFilterSelected = false,
                isAliveFilterSelected = true,
                isDeadFilterSelected = false,
            )
        }
    }

    fun onDeadFilterClick() {
        setState {
            copy(
                isAllFilterSelected = false,
                isAliveFilterSelected = false,
                isDeadFilterSelected = true,
            )
        }
    }

    fun onWorkerClick(
        workerName: String,
        workerRating: Long,
        workerLastShareTime: Long
    ) {
        navigateEvent.postValue(
            Event(
                NavCommand(
                    R.id.showWorkerDetailsFragment,
                    WorkerDetailsNavArgs(workerName,workerRating, workerLastShareTime).toBundleMvRx()
                )
            )
        )
    }


    fun onOptionsClick() {
        navigateEvent.postValue(
            Event(NavCommand(R.id.showOptionsFragment))
        )
    }

    fun onQueryTextChange(text: String?) {
        setState { copy(querySearchText = text?.lowercase()) }
    }


    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WorkersViewModel, WorkersViewState> {
        override fun create(state: WorkersViewState): WorkersViewModel
    }

    companion object :
        MavericksViewModelFactory<WorkersViewModel, WorkersViewState> by hiltMavericksViewModelFactory()
}
package by.lebedev.nanopoolmonitoring.ui.workers.details


import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.hiltMavericksViewModelFactory
import by.lebedev.nanopoolmonitoring.domain.worker.LoadAverageWorkerChartDataUseCase
import by.lebedev.nanopoolmonitoring.domain.worker.LoadAverageWorkerHashratesUseCase
import by.lebedev.nanopoolmonitoring.domain.worker.LoadWorkerCurrentHashrateUseCase
import by.lebedev.nanopoolmonitoring.domain.worker.LoadWorkerLastReportedHashrateUseCase
import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


class WorkerDetailsViewModel @AssistedInject constructor(
    @Assisted initialState: WorkerDetailsViewState,
    private val loadAverageWorkerHashratesUseCase: LoadAverageWorkerHashratesUseCase,
    private val loadWorkerLastReportedHashrateUseCase: LoadWorkerLastReportedHashrateUseCase,
    private val loadWorkerCurrentHashrateUseCase: LoadWorkerCurrentHashrateUseCase,
    private val loadAverageWorkerChartDataUseCase: LoadAverageWorkerChartDataUseCase

) : NanopoolViewModel<WorkerDetailsViewState>(initialState) {

    private val autoUpdateScope = CoroutineScope(Dispatchers.IO)
    private val loader = ObservableLoadingCounter()

    init {
        viewModelScope.launch {
            loader.observable.collect {
                setState { copy(isLoading = it) }
            }
        }
    }

    fun init(workerName:String){
        loadAverageHashrate(workerName)
        loadWorkerLastReportedHashrate(workerName)
        loadWorkerCurrentHashrate(workerName)
        loadWorkerChartData(workerName)
    }

    private fun loadAverageHashrate(workerName:String) {
        loadAverageWorkerHashratesUseCase.invoke(
            LoadAverageWorkerHashratesUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty(),
                workerName
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess {
                setState { copy(averageHashrates = it) }
            }
            .launch()
    }

    private fun loadWorkerLastReportedHashrate(workerName:String) {
        loadWorkerLastReportedHashrateUseCase.invoke(
            LoadWorkerLastReportedHashrateUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty(),
                workerName
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(lastReportedHashrate = it) } }
            .launch()
    }

    private fun loadWorkerCurrentHashrate(workerName:String) {
        loadWorkerCurrentHashrateUseCase.invoke(
            LoadWorkerCurrentHashrateUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty(),
                workerName
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(currentHashrate = it) } }
            .launch()
    }

    private fun loadWorkerChartData(workerName:String) {
        loadAverageWorkerChartDataUseCase.invoke(
            LoadAverageWorkerChartDataUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty(),
                workerName
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(chartData = it) } }
            .launch()
    }

    fun startAutoUpdate(workerName:String) {
        autoUpdateScope.launch {
            while (true) {
                delay(30_000L)
                init(workerName)
            }
        }
    }

    fun stopAutoUpdate() {
        autoUpdateScope.coroutineContext.cancelChildren()
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WorkerDetailsViewModel, WorkerDetailsViewState> {
        override fun create(state: WorkerDetailsViewState): WorkerDetailsViewModel
    }

    companion object :
        MavericksViewModelFactory<WorkerDetailsViewModel, WorkerDetailsViewState> by hiltMavericksViewModelFactory()
}
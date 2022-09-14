package by.lebedev.nanopoolmonitoring.ui.earnings


import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.hiltMavericksViewModelFactory
import by.lebedev.nanopoolmonitoring.domain.account.LoadApproximatedEarningsUseCase
import by.lebedev.nanopoolmonitoring.domain.account.LoadAverageAccountHashratesUseCase
import by.lebedev.nanopoolmonitoring.domain.account.LoadCoinFiatPricesUseCase
import by.lebedev.nanopoolmonitoring.domain.coingecko.LoadCoinAnalyticsUseCase
import by.lebedev.nanopoolmonitoring.domain.pool.LoadNanopoolHashrateUseCase
import by.lebedev.nanopoolmonitoring.domain.pool.LoadNanopoolMinersNumberUseCase
import by.lebedev.nanopoolmonitoring.domain.pool.LoadNanopoolShareCoefficientUseCase
import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import by.lebedev.nanopoolmonitoring.utils.*
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.Input
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


class EarningsViewModel @AssistedInject constructor(
    @Assisted initialState: EarningsViewState,
    private val loadAverageAccountHashratesUseCase: LoadAverageAccountHashratesUseCase,
    private val loadApproximatedEarningsUseCase: LoadApproximatedEarningsUseCase,
    private val loadCoinFiatPricesUseCase: LoadCoinFiatPricesUseCase,
    private val loadCoinAnalyticsUseCase: LoadCoinAnalyticsUseCase,
    private val loadNanopoolMinersNumberUseCase: LoadNanopoolMinersNumberUseCase,
    private val loadNanopoolHashrateUseCase: LoadNanopoolHashrateUseCase,
    private val loadNanopoolShareCoefficientUseCase: LoadNanopoolShareCoefficientUseCase
) : NanopoolViewModel<EarningsViewState>(initialState) {

    private val calculatorScope = CoroutineScope(Dispatchers.Main)
    private val loader = ObservableLoadingCounter()

    init {
        viewModelScope.launch {
            loader.observable.collect {
                setState { copy(isLoading = it) }
            }
        }
    }

    fun init() {
        loadAverageHashrate()
        loadCoinFiatPrices()
        loadCoinAnalytics()
        loadNanopoolMiners()
        loadNanopoolHashrate()
        loadNanopoolShareCoefficient()
    }

    private fun loadAverageHashrate() {
        loadAverageAccountHashratesUseCase.invoke(
            LoadAverageAccountHashratesUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess {
                loadApproximatedEarnings(it.h6)
                setState { copy(averageHashrates = it) }
            }
            .launch()
    }

    private fun loadCoinFiatPrices() {
        loadCoinFiatPricesUseCase.invoke(
            LoadCoinFiatPricesUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess {
                setState { copy(coinFiatPrices = it) }
            }
            .launch()
    }

    private fun loadCoinAnalytics() {
        loadCoinAnalyticsUseCase.invoke(
            SessionBearer.wallet?.walletCoin?.geckoCoinName().orEmpty()
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess {
                setState { copy(coinAnalytics = it) }
            }
            .launch()
    }

    private fun loadNanopoolMiners() {
        loadNanopoolMinersNumberUseCase.invoke(
            SessionBearer.wallet?.walletCoin?.ticker.orEmpty()
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess {
                setState { copy(nanopoolMinersNumber = it) }
            }
            .launch()
    }

    private fun loadNanopoolHashrate() {
        loadNanopoolHashrateUseCase.invoke(
            SessionBearer.wallet?.walletCoin?.ticker.orEmpty()
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess {
                setState { copy(nanopoolHashrate = it) }
            }
            .launch()
    }

    private fun loadNanopoolShareCoefficient() {
        loadNanopoolShareCoefficientUseCase.invoke(
            SessionBearer.wallet?.walletCoin?.ticker.orEmpty()
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess {
                setState { copy(nanopoolShareCoefficient = it) }
            }
            .launch()
    }

    fun loadApproximatedEarnings(hashrate: Double, isCalculator: Boolean = false) {
        loadApproximatedEarningsUseCase.invoke(
            LoadApproximatedEarningsUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                hashrate
            )
        ).trackLoading {
            if (!isCalculator)
                loader
            else setState { copy(isCalculatorLoading = it) }
        }
            .trackError { errorEventBus }
            .trackSuccess {
                setState {
                    if (!isCalculator)
                        copy(approximatedEarnings = it)
                    else copy(calculatorApproximatedEarnings = it)
                }
            }
            .launch()
    }

    fun onFiatSelected(fiat: String) {
        setState {
            copy(
                earningsSelectedFiat = fiat
            )
        }
    }

    fun onAthFiatSelected(fiat: String) {
        setState {
            copy(
                athSelectedFiat = fiat
            )
        }
    }

    fun onAthChangeFiatSelected(fiat: String) {
        setState {
            copy(
                athChangeSelectedFiat = fiat
            )
        }
    }

    fun onCalculatorFiatSelected(fiat: String) {
        setState {
            copy(
                calculatorSelectedFiat = fiat
            )
        }
    }

    fun onCoinPriceFiatSelected(fiat: String) {
        setState {
            copy(
                coinPriceSelectedFiat = fiat
            )
        }
    }

    fun onHashrateChange(input: Input) {

        calculatorScope.coroutineContext.cancelChildren()
        calculatorScope.launch {
            delay(500)
            setState { copy(enteredHashrate = input.text) }
        }
    }

    fun onCalculatorClick() = withState { state ->
        setState { copy(isCalculatorExpanded = !state.isCalculatorExpanded) }
    }

    fun onOptionsClick() {
        navigateEvent.postValue(
            Event(NavCommand(R.id.showOptionsFragment))
        )
    }

    fun setGlobalSelectedFiat(fiatPosition: Int) {
        setState { copy(globalFiatSelection = fiatPosition) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<EarningsViewModel, EarningsViewState> {
        override fun create(state: EarningsViewState): EarningsViewModel
    }

    companion object :
        MavericksViewModelFactory<EarningsViewModel, EarningsViewState> by hiltMavericksViewModelFactory()
}
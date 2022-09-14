package by.lebedev.nanopoolmonitoring.ui.dashboard


import android.content.Context
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.approximated.ApproximatedEarnings
import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.hiltMavericksViewModelFactory
import by.lebedev.nanopoolmonitoring.domain.account.*
import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import by.lebedev.nanopoolmonitoring.ui.workers.details.WorkerDetailsNavArgs
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


class DashboardViewModel @AssistedInject constructor(
    @Assisted initialState: DashboardViewState,
    private val loadAverageAccountHashratesUseCase: LoadAverageAccountHashratesUseCase,
    private val loadLastReportedHashrateUseCase: LoadLastReportedHashrateUseCase,
    private val loadCurrentHashrateUseCase: LoadCurrentHashrateUseCase,
    private val loadBalanceUseCase: LoadBalanceUseCase,
    private val loadChartDataUseCase: LoadChartDataUseCase,
    private val loadApproximatedEarningsUseCase: LoadApproximatedEarningsUseCase,
    private val loadPayoutLimitUseCase: LoadPayoutLimitUseCase,
    private val loadCoinFiatPricesUseCase: LoadCoinFiatPricesUseCase,
    private val loadGeneralInfoUseCase: LoadGeneralInfoUseCase,
    private val loadCalculatedChartDataUseCase: LoadCalculatedChartDataUseCase,
) : NanopoolViewModel<DashboardViewState>(initialState) {

    private val autoUpdateScope = CoroutineScope(Dispatchers.IO)
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
        loadLastReportedHashrate()
        loadCurrentHashrate()
        loadBalance()
        loadChartData()
        loadPayoutLimit()
        loadGeneralInfo()
        loadCoinFiatPrices()
        loadCalculatedChartData()
    }

    private fun loadGeneralInfo() {
        loadGeneralInfoUseCase.invoke(
            LoadGeneralInfoUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(generalInfo = it) } }
            .launch()
    }

    private fun loadPayoutLimit() {
        loadPayoutLimitUseCase.invoke(
            LoadPayoutLimitUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(payoutLimit = it) } }
            .launch()
    }

    private fun loadChartData() {
        loadChartDataUseCase.invoke(
            LoadChartDataUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(chartData = it) } }
            .launch()
    }

    private fun loadCalculatedChartData() {
        loadCalculatedChartDataUseCase.invoke(
            LoadCalculatedChartDataUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(calculatedChartData = it) } }
            .launch()
    }

    private fun loadBalance() {
        loadBalanceUseCase.invoke(
            LoadBalanceUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(balance = it) } }
            .launch()
    }

    private fun loadCurrentHashrate() {
        loadCurrentHashrateUseCase.invoke(
            LoadCurrentHashrateUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(currentHashrate = it) } }
            .launch()
    }

    private fun loadLastReportedHashrate() {
        loadLastReportedHashrateUseCase.invoke(
            LoadLastReportedHashrateUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                SessionBearer.wallet?.walletAddress.orEmpty()
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(lastReportedHashrate = it) } }
            .launch()
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

    private fun loadApproximatedEarnings(hashrate: Double) {
        loadApproximatedEarningsUseCase.invoke(
            LoadApproximatedEarningsUseCase.Params(
                SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                hashrate
            )
        ).trackLoading(loader)
            .trackError { errorEventBus }
            .trackSuccess { setState { copy(approximatedEarnings = it) } }
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

    fun onPayoutProgressVisibilityChanged(visibilityState: Int) {
        setState { copy(payoutVisibilityState = visibilityState) }
    }

    fun setTimeToNextPayoutRes(
        payout: Double,
        balance: Double,
        earnings: ApproximatedEarnings.Earnings,
        ctx: Context
    ) {

        val timeToNextPayoutRes =
            when (val coinsLeft = payout.minus(balance)) {
                in Double.NEGATIVE_INFINITY..0.0 -> ctx.getString(R.string.payout_limit_reached)
                in 0.0..earnings.minute.coins -> ctx.getString(R.string.payout_limit_reach_in_seconds)
                in earnings.minute.coins..earnings.hour.coins -> {
                    val payoutTimeUnitsLeft = coinsLeft.div(earnings.minute.coins).toInt()
                    ctx.getString(
                        R.string.payout_limit_reach_in_custom,
                        ctx.resources.getQuantityString(
                            R.plurals.common_minute_unit_plurals,
                            payoutTimeUnitsLeft,
                            payoutTimeUnitsLeft
                        )
                    )
                }
                in earnings.hour.coins..earnings.day.coins -> {
                    val payoutTimeUnitsLeft = coinsLeft.div(earnings.hour.coins).toInt()
                    ctx.getString(
                        R.string.payout_limit_reach_in_custom,
                        ctx.resources.getQuantityString(
                            R.plurals.common_hour_unit_plurals,
                            payoutTimeUnitsLeft,
                            payoutTimeUnitsLeft
                        )
                    )
                }
                in earnings.day.coins..earnings.week.coins -> {
                    val payoutTimeUnitsLeft = coinsLeft.div(earnings.day.coins).toInt()
                    ctx.getString(
                        R.string.payout_limit_reach_in_custom,
                        ctx.resources.getQuantityString(
                            R.plurals.common_day_unit_plurals,
                            payoutTimeUnitsLeft,
                            payoutTimeUnitsLeft
                        )
                    )
                }
                in earnings.week.coins..earnings.month.coins -> {
                    val payoutTimeUnitsLeft = coinsLeft.div(earnings.week.coins).toInt()
                    ctx.getString(
                        R.string.payout_limit_reach_in_custom,
                        ctx.resources.getQuantityString(
                            R.plurals.common_week_unit_plurals,
                            payoutTimeUnitsLeft,
                            payoutTimeUnitsLeft
                        )
                    )
                }
                in earnings.month.coins..Double.POSITIVE_INFINITY -> {
                    val payoutTimeUnitsLeft = coinsLeft.div(earnings.month.coins).toInt()
                    if (payoutTimeUnitsLeft > 24) {
                        ctx.getString(R.string.payout_limit_reach_in_infinity)
                    } else
                        ctx.getString(
                            R.string.payout_limit_reach_in_custom,
                            ctx.resources.getQuantityString(
                                R.plurals.common_month_unit_plurals,
                                payoutTimeUnitsLeft,
                                payoutTimeUnitsLeft
                            )
                        )
                }

                else -> {
                    ctx.getString(R.string.payout_limit_reach_in_infinity)
                }
            }
        setState { copy(timeToNextPayoutRes = timeToNextPayoutRes) }
    }

    fun startAutoUpdate() {
        autoUpdateScope.launch {
            while (true) {
                delay(30_000L)
                init()
            }
        }
    }

    fun stopAutoUpdate() {
        autoUpdateScope.coroutineContext.cancelChildren()
    }

    fun onOptionsClick() {
        navigateEvent.postValue(
            Event(NavCommand(R.id.showOptionsFragment))
        )
    }

    fun setGlobalSelectedFiat(fiat: String) {
        setState { copy(globalSelectedFiat = fiat) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<DashboardViewModel, DashboardViewState> {
        override fun create(state: DashboardViewState): DashboardViewModel
    }

    companion object :
        MavericksViewModelFactory<DashboardViewModel, DashboardViewState> by hiltMavericksViewModelFactory()
}
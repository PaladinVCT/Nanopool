package by.lebedev.nanopoolmonitoring.ui.options


import android.content.SharedPreferences
import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.hiltMavericksViewModelFactory
import by.lebedev.nanopoolmonitoring.domain.account.LoadPayoutLimitUseCase
import by.lebedev.nanopoolmonitoring.domain.premium.CheckPremiumUseCase
import by.lebedev.nanopoolmonitoring.domain.wallets.ObserveWalletsUseCase
import by.lebedev.nanopoolmonitoring.domain.worker.autoupdate.ClearWorkersAutoUpdateUseCase
import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect


class OptionsViewModel @AssistedInject constructor(
    @Assisted initialState: OptionsViewState,
    private val clearWorkersAutoUpdateUseCase: ClearWorkersAutoUpdateUseCase,
    private val observeWalletsUseCase: ObserveWalletsUseCase,
    private val checkPremiumUseCase: CheckPremiumUseCase,
    private val sp: SharedPreferences,
) : NanopoolViewModel<OptionsViewState>(initialState) {

    init {
        loadAccounts()
    }

    fun cancelNotifications() {
        clearWorkersAutoUpdateUseCase(Unit)
            .launch()
    }

    private fun loadAccounts() {
        viewModelScope.launchObserve(observeWalletsUseCase) { flow ->
            flow.collect {
                setState {
                    copy(accounts = it)
                }
            }
        }
        observeWalletsUseCase.invoke()
    }

    fun checkPremium() {
        checkPremiumUseCase.invoke()
            .trackLoading { setState { copy(isLoading = it) } }
            .trackError { errorEventBus }
            .trackSuccess {
                SessionBearer.isPremium = sp.checkPremium()
                setState { copy(isPremium = SessionBearer.isPremium) }
            }
            .launch()
    }

    fun restorePremium(callback: (Boolean) -> Unit) {
        checkPremiumUseCase.invoke()
            .trackLoading { setState { copy(isLoading = it) } }
            .trackError { errorEventBus }
            .trackSuccess {
                callback(sp.checkPremium())
            }
            .launch()
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<OptionsViewModel, OptionsViewState> {
        override fun create(state: OptionsViewState): OptionsViewModel
    }

    companion object :
        MavericksViewModelFactory<OptionsViewModel, OptionsViewState> by hiltMavericksViewModelFactory()
}
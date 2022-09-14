package by.lebedev.nanopoolmonitoring.ui.widget.configure


import android.content.SharedPreferences
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.hiltMavericksViewModelFactory
import by.lebedev.nanopoolmonitoring.domain.wallets.DeleteWalletUseCase
import by.lebedev.nanopoolmonitoring.domain.wallets.ObserveWalletsUseCase
import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import by.lebedev.nanopoolmonitoring.ui.addwallet.AddWalletNavArgs
import by.lebedev.nanopoolmonitoring.ui.editwallet.EditWalletNavArgs
import by.lebedev.nanopoolmonitoring.ui.options.OptionsNavArgs
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion


class ConfigureWidgetViewModel @AssistedInject constructor(
    @Assisted initialState: ConfigureWidgetViewState,
    private val deleteWalletUseCase: DeleteWalletUseCase,
    private val observeWalletsUseCase: ObserveWalletsUseCase,
    private val sp: SharedPreferences
) : NanopoolViewModel<ConfigureWidgetViewState>(initialState) {

    init {
        loadWallets()
    }

    private fun loadWallets() {
        viewModelScope.launchObserve(observeWalletsUseCase) { flow ->
            flow.collect {
                setState {
                    copy(wallets = it)
                }
            }
        }
        observeWalletsUseCase.invoke()
    }

    fun deleteWallet(wallet: Wallet) {
        deleteWalletUseCase(DeleteWalletUseCase.Params(wallet))
            .trackError { errorEventBus }
            .trackLoading {
                setState { copy(isLoading = it) }
            }
            .onCompletion {
                loadWallets()
            }
            .launch()
    }

    fun onAddWalletClick() {
        navigateEvent.postEvent(
            NavCommand(
                R.id.showAddWalletFragment,
                args = AddWalletNavArgs(true).toBundleMvRx()
            )
        )
    }

    fun onEditWalletClick(wallet: Wallet) {
        navigateEvent.postEvent(
            NavCommand(
                action = R.id.showEditWalletFragment,
                args = EditWalletNavArgs(wallet,true).toBundleMvRx()
            )
        )
    }

    fun onOptionsClick() {
        navigateEvent.postEvent(
            NavCommand(
                action = R.id.showOptionsFragment,
                args = OptionsNavArgs(false).toBundleMvRx()
            )
        )
    }

    fun saveSession(wallet: Wallet) {
        sp.edit().putLong(WALLET_ID, wallet.serverId).apply()
    }

    @AssistedFactory
    interface Factory :
        AssistedViewModelFactory<ConfigureWidgetViewModel, ConfigureWidgetViewState> {
        override fun create(state: ConfigureWidgetViewState): ConfigureWidgetViewModel
    }

    companion object :
        MavericksViewModelFactory<ConfigureWidgetViewModel, ConfigureWidgetViewState> by hiltMavericksViewModelFactory()
}
package by.lebedev.nanopoolmonitoring.ui.editwallet


import androidx.lifecycle.*
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.hiltMavericksViewModelFactory
import by.lebedev.nanopoolmonitoring.domain.wallets.*
import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import by.lebedev.nanopoolmonitoring.utils.*
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.Input
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class EditWalletViewModel @AssistedInject constructor(
    @Assisted initialState: EditWalletViewState,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val checkWalletExistsUseCase: CheckWalletExistsUseCase
) : NanopoolViewModel<EditWalletViewState>(initialState) {

    private val _onWalletNotExist = MutableLiveData<Event<String>>()
    val onWalletNotExist: LiveData<Event<String>> = _onWalletNotExist

    fun onCoinSelected(coinTicker: CoinTicker) {
        setState {
            copy(coinTicker = coinTicker)
        }
    }

    fun onWalletNameChanged(input: Input) {
        setState {
            copy(walletName = input)
        }
    }

    fun onWalletNumberChanged(input: Input) {
        setState {
            copy(walletAddress = input, walletAddressErrorRes = 0)
        }
    }

    fun checkWalletAndUpdate() = withState { state ->
        if (validate(state.walletAddress)) {
            checkWalletExistsUseCase.invoke(
                CheckWalletExistsUseCase.Params(
                    address = state.walletAddress.text,
                    coinTicker = state.coinTicker?.value.orEmpty()
                )
            )
                .trackLoading { setState { copy(isLoading = it) } }
                .trackError { errorEventBus }
                .trackSuccess {
                    if (it.status) {
                        updateWallet(
                            state.walletServerId ?: 0L,
                            state.coinTicker ?: CoinTicker.ETHEREUM,
                            state.walletName.text,
                            state.walletAddress.text
                        )
                    } else {
                        _onWalletNotExist.postValue(Event(it.messageError))
                    }
                }
                .launch()
        }
    }

    private fun updateWallet(
        walletServerId: Long,
        coinTicker: CoinTicker,
        walletName: String,
        walletAddress: String
    ) {
        updateWalletUseCase.invoke(
            UpdateWalletUseCase.Params(
                serverId = walletServerId,
                walletName = walletName,
                walletAddress = walletAddress,
                walletCoinTicker = coinTicker.value
            )
        ).trackLoading { setState { copy(isLoading = it) } }
            .trackError { errorEventBus }
            .trackSuccess {
                withState { state ->
                    if (state.isFromWidgetConfigure) {
                        navigateEvent.postEvent(PopBackStack(R.id.configureWidgetFragment, false))
                    } else {
                        navigateEvent.postValue(Event(PopBackStack(R.id.startScreen, false)))
                    }
                }
            }
            .launch()
    }

    private fun validate(walletNumber: Input): Boolean {
        val walletAddressErrorRes =
            if (walletNumber.text.isEmpty()) R.string.error_wallet_address_empty else 0
        setState {
            copy(
                walletAddressErrorRes = walletAddressErrorRes
            )
        }
        return walletAddressErrorRes == 0
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<EditWalletViewModel, EditWalletViewState> {
        override fun create(state: EditWalletViewState): EditWalletViewModel
    }

    companion object :
        MavericksViewModelFactory<EditWalletViewModel, EditWalletViewState> by hiltMavericksViewModelFactory()
}
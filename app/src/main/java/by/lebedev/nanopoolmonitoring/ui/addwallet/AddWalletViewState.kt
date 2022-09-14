package by.lebedev.nanopoolmonitoring.ui.addwallet

import by.lebedev.nanopoolmonitoring.utils.CoinTicker
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.Input
import com.airbnb.mvrx.MavericksState

data class AddWalletViewState(
    val walletName: Input = Input(),
    val walletAddress: Input = Input(),
    val walletAddressErrorRes: Int = 0,
    val coinTicker: CoinTicker? = null,
    val isLoading: Boolean = false,
    val isFromWidgetConfigure: Boolean = false

) : MavericksState {
    constructor(args: AddWalletNavArgs) : this(
        isFromWidgetConfigure = args.isFromWidgetConfigure
    )
}
package by.lebedev.nanopoolmonitoring.ui.editwallet

import by.lebedev.nanopoolmonitoring.utils.CoinTicker
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.Input
import by.lebedev.nanopoolmonitoring.utils.textToCoinTicker
import com.airbnb.mvrx.MavericksState

data class EditWalletViewState(
    val walletServerId: Long? = null,
    val walletName: Input = Input(),
    val walletAddress: Input = Input(),
    val walletAddressErrorRes: Int = 0,
    val coinTicker: CoinTicker? = null,
    val isLoading: Boolean = false,
    val isFromWidgetConfigure: Boolean = false

) : MavericksState {
    constructor(args: EditWalletNavArgs) : this(
        walletServerId = args.wallet.serverId,
        walletName = Input(args.wallet.walletName),
        walletAddress = Input(args.wallet.walletAddress),
        coinTicker = args.wallet.walletCoin.ticker.textToCoinTicker(),
        isFromWidgetConfigure = args.isFromWidgetConfigure
    )
}
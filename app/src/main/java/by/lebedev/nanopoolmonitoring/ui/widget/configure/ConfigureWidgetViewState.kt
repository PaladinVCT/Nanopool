package by.lebedev.nanopoolmonitoring.ui.widget.configure

import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import com.airbnb.mvrx.MavericksState

data class ConfigureWidgetViewState(
    val wallets: List<Wallet> = listOf(),
    val isLoading: Boolean = false

) : MavericksState {
}
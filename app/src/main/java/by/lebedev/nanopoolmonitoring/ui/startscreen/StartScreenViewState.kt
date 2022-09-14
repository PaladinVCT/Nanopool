package by.lebedev.nanopoolmonitoring.ui.startscreen

import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import com.airbnb.mvrx.MavericksState

data class StartScreenViewState(
    val wallets: List<Wallet> = listOf(),
    val isLoading: Boolean = false

) : MavericksState {
}
package by.lebedev.nanopoolmonitoring.ui.options

import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.utils.CoinTicker
import by.lebedev.nanopoolmonitoring.utils.Event
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.Input
import com.airbnb.mvrx.MavericksState

data class OptionsViewState(
    val isLoading: Boolean = false,
    val accounts: List<Wallet> = listOf(),
    val isNotificationsEnabled: Boolean = true,
    val isPremium: Boolean = false
) : MavericksState {
    constructor(args: OptionsNavArgs) : this (
        isNotificationsEnabled = args.isNotificationsEnabled
    )
}
package by.lebedev.nanopoolmonitoring.ui.mainscreen


import com.airbnb.mvrx.MavericksState

data class MainScreenViewState(
    val isLoading: Boolean = false
) : MavericksState
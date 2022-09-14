package by.lebedev.nanopoolmonitoring.ui.coins


import by.lebedev.nanopoolmonitoring.data.entities.cc.CcCoin
import com.airbnb.mvrx.MavericksState

data class CoinsViewState(
    val ccCoinList: List<CcCoin> = emptyList(),
    val querySearchText: String? = null,
    val isLoading: Boolean = false

) : MavericksState {


}
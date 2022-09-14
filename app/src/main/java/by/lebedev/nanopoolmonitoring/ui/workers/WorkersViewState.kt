package by.lebedev.nanopoolmonitoring.ui.workers


import by.lebedev.nanopoolmonitoring.data.entities.workers.Worker
import com.airbnb.mvrx.MavericksState

data class WorkersViewState(
    val areWorkersExpanded: Boolean = false,
    val isAllFilterSelected: Boolean = true,
    val isAliveFilterSelected: Boolean = false,
    val isDeadFilterSelected: Boolean = false,
    val querySearchText: String? = null,
    val workers: List<Worker> = emptyList(),
    val isLoading: Boolean = false

) : MavericksState {

    val totalWorkersCount = workers.size
    val aliveWorkersCount = workers.count {
        it.hashrate != 0.0
    }
    val deadWorkersCount = workers.count {
        it.hashrate == 0.0
    }
    val deadWorkersList = workers.filter {
        it.hashrate == 0.0
    }

    val aliveWorkersList = workers.filter {
        it.hashrate != 0.0
    }


}
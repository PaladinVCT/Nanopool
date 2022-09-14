package by.lebedev.nanopoolmonitoring.ui.workers


import android.content.Context
import by.lebedev.nanopoolmonitoring.utils.roundPlusHashrate
import by.lebedev.nanopoolmonitoring.worker
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class WorkersController @Inject constructor(
    private val context: Context
) : TypedEpoxyController<WorkersViewState>() {

    var callbacks: Callbacks? = null

    interface Callbacks {
        fun onWorkerClick(workerName: String, workerRating: Long, workerLastShareTime: Long)
    }

    override fun buildModels(data: WorkersViewState) {

        if (data.areWorkersExpanded) {
            val workerList = when {
                data.isAllFilterSelected -> data.workers
                data.isAliveFilterSelected -> data.aliveWorkersList
                data.isDeadFilterSelected -> data.deadWorkersList
                else -> data.workers
            }
            workerList.filter {
                if (!data.querySearchText.isNullOrEmpty())
                    it.id.lowercase().contains(data.querySearchText)
                else true
            }.forEachIndexed { index, worker ->
                worker {
                    id("worker_$index")
                    workerName(worker.id)
                    isAlive(worker.hashrate != 0.0)
                    workerHashrate(worker.hashrate.roundPlusHashrate(2))
                    onClick { _ ->
                        this@WorkersController.callbacks?.onWorkerClick(
                            worker.id,
                            worker.rating,
                            worker.lastShare
                        )
                    }
                }
            }
        }
    }
}

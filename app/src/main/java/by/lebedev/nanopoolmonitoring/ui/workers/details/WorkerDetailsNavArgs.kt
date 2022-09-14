package by.lebedev.nanopoolmonitoring.ui.workers.details

import android.os.Parcelable
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import kotlinx.parcelize.Parcelize

@Parcelize
class WorkerDetailsNavArgs(
    val workerName: String,
    val workerRating: Long,
    val workerLastShareTime: Long
) : Parcelable

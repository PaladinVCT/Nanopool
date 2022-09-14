package by.lebedev.nanopoolmonitoring.utils.autoupdate

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.db.mappers.toListMapper
import by.lebedev.nanopoolmonitoring.data.entities.workers.WorkerAutoUpdate
import by.lebedev.nanopoolmonitoring.data.mappers.worker.autoupdate.WorkerAutoUpdateFromWorkerMapper
import by.lebedev.nanopoolmonitoring.domain.account.LoadWorkersUseCase
import by.lebedev.nanopoolmonitoring.domain.worker.autoupdate.ClearWorkersAutoUpdateUseCase
import by.lebedev.nanopoolmonitoring.domain.worker.autoupdate.GetWorkersAutoUpdateUseCase
import by.lebedev.nanopoolmonitoring.domain.worker.autoupdate.InsertWorkersAutoUpdateUseCase
import by.lebedev.nanopoolmonitoring.ui.BaseApplication.Companion.NOTIFY
import by.lebedev.nanopoolmonitoring.ui.MainActivity
import by.lebedev.nanopoolmonitoring.utils.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn

@HiltWorker
class AutoUpdateWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val loadWorkersUseCase: LoadWorkersUseCase,
    private val getWorkersAutoUpdateUseCase: GetWorkersAutoUpdateUseCase,
    private val insertWorkersAutoUpdateUseCase: InsertWorkersAutoUpdateUseCase,
    private val clearWorkersAutoUpdateUseCase: ClearWorkersAutoUpdateUseCase,
    private val workerAutoUpdateFromWorkerMapper: WorkerAutoUpdateFromWorkerMapper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        //load workers from db
        getWorkersAutoUpdateUseCase()
            .trackSuccess { workerListFromDb ->

                //load workers from backend
                loadWorkersUseCase.invoke(
                    LoadWorkersUseCase.Params(
                        SessionBearer.wallet?.walletCoin?.ticker.orEmpty(),
                        SessionBearer.wallet?.walletAddress.orEmpty()
                    )
                )
                    .trackSuccess { workerListFromNet ->
                        val notifyList = mutableListOf<String>()
                        workerListFromNet.filter { worker ->
                            worker.hashrate == 0.0
                        }.forEach {
                            if (workerListFromDb.filter { workerAo ->
                                    workerAo.isAlive
                                }.map { workerAo ->
                                    workerAo.name
                                }.contains(it.id)) {
                                notifyList.add(it.id)
                            }
                        }

                        if (notifyList.isNotEmpty()){
                            sendNotification(notifyList,context)
                        }

                        //clear db
                        clearWorkers().join()

                        //insert workers to db
                        insertWorkers(
                            workerAutoUpdateFromWorkerMapper.toListMapper().map(workerListFromNet)
                        )
                    }
                    .launchIn(CoroutineScope(Dispatchers.IO))
            }
            .launchIn(CoroutineScope(Dispatchers.IO))


        return Result.success()
    }

    private fun clearWorkers(): Job {
        return clearWorkersAutoUpdateUseCase()
            .trackSuccess {
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

    private fun insertWorkers(workers: List<WorkerAutoUpdate>): Job {
        return insertWorkersAutoUpdateUseCase(workers)
            .trackSuccess {
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

    private fun sendNotification(offlineWorkers: List<String>, ctx: Context) {

        val appIntent = Intent(ctx, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val startAppIntent: PendingIntent = PendingIntent.getActivity(ctx, 0, appIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationToShow = NotificationCompat.Builder(applicationContext, NOTIFY)
            .setSmallIcon(R.drawable.ic_skylight_notification)
            .setColor(ContextCompat.getColor(ctx,R.color.orange))
            .setColorized(true)
            .setContentTitle(ctx.getString(R.string.notification_workers_offline_title))
            .setContentText(offlineWorkers.toString())
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setChannelId(NOTIFY)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(offlineWorkers.toString())
            )
//            .setContentIntent(startAppIntent)
            .build()
        val notificationManager = applicationContext.let { NotificationManagerCompat.from(it) }
        notificationManager.notify(111, notificationToShow)
    }
}